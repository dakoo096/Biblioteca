package com.proyecto.controller;

import com.proyecto.dto.request.PrestamoRequestDTO;
import com.proyecto.dto.response.PrestamoResponseDTO;
import com.proyecto.service.LibroService;
import com.proyecto.service.PrestamoService;
import com.proyecto.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final LibroService libroService;
    private final UsuarioService usuarioService;

    @GetMapping
    public String listarPrestamos(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        Page<PrestamoResponseDTO> prestamosPage = prestamoService.obtenerTodosLosPrestamos(PageRequest.of(page, 10));
        model.addAttribute("prestamos", prestamosPage.getContent());
        model.addAttribute("paginaActual", page);
        model.addAttribute("totalPaginas", prestamosPage.getTotalPages());
        return "listaPrestamos";
    }

    @GetMapping("/nuevo")
    public String formularioNuevoPrestamo(Model model) {
        PrestamoRequestDTO requestDTO = PrestamoRequestDTO.builder()
                .fechaLimite(LocalDate.now().plusDays(14))
                .build();

        model.addAttribute("prestamo", requestDTO);
        model.addAttribute("libros", libroService.obtenerTodosLosLibros());
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "registrarPrestamo";
    }

    @PostMapping("/guardar")
    public String registrarPrestamo(@Valid @ModelAttribute("prestamo") PrestamoRequestDTO prestamoDTO,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("libros", libroService.obtenerTodosLosLibros());
            model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
            return "registrarPrestamo";
        }

        prestamoService.registrarPrestamo(prestamoDTO);
        redirectAttributes.addFlashAttribute("mensajeExito", "Préstamo registrado exitosamente.");
        return "redirect:/prestamos";
    }

    @GetMapping("/devolver/{id}")
    public String devolverLibro(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        prestamoService.devolverLibro(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "El libro ha sido devuelto y reingresado al stock.");
        return "redirect:/prestamos";
    }
}
