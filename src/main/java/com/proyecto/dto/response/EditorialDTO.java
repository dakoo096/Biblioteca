package com.proyecto.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditorialDTO {

    private Long id;

    @NotBlank(message = "El nombre de la editorial es obligatorio")
    private String nombre;

    private String pais;
}
