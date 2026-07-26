package com.proyecto.repository;

import com.proyecto.domain.entity.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReseniaRepository extends JpaRepository<Resenia, Long> {

    List<Resenia> findByLibroIdOrderByFechaCreacionDesc(Long libroId);

    @Query("SELECT AVG(r.puntuacion) FROM Resenia r WHERE r.libro.id = :libroId")
    Double obtenerPromedioCalificacionPorLibro(Long libroId);

    long countByLibroId(Long libroId);
}
