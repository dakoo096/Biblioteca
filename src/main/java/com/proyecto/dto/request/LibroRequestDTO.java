package com.proyecto.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibroRequestDTO {

    private Long id;

    @NotBlank(message = "El título del libro es obligatorio")
    private String titulo;

    @NotBlank(message = "El código ISBN es obligatorio")
    private String isbn;

    private String descripcion;

    private Integer anioPublicacion;

    @NotNull(message = "La cantidad total de ejemplares es obligatoria")
    @Min(value = 1, message = "La cantidad total debe ser al menos 1")
    private Integer cantidadTotal;

    private String portadaUrl;

    @NotNull(message = "Debes seleccionar un autor")
    private Long autorId;

    @NotNull(message = "Debes seleccionar una categoría")
    private Long categoriaId;

    @NotNull(message = "Debes seleccionar una editorial")
    private Long editorialId;
}
