package com.proyecto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibroResponseDTO {

    private Long id;
    private String titulo;
    private String isbn;
    private String descripcion;
    private Integer anioPublicacion;
    private Integer cantidadTotal;
    private Integer cantidadDisponible;
    private String portadaUrl;
    private AutorDTO autor;
    private CategoriaDTO categoria;
    private EditorialDTO editorial;
    private boolean disponible;
}
