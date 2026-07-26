package com.proyecto.dto.response;

import com.proyecto.domain.enums.EstadoPrestamo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestamoResponseDTO {

    private Long id;
    private LibroResponseDTO libro;
    private UsuarioResponseDTO usuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private EstadoPrestamo estado;
    private boolean vencido;
}
