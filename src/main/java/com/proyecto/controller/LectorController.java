package com.proyecto.controller;

import com.proyecto.dto.request.PrestamoRequestDTO;
import com.proyecto.dto.request.ReservaRequestDTO;
import com.proyecto.dto.response.LibroResponseDTO;
import com.proyecto.dto.response.PrestamoResponseDTO;
import com.proyecto.dto.response.ReservaResponseDTO;
import com.proyecto.domain.entity.Usuario;
import com.proyecto.repository.UsuarioRepository;
import com.proyecto.service.LibroService;
import com.proyecto.service.PrestamoService;
import com.proyecto.service.ReseniaService;
import com.proyecto.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class LectorController {

    private final LibroService libroService;
    private final PrestamoService prestamoService;
    private final ReservaService reservaService;
    private final ReseniaService reseniaService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/catalogo")
    public String verCatalogo(Model model) {
        List<LibroResponseDTO> libros = libroService.obtenerTodosLosLibros();
        model.addAttribute("libros", libros);
        return "catalogo";
    }

    @GetMapping("/mi-biblioteca")
    public String verMiBiblioteca(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElse(null);

        if (usuario != null) {
            List<PrestamoResponseDTO> misPrestamos = prestamoService.obtenerTodosLosPrestamos(Pageable.unpaged()).getContent().stream()
                    .filter(p -> p.getUsuario() != null && usuario.getId().equals(p.getUsuario().getId()))
                    .collect(Collectors.toList());

            List<ReservaResponseDTO> misReservas = reservaService.obtenerTodasLasReservas().stream()
                    .filter(r -> r.getUsuario() != null && usuario.getId().equals(r.getUsuario().getId()))
                    .collect(Collectors.toList());

            model.addAttribute("usuario", usuario);
            model.addAttribute("misPrestamos", misPrestamos);
            model.addAttribute("misReservas", misReservas);
        }

        return "miBiblioteca";
    }

    @PostMapping("/prestamos/solicitar")
    public String solicitarPrestamo(@RequestParam("libroId") Long libroId,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

            PrestamoRequestDTO dto = PrestamoRequestDTO.builder()
                    .libroId(libroId)
                    .usuarioId(usuario.getId())
                    .fechaLimite(LocalDate.now().plusDays(14))
                    .build();

            prestamoService.registrarPrestamo(dto);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Solicitud de préstamo procesada con éxito! Puedes pasar a retirar tu libro.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al procesar el préstamo: " + e.getMessage());
        }
        return "redirect:/mi-biblioteca";
    }

    @PostMapping("/reservas/solicitar")
    public String solicitarReserva(@RequestParam("libroId") Long libroId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

            ReservaRequestDTO dto = ReservaRequestDTO.builder()
                    .libroId(libroId)
                    .usuarioId(usuario.getId())
                    .build();

            reservaService.crearReserva(dto);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Reserva realizada exitosamente! Te avisaremos cuando el libro esté listo.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al procesar la reserva: " + e.getMessage());
        }
        return "redirect:/mi-biblioteca";
    }

    @PostMapping("/resenias/agregar")
    public String agregarResenia(@RequestParam("libroId") Long libroId,
                                 @RequestParam("puntuacion") Integer puntuacion,
                                 @RequestParam("comentario") String comentario,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            reseniaService.agregarResenia(libroId, email, puntuacion, comentario);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Gracias por tu opinión y valoración!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/catalogo";
    }
}
