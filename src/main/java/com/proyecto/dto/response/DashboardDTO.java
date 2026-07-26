package com.proyecto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    private long totalLibros;
    private long librosDisponibles;
    private long librosPrestados;
    private long usuariosRegistrados;
    private long prestamosActivos;
    private long reservasActivas;
    private long prestamosVencidos;

    private List<PrestamoResponseDTO> ultimosPrestamos;
    private List<LibroResponseDTO> librosMasPrestados;
}
