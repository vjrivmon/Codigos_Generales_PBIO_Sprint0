require('dotenv').config({ path: '../variables.env' });
const request = require('supertest');
const express = require('express');
const chalk = require('chalk');
const {
  ConsultarMedida,
  agregarMedicion,
  ConsultarDatosUsuario,
  ConsultarSiHayAlerta,
  agregarUsuario,
  EliminarUsuario,
  ConsultarBaseDeDatos,
  verificarUsuario
} = require('../servidorREST'); // Importar las funciones de servidorREST

// Configura la aplicación de Express para pruebas
const app = express();
app.use(express.json()); // Middleware para parsear JSON

// Configurar las rutas usando las funciones de servidorREST
app.get('/mediciones/:id_sensor', ConsultarMedida);
app.post('/mediciones', agregarMedicion);
app.get('/usuarios/:id_usuario', ConsultarDatosUsuario);
app.post('/usuarios', agregarUsuario);
app.delete('/usuarios/:id_usuario', EliminarUsuario);
app.get('/mediciones/:id_sensor', ConsultarSiHayAlerta);
app.get('/usuarios', verificarUsuario);
app.get('/base-datos', ConsultarBaseDeDatos);

describe('API REST Tests', () => {
  const runTest = async (testName, testFunction) => {
    try {
      await testFunction();
      console.log(chalk.green(`${testName} - passed`));
    } catch (error) {
      console.error(chalk.red(`${testName} - failed`));
      throw error; // Re-throw to ensure the test suite fails
    }
  };

  test('GET /mediciones/:id_sensor - should return measurements for a sensor', async () => {
    await runTest('GET /mediciones/:id_sensor', async () => {
      const response = await request(app).get('/mediciones/1');
      expect(response.statusCode).toBe(200);
      expect(response.body).toBeInstanceOf(Array);
    });
  }, 10000);

  test('POST /mediciones - should add a new measurement', async () => {
    await runTest('POST /mediciones', async () => {
      const newMeasurement = {
        hora: '14:00',
        latitud: 40.416775,
        longitud: -3.703790,
        valorGas: 50.00,
        valorTemperatura: 30.00
      };
      const response = await request(app).post('/mediciones').send(newMeasurement);
    expect(response.statusCode).toBe(201);
      expect(response.body).toMatchObject(newMeasurement);
    });
  }, 10000);

  test('GET /usuarios/:id_usuario - should return user data', async () => {
    await runTest('GET /usuarios/:id_usuario', async () => {
      const response = await request(app).get('/usuarios/1');
      expect(response.statusCode).toBe(200);
      expect(response.body).toBeInstanceOf(Object);
    });
  }, 10000);

  test('POST /usuarios - should add a new user', async () => {
    await runTest('POST /usuarios', async () => {
      const newUser = {
        correo: 'test@correo.com',
        contrasena: '123456'
      };
      const response = await request(app).post('/usuarios').send(newUser);
      expect(response.statusCode).toBe(201);
      expect(response.body).toMatchObject(newUser);
    });
  }, 10000);

  test('DELETE /usuarios/:id_usuario - should delete a user', async () => {
    await runTest('DELETE /usuarios/:id_usuario', async () => {
      const response = await request(app).delete('/usuarios/1');
      if (response.statusCode === 200) {
        expect(response.text).toBe('Usuario eliminado correctamente');
      } else {
        expect(response.statusCode).toBe(404);
        expect(response.text).toBe('Usuario no encontrado');
      }
});
  }, 10000);

  test('GET /mediciones/:id_sensor - should check for gas alert', async () => {
    await runTest('GET /mediciones/:id_sensor - alert check', async () => {
      const response = await request(app).get('/mediciones/1');
      expect(response.statusCode).toBe(200);
      expect(response.body).toHaveProperty('hayAlerta');
    });
  }, 10000);

  test('GET /base-datos - should return all database tables', async () => {
    await runTest('GET /base-datos', async () => {
      const response = await request(app).get('/base-datos');
      expect(response.statusCode).toBe(200);
      expect(response.body).toHaveProperty('mediciones');
      expect(response.body).toHaveProperty('usuarios');
      expect(response.body).toHaveProperty('sensores');
    });
  }, 10000);
});