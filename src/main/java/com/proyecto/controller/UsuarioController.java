package com.proyecto.controller;

import com.proyecto.dto.request.UsuarioRequestDTO;
import com.proyecto.dto.response.UsuarioResponseDTO;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "listaUsuarios";
    }

    @GetMapping("/nuevo")
    public String formularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDTO());
        return "registrarUsuario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") UsuarioRequestDTO usuarioDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registrarUsuario";
        }

        usuarioService.registrarUsuario(usuarioDTO);
        redirectAttributes.addFlashAttribute("mensajeExito", "El usuario ha sido registrado exitosamente.");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditarUsuario(@PathVariable("id") Long id, Model model) {
        UsuarioResponseDTO usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
        UsuarioRequestDTO requestDTO = UsuarioRequestDTO.builder()
                .id(usuarioExistente.getId())
                .nombre(usuarioExistente.getNombre())
                .apellido(usuarioExistente.getApellido())
                .email(usuarioExistente.getEmail())
                .telefono(usuarioExistente.getTelefono())
                .build();

        model.addAttribute("usuario", requestDTO);
        return "editarUsuario";
    }

    @PostMapping("/actualizar")
    public String actualizarUsuario(@Valid @ModelAttribute("usuario") UsuarioRequestDTO usuarioDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "editarUsuario";
        }

        usuarioService.actualizarUsuario(usuarioDTO.getId(), usuarioDTO);
        redirectAttributes.addFlashAttribute("mensajeExito", "La información del usuario ha sido actualizada.");
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        usuarioService.eliminarUsuario(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "El usuario ha sido eliminado del sistema.");
        return "redirect:/usuarios";
    }
}
