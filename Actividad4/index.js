const mongoose = require('mongoose');
const readline = require('readline');

const Producto = require('./schemas/Producto');
const Factura = require('./schemas/Factura');
const Detalle = require('./schemas/Detalle');

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

function pregunta(query) {
  return new Promise(resolve => rl.question(query, resolve));
}

async function conectarDB() {
  try {
    await mongoose.connect("mongodb://localhost:27017/crud_facturacion");
    console.log('Conectado a MongoDB exitosamente');
  } catch (error) {
    console.error('Error conectando a MongoDB:', error.message);
    process.exit(1);
  }
}

async function menuProductos() {
  console.log('\n=== MENU PRODUCTOS ===');
  console.log('1. Crear producto');
  console.log('2. Listar productos');
  console.log('3. Actualizar producto');
  console.log('4. Eliminar producto');
  console.log('5. Volver al menu principal');
  
  const opcion = await pregunta('Seleccione una opcion: ');
  
  switch(opcion) {
    case '1':
      await crearProducto();
      break;
    case '2':
      await listarProductos();
      break;
    case '3':
      await actualizarProducto();
      break;
    case '4':
      await eliminarProducto();
      break;
    case '5':
      return;
    default:
      console.log('Opcion invalida');
  }
  
  await menuProductos();
}

async function crearProducto() {
  console.log('\n--- Crear Producto ---');
  const nombre = await pregunta('Nombre del producto: ');
  const precio = parseFloat(await pregunta('Precio del producto: '));
  
  if (isNaN(precio) || precio < 0) {
    console.log('Error: Precio debe ser un numero positivo');
    return;
  }
  
  try {
    const producto = new Producto({ nombre, precio });
    await producto.save();
    console.log(`Producto creado exitosamente con ID: ${producto._id}`);
  } catch (error) {
    console.error('Error creando producto:', error.message);
  }
}

async function listarProductos() {
  console.log('\n--- Lista de Productos ---');
  try {
    const productos = await Producto.find();
    if (productos.length === 0) {
      console.log('No hay productos registrados');
      return;
    }
    
    productos.forEach(producto => {
      console.log(`ID: ${producto._id} | Nombre: ${producto.nombre} | Precio: $${producto.precio}`);
    });
  } catch (error) {
    console.error('Error listando productos:', error.message);
  }
}

async function actualizarProducto() {
  console.log('\n--- Actualizar Producto ---');
  await listarProductos();
  
  const id = await pregunta('ID del producto a actualizar: ');
  
  try {
    const producto = await Producto.findById(id);
    if (!producto) {
      console.log('Producto no encontrado');
      return;
    }
    
    const nuevoNombre = await pregunta(`Nuevo nombre (actual: ${producto.nombre}): `);
    const nuevoPrecio = await pregunta(`Nuevo precio (actual: $${producto.precio}): `);
    
    if (nuevoNombre) producto.nombre = nuevoNombre;
    if (nuevoPrecio && !isNaN(parseFloat(nuevoPrecio)) && parseFloat(nuevoPrecio) >= 0) {
      producto.precio = parseFloat(nuevoPrecio);
    }
    
    await producto.save();
    console.log('Producto actualizado exitosamente');
  } catch (error) {
    console.error('Error actualizando producto:', error.message);
  }
}

async function eliminarProducto() {
  console.log('\n--- Eliminar Producto ---');
  await listarProductos();
  
  const id = await pregunta('ID del producto a eliminar: ');
  
  try {
    const resultado = await Producto.findByIdAndDelete(id);
    if (!resultado) {
      console.log('Producto no encontrado');
      return;
    }
    
    await Detalle.deleteMany({ idProducto: id });
    console.log('Producto eliminado exitosamente');
  } catch (error) {
    console.error('Error eliminando producto:', error.message);
  }
}

async function menuFacturas() {
  console.log('\n=== MENU FACTURAS ===');
  console.log('1. Crear factura');
  console.log('2. Listar facturas');
  console.log('3. Actualizar factura');
  console.log('4. Eliminar factura');
  console.log('5. Agregar producto a factura');
  console.log('6. Ver detalle de factura');
  console.log('7. Volver al menu principal');
  
  const opcion = await pregunta('Seleccione una opcion: ');
  
  switch(opcion) {
    case '1':
      await crearFactura();
      break;
    case '2':
      await listarFacturas();
      break;
    case '3':
      await actualizarFactura();
      break;
    case '4':
      await eliminarFactura();
      break;
    case '5':
      await agregarProductoAFactura();
      break;
    case '6':
      await verDetalleFactura();
      break;
    case '7':
      return;
    default:
      console.log('Opcion invalida');
  }
  
  await menuFacturas();
}

async function crearFactura() {
  console.log('\n--- Crear Factura ---');
  const nro = parseInt(await pregunta('Numero de factura: '));
  const fechaInput = await pregunta('Fecha (YYYY-MM-DD) o presione Enter para fecha actual: ');
  
  if (isNaN(nro)) {
    console.log('Error: Numero de factura debe ser un numero');
    return;
  }
  
  try {
    const factura = new Factura({
      nro,
      fecha: fechaInput ? new Date(fechaInput) : new Date()
    });
    
    await factura.save();
    console.log(`Factura creada exitosamente con numero: ${factura.nro}`);
  } catch (error) {
    if (error.code === 11000) {
      console.log('Error: Ya existe una factura con ese numero');
    } else {
      console.error('Error creando factura:', error.message);
    }
  }
}

