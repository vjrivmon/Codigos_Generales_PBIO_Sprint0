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
  verificarUsuario,
  recuperarContrasena,
  editarDatosUsuario,
  encriptarContrasenas,
  enviarCorreoVerificacion,
  enviarCorreoEditarDatos,
  enviarCorreoRecuperarContrasena,
  enviarCorreoRestablecerContrasena,
  asociarSensorAUsuario,
  obtenerSensorPorCorreo,
  editarNombreSensor // Importar la nueva función
} = require('./servidorREST'); // Importar lógica de negocio desde el archivo separado

// Middleware para procesar JSON
router.use(cors());
router.use(express.json());

router.get('/mediciones/:id_sensor', ConsultarMedida);
router.post('/mediciones', agregarMedicion);
router.get('/usuarios/:id_usuario', ConsultarDatosUsuario);
router.post('/usuarios', agregarUsuario);
router.delete('/usuarios/:id_usuario', EliminarUsuario);
router.get('/mediciones/:id_sensor', ConsultarSiHayAlerta);
router.get('/usuarios', verificarUsuario);
router.put('/usuarios/contrasena', recuperarContrasena);
router.put('/usuarios/:id_usuario', editarDatosUsuario);
router.post('/usuarios/verificar', verificarUsuario);
router.post('/verificar-correo', enviarCorreoVerificacion);
router.post('/editar-datos', enviarCorreoEditarDatos);
router.post('/restablecer-contrasena', enviarCorreoRestablecerContrasena);
router.post('/recuperar-contrasena', enviarCorreoRecuperarContrasena);
router.post('/asociar_dispositivo', asociarSensorAUsuario);
router.put('/sensores/:id_sensor', editarNombreSensor); // Nueva ruta para editar el nombre del sensor

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
router.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs));

// Documentación de Swagger para la nueva ruta
/**
 * @swagger
 * /sensores/{id_sensor}:
 *   put:
 *     summary: Edita el nombre de un sensor
 *     tags: [Sensores]
 *     parameters:
 *       - in: path
 *         name: id_sensor
 *         schema:
 *           type: string
 *         required: true
 *         description: ID del sensor
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               nombre:
 *                 type: string
 *                 description: Nuevo nombre del sensor
 *     responses:
 *       200:
 *         description: Nombre del sensor actualizado correctamente
 *       404:
 *         description: No se encontró ningún sensor con el ID proporcionado
 *       500:
 *         description: Error del servidor
 */

// Esquema de Swagger para Medicion
/**
 * @swagger
 * components:
 *   schemas:
 *     Medicion:
 *       type: object
 *       required:
 *         - id_sensor
 *         - fecha_hora
 *         - ubicacion
 *         - tipo_medicion
 *         - valor
 *       properties:
 *         id_sensor:
 *           type: string
 *           description: ID del sensor
 *         fecha_hora:
 *           type: string
 *           format: date-time
 *           description: Fecha y hora de la medición
 *         ubicacion:
 *           type: object
 *           description: Ubicación de la medición
 *         tipo_medicion:
 *           type: string
 *           description: Tipo de medición
 *         valor:
 *           type: number
 *           format: decimal
 *           description: Valor de la medición
 *       example:
 *         id_sensor: '00:1A:2B:3C:4D:5E'
 *         fecha_hora: '2024-11-21T10:00:00Z'
 *         ubicacion: {"latitud": 40.416775, "longitud": -3.703790}
 *         tipo_medicion: 'Temperatura'
 *         valor: 25.50
 */

// Esquema de Swagger para Usuario
/**
 * @swagger
 * components:
 *   schemas:
 *     Usuario:
 *       type: object
 *       required:
 *         - nombre
 *         - telefono
 *         - correo
 *         - contrasena
 *       properties:
 *         nombre:
 *           type: string
 *           description: Nombre del usuario
 *         telefono:
 *           type: string
 *           description: Teléfono del usuario
 *         correo:
 *           type: string
 *           description: Correo electrónico del usuario
 *         contrasena:
 *           type: string
 *           description: Contraseña del usuario
 *       example:
 *         nombre: 'Vicente'
 *         telefono: '601037577'
 *         correo: 'visi02@gmail.com'
 *         contrasena: 'pass1'
 */

// Esquema de Swagger para Sensor
/**
 * @swagger
 * components:
 *   schemas:
 *     Sensor:
 *       type: object
 *       required:
 *         - id_sensor
 *         - nombre
 *         - funciona
 *       properties:
 *         id_sensor:
 *           type: string
 *           description: ID del sensor
 *         nombre:
 *           type: string
 *           description: Nombre del sensor
 *         funciona:
 *           type: boolean
 *           description: Indica si el sensor funciona
 *       example:
 *         id_sensor: '00:1A:2B:3C:4D:5E'
 *         nombre: 'Sensor 1'
 *         funciona: true
 */

// TÍTULO DE LA API en Swagger
/**
 * @swagger
 * tags:
 *   name: Mediciones
 *   description: API para gestionar las mediciones de los sensores
 * 
 * @swagger
 * tags:
 *   name: Usuarios
 *   description: API para gestionar los usuarios de la aplicación
 * 
 * @swagger
 * tags:
 *   name: Sensores
 *   description: API para gestionar los sensores
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
 *           type: string
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

// Método PUT para editar el nombre de un sensor
/**
 * @swagger
 * /sensores/{id_sensor}:
 *   put:
 *     summary: Edita el nombre de un sensor
 *     tags: [Sensores]
 *     parameters:
 *       - in: path
 *         name: id_sensor
 *         schema:
 *           type: string
 *         required: true
 *         description: ID del sensor
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               nombre:
 *                 type: string
 *                 description: Nuevo nombre del sensor
 *             example:
 *               nombre: 'Nuevo nombre del sensor'
 *     responses:
 *       200:
 *         description: Nombre del sensor actualizado correctamente
 *       404:
 *         description: No se encontró ningún sensor con el ID proporcionado
 *       500:
 *         description: Error del servidor
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

module.exports = router; // Exportar el router
