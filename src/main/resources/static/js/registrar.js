// Call the dataTables jQuery plugin
$(document).ready(function() {
/*aqui se ejecutara el codigo al cargar la pagina*/
});


async function registrarUsuarios(){
//los datos que vamos a enviar deben ser iguales
// a los que recibimos.

/*Aquí lo que hacemos es recibir todos los datos de la tabla/clase*/
    let datos = {};
    datos.nombre = document.getElementById('txtNombre').value;
    datos.apellido = document.getElementById('txtApellido').value;
    datos.email = document.getElementById('txtEmail').value;
    datos.password = document.getElementById('txtPassword').value;

    let repetirPassword = datos.repetirPassword = document.getElementById('txtRepetirPassword').value;

    if(repetirPassword != datos.password){
        alert('La contraseña que escribiste es diferente.');
        return;
    }

//esto es una request:
/*aqui es donde se envian estos datos al servidor.*/
    onst request = await fetch('api/usuarios', {
     method: 'POST',
     headers: {
       'Accept': 'application/json',
       'Content-Type': 'application/json'
     },
  /*JSON.stringify:
  * Toma cualquier objeto
  * de JS y lo convierte
  * a un string de JSON*/
    body: JSON.stringify(datos)
  });
  alert("la cuenta fue creada con éxito");
  window.location.href = 'login.html'
}