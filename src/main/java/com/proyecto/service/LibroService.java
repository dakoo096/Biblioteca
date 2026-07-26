package com.proyecto.service;

import com.proyecto.dto.request.LibroRequestDTO;
import com.proyecto.dto.response.LibroResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LibroService {

    List<LibroResponseDTO> obtenerTodosLosLibros();

    Page<LibroResponseDTO> buscarLibrosConFiltros(String query, Long categoriaId, Long autorId, Long editorialId, Pageable pageable);

    LibroResponseDTO obtenerLibroPorId(Long id);

    LibroResponseDTO obtenerLibroPorIsbn(String isbn);

    LibroResponseDTO guardarLibro(LibroRequestDTO requestDTO);

    LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO requestDTO);

    void eliminarLibroPorId(Long id);

    // Métodos de compatibilidad temporal para migración progresiva
    default java.util.List<LibroResponseDTO> findAllLibros() { return obtenerTodosLosLibros(); }
    default java.util.Optional<com.proyecto.domain.entity.Libro> findLibroById(Long id) { return java.util.Optional.empty(); }
    default com.proyecto.domain.entity.Libro saveLibro(com.proyecto.domain.entity.Libro libro) { return libro; }
    default void updateLibro(Long id, com.proyecto.domain.entity.Libro libro) {}
    default void deleteLibroById(Long id) { eliminarLibroPorId(id); }
}
