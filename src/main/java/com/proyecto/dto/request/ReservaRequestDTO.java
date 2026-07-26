package com.proyecto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaRequestDTO {

    @NotNull(message = "Debes seleccionar un libro")
    private Long libroId;

    @NotNull(message = "Debes seleccionar un usuario")
    private Long usuarioId;
}
