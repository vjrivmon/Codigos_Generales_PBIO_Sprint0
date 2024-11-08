/**
 * @file app.test.js
 * @brief Llamadas con funciones de prueba para interactuar con la base de datos.
 */

require('dotenv').config({ path: '../variables.env' });

/**
 * @module request
 * @description Módulo para realizar solicitudes HTTP utilizando Supertest.
 */
const request = require('supertest');

 /**
 * @module express
 * @description Framework web Node.js para crear aplicaciones web.
 */
const express = require('express');

/**
 * @module servidorREST
 * @brief Módulo que contiene las funciones para interactuar con la base de datos a través del API REST.
 */
const {
  ConsultarMedida, // Función para consultar medición de un sensor
  agregarMedicion, // Función para agregar una nueva medicion al sistema
  ConsultarDatosUsuario, // Función para consultar datos del usuario
  ConsultarSiHayAlerta, // Función para consultar si hay alerta en el sistema
  agregarUsuario, // Función para agregar un nuevo usuario al sistema
  EliminarUsuario, // Función para eliminar un usuario del sistema
  ConsultarBaseDeDatos, // Función para consultar toda la base de datos
  verificarUsuario // Función para verificar un usuario y su contraseña
} = require('../servidorREST'); // Importar las funciones de servidorREST

/**
 * @var app
 * @brief Aplicación Express configurada para pruebas.
 */
const app = express();
app.use(express.json()); // Middleware para parsear JSON

 /**
 * @brief Configurar las rutas usando las funciones de servidorREST.
 */
// Configura la aplicación de Express para pruebas
app.get('/mediciones/:id_sensor', ConsultarMedida); // Ruta para consultar medición de un sensor
app.post('/mediciones', agregarMedicion); // Ruta para agregar una nueva medicion al sistema
app.get('/usuarios/:id_usuario', ConsultarDatosUsuario); // Ruta para consultar datos del usuario
app.post('/usuarios', agregarUsuario); // Ruta para agregar un nuevo usuario al sistema
app.delete('/usuarios/:id_usuario', EliminarUsuario); // Ruta para eliminar un usuario del sistema
app.get('/mediciones/:id_sensor', ConsultarSiHayAlerta); // Ruta para consultar si hay alerta en el sistema
app.post('/verificar-usuario', verificarUsuario); // Ruta para verificar un usuario y su contraseña
app.get('/base-datos', ConsultarBaseDeDatos); // Ruta para consultar toda la base de datos

/**
 * @file app-test.js
 * @brief Tests unitarios para la API REST.
 */
