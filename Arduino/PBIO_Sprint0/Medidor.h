// -*- mode: c++ -*-

#ifndef MEDIDOR_H_INCLUIDO
#define MEDIDOR_H_INCLUIDO
#define pin_gas 28
#define pin_temp 29
#define pin_vref 5
#define TIAGain 499
float sensitivityCode = -41.26;


class Medidor {
  private:

  public:
  // .....................................................
  // constructor
  // .....................................................
  Medidor(  ) {
  } // ()

  // .....................................................
  // .....................................................
  void iniciarMedidor() {
	// las cosas que no se puedan hacer en el constructor, if any
  } // ()
  
  float medirCO2() {
    /*float Vgas = analogRead(pin_gas);
    float Vref = analogRead(pin_vref);
    
    float M = 1 / (sensitivityCode * TIAGain * 1000 * 0.000000001);
    float DiferenciaVgasVref = Vgas - Vref;
    float Vtotal = DiferenciaVgasVref * 3.3 / 1024;
    float ValorPPM = M * Vtotal;
    float ValorPPMpor10paraBeacon = ValorPPM * 10;
    return ValorPPMpor10paraBeacon;  */
    return 33;
  } // ()

  // .....................................................
  // .....................................................
  float medirTemperatura() {
	   float Vtemp = analogRead(pin_temp); // Leer el valor analógico
     float voltage = (analogRead(pin_vref) * Vtemp) / ADC_RESOLUTION; // Convertir a voltaje
     float temperaturaC = voltage * 100; // Convertir el voltaje a grados Celsius (LM35: 10 mV/°C)
     return temperaturaC;
  } // ()

  float getVgas(){
    float Vgas = analogRead(pin_gas);
    return Vgas;
  }

   float getVref(){
    float Vref = analogRead(pin_vref);
    return Vref;
  }
  
}; // class

// ------------------------------------------------------
// ------------------------------------------------------
// ------------------------------------------------------
// ------------------------------------------------------
#endif
