// APIRest.js
const express = require('express');
const cors = require('cors');
const swaggerJsDoc = require('swagger-jsdoc');
const swaggerUi = require('swagger-ui-express');
const dotenv = require('dotenv');
const router = express.Router();


dotenv.config();

const {
  ConsultarMedida,
  agregarMedicion,
  ConsultarDatosUsuario,
  ConsultarSiHayAlerta,
  agregarUsuario,
  EliminarUsuario,
  ConsultarBaseDeDatos,
  verificarUsuario
} = require('./servidorREST'); // Importar lógica de negocio desde el archivo separado
// Inicializar app y cargar variables de entorno
// const app = express();

// Middleware para procesar JSON
// app.use(cors());
// app.use(express.json());

// Middleware para procesar JSON
router.use(cors());
router.use(express.json());

/*
app.get('/mediciones/:id_sensor', ConsultarMedida);
app.post('/mediciones', agregarMedicion);
app.get('/usuarios/:id_usuario', ConsultarDatosUsuario);
app.post('/usuarios', agregarUsuario);
app.delete('/usuarios/:id_usuario', EliminarUsuario);
app.get('/mediciones/:id_sensor', ConsultarSiHayAlerta);
*/

router.get('/mediciones/:id_sensor', ConsultarMedida);
router.post('/mediciones', agregarMedicion);
router.get('/usuarios/:id_usuario', ConsultarDatosUsuario);
router.post('/usuarios', agregarUsuario);
router.delete('/usuarios/:id_usuario', EliminarUsuario);
router.get('/mediciones/:id_sensor', ConsultarSiHayAlerta);
router.get('/usuarios', verificarUsuario);
//router.post('/asociar-sensor', asociarSensorAUsuario);

// Pruebas de depuración
console.log('Configuración inicial cargada');

router.get('/mediciones/:id_sensor', (req, res) => {
  console.log('Ruta /mediciones/:id_sensor accedida');
  ConsultarMedida(req, res);
});

router.post('/mediciones', (req, res) => {
  console.log('Ruta /mediciones accedida');
  agregarMedicion(req, res);
});

router.get('/usuarios/:id_usuario', (req, res) => {
  console.log('Ruta /usuarios/:id_usuario accedida');
  ConsultarDatosUsuario(req, res);
});

router.post('/usuarios', (req, res) => {
  console.log('Ruta /usuarios accedida');
  agregarUsuario(req, res);
});

router.delete('/usuarios/:id_usuario', (req, res) => {
  console.log('Ruta /usuarios/:id_usuario accedida');
  EliminarUsuario(req, res);
});

router.get('/mediciones/:id_sensor', (req, res) => {
  console.log('Ruta /mediciones/:id_sensor accedida');
  ConsultarSiHayAlerta(req, res);
});

router.get('/base-datos', ConsultarBaseDeDatos);

// Pruebas de depuración
console.log('Configuración de rutas cargada');

// Swagger Setup
const swaggerOptions = {
  swaggerDefinition: {
    openapi: '3.0.0',
    info: {
      title: 'API REST de Mediciones',
      version: '1.0.0',
      description: 'API para gestionar las mediciones'
    },
    servers: [
      {
        url: 'http://localhost:8080'
      }
    ]
  },
  apis: ['./APIRest.js']
};
const swaggerDocs = swaggerJsDoc(swaggerOptions);
// app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs));
router.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs));

// Rutas de la API que usan la lógica de negocio
/*
app.get('/mediciones', servidorRest.ConsultarMedida);
app.post('/mediciones', servidorRest.agregarMedicion);
app.get('/usuarios', servidorRest.ConsultarDatosUsuario);
app.get('/mediciones', servidorRest.ConsultarSiHayAlerta);
app.post('/usuarios', servidorRest.agregarUsuario);
app.delete('/usuarios', servidorRest.EliminarUsuario);
*/
// Levantar servidor
module.exports = router; // Exportar el router

