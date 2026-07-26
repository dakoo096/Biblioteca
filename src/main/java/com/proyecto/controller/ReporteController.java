package com.proyecto.controller;

import com.proyecto.dto.response.LibroResponseDTO;
import com.proyecto.dto.response.PrestamoResponseDTO;
import com.proyecto.service.LibroService;
import com.proyecto.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final LibroService libroService;
    private final PrestamoService prestamoService;

    @GetMapping("/libros-pdf")
    public String reporteLibros(Model model) {
        List<LibroResponseDTO> libros = libroService.obtenerTodosLosLibros();
        model.addAttribute("libros", libros);
        model.addAttribute("fechaGeneracion", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        return "reportes/reporteLibros";
    }

    @GetMapping("/prestamos-pdf")
    public String reportePrestamos(Model model) {
        List<PrestamoResponseDTO> prestamos = prestamoService.obtenerTodosLosPrestamos(Pageable.unpaged()).getContent();
        model.addAttribute("prestamos", prestamos);
        model.addAttribute("fechaGeneracion", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        return "reportes/reportePrestamos";
    }
}
