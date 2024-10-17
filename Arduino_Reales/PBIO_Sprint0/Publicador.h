// -*- mode: c++ -*-

// --------------------------------------------------------------
// Jordi Bataller i Mascarell
// --------------------------------------------------------------

#ifndef PUBLICADOR_H_INCLUIDO
#define PUBLICADOR_H_INCLUIDO

// --------------------------------------------------------------
// --------------------------------------------------------------
class Publicador {

  // ............................................................
  // ............................................................
private:

  /*uint8_t beaconUUID[16] = { //UUID del Beacon // --------------------------------------------- CAMBIAR
	'E', 'P', 'S', 'G', '-', 'G', 'T', 'I', 
	'-', 'P', 'R', 'O', 'Y', '-', '3', 'A'
	};*/

uint8_t beaconUUID[16] = { //UUID del Beacon // --------------------------------------------- CAMBIAR
  'M', 'A', 'T', 'X', 'O', '-', 'L', 'L', 
  'E', 'V', 'A', 'N', 'T', '-', 'U', 'D'
  };
  

  // ............................................................
  // ............................................................
public:
  EmisoraBLE laEmisora {
	"Manolito", //  nombre emisora   //Nombre que aparece como dispositivo en nRFConnect ----------- CAMBIAR
	  0x004c, // fabricanteID (Apple)
	  4 // txPower
	  };
  
  const int RSSI = -53;

  // ............................................................
  // ............................................................
public:

  // ............................................................
  // ............................................................
  enum MedicionesID  {  // --------------------------------------------- CAMBIAR
	CO2 = 11,
	TEMPERATURA = 12,
	RUIDO = 13
  };

  // ............................................................
  // ............................................................
  Publicador( ) {
	// ATENCION: no hacerlo aquí. (*this).laEmisora.encenderEmisora();
	// Pondremos un método para llamarlo desde el setup() más tarde
  } // ()

  // ............................................................
  // ............................................................
  void encenderEmisora() {
	(*this).laEmisora.encenderEmisora();
  } // ()

  // ............................................................
  // ............................................................
  void publicar( int16_t valorCO2, uint8_t contador, long tiempoEspera, int16_t valorTemp ) {

	//
	// 1. empezamos anuncio
	//
	//uint16_t major = (MedicionesID::CO2 << 8) + contador;
	(*this).laEmisora.emitirAnuncioIBeacon( (*this).beaconUUID, 
											ValorCO2,//major
											valorTemp, // minor
											(*this).RSSI // rssi
									);

	/*
	Globales::elPuerto.escribir( "   publicarCO2(): valor=" );
	Globales::elPuerto.escribir( valorCO2 );
	Globales::elPuerto.escribir( "   contador=" );
	Globales::elPuerto.escribir( contador );
	Globales::elPuerto.escribir( "   todo="  );
	Globales::elPuerto.escribir( major );
	Globales::elPuerto.escribir( "\n" );
	*/

	//
	// 2. esperamos el tiempo que nos digan
	//
	esperar( tiempoEspera );

	//
	// 3. paramos anuncio
	//
	(*this).laEmisora.detenerAnuncio();
  } // ()

  // ............................................................
  // ............................................................
  void publicarTemperatura( int16_t valorTemperatura,
							uint8_t contador, long tiempoEspera ) {

	uint16_t major = (MedicionesID::TEMPERATURA << 8) + contador;
	(*this).laEmisora.emitirAnuncioIBeacon( (*this).beaconUUID, 
											major,
											valorTemperatura, // minor
											(*this).RSSI // rssi
									);
	esperar( tiempoEspera );

	(*this).laEmisora.detenerAnuncio();
  } // ()

//----------------------------------------------------------------------------------------------
// Función para publicar el valor de concentración de COx y temperatura como un anuncio Beacon.
  void publicarCoXTemp(int16_t valorCox, int16_t valorTemperatura,long tiempoEspera)
  {
  (*this).laEmisora.emitirAnuncioIBeacon( (*this).beaconUUID, 
                      valorCox,
                      valorTemperatura, // minor
                      (*this).RSSI // rssi
                  );
  esperar( tiempoEspera );

  (*this).laEmisora.detenerAnuncio();
  }
  
}; // class

// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
#endif
