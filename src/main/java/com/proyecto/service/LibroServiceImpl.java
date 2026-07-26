package com.proyecto.service;

import com.proyecto.domain.entity.Autor;
import com.proyecto.domain.entity.Categoria;
import com.proyecto.domain.entity.Editorial;
import com.proyecto.domain.entity.Libro;
import com.proyecto.dto.request.LibroRequestDTO;
import com.proyecto.dto.response.AutorDTO;
import com.proyecto.dto.response.CategoriaDTO;
import com.proyecto.dto.response.EditorialDTO;
import com.proyecto.dto.response.LibroResponseDTO;
import com.proyecto.exception.BusinessRuleException;
import com.proyecto.exception.ResourceNotFoundException;
import com.proyecto.repository.AutorRepository;
import com.proyecto.repository.CategoriaRepository;
import com.proyecto.repository.EditorialRepository;
import com.proyecto.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final EditorialRepository editorialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> obtenerTodosLosLibros() {
        return libroRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LibroResponseDTO> buscarLibrosConFiltros(String query, Long categoriaId, Long autorId, Long editorialId, Pageable pageable) {
        return libroRepository.buscarConFiltros(query, categoriaId, autorId, editorialId, pageable)
                .map(this::convertirAResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponseDTO obtenerLibroPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el libro con ID: " + id));
        return convertirAResponseDTO(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponseDTO obtenerLibroPorIsbn(String isbn) {
        Libro libro = libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el libro con ISBN: " + isbn));
        return convertirAResponseDTO(libro);
    }

    @Override
    @Transactional
    public LibroResponseDTO guardarLibro(LibroRequestDTO requestDTO) {
        if (libroRepository.existsByIsbn(requestDTO.getIsbn())) {
            throw new BusinessRuleException("Ya existe un libro registrado con el ISBN: " + requestDTO.getIsbn());
        }

        Autor autor = autorRepository.findById(requestDTO.getAutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado con ID: " + requestDTO.getAutorId()));
        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + requestDTO.getCategoriaId()));
        Editorial editorial = editorialRepository.findById(requestDTO.getEditorialId())
                .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada con ID: " + requestDTO.getEditorialId()));

        Libro libro = Libro.builder()
                .titulo(requestDTO.getTitulo())
                .isbn(requestDTO.getIsbn())
                .descripcion(requestDTO.getDescripcion())
                .anioPublicacion(requestDTO.getAnioPublicacion())
                .cantidadTotal(requestDTO.getCantidadTotal())
                .cantidadDisponible(requestDTO.getCantidadTotal())
                .portadaUrl(requestDTO.getPortadaUrl())
                .autor(autor)
                .categoria(categoria)
                .editorial(editorial)
                .build();

        Libro guardado = libroRepository.save(libro);
        return convertirAResponseDTO(guardado);
    }

    @Override
    @Transactional
    public LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO requestDTO) {
        Libro libroBD = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el libro con ID: " + id));

        if (!libroBD.getIsbn().equalsIgnoreCase(requestDTO.getIsbn()) && libroRepository.existsByIsbn(requestDTO.getIsbn())) {
            throw new BusinessRuleException("Ya existe otro libro con el ISBN: " + requestDTO.getIsbn());
        }

        Autor autor = autorRepository.findById(requestDTO.getAutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado"));
        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        Editorial editorial = editorialRepository.findById(requestDTO.getEditorialId())
                .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada"));

        int diferenciaStock = requestDTO.getCantidadTotal() - libroBD.getCantidadTotal();
        int nuevaCantidadDisponible = libroBD.getCantidadDisponible() + diferenciaStock;
        if (nuevaCantidadDisponible < 0) {
            throw new BusinessRuleException("No es posible reducir el stock total por debajo de los ejemplares prestados actualmente.");
        }

        libroBD.setTitulo(requestDTO.getTitulo());
        libroBD.setIsbn(requestDTO.getIsbn());
        libroBD.setDescripcion(requestDTO.getDescripcion());
        libroBD.setAnioPublicacion(requestDTO.getAnioPublicacion());
        libroBD.setCantidadTotal(requestDTO.getCantidadTotal());
        libroBD.setCantidadDisponible(nuevaCantidadDisponible);
        libroBD.setPortadaUrl(requestDTO.getPortadaUrl());
        libroBD.setAutor(autor);
        libroBD.setCategoria(categoria);
        libroBD.setEditorial(editorial);

        Libro actualizado = libroRepository.save(libroBD);
        return convertirAResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarLibroPorId(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. No existe el libro con ID: " + id);
        }
        libroRepository.deleteById(id);
    }

    private LibroResponseDTO convertirAResponseDTO(Libro libro) {
        AutorDTO autorDTO = libro.getAutor() != null ? AutorDTO.builder()
                .id(libro.getAutor().getId())
                .nombre(libro.getAutor().getNombre())
                .apellido(libro.getAutor().getApellido())
                .nacionalidad(libro.getAutor().getNacionalidad())
                .build() : null;

        CategoriaDTO categoriaDTO = libro.getCategoria() != null ? CategoriaDTO.builder()
                .id(libro.getCategoria().getId())
                .nombre(libro.getCategoria().getNombre())
                .descripcion(libro.getCategoria().getDescripcion())
                .build() : null;

        EditorialDTO editorialDTO = libro.getEditorial() != null ? EditorialDTO.builder()
                .id(libro.getEditorial().getId())
                .nombre(libro.getEditorial().getNombre())
                .pais(libro.getEditorial().getPais())
                .build() : null;

        return LibroResponseDTO.builder()
                .id(libro.getId())
                .titulo(libro.getTitulo())
                .isbn(libro.getIsbn())
                .descripcion(libro.getDescripcion())
                .anioPublicacion(libro.getAnioPublicacion())
                .cantidadTotal(libro.getCantidadTotal())
                .cantidadDisponible(libro.getCantidadDisponible())
                .portadaUrl(libro.getPortadaUrl())
                .autor(autorDTO)
                .categoria(categoriaDTO)
                .editorial(editorialDTO)
                .disponible(libro.tieneStockDisponible())
                .build();
    }
}
