const mongoose = require('mongoose');

const detalleSchema = new mongoose.Schema({
  nroFactura: {
    type: Number,
    required: true,
    ref: 'Factura'
  },
  idProducto: {
    type: mongoose.Schema.Types.ObjectId,
    required: true,
    ref: 'Producto'
  },
  cantidad: {
    type: Number,
    required: true,
    min: 1
  },
  subtotal: {
    type: Number,
    required: true,
    min: 0
  }
}, {
  timestamps: true
});

module.exports = mongoose.model('Detalle', detalleSchema);