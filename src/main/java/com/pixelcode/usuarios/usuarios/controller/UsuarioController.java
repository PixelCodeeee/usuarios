package com.pixelcode.usuarios.usuarios.controller;

import com.pixelcode.usuarios.usuarios.dto.UsuarioDTO;
import com.pixelcode.usuarios.usuarios.enums.TipoUsuario;
import com.pixelcode.usuarios.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST Unificado de Usuarios
 * Maneja endpoints para: ALUMNOS, PROFESORES, TUTORES y ADMINISTRADORES
 * 
 * Implementa EPIC06 de la evaluación:
 * - HU06.1: Listar usuarios (GET /api/usuarios)
 * - HU06.2: Agregar usuarios (POST /api/usuarios/*)
 * - HU06.3: Editar usuarios (PUT /api/usuarios/{id})
 * - HU06.4: Baja lógica de usuarios (DELETE /api/usuarios/{id})
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    // ==================== ENDPOINTS GENERALES ====================
    
    /**
     * HU06.1: Lista todos los usuarios con paginación
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Boolean activo) {
        
        log.info("GET /api/usuarios - page: {}, size: {}, activo: {}", page, size, activo);
        
        Sort sort = sortDir.equalsIgnoreCase("asc") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<UsuarioDTO> usuarios = activo != null && activo
                ? usuarioService.listarActivos(pageable)
                : usuarioService.listarTodos(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("usuarios", usuarios.getContent());
        response.put("currentPage", usuarios.getNumber());
        response.put("totalItems", usuarios.getTotalElements());
        response.put("totalPages", usuarios.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene un usuario por ID
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/usuarios/{}", id);
        
        Optional<UsuarioDTO> usuario = usuarioService.obtenerPorId(id);
        
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Usuario no encontrado");
        error.put("id", id.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    /**
     * Búsqueda por nombre
     * GET /api/usuarios/buscar/nombre?q=texto
     */
    @GetMapping("/buscar/nombre")
    public ResponseEntity<Page<UsuarioDTO>> buscarPorNombre(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/usuarios/buscar/nombre?q={}", q);
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> usuarios = usuarioService.buscarPorNombre(q, pageable);
        return ResponseEntity.ok(usuarios);
    }
    
    /**
     * Búsqueda por email
     * GET /api/usuarios/buscar/email?q=texto
     */
    @GetMapping("/buscar/email")
    public ResponseEntity<Page<UsuarioDTO>> buscarPorEmail(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/usuarios/buscar/email?q={}", q);
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> usuarios = usuarioService.buscarPorEmail(q, pageable);
        return ResponseEntity.ok(usuarios);
    }
    
    /**
     * HU06.3: Actualiza cualquier usuario
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO dto) {
        
        log.info("PUT /api/usuarios/{}", id);
        
        Optional<UsuarioDTO> usuarioActualizado = usuarioService.actualizarUsuario(id, dto);
        
        if (usuarioActualizado.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario actualizado exitosamente");
            response.put("usuario", usuarioActualizado.get());
            return ResponseEntity.ok(response);
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Error al actualizar usuario. Verifica los datos.");
        error.put("id", id.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * HU06.4: Baja lógica de cualquier usuario
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/usuarios/{}", id);
        
        Map<String, String> response = new HashMap<>();
        
        if (usuarioService.eliminarUsuario(id)) {
            response.put("mensaje", "Usuario eliminado exitosamente");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
        }
        
        response.put("mensaje", "Usuario no encontrado");
        response.put("id", id.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Reactiva un usuario inactivo
     * PATCH /api/usuarios/{id}/reactivar
     */
    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<?> reactivar(@PathVariable Long id) {
        log.info("PATCH /api/usuarios/{}/reactivar", id);
        
        if (usuarioService.reactivarUsuario(id)) {
            Optional<UsuarioDTO> usuario = usuarioService.obtenerPorId(id);
            if (usuario.isPresent()) {
                return ResponseEntity.ok(usuario.get());
            }
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Usuario no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    // ==================== ENDPOINTS PARA ALUMNOS ====================
    
    /**
     * Lista solo alumnos
     * GET /api/usuarios/alumnos
     */
    @GetMapping("/alumnos")
    public ResponseEntity<Map<String, Object>> listarAlumnos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("GET /api/usuarios/alumnos");
        
        Sort sort = sortDir.equalsIgnoreCase("asc") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UsuarioDTO> alumnos = usuarioService.listarPorRol(TipoUsuario.ALUMNO, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("alumnos", alumnos.getContent());
        response.put("currentPage", alumnos.getNumber());
        response.put("totalItems", alumnos.getTotalElements());
        response.put("totalPages", alumnos.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lista simple de alumnos activos (sin paginación)
     * GET /api/usuarios/alumnos/simple
     */
    @GetMapping("/alumnos/simple")
    public ResponseEntity<List<UsuarioDTO>> listarAlumnosSimple() {
        log.info("GET /api/usuarios/alumnos/simple");
        List<UsuarioDTO> alumnos = usuarioService.listarActivosPorRolSimple(TipoUsuario.ALUMNO);
        return ResponseEntity.ok(alumnos);
    }
    
    /**
     * Busca alumno por matrícula
     * GET /api/usuarios/alumnos/matricula/{matricula}
     */
    @GetMapping("/alumnos/matricula/{matricula}")
    public ResponseEntity<?> buscarAlumnoPorMatricula(@PathVariable String matricula) {
        log.info("GET /api/usuarios/alumnos/matricula/{}", matricula);
        
        Optional<UsuarioDTO> alumno = usuarioService.obtenerPorMatricula(matricula);
        
        if (alumno.isPresent()) {
            return ResponseEntity.ok(alumno.get());
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Alumno no encontrado");
        error.put("matricula", matricula);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    /**
     * HU06.2: Crea un nuevo alumno
     * POST /api/usuarios/alumno
     */
    @PostMapping("/alumno")
    public ResponseEntity<?> crearAlumno(@Valid @RequestBody UsuarioDTO dto) {
        log.info("POST /api/usuarios/alumno");
        
        dto.setRol(TipoUsuario.ALUMNO);
        Optional<UsuarioDTO> alumno = usuarioService.crearUsuario(dto);
        
        if (alumno.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Alumno creado exitosamente");
            response.put("alumno", alumno.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Error al crear alumno. Verifica los datos.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    // ==================== ENDPOINTS PARA PROFESORES ====================
    
    /**
     * Lista solo profesores
     * GET /api/usuarios/profesores
     */
    @GetMapping("/profesores")
    public ResponseEntity<Map<String, Object>> listarProfesores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("GET /api/usuarios/profesores");
        
        Sort sort = sortDir.equalsIgnoreCase("asc") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UsuarioDTO> profesores = usuarioService.listarPorRol(TipoUsuario.MAESTRO, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("profesores", profesores.getContent());
        response.put("currentPage", profesores.getNumber());
        response.put("totalItems", profesores.getTotalElements());
        response.put("totalPages", profesores.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lista simple de profesores activos
     * GET /api/usuarios/profesores/simple
     */
    @GetMapping("/profesores/simple")
    public ResponseEntity<List<UsuarioDTO>> listarProfesoresSimple() {
        log.info("GET /api/usuarios/profesores/simple");
        List<UsuarioDTO> profesores = usuarioService.listarActivosPorRolSimple(TipoUsuario.MAESTRO);
        return ResponseEntity.ok(profesores);
    }
    
    /**
     * HU06.2: Crea un nuevo profesor
     * POST /api/usuarios/profesor
     */
    @PostMapping("/profesor")
    public ResponseEntity<?> crearProfesor(@Valid @RequestBody UsuarioDTO dto) {
        log.info("POST /api/usuarios/profesor");
        
        dto.setRol(TipoUsuario.MAESTRO);
        Optional<UsuarioDTO> profesor = usuarioService.crearUsuario(dto);
        
        if (profesor.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Profesor creado exitosamente");
            response.put("profesor", profesor.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Error al crear profesor. Verifica los datos.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    // ==================== ENDPOINTS PARA TUTORES ====================
    
    /**
     * Lista solo tutores
     * GET /api/usuarios/tutores
     */
    @GetMapping("/tutores")
    public ResponseEntity<Map<String, Object>> listarTutores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("GET /api/usuarios/tutores");
        
        Sort sort = sortDir.equalsIgnoreCase("asc") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UsuarioDTO> tutores = usuarioService.listarPorRol(TipoUsuario.TUTOR, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("tutores", tutores.getContent());
        response.put("currentPage", tutores.getNumber());
        response.put("totalItems", tutores.getTotalElements());
        response.put("totalPages", tutores.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lista simple de tutores activos
     * GET /api/usuarios/tutores/simple
     */
    @GetMapping("/tutores/simple")
    public ResponseEntity<List<UsuarioDTO>> listarTutoresSimple() {
        log.info("GET /api/usuarios/tutores/simple");
        List<UsuarioDTO> tutores = usuarioService.listarActivosPorRolSimple(TipoUsuario.TUTOR);
        return ResponseEntity.ok(tutores);
    }
    
    /**
     * HU06.2: Crea un nuevo tutor
     * POST /api/usuarios/tutor
     */
    @PostMapping("/tutor")
    public ResponseEntity<?> crearTutor(@Valid @RequestBody UsuarioDTO dto) {
        log.info("POST /api/usuarios/tutor");
        
        dto.setRol(TipoUsuario.TUTOR);
        Optional<UsuarioDTO> tutor = usuarioService.crearUsuario(dto);
        
        if (tutor.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Tutor creado exitosamente");
            response.put("tutor", tutor.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Error al crear tutor. Verifica los datos.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    // ==================== ENDPOINTS PARA ADMINISTRADORES ====================
    
    /**
     * Lista solo administradores
     * GET /api/usuarios/administradores
     */
    @GetMapping("/administradores")
    public ResponseEntity<List<UsuarioDTO>> listarAdministradores() {
        log.info("GET /api/usuarios/administradores");
        List<UsuarioDTO> admins = usuarioService.listarActivosPorRolSimple(TipoUsuario.ADMINISTRADOR);
        return ResponseEntity.ok(admins);
    }
    
    /**
     * HU06.2: Crea un nuevo administrador
     * POST /api/usuarios/administrador
     */
    @PostMapping("/administrador")
    public ResponseEntity<?> crearAdministrador(@Valid @RequestBody UsuarioDTO dto) {
        log.info("POST /api/usuarios/administrador");
        
        dto.setRol(TipoUsuario.ADMINISTRADOR);
        Optional<UsuarioDTO> admin = usuarioService.crearUsuario(dto);
        
        if (admin.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Administrador creado exitosamente");
            response.put("administrador", admin.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Error al crear administrador. Verifica los datos.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    // ==================== ENDPOINTS DE VALIDACIÓN ====================
    
    /**
     * Verifica si existe un email
     * GET /api/usuarios/email/existe?email=test@test.com
     */
    @GetMapping("/email/existe")
    public ResponseEntity<Map<String, Boolean>> verificarEmail(@RequestParam String email) {
        log.info("GET /api/usuarios/email/existe?email={}", email);
        boolean existe = usuarioService.obtenerPorEmail(email).isPresent();
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", existe);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Verifica si existe una matrícula
     * GET /api/usuarios/matricula/existe?matricula=12345
     */
    @GetMapping("/matricula/existe")
    public ResponseEntity<Map<String, Boolean>> verificarMatricula(@RequestParam String matricula) {
        log.info("GET /api/usuarios/matricula/existe?matricula={}", matricula);
        boolean existe = usuarioService.obtenerPorMatricula(matricula).isPresent();
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", existe);
        
        return ResponseEntity.ok(response);
    }
}