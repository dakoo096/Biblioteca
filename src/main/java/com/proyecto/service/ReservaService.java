package com.proyecto.service;

import com.proyecto.dto.request.ReservaRequestDTO;
import com.proyecto.dto.response.ReservaResponseDTO;

import java.util.List;

public interface ReservaService {

    ReservaResponseDTO crearReserva(ReservaRequestDTO requestDTO);

    void cancelarReserva(Long reservaId);

    List<ReservaResponseDTO> obtenerReservasPorLibro(Long libroId);

    List<ReservaResponseDTO> obtenerTodasLasReservas();
}
