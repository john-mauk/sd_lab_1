var noble = require('noble');

var mysql      = require('mysql');
var poolCluster = mysql.createPoolCluster();
var connection = mysql.createConnection({
  // host     : 'ex-design.cfilhwe2swqf.us-west-2.rds.amazonaws.com',
  // user     : 'johnny',
  // password : 'Jamochame13',
  // database : 'ex_design_database'

  host: 'ex-design.cfilhwe2swqf.us-west-2.rds.amazonaws.com',
  user: 'johnny',
  password: '[Jamochame13]',
  port: '3306',
  database : 'ex_design_database'
});

connection.connect(function(err) {
  if (err) {
    console.error('error connecting: ' + err.stack);
    return;
  }

  console.log('connected as id ' + connection.threadId);
});

var serviceUuid = '6e400001b5a3f393e0a9e50e24dcca9e';
var readUuid = '6e400003b5a3f393e0a9e50e24dcca9e';
var writeUuid = '6e400002b5a3f393e0a9e50e24dcca9e';

var readChar = null;
var writeChar = null;

noble.on('stateChange', function(state) {
  if (state === 'poweredOn') {
    noble.startScanning();
  } else {
    noble.stopScanning();
  }
});

noble.on('discover', function(peripheral) {
    if(peripheral.advertisement.localName === "Adafruit Bluefruit LE") {
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
            process.exit(0);
        });
    }
});

// for the database, you need <temp_datetime>, <temp_c_val>, <temp_series_name>
// example: <'yyyy-mm-dd hh:mm:ss'>, <13>, <(const) Temps>

function readWriteData() {
    readChar.on('read', function(data, isNotification) {
        console.log(data.toString());
        poolCluster.add('temps', data.toString()); // add a named configuration
    });
    readChar.subscribe(function(err) {
        console.log('subscribed');
    });
    writeChar.write(function(){
        asdfasdf
    });
}
