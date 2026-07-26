package com.proyecto.service;

import com.proyecto.domain.entity.Libro;
import com.proyecto.domain.entity.Resenia;
import com.proyecto.domain.entity.Usuario;
import com.proyecto.dto.response.ReseniaDTO;
import com.proyecto.exception.BusinessRuleException;
import com.proyecto.exception.ResourceNotFoundException;
import com.proyecto.repository.LibroRepository;
import com.proyecto.repository.ReseniaRepository;
import com.proyecto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReseniaServiceImpl implements ReseniaService {

    private final ReseniaRepository reseniaRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ReseniaDTO agregarResenia(Long libroId, String userEmail, Integer puntuacion, String comentario) {
        if (puntuacion == null || puntuacion < 1 || puntuacion > 5) {
            throw new BusinessRuleException("La puntuación debe ser entre 1 y 5 estrellas.");
        }

        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el libro con ID: " + libroId));

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con email: " + userEmail));

        Resenia resenia = Resenia.builder()
                .libro(libro)
                .usuario(usuario)
                .puntuacion(puntuacion)
                .comentario(comentario != null ? comentario.trim() : "")
                .build();

        Resenia guardada = reseniaRepository.save(resenia);
        return convertirADTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReseniaDTO> obtenerReseniasPorLibro(Long libroId) {
        return reseniaRepository.findByLibroIdOrderByFechaCreacionDesc(libroId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double obtenerPromedioCalificacion(Long libroId) {
        Double promedio = reseniaRepository.obtenerPromedioCalificacionPorLibro(libroId);
        return promedio != null ? Math.round(promedio * 10.0) / 10.0 : 0.0;
    }

    private ReseniaDTO convertirADTO(Resenia resenia) {
        return ReseniaDTO.builder()
                .id(resenia.getId())
                .libroId(resenia.getLibro().getId())
                .libroTitulo(resenia.getLibro().getTitulo())
                .usuarioNombre(resenia.getUsuario().getNombreCompleto())
                .puntuacion(resenia.getPuntuacion())
                .comentario(resenia.getComentario())
                .fechaCreacion(resenia.getFechaCreacion())
                .build();
    }
}
