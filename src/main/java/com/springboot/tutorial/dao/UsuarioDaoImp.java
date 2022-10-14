package com.springboot.tutorial.dao;

import com.springboot.tutorial.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional

/*Repository:
* declara que va a tener la funcionalidad de
* poder acceder a la base de datos*/

/*Transactional:
* le provee a la clase los métodos para poder
* armar las consultas SQL a la base de datos.
* También como las va a armar y ejecutar, esto
* lo va a hacer en fragmentos de transacción.*/
public class UsuarioDaoImp implements UsuarioDao{

    @PersistenceContext
    EntityManager entityManager;

    /*EntityManager:
    * nos sirve para hacer la conexión con la
    * base de datos.*/

    @Override
    @Transactional
    public List<Usuario> getUsuario() {
        /*esta consulta es bastante parecida
        * a las de sql pero en realidad esta
        * haciendo una consulta a hibernate*/
        String query = "FROM Usuario";
        /*con este método del entity le decimos que
        * queremos que haga una consulta y que la
        * transforme en una lista.*/
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = entityManager.find(Usuario.class, id);
        entityManager.remove(usuario);
    }

    @Override
    public void registrar(Usuario usuario) {
        entityManager.merge(usuario);
    }

    @Override
    public Usuario obtenerUsuarioPorCredenciales(Usuario usuario) {
        String query = "FROM Usuario WHERE email = :email";
        List<Usuario> lista = entityManager.createQuery(query)
                .setParameter("email", usuario.getEmail())
                .getResultList();

        if (lista.isEmpty()){
            return null;
        }

        /*de esta manera tomamos un dato de la base de datos*/
        String passwordHashed = lista.get(0).getPassword();

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        if (argon2.verify(passwordHashed, usuario.getPassword().toCharArray())){
            return lista.get(0);
        }
        return null;
    }

}
