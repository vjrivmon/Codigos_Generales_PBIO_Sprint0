// -*-c++-*-

// --------------------------------------------------------------
//
// Jordi Bataller i Mascarell
// 2019-07-07
//
// --------------------------------------------------------------

// https://learn.sparkfun.com/tutorials/nrf52840-development-with-arduino-and-circuitpython

// https://stackoverflow.com/questions/29246805/can-an-ibeacon-have-a-data-payload

// --------------------------------------------------------------
// --------------------------------------------------------------
#include <bluefruit.h>

#undef min // vaya tela, están definidos en bluefruit.h y  !
#undef max // colisionan con los de la biblioteca estándar
#define PIN_O3 5
// --------------------------------------------------------------
// --------------------------------------------------------------
#include "LED.h"
#include "PuertoSerie.h"

// --------------------------------------------------------------
// --------------------------------------------------------------
namespace Globales {
  
  LED elLED ( /* NUMERO DEL PIN LED = */ 7 );

  PuertoSerie elPuerto ( /* velocidad = */ 115200 ); // 115200 o 9600 o ...

  // Serial1 en el ejemplo de Curro creo que es la conexión placa-sensor 
};

// --------------------------------------------------------------
// --------------------------------------------------------------
#include "EmisoraBLE.h"
#include "Publicador.h"
#include "Medidor_fijo.h"


// --------------------------------------------------------------
// --------------------------------------------------------------
namespace Globales {

  Publicador elPublicador;

  Medidor elMedidor;

}; // namespace

// --------------------------------------------------------------
// --------------------------------------------------------------
void inicializarPlaquita () {

  // de momento nada

} // ()

// --------------------------------------------------------------
// setup()
// --------------------------------------------------------------
void setup() {

  Globales::elPuerto.esperarDisponible();

  // 
  // 
  // 
  inicializarPlaquita();

  // Suspend Loop() to save power
  // suspendLoop();

  // 
  // 
  // 
  Globales::elPublicador.encenderEmisora();

  // Globales::elPublicador.laEmisora.pruebaEmision();
  

  // 
  // 
  // 
  esperar( 1000 );

  Globales::elPuerto.escribir( "---- setup(): fin ---- \n " );


  //-----------------------------------------------------------------------
  //--------- Probar funcionamiento mandar beacon valores major minor -----
/* Agrega aquí el bloque de código
    Globales::elPuerto.escribir("UUID: ");
    for (int i = 0; i < 16; i++) {
        Globales::elPuerto.escribir(Globales::elPublicador.beaconUUID[i]);
    }*/
    Globales::elPuerto.escribir("\n");

    // Si tienes un valor mayor inicial, puedes usarlo aquí
    uint16_t major = 0; // O algún valor por defecto
    Globales::elPuerto.escribir("Major: ");
    Globales::elPuerto.escribir(major);
    Globales::elPuerto.escribir("\n");

    Globales::elPuerto.escribir("Minor: ");
    Globales::elPuerto.escribir(0); // Coloca un valor por defecto o inicial aquí
    Globales::elPuerto.escribir("\n");



  

} // setup ()

// --------------------------------------------------------------
// --------------------------------------------------------------
inline void lucecitas() {
  using namespace Globales;

  elLED.brillar( 100 ); // 100 encendido
  esperar ( 400 ); //  100 apagado
  elLED.brillar( 100 ); // 100 encendido
  esperar ( 400 ); //  100 apagado
  Globales::elLED.brillar( 100 ); // 100 encendido
  esperar ( 400 ); //  100 apagado
  Globales::elLED.brillar( 1000 ); // 1000 encendido
  esperar ( 1000 ); //  100 apagado

  
  // 
  // 
  // 
  Globales::elMedidor.iniciarMedidor();
} // ()

// --------------------------------------------------------------
// loop ()
// --------------------------------------------------------------
namespace Loop {
  uint8_t cont = 0;
};

// ..............................................................
// ..............................................................
void loop () {

  using namespace Loop;
  using namespace Globales;

  cont++;

  elPuerto.escribir( "\n---- loop(): empieza " );
  elPuerto.escribir( cont );
  elPuerto.escribir( "\n" );


  lucecitas();

  // 
  // mido y publico
  // 
  float valorCO2 = elMedidor.medirCO2();
  
  elPublicador.publicarCO2( valorCO2, cont, 1000 // intervalo de emisión
							);


//-----------------------------------------------------------------------------
// Código para mostrar el UUID, Major y Minor
    uint16_t major = (Publicador::MedicionesID::CO2 << 8) + cont; // Obtén el Major
    /*Globales::elPuerto.escribir("UUID: ");
    for (int i = 0; i < 16; i++) {
        Globales::elPuerto.escribir(Globales::elPublicador.beaconUUID[i]);
    }*/
    Globales::elPuerto.escribir("\nMajor: ");
    Globales::elPuerto.escribir(major);
    Globales::elPuerto.escribir("\nMinor: ");
    Globales::elPuerto.escribir(valorCO2); // Usando el valor de CO2 como Minor
    Globales::elPuerto.escribir("\n");

  
  
  // 
  // prueba para emitir un iBeacon y poner
  // en la carga (21 bytes = uuid 16 major 2 minor 2 txPower 1 )
  // lo que queramos (sin seguir dicho formato)
  // 
  // Al terminar la prueba hay que hacer Publicador::laEmisora privado
  // 
  /*char datos[21] = {
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H'
  };*/

  char datos[21] = { 
    'H', 'o', 'y', 
    'N', 'o', 
    'H', 'a', 'c', 'e', 
    'U', 'n', 
    'D', 'i', 'a', 
    'S', 'o', 'l', 'e', 'a', 'd', 'o'};     //Mensaje que llega al movil --------- CAMBIAR

  // elPublicador.laEmisora.emitirAnuncioIBeaconLibre ( &datos[0], 21 );
  elPublicador.laEmisora.emitirAnuncioIBeaconLibre ( &datos[0], 21 );





  Serial.print("Gas: ");
  Serial.println(valorCO2);


  
 
  // Añade un retardo para evitar saturar el puerto serie
  delay(1000);
  
//------------------------------------------------------------------------


  esperar( 2000 );

  elPublicador.laEmisora.detenerAnuncio();

  // 
  // 
  // 
  elPuerto.escribir( "---- loop(): acaba **** " );
  elPuerto.escribir( cont );
  elPuerto.escribir( "\n" );
  
  
} // loop ()
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