// Ejemplo de uso de la API
/**
 * @swagger
 * components:
 *   schemas:
 *     Medicion:
 *       type: object
 *       required:
 *         - id
 *         - hora
 *         - latitud
 *         - longitud
 *         - valorGas
 *         - valorTemperatura
 *       properties:
 *         id:
 *           type: integer
 *           description: ID de la medición
 *         hora:
 *           type: string
 *           description: Hora de la medición
 *         latitud:
 *           type: number
 *           format: double
 *           description: latitud de la medición
 *         longitud:
 *           type: number
 *           format: double
 *           description: longitud de la medición
 *         id_sensor:
 *           type: integer
 *           description: ID del sensor
 *         valorGas:
 *           type: number
 *           description: Valor de la medición de Gas
 *         valorTemperatura:
 *           type: number
 *           description: Valor de la medición de Temperatura
 *       example:
 *         hora: '10:00'
 *         latitud: 40.416775
 *         longitud: -3.703790
 *         valorGas: 40.00
 *         valorTemperatura: 32.00
 */

// Esquema de Swagger para Usuario
/**
 * @swagger
 * components:
 *   schemas:
 *     Usuario:
 *       type: object
 *       required:
 *         - correo
 *         - contrasena
 *       properties:
 *         correo:
 *           type: string
 *           description: Correo electrónico del usuario
 *         contrasenya:
 *           type: string
 *           description: Contraseña del usuario
 *       example:
 *         correo: 'ejemplo@correo.com'
 *         contrasena: '123456'
 */

// TÍTULO DE LA API en Swagger

/**
 * @swagger
 * tags:
 *   name: Mediciones
 *   description: API para gestionar las mediciones de los sensores de Gas y Temperatura en diferentes ubicaciones
 * 
 * @swagger
 * tags:
 *   name: Usuarios
 *   description: API para gestionar los usuarios de la aplicación
 */


// Método GET para consultar medida por id_sensor

/**
 * @swagger
 * /mediciones/{id_sensor}:
 *   get:
 *     summary: Obtiene las mediciones por id del sensor
 *     tags: [Mediciones]
 *     parameters:
 *       - in: path
 *         name: id_sensor
 *         schema:
 *           type: integer
 *         required: true
 *         description: ID del sensor
 *     responses:
 *       200:
 *         description: Lista de mediciones del sensor
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 $ref: '#/components/schemas/Medicion'
 */


// POST para agregar medición
/**
 * @swagger
 * /mediciones:
 *   post:
 *     summary: Agrega una nueva medición
 *     tags: [Mediciones]
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/Medicion'
 *     responses:
 *       201:
 *         description: Medición creada exitosamente
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Medicion'
 */


// GET para consultar usuario por id
/**
 * @swagger
 * /usuarios/{id_usuario}:
 *   get:
 *     summary: Obtiene los datos de un usuario por id
 *     tags: [Usuarios]
 *     parameters:
 *       - in: path
 *         name: id_usuario
 *         schema:
 *           type: integer
 *         required: true
 *         description: ID del usuario
 *     responses:
 *       200:
 *         description: Datos del usuario
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Usuario'
 */


// POST para agregar un nuevo usuario
/**
 * @swagger
 * /usuarios:
 *   post:
 *     summary: Agrega un nuevo usuario
 *     tags: [Usuarios]
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/Usuario'
 *     responses:
 *       201:
 *         description: Usuario creado exitosamente
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Usuario'
 */


// Método DELETE para eliminar un usuario por id
/**
 * @swagger
 * /usuarios/{id_usuario}:
 *   delete:
 *     summary: Elimina un usuario por su id
 *     tags: [Usuarios]
 *     parameters:
 *       - in: path
 *         name: id_usuario
 *         schema:
 *           type: integer
 *         required: true
 *         description: ID del usuario
 *     responses:
 *       200:
 *         description: Usuario eliminado correctamente
 *       404:
 *         description: Usuario no encontrado
 */


// GET para consultar si hay alerta por valor de gas
/**
 * @swagger
 * /mediciones/valorGas:
 *   get:
 *     summary: Consulta si hay una alerta para un sensor
 *     tags: [Mediciones]
 *     parameters:
 *       - in: path
 *         name: id_sensor
 *         schema:
 *           type: integer
 *         required: true
 *         description: ID del sensor
 *     responses:
 *       200:
 *         description: Indica si hay una alerta
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 hayAlerta:
 *                   type: boolean
 *                   description: Indica si existe una alerta de gas
 */



