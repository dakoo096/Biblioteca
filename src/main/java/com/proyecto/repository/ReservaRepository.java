package com.proyecto.repository;

import com.proyecto.domain.entity.Reserva;
import com.proyecto.domain.enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByLibroIdAndEstadoOrderByFechaReservaAsc(Long libroId, EstadoReserva estado);

    long countByEstado(EstadoReserva estado);

    boolean existsByLibroIdAndUsuarioIdAndEstado(Long libroId, Long usuarioId, EstadoReserva estado);
}
