# 📚 Biblioteca SaaS - Sistema de Gestión Bibliotecaria Virtual

Un sistema web moderno, dinámico e integral para la gestión de bibliotecas virtuales, desarrollado con **Spring Boot 3**, **Spring Security**, **Spring Data JPA**, **Thymeleaf** y un **Sistema de Diseño propio** (estética Linear/Vercel) con soporte completo para **Modo Oscuro**, notificaciones flotantes y roles de acceso.

---

## ✨ Características Destacadas

### 🔐 Autenticación & Seguridad por Roles
- **Roles Diferenciados (`ROLE_ADMIN` y `ROLE_LECTOR`):** Control de acceso estricto a las vistas y funciones según el rol del usuario autenticado.
- **Registro de Lectores:** Formulario público de registro para nuevos usuarios con validación de datos.
- **Seguridad Spring Security 6:** Encriptación de contraseñas y protección de rutas.

### 📊 Dashboard Analítico (Administradores)
- Métricas clave en tiempo real: Libros totales, ejemplares disponibles, préstamos activos, solicitudes vencidas y número de lectores registrados.
- Tabla resumen de los últimos préstamos y actividades registradas.

### 📖 Catálogo de Libros Interactivo
- Buscador interactivo en tiempo real por título, autor, categoría o ISBN.
- Fichas de libros con insignias de categoría, estado de stock (Disponibles / Agotados) y modal para dejar reseñas y calificaciones.
- Botones de solicitud directa de préstamos o reservas de libros según disponibilidad.

### 🔄 Gestión de Préstamos y Reservas
- **Préstamos:** Registro de préstamos con cálculo de fechas límites de devolución y estados (`ACTIVO`, `DEVUELTO`, `VENCIDO`).
- **Reservas:** Solicitud y seguimiento de reservas de libros agotados (`PENDIENTE`, `CUMPLIDA`, `CANCELADA`).
- **Mi Biblioteca (Panel de Lector):** Vista personalizada donde cada lector consulta su historial de préstamos activos, fechas límite y reservas pendientes.

### 🖨️ Módulo de Reportes e Impresión
- Generación de reportes listos para imprimir o guardar como PDF (Inventario General de Libros y Reporte de Préstamos).

### 🎨 Experiencia Visual & Design System (UI/UX)
- **Modo Oscuro & Modo Claro Completo:** Alternador de tema en tiempo real, persistencia en `localStorage`, compatibilidad con las preferencias del sistema (`prefers-color-scheme`) y carga libre de parpadeos blancos (Anti-FOUC).
- **Notificaciones Flotantes (Toast Alerts):** Avisos emergentes en el centro superior de la pantalla con animaciones de entrada/salida (`toastSlideDown` / `toastFadeOut`), auto-desaparición a los 4 segundos y botón de cierre manual.
- **Sticky Footer Flexbox:** Pie de página pegado de forma impecable a la base de la ventana independientemente de la cantidad de contenido en la vista.

---

## 🔑 Cuentas Demo para Pruebas

Para probar las funcionalidades sin necesidad de configurar nuevos usuarios, puedes utilizar las siguientes credenciales:

| Rol | Correo Electrónico | Contraseña |
| :--- | :--- | :--- |
| 👑 **Administrador** | `admin@biblioteca.com` | `admin123` |
| 📖 **Lector** | `ana.garcia@email.com` | `lector123` |

---

## 🛠️ Stack Tecnológico

- **Lenguaje:** Java 17
- **Framework Principal:** Spring Boot 3.3.4
- **Seguridad:** Spring Security 6 & Thymeleaf Extras Spring Security 6
- **Persistencia de Datos:** Spring Data JPA / Hibernate
- **Base de Datos:** MySQL 8+
- **Motor de Plantillas:** Thymeleaf
- **Frontend & Estilos:** Bootstrap 5.3.3, Bootstrap Icons, SweetAlert2, CSS3 personalizado con variables CSS
- **Herramientas & Auxiliares:** Lombok, Maven, Spring Boot DevTools

---

## 📁 Estructura del Proyecto

```text
src/main/java/com/proyecto/
├── config/             # Configuración de Spring Security y Beans
├── controller/         # Controladores MVC (Auth, Dashboard, Libro, Lector, Prestamo, Reserva, Usuario, Reporte)
├── domain/             # Entidades JPA (Libro, Autor, Categoria, Editorial, Usuario, Prestamo, Reserva, Resenia) y Enums
├── dto/                # Data Transfer Objects
├── exception/          # Manejo global de excepciones
├── repository/         # Repositorios JPA
└── service/            # Capa de lógica de negocio y servicios

src/main/resources/
├── static/             # Archivos estáticos (CSS personalizado, JS, imágenes)
└── templates/          # Plantillas Thymeleaf (Vistas HTML y fragmentos reutilizables)
```

---

## 🚀 Instalación y Configuración

### Prerrequisitos
- **Java JDK 17** o superior instalado.
- **MySQL Server** ejecutándose localmente o en un servidor remoto.
- **Apache Maven** (o utilizar el wrapper `./mvnw` incluido).

### Pasos

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/AppBiblioteca.git
   cd AppBiblioteca
   ```

2. **Configurar la Base de Datos:**
   Crea la base de datos en MySQL:
   ```sql
   CREATE DATABASE db_biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Configurar `application.properties`:**
   Edita el archivo `src/main/resources/application.properties` con tus credenciales de MySQL:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/db_biblioteca?useSSL=false&serverTimezone=UTC
   spring.datasource.username=TU_USUARIO_MYSQL
   spring.datasource.password=TU_CONTRASEÑA_MYSQL
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Ejecutar la Aplicación:**
   ```bash
   ./mvnw spring-boot:run
   ```
   *En Windows (PowerShell):*
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

5. **Acceder en el navegador:**
   Navega a [http://localhost:8080](http://localhost:8080) para iniciar sesión o explorar el sistema.

---

## 📸 Capturas de Pantalla

![bibliotecaVirtual1](https://github.com/user-attachments/assets/a7b962ba-6dcd-4089-8465-d2f80eaee654)
![bibliotecaVirtual2](https://github.com/user-attachments/assets/dddcaa5d-14aa-4205-90da-30849c039d6d)
![bibliotecaVirtual3](https://github.com/user-attachments/assets/012f8b16-2f19-463f-8726-84c2a6f209fc)

---

## 📝 Licencia

Este proyecto está disponible bajo la licencia MIT.