describe('API REST Tests', () => {
  /**
   * @function runTest
   * @brief Función para ejecutar un test y mostrar su resultado.
   *
   * @param {string} testName Nombre del test
   * @param {function} testFunction Función que realizará el test
   */
  const runTest = async (testName, testFunction) => {
    try {
      await testFunction();
      console.log(`${testName} - passed`); // Si el test pasa, mostrar mensaje en consola
    } catch (error) {
      console.error(`${testName} - failed`); // Si el test falla, mostrar mensaje en consola
      throw error; // Re-throw para asegurar que la suite de tests falle
    }
  };

  /**
   * @test GET /mediciones/:id_sensor - debería devolver mediciones para un sensor
   */
  test('GET /mediciones/:id_sensor - debería devolver mediciones para un sensor', async () => {
    await runTest('GET /mediciones/:id_sensor', async () => {
      const response = await request(app).get('/mediciones/00:1A:2B:3M:4D:5E');
      expect(response.statusCode).toBe(200); // El estado de respuesta debe ser 200
      expect(response.body).toBeInstanceOf(Array); // La respuesta debe ser un array
    });
  }, 10000);

  /**
   * @test POST /mediciones - debería agregar una nueva medicion
   */
  test('POST /mediciones - debería agregar una nueva medicion', async () => {
    await runTest('POST /mediciones', async () => {
      const newMeasurement = { // Datos para la nueva medición
        hora: '14:00',
        latitud: 40.416775,
        longitud: -3.703790,
        id_sensor: '00:1A:2B:3M:4D:5E',
        valorGas: 50.00,
        valorTemperatura: 30.00
      };
      const response = await request(app).post('/mediciones').send(newMeasurement);
      expect(response.statusCode).toBe(201); // El estado de respuesta debe ser 201
      expect(response.body).toMatchObject(newMeasurement); // La respuesta debe coincidir con la medición nueva
    });
  }, 10000);

  /**
   * @test GET /usuarios/:id_usuario - debería devolver datos del usuario
   */
  test('GET /usuarios/:id_usuario - debería devolver datos del usuario', async () => {
    await runTest('GET /usuarios/:id_usuario', async () => {
      const response = await request(app).get('/usuarios/1');
      expect(response.statusCode).toBe(200); // El estado de respuesta debe ser 200
      expect(response.body).toBeInstanceOf(Object); // La respuesta debe ser un objeto
    });
  }, 10000);

  /**
   * @test POST /usuarios - debería agregar un nuevo usuario
   */
  test('POST /usuarios - debería agregar un nuevo usuario', async () => {
    await runTest('POST /usuarios', async () => {
      const newUser = { // Datos para el nuevo usuario
        nombre: 'Test',
        telefono: '123456789',
        correo: 'test@correo.com',
        contrasena: '123456'
      };
      const response = await request(app).post('/usuarios').send(newUser);
      expect(response.statusCode).toBe(201); // El estado de respuesta debe ser 201
      expect(response.body).toMatchObject(newUser); // La respuesta debe coincidir con el nuevo usuario
    });
  }, 10000);

  /**
   * @test DELETE /usuarios/:id_usuario - debería eliminar un usuario
   */
  test('DELETE /usuarios/:id_usuario - debería eliminar un usuario', async () => {
    await runTest('DELETE /usuarios/:id_usuario', async () => {
      const response = await request(app).delete('/usuarios/1');
      if (response.statusCode === 200) { // Si el estado de respuesta es 200, verificar el mensaje
        expect(response.text).toBe('Usuario eliminado correctamente'); // El mensaje debe ser "Usuario eliminado correctamente"
      } else { // Si el estado de respuesta no es 200, verificar el mensaje
        expect(response.statusCode).toBe(404); // El estado de respuesta debe ser 404
        expect(response.text).toBe('Usuario no encontrado'); // El mensaje debe ser "Usuario no encontrado"
      }
    });
  }, 10000);

  /**
   * @test GET /mediciones/:id_sensor - debería comprobar si hay alerta de gas
   */
  test('GET /mediciones/:id_sensor - debería comprobar si hay alerta de gas', async () => {
    await runTest('GET /mediciones/:id_sensor - verificación de alerta', async () => {
      const response = await request(app).get('/mediciones/00:1A:2B:3M:4D:5E');
      expect(response.statusCode).toBe(200); // El estado de respuesta debe ser 200
      expect(response.body).toHaveProperty('hayAlerta'); // La respuesta debe tener la propiedad "hayAlerta"
    });
  }, 10000);

  /**
   * @test GET /base-datos - debería devolver todas las tablas de la base de datos
   */
  test('GET /base-datos - debería devolver todas las tablas de la base de datos', async () => {
    await runTest('GET /base-datos', async () => {
      const response = await request(app).get('/base-datos');
      expect(response.statusCode).toBe(200);
      expect(response.body).toHaveProperty('mediciones');
      expect(response.body).toHaveProperty('usuarios');
      expect(response.body).toHaveProperty('sensores');
      expect(response.statusCode).toBe(200); // El estado de respuesta debe ser 200
      expect(response.body).toHaveProperty('mediciones'); // La respuesta debe tener la propiedad "mediciones"
      expect(response.body).toHaveProperty('usuarios'); // La respuesta debe tener la propiedad "usuarios"
      expect(response.body).toHaveProperty('sensores'); // La respuesta debe tener la propiedad "sensores"
    });
  }, 10000);

  /**
   * @test PUT /usuarios/contrasena - debería actualizar la contraseña del usuario
   */
  test('PUT /usuarios/contrasena - debería actualizar la contraseña del usuario', async () => {
    await runTest('PUT /usuarios/contrasena', async () => {
      const updatePassword = { // Datos para actualizar la contraseña
        correo: 'test@correo.com',
        nuevaContrasena: 'nueva123'
      };
      const response = await request(app).put('/usuarios/contrasena').send(updatePassword);
      expect(response.statusCode).toBe(200); // El estado de respuesta debe ser 200
      expect(response.text).toBe('Contraseña actualizada correctamente'); // El mensaje debe ser "Contraseña actualizada correctamente"
    });
  }, 10000);

  /**
   * @test PUT /usuarios - debería actualizar los datos del usuario
   */
  test('PUT /usuarios - debería actualizar los datos del usuario', async () => {
    await runTest('PUT /usuarios', async () => {
      const updateUser = { // Datos para actualizar el usuario
        id_usuario: 1,
        nombre: 'Nuevo Nombre',
        telefono: '123456789',
        correo: 'nuevo@correo.com',
        contrasena: 'nueva123'
      };
      const response = await request(app).put('/usuarios').send(updateUser);
      expect(response.statusCode).toBe(200); // El estado de respuesta debe ser 200
      expect(response.text).toBe('Datos del usuario actualizados correctamente'); // El mensaje debe ser "Datos del usuario actualizados correctamente"
    });
  }, 10000);
});
