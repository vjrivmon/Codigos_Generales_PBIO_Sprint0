// Simulación de datos para pruebas y desarrollo.
/**
 * @brief Colección de mediciones simuladas.
 */
const medicionesFake = [
    /**
     * Medición simulada número 1.
     *
     * @param id Identificador único.
     * @param hora Hora de la medición en formato 'HH:mm'.
     * @param latitud Latitud del lugar donde se realizó la medición (en grados decimales).
     * @param longitud Longitud del lugar donde se realizó la medición (en grados decimales).
     * @param id_sensor Identificador único del sensor que realizó la medición.
     * @param valorGas Valor de gas medido en partes por millón.
     * @param valorTemperatura Valor de temperatura medido en grados Celsius.
     */
    { id: 1, hora: '10:00', latitud: 40.416775, longitud: -3.703790, id_sensor: 101, valorGas: 40.00, valorTemperatura: 35.00 },
    /**
     * Medición simulada número 2.
     */
    { id: 2, hora: '11:00', latitud: 41.385064, longitud: 2.173404, id_sensor: 102, valorGas: 30.00, valorTemperatura: 32.00 },
];
  
/**
 * @brief Colección de usuarios simulados.
 */
  const usuariosFake = [
    /**
     * Usuario simulado número 1.
     *
     * @param id Identificador único.
     * @param correo Correo electrónico del usuario.
     * @param contrasenya Contraseña del usuario (no recomendada en producción).
     */
    { id: 1, correo: 'usuario1@example.com', contrasenya: 'pass1' },
    /**
     * Usuario simulado número 2.
     */
    { id: 2, correo: 'usuario2@example.com', contrasenya: 'pass2' },
  ];
  
  // Simulación de funciones

/**
 * @brief Consulta una medición específica por su identificador de sensor.
 *
 * @param idSensor Identificador del sensor cuya medición se desea consultar.
 * @return Colección de mediciones (puede ser vacía si no se encuentra ninguna).
 */
  function ConsultarMedida(idSensor) {
    return medicionesFake.filter(medicion => medicion.id_sensor === idSensor);
  }
  
/**
 * @brief Agrega una nueva medición a la colección de mediciones.
 *
 * @param medicion Medición a agregar (con el identificador automáticamente asignado).
 * @return La medición agregada con su nuevo identificador.
 */
  function agregarMedicion(medicion) {
    medicion.id = medicionesFake.length + 1;
    medicionesFake.push(medicion);
    return medicion;
  }
  
/**
 * @brief Consulta datos de un usuario específico por su identificador.
 *
 * @param idUsuario Identificador del usuario cuyos datos se desean consultar.
 * @return Objeto de usuario (null si no existe).
 */
  function ConsultarDatosUsuario(idUsuario) {
    return usuariosFake.find(usuario => usuario.id === idUsuario);
  }
  
/**
 * @brief Verifica si hay una alerta para un sensor específico.
 *
 * @param idSensor Identificador del sensor que se está verificando.
 * @return True si hay una alerta, false en caso contrario.
 */
  function ConsultarSiHayAlerta(idSensor) {
    const medicion = medicionesFake.find(med => med.id_sensor === idSensor);
    return medicion && medicion.valorGas > 50;  // Ejemplo de alerta si valorGas > 50
  }
  
/**
 * @brief Agrega un nuevo usuario a la colección de usuarios.
 *
 * @param usuario Usuario a agregar (con el identificador automáticamente asignado).
 * @return El usuario agregado con su nuevo identificador.
 */
  function agregarUsuario(usuario) {
    usuario.id = usuariosFake.length + 1;
    usuariosFake.push(usuario);
    return usuario;
  }
  
/**
 * @brief Elimina un usuario específico de la colección de usuarios.
 *
 * @param idUsuario Identificador del usuario a eliminar.
 * @return El objeto eliminado (null si no existe).
 */
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
