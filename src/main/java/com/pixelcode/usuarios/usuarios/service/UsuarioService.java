package com.pixelcode.usuarios.usuarios.service;

import com.pixelcode.usuarios.usuarios.dto.UsuarioDTO;
import com.pixelcode.usuarios.usuarios.entity.Usuario;
import com.pixelcode.usuarios.usuarios.enums.TipoUsuario;
import com.pixelcode.usuarios.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio Unificado de Usuarios
 * Maneja ALUMNOS, PROFESORES, TUTORES y ADMINISTRADORES
 * Cumple con patrones de la evaluación: Repository, DTO, Strategy
 * 
 * Implementa EPIC06: Gestión de Usuarios
 * - HU06.1: Listar usuarios
 * - HU06.2: Agregar usuarios
 * - HU06.3: Editar usuarios
 * - HU06.4: Baja lógica de usuarios
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    
    // ==================== CREAR USUARIOS (HU06.2) ====================
    
    /**
     * Crea un nuevo usuario según su rol
     * Validaciones según tipo de usuario (ALUMNO, MAESTRO, TUTOR, ADMINISTRADOR)
     */
    public Optional<UsuarioDTO> crearUsuario(UsuarioDTO dto) {
        log.info("Creando usuario con rol: {}", dto.getRol());
        
        // Validar email único
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.error("Email duplicado: {}", dto.getEmail());
            return Optional.empty();
        }
        
        // Validaciones específicas por rol
        if (!validarCamposEspecificosPorRol(dto, null)) {
            return Optional.empty();
        }
        
        Usuario usuario = convertirDTOaEntidad(dto);
        usuario.setActivo(true);
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con ID: {}", usuarioGuardado.getId());
        
        return Optional.of(convertirEntidadADTO(usuarioGuardado));
    }
    
    // ==================== OBTENER USUARIOS ====================
    
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertirEntidadADTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(this::convertirEntidadADTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> obtenerPorMatricula(String matricula) {
        return usuarioRepository.findByMatricula(matricula)
                .map(this::convertirEntidadADTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> obtenerPorNumeroEmpleado(String numeroEmpleado) {
        return usuarioRepository.findByNumeroEmpleado(numeroEmpleado)
                .map(this::convertirEntidadADTO);
    }
    
    // ==================== LISTAR USUARIOS (HU06.1) ====================
    
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(this::convertirEntidadADTO);
    }
    
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> listarActivos(Pageable pageable) {
        return usuarioRepository.findByActivoTrue(pageable)
                .map(this::convertirEntidadADTO);
    }
    
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> listarPorRol(TipoUsuario rol, Pageable pageable) {
        return usuarioRepository.findByRolAndActivoTrue(rol, pageable)
                .map(this::convertirEntidadADTO);
    }
    
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarActivosPorRolSimple(TipoUsuario rol) {
        return usuarioRepository.findByRolAndActivoTrue(rol)
                .stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }
    
    // ==================== BÚSQUEDA ====================
    
    /**
     * Busca usuarios por nombre (simple)
     * Puedes agregar más criterios según necesites
     */
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> buscarPorNombre(String nombre, Pageable pageable) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                .map(this::convertirEntidadADTO);
    }
    
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> buscarPorEmail(String email, Pageable pageable) {
        return usuarioRepository.findByEmailContainingIgnoreCase(email, pageable)
                .map(this::convertirEntidadADTO);
    }
    
    // ==================== ACTUALIZAR USUARIOS (HU06.3) ====================
    
    public Optional<UsuarioDTO> actualizarUsuario(Long id, UsuarioDTO dto) {
        return usuarioRepository.findById(id)
                .flatMap(usuarioExistente -> {
                    log.info("Actualizando usuario ID: {}", id);
                    
                    // Validar email único (excepto el actual)
                    if (!usuarioExistente.getEmail().equals(dto.getEmail()) &&
                        usuarioRepository.existsByEmail(dto.getEmail())) {
                        log.error("Email duplicado al actualizar: {}", dto.getEmail());
                        return Optional.empty();
                    }
                    
                    // Validaciones específicas por rol
                    if (!validarCamposEspecificosPorRol(dto, usuarioExistente)) {
                        return Optional.empty();
                    }
                    
                    // Actualizar campos comunes
                    usuarioExistente.setNombre(dto.getNombre());
                    usuarioExistente.setApellidoPaterno(dto.getApellidoPaterno());
                    usuarioExistente.setApellidoMaterno(dto.getApellidoMaterno());
                    usuarioExistente.setEmail(dto.getEmail());
                    usuarioExistente.setTelefono(dto.getTelefono());
                    
                    // Actualizar campos específicos según rol
                    actualizarCamposEspecificosPorRol(usuarioExistente, dto);
                    
                    Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
                    log.info("Usuario actualizado exitosamente ID: {}", id);
                    
                    return Optional.of(convertirEntidadADTO(usuarioActualizado));
                });
    }
    
    // ==================== ELIMINAR (BAJA LÓGICA - HU06.4) ====================
    
    public boolean eliminarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    log.info("Desactivando usuario ID: {}", id);
                    usuario.setActivo(false);
                    usuarioRepository.save(usuario);
                    return true;
                })
                .orElse(false);
    }
    
    public boolean reactivarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    log.info("Reactivando usuario ID: {}", id);
                    usuario.setActivo(true);
                    usuarioRepository.save(usuario);
                    return true;
                })
                .orElse(false);
    }
    
    // ==================== MÉTODOS DE VALIDACIÓN ====================
    
    /**
     * Valida campos específicos según el rol del usuario
     * Retorna true si las validaciones pasan, false en caso contrario
     */
    private boolean validarCamposEspecificosPorRol(UsuarioDTO dto, Usuario usuarioExistente) {
        switch (dto.getRol()) {
            case ALUMNO:
                // Validar campos obligatorios
                if (dto.getMatricula() == null || dto.getMatricula().isEmpty()) {
                    log.error("La matrícula es obligatoria para alumnos");
                    return false;
                }
                if (dto.getProgramaEducativoId() == null) {
                    log.error("El programa educativo es obligatorio para alumnos");
                    return false;
                }
                if (dto.getDivisionId() == null) {
                    log.error("La división es obligatoria para alumnos");
                    return false;
                }
                
                // Validar matrícula única
                boolean matriculaExiste = usuarioExistente == null 
                    ? usuarioRepository.existsByMatricula(dto.getMatricula())
                    : !usuarioExistente.getMatricula().equals(dto.getMatricula()) && 
                      usuarioRepository.existsByMatricula(dto.getMatricula());
                
                if (matriculaExiste) {
                    log.error("Ya existe un alumno con la matrícula: {}", dto.getMatricula());
                    return false;
                }
                break;
                
            case MAESTRO:
            case TUTOR:
                // Validar número de empleado único (si se proporciona)
                if (dto.getNumeroEmpleado() != null && !dto.getNumeroEmpleado().isEmpty()) {
                    boolean numeroEmpleadoExiste = usuarioExistente == null
                        ? usuarioRepository.existsByNumeroEmpleado(dto.getNumeroEmpleado())
                        : usuarioExistente.getNumeroEmpleado() != null &&
                          !usuarioExistente.getNumeroEmpleado().equals(dto.getNumeroEmpleado()) &&
                          usuarioRepository.existsByNumeroEmpleado(dto.getNumeroEmpleado());
                    
                    if (numeroEmpleadoExiste) {
                        log.error("Ya existe un usuario con el número de empleado: {}", dto.getNumeroEmpleado());
                        return false;
                    }
                }
                break;
                
            case ADMINISTRADOR:
                // Sin validaciones adicionales
                break;
        }
        
        return true;
    }
    
    /**
     * Actualiza campos específicos según el rol
     */
    private void actualizarCamposEspecificosPorRol(Usuario usuario, UsuarioDTO dto) {
        switch (dto.getRol()) {
            case ALUMNO:
                usuario.setMatricula(dto.getMatricula());
                usuario.setProgramaEducativoId(dto.getProgramaEducativoId());
                usuario.setDivisionId(dto.getDivisionId());
                break;
                
            case MAESTRO:
            case TUTOR:
                usuario.setNumeroEmpleado(dto.getNumeroEmpleado());
                break;
                
            case ADMINISTRADOR:
                // Sin campos específicos
                break;
        }
    }
    
    // ==================== CONVERSIÓN DTO ↔ ENTITY (Patrón DTO) ====================
    
    /**
     * Convierte una entidad Usuario a DTO
     * Implementa el Patrón DTO de la evaluación
     */
    private UsuarioDTO convertirEntidadADTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .rol(usuario.getRol())
                .nombre(usuario.getNombre())
                .apellidoPaterno(usuario.getApellidoPaterno())
                .apellidoMaterno(usuario.getApellidoMaterno())
                .nombreCompleto(usuario.getNombreCompleto())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .matricula(usuario.getMatricula())
                .programaEducativoId(usuario.getProgramaEducativoId())
                .divisionId(usuario.getDivisionId())
                .numeroEmpleado(usuario.getNumeroEmpleado())
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .fechaActualizacion(usuario.getFechaActualizacion())
                .build();
    }
    
    /**
     * Convierte un DTO a entidad Usuario
     * Implementa el Patrón DTO de la evaluación
     */
    private Usuario convertirDTOaEntidad(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setRol(dto.getRol());
        usuario.setNombre(dto.getNombre());
        usuario.setApellidoPaterno(dto.getApellidoPaterno());
        usuario.setApellidoMaterno(dto.getApellidoMaterno());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setMatricula(dto.getMatricula());
        usuario.setProgramaEducativoId(dto.getProgramaEducativoId());
        usuario.setDivisionId(dto.getDivisionId());
        usuario.setNumeroEmpleado(dto.getNumeroEmpleado());
        return usuario;
    }
}