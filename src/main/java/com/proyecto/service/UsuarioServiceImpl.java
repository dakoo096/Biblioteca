package com.proyecto.service;

import com.proyecto.domain.entity.Usuario;
import com.proyecto.dto.request.UsuarioRequestDTO;
import com.proyecto.dto.response.UsuarioResponseDTO;
import com.proyecto.exception.BusinessRuleException;
import com.proyecto.exception.ResourceNotFoundException;
import com.proyecto.repository.UsuarioRepository;
import com.proyecto.domain.enums.Rol;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con ID: " + id));
        return convertirAResponseDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO) {
        if (usuarioRepository.existsByEmailIgnoreCase(requestDTO.getEmail())) {
            throw new BusinessRuleException("Ya existe un usuario registrado con el email: " + requestDTO.getEmail());
        }

        String rawPassword = requestDTO.getPassword();
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            rawPassword = "lector123";
        }

        Rol rolAsignado = Rol.ROLE_LECTOR;
        if (requestDTO.getRol() != null && !requestDTO.getRol().trim().isEmpty()) {
            try {
                rolAsignado = Rol.valueOf(requestDTO.getRol().trim());
            } catch (Exception ignored) {}
        }

        Usuario usuario = Usuario.builder()
                .nombre(requestDTO.getNombre())
                .apellido(requestDTO.getApellido())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(rawPassword))
                .rol(rolAsignado)
                .telefono(requestDTO.getTelefono())
                .activo(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return convertirAResponseDTO(guardado);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO requestDTO) {
        Usuario usuarioBD = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        if (!usuarioBD.getEmail().equalsIgnoreCase(requestDTO.getEmail()) && usuarioRepository.existsByEmailIgnoreCase(requestDTO.getEmail())) {
            throw new BusinessRuleException("Ya existe otro usuario registrado con el email: " + requestDTO.getEmail());
        }

        usuarioBD.setNombre(requestDTO.getNombre());
        usuarioBD.setApellido(requestDTO.getApellido());
        usuarioBD.setEmail(requestDTO.getEmail());
        usuarioBD.setTelefono(requestDTO.getTelefono());

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().trim().isEmpty()) {
            usuarioBD.setPassword(passwordEncoder.encode(requestDTO.getPassword().trim()));
        }

        if (requestDTO.getRol() != null && !requestDTO.getRol().trim().isEmpty()) {
            try {
                usuarioBD.setRol(Rol.valueOf(requestDTO.getRol().trim()));
            } catch (Exception ignored) {}
        }

        Usuario actualizado = usuarioRepository.save(usuarioBD);
        return convertirAResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró el usuario a eliminar con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDTO convertirAResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol() != null ? usuario.getRol().name() : "ROLE_LECTOR")
                .activo(usuario.getActivo() != null ? usuario.getActivo() : true)
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }
}
