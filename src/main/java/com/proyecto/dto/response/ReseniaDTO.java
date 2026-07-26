package com.proyecto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReseniaDTO {
    private Long id;
    private Long libroId;
    private String libroTitulo;
    private String usuarioNombre;
    private Integer puntuacion;
    private String comentario;
    private LocalDateTime fechaCreacion;
}
