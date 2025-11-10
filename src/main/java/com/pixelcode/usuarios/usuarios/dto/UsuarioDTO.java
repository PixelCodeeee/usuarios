package com.pixelcode.usuarios.usuarios.dto;

import com.pixelcode.usuarios.usuarios.enums.TipoUsuario;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO Base de Usuario
 * Implementa el Patrón DTO de la evaluación
 * 
 * Contiene todos los campos posibles para los 4 roles:
 * - ALUMNO (matrícula, programaEducativoId, divisionId)
 * - MAESTRO (numeroEmpleado)
 * - TUTOR (numeroEmpleado)
 * - ADMINISTRADOR (solo campos comunes)
 * 
 * Se usa para:
 * - Respuestas del servidor (GET)
 * - Transferencia de datos entre capas
 * 
 * Cumple con EPIC06 de la evaluación
 * 
 * @author Equipo PixelCode
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    
    // ==================== CAMPOS DE IDENTIFICACIÓN ====================
    
    private Long id;
    
    @NotNull(message = "El rol es obligatorio")
    private TipoUsuario rol;
    
    // ==================== CAMPOS COMUNES ====================
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido paterno debe tener entre 2 y 100 caracteres")
    private String apellidoPaterno;
    
    @Size(max = 100, message = "El apellido materno no puede exceder 100 caracteres")
    private String apellidoMaterno;
    
    private String nombreCompleto;
    
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos", 
             groups = ValidationGroups.PhoneValidation.class)
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    // ==================== CAMPOS ESPECÍFICOS DE ALUMNO ====================
    
    @Size(min = 5, max = 20, message = "La matrícula debe tener entre 5 y 20 caracteres")
    private String matricula;
    
    @Positive(message = "El ID del programa educativo debe ser positivo")
    private Long programaEducativoId;
    
    @Positive(message = "El ID de la división debe ser positivo")
    private Long divisionId;
    
    // ==================== CAMPOS ESPECÍFICOS DE MAESTRO/TUTOR ====================
    
    @Size(max = 50, message = "El número de empleado no puede exceder 50 caracteres")
    private String numeroEmpleado;
    
    // ==================== CAMPOS DE AUDITORÍA ====================
    
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // ==================== GRUPOS DE VALIDACIÓN ====================
    
    /**
     * Grupos de validación para diferentes contextos
     */
    public interface ValidationGroups {
        interface PhoneValidation {}
        interface CreateValidation {}
        interface UpdateValidation {}
    }
}

/**
 * DTO simplificado para crear/editar ALUMNOS
 * Contiene solo los campos necesarios para alumnos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class AlumnoDTO {
    
    private Long id;
    
    @NotBlank(message = "La matrícula es obligatoria")
    @Size(min = 5, max = 20, message = "La matrícula debe tener entre 5 y 20 caracteres")
    private String matricula;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido paterno debe tener entre 2 y 100 caracteres")
    private String apellidoPaterno;
    
    @Size(max = 100, message = "El apellido materno no puede exceder 100 caracteres")
    private String apellidoMaterno;
    
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    private String email;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    private String telefono;
    
    @NotNull(message = "El ID del programa educativo es obligatorio")
    @Positive(message = "El ID del programa educativo debe ser positivo")
    private Long programaEducativoId;
    
    @NotNull(message = "El ID de la división es obligatorio")
    @Positive(message = "El ID de la división debe ser positivo")
    private Long divisionId;
    
    private Boolean activo;
}

/**
 * DTO simplificado para crear/editar PROFESORES
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ProfesorDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 100, message = "El apellido paterno no puede exceder 100 caracteres")
    private String apellidoPaterno;
    
    @Size(max = 100, message = "El apellido materno no puede exceder 100 caracteres")
    private String apellidoMaterno;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    @Size(max = 50, message = "El número de empleado no puede exceder 50 caracteres")
    private String numeroEmpleado;
    
    private Boolean activo;
}

/**
 * DTO simplificado para crear/editar TUTORES
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class TutorDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 100, message = "El apellido paterno no puede exceder 100 caracteres")
    private String apellidoPaterno;
    
    @Size(max = 100, message = "El apellido materno no puede exceder 100 caracteres")
    private String apellidoMaterno;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    private String email;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    @Size(max = 50, message = "El número de empleado no puede exceder 50 caracteres")
    private String numeroEmpleado;
    
    private Boolean activo;
}

/**
 * DTO simplificado para crear/editar ADMINISTRADORES
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class AdministradorDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 100, message = "El apellido paterno no puede exceder 100 caracteres")
    private String apellidoPaterno;
    
    @Size(max = 100, message = "El apellido materno no puede exceder 100 caracteres")
    private String apellidoMaterno;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    private String email;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    private Boolean activo;
}