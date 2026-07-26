package com.proyecto.service;

import com.proyecto.dto.response.ReseniaDTO;
import java.util.List;

public interface ReseniaService {
    ReseniaDTO agregarResenia(Long libroId, String userEmail, Integer puntuacion, String comentario);
    List<ReseniaDTO> obtenerReseniasPorLibro(Long libroId);
    Double obtenerPromedioCalificacion(Long libroId);
}
