package com.proyecto.controller;

import com.proyecto.entity.Libro;
import com.proyecto.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping("/")
    public String listarTodosLosLibros(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String q,
            Model model
    ) {
        Page<Libro> librosPage;

        if (q != null && !q.trim().isEmpty()) {
            librosPage = libroService.buscarLibrosPaginados(q, page, size);
            model.addAttribute("q", q);
        } else {
            librosPage = libroService.findLibrosPaginados(page, size);
        }

        model.addAttribute("libros", librosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", librosPage.getTotalPages());
        model.addAttribute("size", size);

        return "listaLibros";
    }




    @GetMapping("/nuevoLibro")
    public String registrarNuevoLibro() {
        return "/registrarLibro";
    }

    @PostMapping("/guardarLibro")
    public String guardarLibro(Libro libro, RedirectAttributes redirectAttributes) {
        libroService.saveLibro(libro);
        redirectAttributes.addFlashAttribute(
                "mensaje",
                "📘 Libro guardado correctamente"
        );
        redirectAttributes.addFlashAttribute(
                "tipoMensaje",
                "success"
        );
        return "redirect:/";
    }

    @GetMapping("/editarLibro/{id}")
    public String editarLibro(@PathVariable Long id, Model model) {
        Libro libro = libroService.findLibroById(id).get();
        model.addAttribute("libro", libro);
        return "/editarLibro";
    }

    @PostMapping("/actualizarLibro")
    public String actualizarLibro(
            @RequestParam("idLibro") Long id,
            Libro libro,
            RedirectAttributes redirectAttributes
    ) {
        libroService.updateLibro(id, libro);
        redirectAttributes.addFlashAttribute(
                "mensaje",
                "✏️ Libro actualizado correctamente"
        );
        redirectAttributes.addFlashAttribute(
                "tipoMensaje",
                "warning"
        );
        return "redirect:/";
    }

    @GetMapping("/eliminarLibro/{id}")
    public String eliminarLibro(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        libroService.deleteLibroById(id);
        redirectAttributes.addFlashAttribute(
                "mensaje",
                "🗑 Libro eliminado correctamente"
        );
        redirectAttributes.addFlashAttribute(
                "tipoMensaje",
                "danger"
        );
        return "redirect:/";
    }

    @GetMapping("/buscar")
    public String buscarLibros(@RequestParam("q") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return "redirect:/";
        }
        return "redirect:/?q=" + keyword;
    }



    //ver detalle de libro
    @GetMapping("/libro/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return libroService.findLibroById(id)
                .map(libro -> {
                    model.addAttribute("libro", libro);
                    return "detalleLibro";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("mensaje", "⚠️ El libro no existe");
                    redirect.addFlashAttribute("tipoMensaje", "warning");
                    return "redirect:/";
                });
    }



}
