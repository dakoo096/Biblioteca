package com.proyecto.controller;

import com.proyecto.dto.request.ReservaRequestDTO;
import com.proyecto.service.LibroService;
import com.proyecto.service.ReservaService;
import com.proyecto.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final LibroService libroService;
    private final UsuarioService usuarioService;

    @GetMapping
    public String listarReservas(Model model) {
        model.addAttribute("reservas", reservaService.obtenerTodasLasReservas());
        return "listaReservas";
    }

    @GetMapping("/nueva")
    public String formularioNuevaReserva(Model model) {
        model.addAttribute("reserva", new ReservaRequestDTO());
        model.addAttribute("libros", libroService.obtenerTodosLosLibros());
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "registrarReserva";
    }

    @PostMapping("/guardar")
    public String guardarReserva(@Valid @ModelAttribute("reserva") ReservaRequestDTO reservaDTO,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("libros", libroService.obtenerTodosLosLibros());
            model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
            return "registrarReserva";
        }

        reservaService.crearReserva(reservaDTO);
        redirectAttributes.addFlashAttribute("mensajeExito", "La reserva del libro ha sido registrada correctamente.");
        return "redirect:/reservas";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarReserva(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        reservaService.cancelarReserva(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "La reserva ha sido cancelada.");
        return "redirect:/reservas";
    }
}
