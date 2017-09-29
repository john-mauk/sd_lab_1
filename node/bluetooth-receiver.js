var noble = require('noble');

var mysql      = require('mysql');
var poolCluster = mysql.createPoolCluster();
var connection = mysql.createConnection({
  host: 'ex-design.cfilhwe2swqf.us-west-2.rds.amazonaws.com',
  user: 'johnny',
  password: '[Jamochame13]',
  port: '3306',
  database : 'ex_design_database'
});

var isDatabaseConnected = false;

connection.connect(function(err) {
  if (err) {
    console.error('error connecting: ' + err.stack);
    return;
  }
  isDatabaseConnected = true;
});



var serviceUuid = '6e400001b5a3f393e0a9e50e24dcca9e';
var readUuid = '6e400003b5a3f393e0a9e50e24dcca9e';
var writeUuid = '6e400002b5a3f393e0a9e50e24dcca9e';

var readChar = null;
var writeChar = null;


dataBaseDisconnected();

noble.on('stateChange', function(state) {
  if (state === 'poweredOn') {
    noble.startScanning();
  } else {
    noble.stopScanning();
  }
});

noble.on('discover', function(peripheral) {

    // peripheral.id = "dfa4c6667ca14d778009d8675507813c";
    if(peripheral.advertisement.localName === "Adafruit Bluefruit LE 60BD") {
    // if(peripheral.id = "dfa4c6667ca14d778009d8675507813c") {
        peripheral.connect(function(error) {
            console.log("connected");
            noble.stopScanning();

            peripheral.discoverServices([serviceUuid], function(error, services) {
                services.forEach(function(service) {
                    service.discoverCharacteristics(['6e400003b5a3f393e0a9e50e24dcca9e', '6e400002b5a3f393e0a9e50e24dcca9e'], function(err, characteristics) {
                        characteristics.forEach(function(characteristic) {
                            if(characteristic.uuid === readUuid) {
                                readChar = characteristic;
                                console.log('saved readChar');
                            } else {
                                writeChar = characteristic;
                                console.log('saved writeChar');
                            }
                        });
                        if(readChar && writeChar) {
                            console.log('to readWriteData()');
                            readWriteData();
                        }
                    });
                });
            });
        });

        peripheral.once("disconnect", function() {
            dataBaseDisconnected();
            process.exit(0);
        });
    }
});

// for the database, you need <temp_datetime>, <temp_c_val>, <temp_series_name>
// example: <'yyyy-mm-dd hh:mm:ss'>, <13>, <(const) Temps>

function readWriteData() {
    readChar.on('read', function(data, isNotification) {
        console.log(data.toString());
        if(isDatabaseConnected) {
          var sql = "INSERT INTO temps (temp_datetime, temp_c_val, temp_series_name) VALUES ?";
          var values = [
              [new Date().toISOString().replace(/T/, ' ').replace(/\..+/), data.toString(), 'Temps']
          ];
          connection.query(sql, [values], function (err, result) {
            if (err) throw err;
          });

          dataBaseConnected();
        }
    });
    readChar.subscribe(function(err) {
        console.log('subscribed');
    });
}

function dataBaseConnected() {
    if(isDatabaseConnected) {
      var sql = "UPDATE controls SET control_val='1' WHERE control_type='Probe'";
      connection.query(sql, function (err, result) {
        if (err) throw err;
      });
    }
}

function dataBaseDisconnected() {
    if(isDatabaseConnected) {
      var sql = "UPDATE controls SET control_val='0' WHERE control_type='Probe'";
      connection.query(sql, function (err, result) {
        if (err) throw err;
      });
    }
}
