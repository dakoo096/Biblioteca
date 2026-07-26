package com.proyecto.config;

import com.proyecto.domain.entity.Autor;
import com.proyecto.domain.entity.Categoria;
import com.proyecto.domain.entity.Editorial;
import com.proyecto.domain.entity.Libro;
import com.proyecto.domain.entity.Prestamo;
import com.proyecto.domain.entity.Usuario;
import com.proyecto.domain.enums.EstadoPrestamo;
import com.proyecto.repository.AutorRepository;
import com.proyecto.repository.CategoriaRepository;
import com.proyecto.repository.EditorialRepository;
import com.proyecto.repository.LibroRepository;
import com.proyecto.repository.PrestamoRepository;
import com.proyecto.repository.UsuarioRepository;
import com.proyecto.domain.enums.Rol;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final EditorialRepository editorialRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (autorRepository.count() == 0) {
            inicializarDatosSemilla();
        }
    }

    private void inicializarDatosSemilla() {
        // 1. Autores
        Autor a1 = autorRepository.save(Autor.builder().nombre("Gabriel").apellido("García Márquez").nacionalidad("Colombiana").build());
        Autor a2 = autorRepository.save(Autor.builder().nombre("Miguel").apellido("de Cervantes").nacionalidad("Española").build());
        Autor a3 = autorRepository.save(Autor.builder().nombre("Antoine").apellido("de Saint-Exupéry").nacionalidad("Francesa").build());

        // 2. Categorías
        Categoria c1 = categoriaRepository.save(Categoria.builder().nombre("Novela").descripcion("Obras narrativas de ficción").build());
        Categoria c2 = categoriaRepository.save(Categoria.builder().nombre("Ficción Clásica").descripcion("Obras de literatura universal").build());

        // 3. Editoriales
        Editorial e1 = editorialRepository.save(Editorial.builder().nombre("Editorial Sudamericana").pais("Argentina").build());
        Editorial e2 = editorialRepository.save(Editorial.builder().nombre("Alianza Editorial").pais("España").build());

        // 4. Usuarios (Admin y Lectores)
        Usuario admin = usuarioRepository.save(Usuario.builder()
                .nombre("Administrador")
                .apellido("Sistema")
                .email("admin@biblioteca.com")
                .password(passwordEncoder.encode("admin123"))
                .rol(Rol.ROLE_ADMIN)
                .telefono("+54 11 9999-8888")
                .activo(true)
                .build());

        Usuario u1 = usuarioRepository.save(Usuario.builder()
                .nombre("Ana")
                .apellido("García")
                .email("ana.garcia@email.com")
                .password(passwordEncoder.encode("lector123"))
                .rol(Rol.ROLE_LECTOR)
                .telefono("+54 11 4455-6677")
                .activo(true)
                .build());

        Usuario u2 = usuarioRepository.save(Usuario.builder()
                .nombre("Carlos")
                .apellido("López")
                .email("carlos.lopez@email.com")
                .password(passwordEncoder.encode("lector123"))
                .rol(Rol.ROLE_LECTOR)
                .telefono("+54 11 2233-4455")
                .activo(true)
                .build());

        // 5. Libros
        Libro l1 = libroRepository.save(Libro.builder()
                .titulo("Cien Años de Soledad")
                .isbn("978-0307474728")
                .descripcion("Obra cumbre del realismo mágico latinoamericano.")
                .anioPublicacion(1967)
                .cantidadTotal(5)
                .cantidadDisponible(4)
                .autor(a1)
                .categoria(c1)
                .editorial(e1)
                .build());

        Libro l2 = libroRepository.save(Libro.builder()
                .titulo("Don Quijote de la Mancha")
                .isbn("978-8424116568")
                .descripcion("El hidalgo Don Quijote y su fiel escudero Sancho Panza.")
                .anioPublicacion(1605)
                .cantidadTotal(3)
                .cantidadDisponible(3)
                .autor(a2)
                .categoria(c2)
                .editorial(e2)
                .build());

        Libro l3 = libroRepository.save(Libro.builder()
                .titulo("El Principito")
                .isbn("978-0156013987")
                .descripcion("Fábula poética con ilustraciones hechas por el propio autor.")
                .anioPublicacion(1943)
                .cantidadTotal(4)
                .cantidadDisponible(4)
                .autor(a3)
                .categoria(c1)
                .editorial(e2)
                .build());

        // 6. Préstamos Semilla
        prestamoRepository.save(Prestamo.builder()
                .libro(l1)
                .usuario(u1)
                .fechaPrestamo(LocalDate.now().minusDays(5))
                .fechaLimite(LocalDate.now().plusDays(9))
                .estado(EstadoPrestamo.ACTIVO)
                .build());
    }
}
