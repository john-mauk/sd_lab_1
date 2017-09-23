var noble_ble = require('noble');

noble_ble.startScanning(); // any service UUID, no duplicates


noble_ble.startScanning([], true); // any service UUID, allow duplicates


var serviceUUIDs = ["<service UUID 1>", ...]; // default: [] => all
var allowDuplicates = <false|true>; // default: false

noble_ble.startScanning(serviceUUIDs, allowDuplicates[, callback(error)]); // particular UUID's
