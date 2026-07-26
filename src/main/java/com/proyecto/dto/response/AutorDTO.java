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
public class AutorDTO {

    private Long id;

    @NotBlank(message = "El nombre del autor es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido del autor es obligatorio")
    private String apellido;

    private String nacionalidad;

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
