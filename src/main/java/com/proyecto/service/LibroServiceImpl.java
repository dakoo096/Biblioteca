
package com.proyecto.service;

import com.proyecto.entity.Libro;
import com.proyecto.repository.LibroRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroServiceImpl implements LibroService{
    //inyerctamos el repositorio
     @Autowired
    private LibroRepository libroRepository;
    @Override
    public List<Libro> findAllLibros() {
        return libroRepository.findAll();
    }

    @Override
    public Optional<Libro> findLibroById(Long iLibro) {
        return libroRepository.findById(iLibro);
    }

    @Override
    public Libro saveLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public void updateLibro(Long idLibro, Libro libro) {
        //primero obtenemos el libro y luego lo sobreescribimos
        Libro libroBD = findLibroById(idLibro).get();
        libroBD.setTitulo(libro.getTitulo());
        libroBD.setAutor(libro.getAutor());
        libroBD.setIsbn(libro.getIsbn());
        libroRepository.save(libro);
    }

    @Override
    public void deleteLibroById(Long idLibro) {
        libroRepository.deleteById(idLibro);
    }
    
}
