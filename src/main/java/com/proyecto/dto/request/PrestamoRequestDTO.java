package com.proyecto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestamoRequestDTO {

    @NotNull(message = "Debes seleccionar un libro")
    private Long libroId;

    @NotNull(message = "Debes seleccionar un usuario")
    private Long usuarioId;

    @NotNull(message = "La fecha límite del préstamo es obligatoria")
    private LocalDate fechaLimite;
}
