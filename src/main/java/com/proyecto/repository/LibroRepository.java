package com.proyecto.repository;

import com.proyecto.domain.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query("SELECT l FROM Libro l WHERE " +
           "(:query IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(l.isbn) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:categoriaId IS NULL OR l.categoria.id = :categoriaId) AND " +
           "(:autorId IS NULL OR l.autor.id = :autorId) AND " +
           "(:editorialId IS NULL OR l.editorial.id = :editorialId)")
    Page<Libro> buscarConFiltros(@Param("query") String query,
                                @Param("categoriaId") Long categoriaId,
                                @Param("autorId") Long autorId,
                                @Param("editorialId") Long editorialId,
                                Pageable pageable);

    @Query("SELECT COALESCE(SUM(l.cantidadDisponible), 0) FROM Libro l")
    long sumCantidadDisponible();

    @Query("SELECT COALESCE(SUM(l.cantidadTotal - l.cantidadDisponible), 0) FROM Libro l")
    long sumCantidadPrestados();
}
