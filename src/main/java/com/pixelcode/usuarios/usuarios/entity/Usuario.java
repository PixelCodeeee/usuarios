package com.pixelcode.usuarios.usuarios.entity;

import com.pixelcode.usuarios.usuarios.enums.TipoUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Usuario UNIFICADA
 * Agrupa: Alumnos, Profesores, Tutores y Administradores
 * 
 * Implementa:
 * - Patrón Repository (con UsuarioRepository)
 * - Patrón DTO (conversión en UsuarioService)
 * 
 * Cumple con EPIC06 de la evaluación
 * 
 * @author Equipo PixelCode
 * @version 1.0
 */
@Entity
@Table(name = "usuarios", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_matricula", columnList = "matricula"),
    @Index(name = "idx_numero_empleado", columnList = "numeroEmpleado"),
    @Index(name = "idx_rol", columnList = "rol")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // ==================== ROL DEL USUARIO ====================
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "El rol es obligatorio")
    private TipoUsuario rol;
    
    // ==================== CAMPOS COMUNES ====================
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 100, message = "El apellido paterno no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String apellidoPaterno;
    
    @Size(max = 100, message = "El apellido materno no puede exceder 100 caracteres")
    @Column(length = 100)
    private String apellidoMaterno;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(length = 20)
    private String telefono;
    
    // ==================== CAMPOS ESPECÍFICOS DE ALUMNOS ====================
    
    @Column(unique = true, length = 20)
    private String matricula;
    
    @Column(name = "programa_educativo_id")
    private Long programaEducativoId;
    
    @Column(name = "division_id")
    private Long divisionId;
    
    // ==================== CAMPOS ESPECÍFICOS DE MAESTROS Y TUTORES ====================
    
    @Column(unique = true, length = 50)
    private String numeroEmpleado;
    
    // ==================== CAMPOS DE AUDITORÍA ====================
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // ==================== MÉTODOS DEL CICLO DE VIDA ====================
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    // ==================== MÉTODOS DE UTILIDAD ====================
    
    /**
     * Obtiene el nombre completo del usuario
     * @return Nombre completo con apellidos
     */
    @Transient
    public String getNombreCompleto() {
        StringBuilder nombreCompleto = new StringBuilder(nombre)
                .append(" ")
                .append(apellidoPaterno);
        
        if (apellidoMaterno != null && !apellidoMaterno.isEmpty()) {
            nombreCompleto.append(" ").append(apellidoMaterno);
        }
        
        return nombreCompleto.toString();
    }
    
    /**
     * Obtiene el identificador único según el rol
     * - ALUMNO: matrícula
     * - MAESTRO/TUTOR: número de empleado
     * - ADMINISTRADOR: email
     * 
     * @return Identificador único del usuario
     */
    @Transient
    public String getIdentificadorUnico() {
        switch (rol) {
            case ALUMNO:
                return matricula;
            case MAESTRO:
            case TUTOR:
                return numeroEmpleado != null ? numeroEmpleado : email;
            case ADMINISTRADOR:
                return email;
            default:
                return id != null ? id.toString() : email;
        }
    }
}