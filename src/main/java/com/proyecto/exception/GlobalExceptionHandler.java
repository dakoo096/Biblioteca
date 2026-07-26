package com.proyecto.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String manejarRecursoNoEncontrado(ResourceNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        return "redirect:/libros";
    }

    @ExceptionHandler(BusinessRuleException.class)
    public String manejarReglaDeNegocio(BusinessRuleException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        return "redirect:/libros";
    }

    @ExceptionHandler(InsufficientStockException.class)
    public String manejarStockInsuficiente(InsufficientStockException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        return "redirect:/prestamos/nuevo";
    }

    @ExceptionHandler(Exception.class)
    public String manejarErrorInesperado(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado: " + ex.getMessage());
        return "redirect:/";
    }
}
