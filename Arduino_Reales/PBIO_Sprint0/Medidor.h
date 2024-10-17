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


  // Variables públicas para almacenar los valores medidos.
  public:

    float VeGas;        // Tensión del sensor de ozono
    float VeRef;        // Tensión de referencia
    float VeTemp;       // Tensión del sensor de temperatura

    float aGas;         // Valor analógico del sensor de ozono
    float aRef;         // Valor analógico de referencia
    float aTemp;        // Valor analógico del sensor de temperatura

    float Temp;         // Temperatura medida
    float Vmedida;      // Tensión medida
    float Cx;           // Concentración de ozono
    float Itemp;        // Variación de temperatura
    float fCx;          // Concentración ajustada de ozono
  // .....................................................
  // constructor
  // .....................................................
  Medidor(  ) {
    pinMode(pin_gas, INPUT);
    pinMode(pin_vref, INPUT);
    pinMode(pin_temp, INPUT);
  } // ()

  // .....................................................
  // .....................................................
  void iniciarMedidor() {
	// las cosas que no se puedan hacer en el constructor, if any
  } // ()
  
  float medirCO2() {
    // Leer los valores analógicos de los sensores
    aGas = analogRead(pin_gas);
    aRef = analogRead(pin_vref);
    aTemp = analogRead(pin_temp);

    // Convertir los valores analógicos en tensiones (0-3.3V) 1024
    
    VeGas = (aGas / 1024) * 3.3;
    VeRef = (aRef / 1024) * 3.3;
    VeTemp = (aTemp / 1024) * 3.3;

    // Calcular la tensión medida
    Vmedida = VeGas - VeRef;

    // Calcular la concentración de ozono (Cx) utilizando una fórmula específica. float sensitivityCode = -41.26;
    Cx = (Vmedida) / (sensitivityCode * 499 * 0.000001);

    // Inicializar Cx final con el valor de Cx
    finalCx = Cx;
    /*float Vgas = analogRead(pin_gas);
    float Vref = analogRead(pin_vref);
    
    float M = 1 / (sensitivityCode * TIAGain * 1000 * 0.000000001);
    float DiferenciaVgasVref = Vgas - Vref;
    float Vtotal = DiferenciaVgasVref * 3.3 / 1024;
    float ValorPPM = M * Vtotal;
    float ValorPPMpor10paraBeacon = ValorPPM * 10;
    return ValorPPMpor10paraBeacon;  */
    return finalCx;
  } // ()

  // .....................................................
  // .....................................................
  float medirTemp() {
	   float Vtemp = analogRead(pin_temp); // Leer el valor analógico
     float voltage = (analogRead(pin_vref) * Vtemp) / ADC_RESOLUTION; // Convertir a voltaje
     float temperaturaC = voltage * 100; // Convertir el voltaje a grados Celsius (LM35: 10 mV/°C)
     return temperaturaC;
  } // ()
  
  /*
  float getVgas(){
    float Vgas = analogRead(pin_gas);
    return Vgas;
  }

   float getVref(){
    float Vref = analogRead(pin_vref);
    return Vref;
  }
  */
  
}; // class

// ------------------------------------------------------
// ------------------------------------------------------
// ------------------------------------------------------
// ------------------------------------------------------
#endif
