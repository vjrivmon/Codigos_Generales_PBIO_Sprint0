const medicionesFake = [
    { id: 1, hora: '10:00', latitud: 40.416775, longitud: -3.703790, id_sensor: 101, valorGas: 40.00, valorTemperatura: 35.00 },
    { id: 2, hora: '11:00', latitud: 41.385064, longitud: 2.173404, id_sensor: 102, valorGas: 30.00, valorTemperatura: 32.00 },
  ];
  
  const usuariosFake = [
    { id: 1, correo: 'usuario1@example.com', contrasenya: 'pass1' },
    { id: 2, correo: 'usuario2@example.com', contrasenya: 'pass2' },
  ];
  
  // Simulación de funciones
  function ConsultarMedida(idSensor) {
    return medicionesFake.filter(medicion => medicion.id_sensor === idSensor);
  }
  
  function agregarMedicion(medicion) {
    medicion.id = medicionesFake.length + 1;
    medicionesFake.push(medicion);
    return medicion;
  }
  
  function ConsultarDatosUsuario(idUsuario) {
    return usuariosFake.find(usuario => usuario.id === idUsuario);
  }
  
  function ConsultarSiHayAlerta(idSensor) {
    const medicion = medicionesFake.find(med => med.id_sensor === idSensor);
    return medicion && medicion.valorGas > 50;  // Ejemplo de alerta si valorGas > 50
  }
  
  function agregarUsuario(usuario) {
    usuario.id = usuariosFake.length + 1;
    usuariosFake.push(usuario);
    return usuario;
  }
  
  function EliminarUsuario(idUsuario) {
    const index = usuariosFake.findIndex(usuario => usuario.id === idUsuario);
    if (index !== -1) {
      return usuariosFake.splice(index, 1);
    }
    return null;
  }
  
  // Exportar las funciones para ser usadas en el frontend
  module.exports = {
    ConsultarMedida,
    agregarMedicion,
    ConsultarDatosUsuario,
    ConsultarSiHayAlerta,
    agregarUsuario,
    EliminarUsuario
  };
  