package com.pixelcode.usuarios.usuarios.repository;

import com.pixelcode.usuarios.usuarios.entity.Usuario;
import com.pixelcode.usuarios.usuarios.enums.TipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository de Usuario
 * Implementa el Patrón Repository de la evaluación
 * 
 * Abstrae el acceso a datos y proporciona una interfaz
 * orientada a objetos para operaciones CRUD
 * 
 * Cumple con EPIC06 (HU06.1 a HU06.4) de la evaluación
 * 
 * Ventajas del patrón:
 * - Separa lógica de negocio del acceso a datos
 * - Facilita testing con mocks
 * - Permite cambiar la fuente de datos sin afectar la lógica
 * - Spring Data JPA genera implementaciones automáticas
 * 
 * @author Equipo PixelCode
 * @version 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // ==================== BÚSQUEDAS POR EMAIL (HU06.1) ====================
    
    /**
     * Busca un usuario por email
     * Spring genera automáticamente: SELECT * FROM usuarios WHERE email = ?
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el email dado
     * Spring genera automáticamente: SELECT COUNT(*) FROM usuarios WHERE email = ?
     */
    boolean existsByEmail(String email);
    
    // ==================== BÚSQUEDAS POR ROL (HU06.1) ====================
    
    /**
     * Lista usuarios por rol con paginación
     * Spring genera: SELECT * FROM usuarios WHERE rol = ? LIMIT ? OFFSET ?
     */
    Page<Usuario> findByRol(TipoUsuario rol, Pageable pageable);
    
    /**
     * Lista usuarios activos por rol (sin paginación)
     * Spring genera: SELECT * FROM usuarios WHERE rol = ? AND activo = true
     */
    List<Usuario> findByRolAndActivoTrue(TipoUsuario rol);
    
    /**
     * Lista usuarios activos por rol con paginación
     * Spring genera: SELECT * FROM usuarios WHERE rol = ? AND activo = true LIMIT ? OFFSET ?
     */
    Page<Usuario> findByRolAndActivoTrue(TipoUsuario rol, Pageable pageable);
    
    // ==================== BÚSQUEDAS POR MATRÍCULA (ALUMNOS) ====================
    
    /**
     * Busca un alumno por matrícula
     * Spring genera: SELECT * FROM usuarios WHERE matricula = ?
     */
    Optional<Usuario> findByMatricula(String matricula);
    
    /**
     * Verifica si existe un alumno con la matrícula dada
     * Spring genera: SELECT COUNT(*) FROM usuarios WHERE matricula = ?
     */
    boolean existsByMatricula(String matricula);
    
    // ==================== BÚSQUEDAS POR NÚMERO DE EMPLEADO (MAESTROS/TUTORES) ====================
    
    /**
     * Busca un maestro/tutor por número de empleado
     * Spring genera: SELECT * FROM usuarios WHERE numero_empleado = ?
     */
    Optional<Usuario> findByNumeroEmpleado(String numeroEmpleado);
    
    /**
     * Verifica si existe un usuario con el número de empleado dado
     * Spring genera: SELECT COUNT(*) FROM usuarios WHERE numero_empleado = ?
     */
    boolean existsByNumeroEmpleado(String numeroEmpleado);
    
    // ==================== BÚSQUEDAS POR ACTIVO (HU06.1, HU06.4) ====================
    
    /**
     * Lista todos los usuarios activos con paginación
     * Spring genera: SELECT * FROM usuarios WHERE activo = true LIMIT ? OFFSET ?
     */
    Page<Usuario> findByActivoTrue(Pageable pageable);
    
    /**
     * Lista todos los usuarios activos (sin paginación)
     * Spring genera: SELECT * FROM usuarios WHERE activo = true
     */
    List<Usuario> findByActivoTrue();
    
    // ==================== BÚSQUEDAS POR PROGRAMA Y DIVISIÓN (ALUMNOS) ====================
    
    /**
     * Lista alumnos activos por programa educativo
     * Spring genera: SELECT * FROM usuarios WHERE programa_educativo_id = ? AND activo = true
     */
    List<Usuario> findByProgramaEducativoIdAndActivoTrue(Long programaEducativoId);
    
    /**
     * Lista alumnos activos por división
     * Spring genera: SELECT * FROM usuarios WHERE division_id = ? AND activo = true
     */
    List<Usuario> findByDivisionIdAndActivoTrue(Long divisionId);
    
    // ==================== BÚSQUEDA POR NOMBRE (HU06.1) ====================
    
    /**
     * Busca usuarios cuyo nombre contiene el texto dado
     * Spring genera: SELECT * FROM usuarios WHERE nombre LIKE %texto%
     */
    Page<Usuario> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    
    /**
     * Busca usuarios cuyo apellido paterno contiene el texto dado
     * Spring genera: SELECT * FROM usuarios WHERE apellido_paterno LIKE %texto%
     */
    Page<Usuario> findByApellidoPaternoContainingIgnoreCase(String apellidoPaterno, Pageable pageable);
    
    /**
     * Busca usuarios cuyo email contiene el texto dado
     * Spring genera: SELECT * FROM usuarios WHERE email LIKE %texto%
     */
    Page<Usuario> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    
    // ==================== CONTADORES ====================
    
    /**
     * Cuenta usuarios activos por rol
     * Spring genera: SELECT COUNT(*) FROM usuarios WHERE rol = ? AND activo = true
     */
    long countByRolAndActivoTrue(TipoUsuario rol);
    
    /**
     * Cuenta total de usuarios activos
     * Spring genera: SELECT COUNT(*) FROM usuarios WHERE activo = true
     */
    long countByActivoTrue();
}