package com.pixelcode.usuarios.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio Unificado de Usuarios
 * Agrupa: Alumnos, Profesores, Tutores y Administradores
 * 
 * Implementa los patrones de diseño de la evaluación:
 * ✅ Repository Pattern: UsuarioRepository
 * ✅ DTO Pattern: UsuarioDTO y conversiones en UsuarioService
 * ✅ Strategy Pattern: Preparado en TipoUsuario enum
 * 
 * Cumple con EPIC06 de la evaluación:
 * - HU06.1: Listar usuarios (GET /api/usuarios)
 * - HU06.2: Agregar usuarios (POST /api/usuarios/{rol})
 * - HU06.3: Editar usuarios (PUT /api/usuarios/{id})
 * - HU06.4: Baja lógica (DELETE /api/usuarios/{id})
 * 
 * Arquitectura de Microservicios según evaluación:
 * - Servicio independiente y autónomo
 * - Base de datos propia (usuarios_db)
 * - APIs RESTful para comunicación
 * - Escalabilidad horizontal
 * 
 * Stack Tecnológico:
 * - Backend: Spring Boot 3.x
 * - Base de datos: H2 (desarrollo) / MySQL (producción)
 * - ORM: JPA/Hibernate
 * - Validación: Jakarta Validation
 * 
 * @author Luna Jimenez Joshua Daniel (Scrum Master)
 * @author Moreno Santiago Yanny Galilea (Frontend Developer)
 * @author Rodriguez Trejo Thania Margoth (Analista de Documentación)
 * @author Aguillón García Vanessa Elizabeth (Backend Developer)
 * @author Flores García Karen Nayely (QA Engineer)
 * 
 * @version 1.0
 * @since 2025-10-05
 */
@SpringBootApplication
public class UsuariosServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UsuariosServiceApplication.class, args);
        
        // Banner informativo
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║           MICROSERVICIO DE USUARIOS INICIADO                 ║");
        System.out.println("║                                                              ║");
        System.out.println("║   Servicio: usuarios-service                                 ║");
        System.out.println("║  Puerto: 8080                                                ║");
        System.out.println("║  H2 Console: http://localhost:8080/h2-console             ║");
        System.out.println("║  API Base: http://localhost:8080/api/usuarios             ║");
        System.out.println("║                                                           ║");
        System.out.println("║  Endpoints disponibles:                                   ║");
        System.out.println("║  • GET    /api/usuarios              [Listar todos]       ║");
        System.out.println("║  • GET    /api/usuarios/{id}         [Obtener por ID]     ║");
        System.out.println("║  • GET    /api/usuarios/alumnos      [Listar alumnos]     ║");
        System.out.println("║  • GET    /api/usuarios/profesores   [Listar profesores]  ║");
        System.out.println("║  • GET    /api/usuarios/tutores      [Listar tutores]     ║");
        System.out.println("║  • POST   /api/usuarios/alumno       [Crear alumno]       ║");
        System.out.println("║  • POST   /api/usuarios/profesor     [Crear profesor]     ║");
        System.out.println("║  • POST   /api/usuarios/tutor        [Crear tutor]        ║");
        System.out.println("║  • PUT    /api/usuarios/{id}         [Actualizar]         ║");
        System.out.println("║  • DELETE /api/usuarios/{id}         [Baja lógica]        ║");
        System.out.println("║                                                            ║");
        System.out.println("║  Patrones implementados:                                  ║");
        System.out.println("║  ✓ Repository Pattern                                     ║");
        System.out.println("║  ✓ DTO Pattern                                            ║");
        System.out.println("║  ✓ Strategy Pattern (preparado)                           ║");
        System.out.println("║                                                            ║");
        System.out.println("║  🎓 Equipo PixelCode - UTEQ 2025                          ║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
}