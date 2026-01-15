
package com.proyecto.repository;

import com.proyecto.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro,Long> {

    Page<Libro> findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase(
            String titulo,
            String autor,
            Pageable pageable
    );

}
