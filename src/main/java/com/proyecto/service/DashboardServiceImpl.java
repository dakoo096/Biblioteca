package com.proyecto.service;

import com.proyecto.domain.enums.EstadoPrestamo;
import com.proyecto.domain.enums.EstadoReserva;
import com.proyecto.dto.response.DashboardDTO;
import com.proyecto.dto.response.PrestamoResponseDTO;
import com.proyecto.repository.LibroRepository;
import com.proyecto.repository.PrestamoRepository;
import com.proyecto.repository.ReservaRepository;
import com.proyecto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;
    private final ReservaRepository reservaRepository;
    private final PrestamoService prestamoService;

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO obtenerMétricasDashboard() {
        long totalLibros = libroRepository.count();
        long disponibles = libroRepository.sumCantidadDisponible();
        long prestados = libroRepository.sumCantidadPrestados();
        long usuarios = usuarioRepository.count();

        long prestamosActivos = prestamoRepository.countByEstado(EstadoPrestamo.ACTIVO);
        long reservasActivas = reservaRepository.countByEstado(EstadoReserva.PENDIENTE);
        long prestamosVencidos = prestamoRepository.countPrestamosVencidos(LocalDate.now());

        List<PrestamoResponseDTO> ultimosPrestamos = prestamoRepository.findTop5ByOrderByFechaPrestamoDesc(PageRequest.of(0, 5))
                .stream()
                .map(prestamo -> prestamoService.obtenerPrestamoPorId(prestamo.getId()))
                .collect(Collectors.toList());

        return DashboardDTO.builder()
                .totalLibros(totalLibros)
                .librosDisponibles(disponibles)
                .librosPrestados(prestados)
                .usuariosRegistrados(usuarios)
                .prestamosActivos(prestamosActivos)
                .reservasActivas(reservasActivas)
                .prestamosVencidos(prestamosVencidos)
                .ultimosPrestamos(ultimosPrestamos)
                .build();
    }
}
