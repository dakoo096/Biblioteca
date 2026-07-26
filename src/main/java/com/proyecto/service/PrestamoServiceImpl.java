package com.proyecto.service;

import com.proyecto.domain.entity.Libro;
import com.proyecto.domain.entity.Prestamo;
import com.proyecto.domain.entity.Reserva;
import com.proyecto.domain.entity.Usuario;
import com.proyecto.domain.enums.EstadoPrestamo;
import com.proyecto.domain.enums.EstadoReserva;
import com.proyecto.dto.request.PrestamoRequestDTO;
import com.proyecto.dto.response.AutorDTO;
import com.proyecto.dto.response.CategoriaDTO;
import com.proyecto.dto.response.EditorialDTO;
import com.proyecto.dto.response.LibroResponseDTO;
import com.proyecto.dto.response.PrestamoResponseDTO;
import com.proyecto.dto.response.UsuarioResponseDTO;
import com.proyecto.exception.BusinessRuleException;
import com.proyecto.exception.InsufficientStockException;
import com.proyecto.exception.ResourceNotFoundException;
import com.proyecto.repository.LibroRepository;
import com.proyecto.repository.PrestamoRepository;
import com.proyecto.repository.ReservaRepository;
import com.proyecto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;

    @Override
    @Transactional
    public PrestamoResponseDTO registrarPrestamo(PrestamoRequestDTO requestDTO) {
        Libro libro = libroRepository.findById(requestDTO.getLibroId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el libro con ID: " + requestDTO.getLibroId()));

        Usuario usuario = usuarioRepository.findById(requestDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con ID: " + requestDTO.getUsuarioId()));

        if (!libro.tieneStockDisponible()) {
            throw new InsufficientStockException("El libro '" + libro.getTitulo() + "' no cuenta con stock disponible para préstamo. Puedes realizar una reserva.");
        }

        if (requestDTO.getFechaLimite().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("La fecha límite del préstamo no puede ser en el pasado.");
        }

        libro.decrementarStock();
        libroRepository.save(libro);

        Prestamo prestamo = Prestamo.builder()
                .libro(libro)
                .usuario(usuario)
                .fechaPrestamo(LocalDate.now())
                .fechaLimite(requestDTO.getFechaLimite())
                .estado(EstadoPrestamo.ACTIVO)
                .build();

        Prestamo guardado = prestamoRepository.save(prestamo);
        return convertirAResponseDTO(guardado);
    }

    @Override
    @Transactional
    public PrestamoResponseDTO devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el préstamo con ID: " + prestamoId));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BusinessRuleException("Este préstamo ya fue devuelto previamente.");
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo.setFechaDevolucion(LocalDate.now());

        Libro libro = prestamo.getLibro();
        libro.incrementarStock();
        libroRepository.save(libro);

        // Si hay reservas pendientes para este libro, marcar la primera reserva como notificada/disponible
        List<Reserva> reservasPendientes = reservaRepository.findByLibroIdAndEstadoOrderByFechaReservaAsc(libro.getId(), EstadoReserva.PENDIENTE);
        if (!reservasPendientes.isEmpty()) {
            Reserva primeraReserva = reservasPendientes.get(0);
            primeraReserva.setEstado(EstadoReserva.CUMPLIDA);
            reservaRepository.save(primeraReserva);
        }

        Prestamo actualizado = prestamoRepository.save(prestamo);
        return convertirAResponseDTO(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> obtenerPrestamosActivos() {
        return prestamoRepository.findByEstado(EstadoPrestamo.ACTIVO, Pageable.unpaged())
                .getContent().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrestamoResponseDTO> obtenerTodosLosPrestamos(Pageable pageable) {
        return prestamoRepository.findAll(pageable)
                .map(this::convertirAResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponseDTO obtenerPrestamoPorId(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el préstamo con ID: " + id));
        return convertirAResponseDTO(prestamo);
    }

    @Override
    @Transactional
    public void verificarYActualizarPrestamosVencidos() {
        List<Prestamo> vencidos = prestamoRepository.findPrestamosVencidos(LocalDate.now());
        for (Prestamo prestamo : vencidos) {
            prestamo.setEstado(EstadoPrestamo.VENCIDO);
        }
        prestamoRepository.saveAll(vencidos);
    }

    private PrestamoResponseDTO convertirAResponseDTO(Prestamo prestamo) {
        Libro libro = prestamo.getLibro();
        LibroResponseDTO libroDTO = LibroResponseDTO.builder()
                .id(libro.getId())
                .titulo(libro.getTitulo())
                .isbn(libro.getIsbn())
                .cantidadDisponible(libro.getCantidadDisponible())
                .cantidadTotal(libro.getCantidadTotal())
                .portadaUrl(libro.getPortadaUrl())
                .autor(libro.getAutor() != null ? AutorDTO.builder()
                        .id(libro.getAutor().getId())
                        .nombre(libro.getAutor().getNombre())
                        .apellido(libro.getAutor().getApellido())
                        .build() : null)
                .categoria(libro.getCategoria() != null ? CategoriaDTO.builder()
                        .id(libro.getCategoria().getId())
                        .nombre(libro.getCategoria().getNombre())
                        .build() : null)
                .editorial(libro.getEditorial() != null ? EditorialDTO.builder()
                        .id(libro.getEditorial().getId())
                        .nombre(libro.getEditorial().getNombre())
                        .build() : null)
                .build();

        Usuario usuario = prestamo.getUsuario();
        UsuarioResponseDTO usuarioDTO = UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .build();

        return PrestamoResponseDTO.builder()
                .id(prestamo.getId())
                .libro(libroDTO)
                .usuario(usuarioDTO)
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaLimite(prestamo.getFechaLimite())
                .fechaDevolucion(prestamo.getFechaDevolucion())
                .estado(prestamo.getEstado())
                .vencido(prestamo.estaVencido())
                .build();
    }
}
