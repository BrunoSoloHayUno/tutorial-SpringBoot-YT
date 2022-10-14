package com.springboot.tutorial.dao;

import com.springboot.tutorial.models.Usuario;

import java.util.List;

public interface UsuarioDao {

    List<Usuario> getUsuario();

    void eliminar(Long id);

    void registrar(Usuario usuario);

    Usuario obtenerUsuarioPorCredenciales(Usuario usuario);
}
