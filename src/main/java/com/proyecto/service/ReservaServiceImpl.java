package com.proyecto.service;

import com.proyecto.domain.entity.Libro;
import com.proyecto.domain.entity.Reserva;
import com.proyecto.domain.entity.Usuario;
import com.proyecto.domain.enums.EstadoReserva;
import com.proyecto.dto.request.ReservaRequestDTO;
import com.proyecto.dto.response.AutorDTO;
import com.proyecto.dto.response.LibroResponseDTO;
import com.proyecto.dto.response.ReservaResponseDTO;
import com.proyecto.dto.response.UsuarioResponseDTO;
import com.proyecto.exception.BusinessRuleException;
import com.proyecto.exception.ResourceNotFoundException;
import com.proyecto.repository.LibroRepository;
import com.proyecto.repository.ReservaRepository;
import com.proyecto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ReservaResponseDTO crearReserva(ReservaRequestDTO requestDTO) {
        Libro libro = libroRepository.findById(requestDTO.getLibroId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + requestDTO.getLibroId()));

        Usuario usuario = usuarioRepository.findById(requestDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + requestDTO.getUsuarioId()));

        if (libro.tieneStockDisponible()) {
            throw new BusinessRuleException("No es necesario reservar este libro porque tiene stock disponible para préstamo inmediato.");
        }

        if (reservaRepository.existsByLibroIdAndUsuarioIdAndEstado(libro.getId(), usuario.getId(), EstadoReserva.PENDIENTE)) {
            throw new BusinessRuleException("El usuario ya tiene una reserva activa para este libro.");
        }

        Reserva reserva = Reserva.builder()
                .libro(libro)
                .usuario(usuario)
                .estado(EstadoReserva.PENDIENTE)
                .build();

        Reserva guardada = reservaRepository.save(reserva);
        return convertirAResponseDTO(guardada);
    }

    @Override
    @Transactional
    public void cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + reservaId));

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorLibro(Long libroId) {
        return reservaRepository.findByLibroIdAndEstadoOrderByFechaReservaAsc(libroId, EstadoReserva.PENDIENTE).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerTodasLasReservas() {
        return reservaRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    private ReservaResponseDTO convertirAResponseDTO(Reserva reserva) {
        Libro libro = reserva.getLibro();
        LibroResponseDTO libroDTO = LibroResponseDTO.builder()
                .id(libro.getId())
                .titulo(libro.getTitulo())
                .isbn(libro.getIsbn())
                .portadaUrl(libro.getPortadaUrl())
                .autor(libro.getAutor() != null ? AutorDTO.builder()
                        .nombre(libro.getAutor().getNombre())
                        .apellido(libro.getAutor().getApellido())
                        .build() : null)
                .build();

        Usuario usuario = reserva.getUsuario();
        UsuarioResponseDTO usuarioDTO = UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .build();

        return ReservaResponseDTO.builder()
                .id(reserva.getId())
                .libro(libroDTO)
                .usuario(usuarioDTO)
                .fechaReserva(reserva.getFechaReserva())
                .estado(reserva.getEstado())
                .build();
    }
}
