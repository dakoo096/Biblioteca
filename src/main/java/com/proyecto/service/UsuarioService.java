package com.proyecto.service;

import com.proyecto.dto.request.UsuarioRequestDTO;
import com.proyecto.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    List<UsuarioResponseDTO> obtenerTodosLosUsuarios();

    UsuarioResponseDTO obtenerUsuarioPorId(Long id);

    UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO);

    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO requestDTO);

    void eliminarUsuario(Long id);
}
