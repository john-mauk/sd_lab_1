#include <Arduino.h>
#include <SPI.h>
#if not defined (_VARIANT_ARDUINO_DUE_X_) && not defined (_VARIANT_ARDUINO_ZERO_)
  #include <SoftwareSerial.h>
#endif

#if defined(ARDUINO_SAMD_ZERO) && defined(SERIAL_PORT_USBVIRTUAL)
  // Required for Serial on Zero based boards
  #define Serial SERIAL_PORT_USBVIRTUAL
#endif

#include "Adafruit_BLE.h"
#include "Adafruit_BluefruitLE_SPI.h"
#include "Adafruit_BluefruitLE_UART.h"

#include "BluefruitConfig.h"



/********************************************************************/
// First we include the libraries
#include <OneWire.h>
#include <DallasTemperature.h>
/********************************************************************/
// Data wire is plugged into pin 2 on the Arduino 
#define ONE_WIRE_BUS A1
#define PUSH_BUTTON A0

#define LED1 13
#define LED2 12
#define LED3 11
#define LED4 10
#define LED5 9
#define LED6 6
#define LED7 5
/********************************************************************/
// Setup a oneWire instance to communicate with any OneWire devices  
// (not just Maxim/Dallas temperature ICs) 
OneWire oneWire(ONE_WIRE_BUS); 
/********************************************************************/
// Pass our oneWire reference to Dallas Temperature. 
DallasTemperature sensors(&oneWire);
/********************************************************************/ 

Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_CS, BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);

void error(const __FlashStringHelper*err) {
  Serial.println(err);
  while (1);
}

void setup() {
//  // initialize digital pin LED_BUILTIN as an output.
//  pinMode(13, OUTPUT);
  ledShowOff();

  Serial.begin(115200);

  if ( !ble.begin(VERBOSE_MODE) )
  {
    error(F("Couldn't find Bluefruit"));
  }
  Serial.println( F("OK!") );

  /* Disable command echo from Bluefruit */
  ble.echo(false);

  ble.verbose(false);  // debug info is a little annoying after this point!

  // Start up the library
  sensors.begin();
}

void loop() {
  //ledShowOff();
  
  // request to all devices on the bus 
  /********************************************************************/
   sensors.requestTemperatures(); // Send the command to get temperature readings
  /********************************************************************/
  float temperature = sensors.getTempCByIndex(0);
  // ledControl((byte)round(temperature), true);

   
   // check if the pushbutton is pressed. If it is, the buttonState is HIGH:
  if (digitalRead(PUSH_BUTTON) == HIGH) {
    // ledShowOff();
    ledControl((byte)round(temperature), true);
  } else {
    ledControl((byte)round(temperature), false);
  }
  ble.print("AT+BLEUARTTX=");
  ble.println(temperature);

  delay(600);
}

void ledControl(byte temperature, bool pressed) {
  Serial.println(F("inside ledControl function"));
  Serial.println(temperature);
  int temp = temperature;
  if(!pressed || temperature == 0) {
    digitalWrite(LED1, LOW);
    digitalWrite(LED2, LOW);
    digitalWrite(LED3, LOW);
    digitalWrite(LED4, LOW);
    digitalWrite(LED5, LOW);
    digitalWrite(LED6, LOW);
    digitalWrite(LED7, LOW);
  } else {
    temp = temperature + ((temperature >> 6) & 1) * 128;
//    if(temperature > 0) {
//      temp = temperature;
//    } else {
//      temp = 126 + temperature + 2;
//    }
    if((temp >> 0) & 1) digitalWrite(LED1, HIGH);
    else digitalWrite(LED1, LOW);
    if((temp >> 1) & 1) digitalWrite(LED2, HIGH);
    else digitalWrite(LED2, LOW);
    if((temp >> 2) & 1) digitalWrite(LED3, HIGH);
    else digitalWrite(LED3, LOW);
    if((temp >> 3) & 1) digitalWrite(LED4, HIGH);
    else digitalWrite(LED4, LOW);
    if((temp >> 4) & 1) digitalWrite(LED5, HIGH);
    else digitalWrite(LED5, LOW);
    if((temp >> 5) & 1) digitalWrite(LED6, HIGH);
    else digitalWrite(LED6, LOW);
    if((temp >> 6) & 1) digitalWrite(LED7, HIGH);
    else digitalWrite(LED7, LOW);
  }
}

void ledShowOff() {

  digitalWrite(LED1, HIGH);
  delay(1000);
  digitalWrite(LED1, LOW);
  digitalWrite(LED2, HIGH);
  delay(1000);
  digitalWrite(LED2, LOW);
  digitalWrite(LED3, HIGH);
  delay(1000);
  digitalWrite(LED3, LOW);
  digitalWrite(LED4, HIGH);
  delay(1000);
  digitalWrite(LED4, LOW);
  digitalWrite(LED5, HIGH);
  delay(1000);
  digitalWrite(LED5, LOW);
  digitalWrite(LED6, HIGH);
  delay(1000);
  digitalWrite(LED6, LOW);
  digitalWrite(LED7, HIGH);
  delay(1000);
  digitalWrite(LED7, LOW);
}

