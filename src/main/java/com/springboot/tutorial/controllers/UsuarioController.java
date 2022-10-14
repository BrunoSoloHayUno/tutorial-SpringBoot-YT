package com.springboot.tutorial.controllers;


import com.springboot.tutorial.dao.UsuarioDao;
import com.springboot.tutorial.models.Usuario;
import com.springboot.tutorial.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/*Controllers:
* sirven para manejar las urls
* por ejemplo "dominio/usuario"
* te va a llevar a un lado
* y dominio/empresa te llevaría a otro.
* */
@RestController
public class UsuarioController {

    /*Autowired:
    * hace que se cree un objeto de la clase definida
    * (en este caso UsuarioDao) y la guarda en esta
    * variable.
    * Si usamos el objeto con Autowired incluido en
    * otras partes del proyecto, este objeto va a estar
    * compartido en memoria, de esta manera se evita crear
    * demasiados objetos, ya que con que uno este declarado
    * una vez ya los demás hacen referencia al mismo sector
    * de memoria.*/
    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.GET)
    public Usuario getUsuario(@PathVariable Long id){
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Bruno");
        usuario.setApellido("Cyvian");
        usuario.setEmail("BrunoCyvian@hotmail.com");
        usuario.setTelefono("0303456");
        return usuario;
    }

    @RequestMapping(value = "api/usuarios")
    //con el RequestHeader guardamos el token que nos viene en la cabecera.
    public List<Usuario> listarUsuario(@RequestHeader(value="Authorization") String token){
        if(!validarToken(token)){
            return null;
        }
       return usuarioDao.getUsuario();
    }

    private boolean validarToken(String token){
        String usuarioId = jwtUtil.getKey(token);
        return usuarioId != null;
    }


    /*RequestBody: convierte el JSON que recibe en un usuario de manera automática*/
    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void registrarUsuario(@RequestBody Usuario usuario){

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, usuario.getPassword().toCharArray());
        usuario.setPassword(hash);


        usuarioDao.registrar(usuario);
    }

    @RequestMapping(value = "usuario45")
    public Usuario modificarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNombre("Bruno");
        usuario.setApellido("Cyvian");
        usuario.setEmail("BrunoCyvian@hotmail.com");
        usuario.setTelefono("0303456");
        return usuario;
    }

    /*Definir un metodo con RequestMethod solo indica el metodo a utilizar
    * pero no influye en la logica del metodo que estamos usando
    * por ende si indicamos un delete pero en la logica del metodo estamos
    * agregando uno nuevo esto ultimo es en realidad lo que va a pasar.*/
    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
    public void eliminarUsuario(@RequestHeader(value="Authorization") String token,@PathVariable Long id){

        if(!validarToken(token)){
            return;
        }
        usuarioDao.eliminar(id);
    }

    //@RequestMapping(value = "usuario123")
    //public Usuario buscarUsuario(){
    //    Usuario usuario = new Usuario();
    //    usuario.setNombre("Bruno");
    //    usuario.setApellido("Cyvian");
    //    usuario.setEmail("BrunoCyvian@hotmail.com");
    //    usuario.setTelefono("0303456");
    //    return usuario;
    //}


}
