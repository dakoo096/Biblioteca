package com.proyecto.service;

import com.proyecto.dto.request.PrestamoRequestDTO;
import com.proyecto.dto.response.PrestamoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PrestamoService {

    PrestamoResponseDTO registrarPrestamo(PrestamoRequestDTO requestDTO);

    PrestamoResponseDTO devolverLibro(Long prestamoId);

    List<PrestamoResponseDTO> obtenerPrestamosActivos();

    Page<PrestamoResponseDTO> obtenerTodosLosPrestamos(Pageable pageable);

    PrestamoResponseDTO obtenerPrestamoPorId(Long id);

    void verificarYActualizarPrestamosVencidos();
}
