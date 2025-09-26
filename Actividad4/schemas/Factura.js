const mongoose = require('mongoose');

const facturaSchema = new mongoose.Schema({
  nro: {
    type: Number,
    required: true,
    unique: true
  },
  fecha: {
    type: Date,
    required: true,
    default: Date.now
  }
}, {
  timestamps: true
});

module.exports = mongoose.model('Factura', facturaSchema);