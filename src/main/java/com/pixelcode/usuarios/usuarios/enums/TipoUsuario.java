package com.pixelcode.usuarios.usuarios.enums;

/**
 * Enumeración de Tipos de Usuario
 * 
 * Define los 4 roles principales del sistema según la evaluación:
 * - ALUMNO: Estudiante que registra asistencia
 * - MAESTRO: Profesor que genera códigos QR y gestiona asistencias
 * - TUTOR: Profesor tutor que monitorea grupos de alumnos
 * - ADMINISTRADOR: Gestiona catálogos y configuraciones del sistema
 * 
 * Preparado para implementar el Patrón Strategy según evaluación
 * 
 * @author Equipo PixelCode
 * @version 1.0
 */
public enum TipoUsuario {
    
    /**
     * ALUMNO: Estudiante del sistema
     * - Escanea códigos QR para registrar asistencia
     * - Consulta su historial de asistencias
     * - Sube justificantes de faltas
     * - Recibe alertas por faltas acumuladas
     */
    ALUMNO("Alumno", "Estudiante del sistema"),
    
    /**
     * MAESTRO: Profesor que imparte materias
     * - Genera códigos QR dinámicos
     * - Gestiona listas de asistencia
     * - Consulta reportes de sus grupos
     * - Aprueba/rechaza justificantes
     */
    MAESTRO("Maestro", "Profesor que imparte materias"),
    
    /**
     * TUTOR: Profesor tutor de grupos
     * - Monitorea asistencias de grupos tutorados
     * - Recibe alertas de alumnos en riesgo
     * - Consulta reportes consolidados
     * - Aprueba/rechaza justificantes
     */
    TUTOR("Tutor", "Profesor tutor de grupos"),
    
    /**
     * ADMINISTRADOR: Gestiona el sistema
     * - Gestiona catálogos (programas, divisiones, materias, grupos)
     * - Gestiona usuarios (alumnos, maestros, tutores)
     * - Realiza asignaciones (alumnos a grupos, maestros a materias)
     * - Configura parámetros del sistema
     */
    ADMINISTRADOR("Administrador", "Gestiona el sistema");
    
    private final String displayName;
    private final String descripcion;
    
    TipoUsuario(String displayName, String descripcion) {
        this.displayName = displayName;
        this.descripcion = descripcion;
    }
    
    /**
     * Obtiene el nombre para mostrar
     * @return Nombre legible del rol
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Obtiene la descripción del rol
     * @return Descripción del rol
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Verifica si el rol es de tipo docente (MAESTRO o TUTOR)
     * @return true si es MAESTRO o TUTOR
     */
    public boolean esDocente() {
        return this == MAESTRO || this == TUTOR;
    }
    
    /**
     * Verifica si el rol puede gestionar asistencias
     * @return true si es MAESTRO o TUTOR
     */
    public boolean puedeGestionarAsistencias() {
        return this == MAESTRO || this == TUTOR;
    }
    
    /**
     * Verifica si el rol puede acceder a catálogos
     * @return true si es ADMINISTRADOR
     */
    public boolean puedeGestionarCatalogos() {
        return this == ADMINISTRADOR;
    }
    
    /**
     * Obtiene el rol desde un String
     * @param valor String con el nombre del rol
     * @return TipoUsuario correspondiente
     * @throws IllegalArgumentException si el valor no es válido
     */
    public static TipoUsuario fromString(String valor) {
        if (valor == null) {
            return null;
        }
        
        for (TipoUsuario tipo : TipoUsuario.values()) {
            if (tipo.name().equalsIgnoreCase(valor) || 
                tipo.displayName.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        
        throw new IllegalArgumentException("Rol no válido: " + valor);
    }
}