async function listarFacturas() {
  console.log('\n--- Lista de Facturas ---');
  try {
    const facturas = await Factura.find();
    if (facturas.length === 0) {
      console.log('No hay facturas registradas');
      return;
    }
    
    facturas.forEach(factura => {
      console.log(`Numero: ${factura.nro} | Fecha: ${factura.fecha.toLocaleDateString()}`);
    });
  } catch (error) {
    console.error('Error listando facturas:', error.message);
  }
}

async function actualizarFactura() {
  console.log('\n--- Actualizar Factura ---');
  await listarFacturas();
  
  const nro = parseInt(await pregunta('Numero de factura a actualizar: '));
  
  if (isNaN(nro)) {
    console.log('Error: Numero de factura debe ser un numero');
    return;
  }
  
  try {
    const factura = await Factura.findOne({ nro });
    if (!factura) {
      console.log('Factura no encontrada');
      return;
    }
    
    const nuevaFecha = await pregunta(`Nueva fecha (actual: ${factura.fecha.toLocaleDateString()}) YYYY-MM-DD: `);
    
    if (nuevaFecha) {
      factura.fecha = new Date(nuevaFecha);
    }
    
    await factura.save();
    console.log('Factura actualizada exitosamente');
  } catch (error) {
    console.error('Error actualizando factura:', error.message);
  }
}

async function eliminarFactura() {
  console.log('\n--- Eliminar Factura ---');
  await listarFacturas();
  
  const nro = parseInt(await pregunta('Numero de factura a eliminar: '));
  
  if (isNaN(nro)) {
    console.log('Error: Numero de factura debe ser un numero');
    return;
  }
  
  try {
    const resultado = await Factura.findOneAndDelete({ nro });
    if (!resultado) {
      console.log('Factura no encontrada');
      return;
    }
    
    await Detalle.deleteMany({ nroFactura: nro });
    console.log('Factura eliminada exitosamente');
  } catch (error) {
    console.error('Error eliminando factura:', error.message);
  }
}

async function agregarProductoAFactura() {
  console.log('\n--- Agregar Producto a Factura ---');
  
  await listarFacturas();
  const nroFactura = parseInt(await pregunta('Numero de factura: '));
  
  if (isNaN(nroFactura)) {
    console.log('Error: Numero de factura debe ser un numero');
    return;
  }
  
  const factura = await Factura.findOne({ nro: nroFactura });
  if (!factura) {
    console.log('Factura no encontrada');
    return;
  }
  
  await listarProductos();
  const idProducto = await pregunta('ID del producto: ');
  const cantidad = parseInt(await pregunta('Cantidad: '));
  
  if (isNaN(cantidad) || cantidad < 1) {
    console.log('Error: Cantidad debe ser un numero positivo');
    return;
  }
  
  try {
    const producto = await Producto.findById(idProducto);
    if (!producto) {
      console.log('Producto no encontrado');
      return;
    }
    
    const subtotal = cantidad * producto.precio;
    
    const detalleExistente = await Detalle.findOne({ nroFactura, idProducto });
    
    if (detalleExistente) {
      detalleExistente.cantidad += cantidad;
      detalleExistente.subtotal = detalleExistente.cantidad * producto.precio;
      await detalleExistente.save();
      console.log('Cantidad actualizada en la factura');
    } else {
      const detalle = new Detalle({
        nroFactura,
        idProducto,
        cantidad,
        subtotal
      });
      
      await detalle.save();
      console.log('Producto agregado a la factura exitosamente');
    }
  } catch (error) {
    console.error('Error agregando producto a factura:', error.message);
  }
}

async function verDetalleFactura() {
  console.log('\n--- Detalle de Factura ---');
  await listarFacturas();
  
  const nroFactura = parseInt(await pregunta('Numero de factura: '));
  
  if (isNaN(nroFactura)) {
    console.log('Error: Numero de factura debe ser un numero');
    return;
  }
  
  try {
    const factura = await Factura.findOne({ nro: nroFactura });
    if (!factura) {
      console.log('Factura no encontrada');
      return;
    }
    
    const detalles = await Detalle.find({ nroFactura }).populate('idProducto');
    
    if (detalles.length === 0) {
      console.log('Esta factura no tiene productos');
      return;
    }
    
    console.log(`\nFactura Numero: ${factura.nro}`);
    console.log(`Fecha: ${factura.fecha.toLocaleDateString()}`);
    console.log('\nProductos:');
    console.log('---------------------------------------------------------------');
    
    let total = 0;
    detalles.forEach(detalle => {
      console.log(`${detalle.idProducto.nombre} | Cantidad: ${detalle.cantidad} | Precio Unit: $${detalle.idProducto.precio} | Subtotal: $${detalle.subtotal}`);
      total += detalle.subtotal;
    });
    
    console.log('---------------------------------------------------------------');
    console.log(`TOTAL: $${total}`);
    
  } catch (error) {
    console.error('Error consultando detalle de factura:', error.message);
  }
}

async function menuPrincipal() {
  console.log('\n=== SISTEMA DE FACTURACION ===');
  console.log('1. Gestionar Productos');
  console.log('2. Gestionar Facturas');
  console.log('3. Salir');
  
  const opcion = await pregunta('Seleccione una opcion: ');
  
  switch(opcion) {
    case '1':
      await menuProductos();
      break;
    case '2':
      await menuFacturas();
      break;
    case '3':
      rl.close();
      process.exit(0);
    default:
      console.log('Opcion invalida');
  }
  
  await menuPrincipal();
}

async function iniciarApp() {
  await conectarDB();
  console.log('Bienvenido al Sistema de Facturacion');
  await menuPrincipal();
}

iniciarApp().catch(error => {
  console.error('Error iniciando aplicacion:', error);
  process.exit(1);
});