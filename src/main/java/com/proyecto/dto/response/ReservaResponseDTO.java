package com.proyecto.dto.response;

import com.proyecto.domain.enums.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaResponseDTO {

    private Long id;
    private LibroResponseDTO libro;
    private UsuarioResponseDTO usuario;
    private LocalDateTime fechaReserva;
    private EstadoReserva estado;
}
