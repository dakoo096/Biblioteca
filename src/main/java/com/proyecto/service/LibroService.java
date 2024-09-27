package com.proyecto.service;

import com.proyecto.entity.Libro;
import java.util.List;
import java.util.Optional;

public interface LibroService {

    //buscar todos los libros
    List<Libro> findAllLibros();

    //buscar libro por id
    Optional<Libro> findLibroById(Long iLibro);

    //guardar libro
    Libro saveLibro(Libro libro);

    //actualizar
    void updateLibro(Long idLibro, Libro libro);

    //delete
    void deleteLibroById(Long idLibro);

}
