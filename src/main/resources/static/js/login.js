// Call the dataTables jQuery plugin
$(document).ready(function() {
/*aqui se ejecutara el codigo al cargar la pagina*/
});


async function iniciarSesion(){
//los datos que vamos a enviar deben ser iguales
// a los que recibimos.

/*Aquí lo que hacemos es recibir todos los datos de la tabla/clase*/
    let datos = {};
    datos.email = document.getElementById('txtEmail').value;
    datos.password = document.getElementById('txtPassword').value;

//esto es una request:
/*aqui es donde se envian estos datos al servidor.*/
const request = await fetch('api/login', {
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

const respuesta = await request.text();
if(respuesta != 'Cagaste'){
//de esta manera guardamos el toquen en el navegador.
    localStorage.token = respuesta;
    localStorage.email = datos.email;
    window.location.href = 'usuarios.html'
} else {
    alert("Las credenciales son incorrectas. Por favor intente nuevamente.");
}
}