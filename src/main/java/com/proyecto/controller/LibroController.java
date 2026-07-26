package com.proyecto.controller;

import com.proyecto.dto.request.LibroRequestDTO;
import com.proyecto.dto.response.LibroResponseDTO;
import com.proyecto.repository.AutorRepository;
import com.proyecto.repository.CategoriaRepository;
import com.proyecto.repository.EditorialRepository;
import com.proyecto.service.LibroService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final EditorialRepository editorialRepository;

    @GetMapping("/libros")
    public String listarLibros(@RequestParam(name = "query", required = false) String query,
                               @RequestParam(name = "categoriaId", required = false) Long categoriaId,
                               @RequestParam(name = "autorId", required = false) Long autorId,
                               @RequestParam(name = "editorialId", required = false) Long editorialId,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               Model model) {
        Page<LibroResponseDTO> librosPage = libroService.buscarLibrosConFiltros(query, categoriaId, autorId, editorialId, PageRequest.of(page, 10));
        
        model.addAttribute("libros", librosPage.getContent());
        model.addAttribute("paginaActual", page);
        model.addAttribute("totalPaginas", librosPage.getTotalPages());
        model.addAttribute("query", query);
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("editoriales", editorialRepository.findAll());
        return "listaLibros";
    }

    @GetMapping({"/nuevoLibro", "/libros/nuevo"})
    public String formularioNuevoLibro(Model model) {
        model.addAttribute("libro", new LibroRequestDTO());
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("editoriales", editorialRepository.findAll());
        return "registrarLibro";
    }

    @PostMapping({"/guardarLibro", "/libros/guardar"})
    public String guardarLibro(@Valid @ModelAttribute("libro") LibroRequestDTO libroDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("autores", autorRepository.findAll());
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("editoriales", editorialRepository.findAll());
            return "registrarLibro";
        }
        
        libroService.guardarLibro(libroDTO);
        redirectAttributes.addFlashAttribute("mensajeExito", "El libro se ha registrado exitosamente en el catálogo.");
        return "redirect:/libros";
    }

    @GetMapping({"/editarLibro/{id}", "/libros/editar/{id}"})
    public String formularioEditarLibro(@PathVariable("id") Long id, Model model) {
        LibroResponseDTO libroExistente = libroService.obtenerLibroPorId(id);
        
        LibroRequestDTO requestDTO = LibroRequestDTO.builder()
                .id(libroExistente.getId())
                .titulo(libroExistente.getTitulo())
                .isbn(libroExistente.getIsbn())
                .descripcion(libroExistente.getDescripcion())
                .anioPublicacion(libroExistente.getAnioPublicacion())
                .cantidadTotal(libroExistente.getCantidadTotal())
                .portadaUrl(libroExistente.getPortadaUrl())
                .autorId(libroExistente.getAutor() != null ? libroExistente.getAutor().getId() : null)
                .categoriaId(libroExistente.getCategoria() != null ? libroExistente.getCategoria().getId() : null)
                .editorialId(libroExistente.getEditorial() != null ? libroExistente.getEditorial().getId() : null)
                .build();

        model.addAttribute("libro", requestDTO);
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("editoriales", editorialRepository.findAll());
        return "editarLibro";
    }

    @PostMapping({"/actualizarLibro", "/libros/actualizar"})
    public String actualizarLibro(@Valid @ModelAttribute("libro") LibroRequestDTO libroDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("autores", autorRepository.findAll());
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("editoriales", editorialRepository.findAll());
            return "editarLibro";
        }

        libroService.actualizarLibro(libroDTO.getId(), libroDTO);
        redirectAttributes.addFlashAttribute("mensajeExito", "Los cambios del libro han sido guardados correctamente.");
        return "redirect:/libros";
    }

    @GetMapping({"/eliminarLibro/{id}", "/libros/eliminar/{id}"})
    public String eliminarLibro(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        libroService.eliminarLibroPorId(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "El libro ha sido eliminado del catálogo.");
        return "redirect:/libros";
    }
}
