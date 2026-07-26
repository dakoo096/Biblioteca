package com.proyecto.repository;

import com.proyecto.domain.entity.Prestamo;
import com.proyecto.domain.enums.EstadoPrestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioIdAndEstado(Long usuarioId, EstadoPrestamo estado);

    long countByEstado(EstadoPrestamo estado);

    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.estado = 'ACTIVO' AND p.fechaLimite < :hoy")
    long countPrestamosVencidos(@Param("hoy") LocalDate hoy);

    @Query("SELECT p FROM Prestamo p WHERE p.estado = 'ACTIVO' AND p.fechaLimite < :hoy")
    List<Prestamo> findPrestamosVencidos(@Param("hoy") LocalDate hoy);

    Page<Prestamo> findByEstado(EstadoPrestamo estado, Pageable pageable);

    @Query("SELECT p FROM Prestamo p ORDER BY p.fechaPrestamo DESC")
    List<Prestamo> findTop5ByOrderByFechaPrestamoDesc(Pageable pageable);
}
