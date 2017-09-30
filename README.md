# Senior Design Lab I

## Abstract

This is a project for Senior Design class. We (John Mauk, Emily McGee, Yangyue Ma, and Thomas Han) are to design a robust temperature sensor collector with the ability to visualize the temperature data on a webpage.

It contains a temeperature sensor, an Adafruit Feather 32u4 BLE (BluFruit), a button, a switch, 7 LEDs, and a Lithium Polymer battery (LiPo).

## How to use it

### Arduino
The software should be loaded on chip. Just plug in the LiPo battery or turn on the switch if the battery is plugged-in.

### Node.js
First, use command line and go to the folder where "node" is located. If you are running for the first time, then several npm library needs to be installed.
Run:
```{r} npm install noble ```
Then:
```{r} npm install mysql ```
Finally, under command line, for MacOS type
```{r} node bluetooth-receiver.js ```
or for Linux, run
```{r} nodejs bluetooth-receiver.js ```
