// Variables globales
let productos = [];
let carrito = JSON.parse(localStorage.getItem('carrito')) || [];
let divisaActual = 'CLP';
let tasaCambio = 1;

// Elementos del DOM
const productGrid = document.getElementById('productGrid');
const loadingProducts = document.getElementById('loadingProducts');
const mensajeNoEncontrado = document.getElementById('mensaje-no-encontrado');
const buscarInput = document.getElementById('buscar');
const selectDivisa = document.getElementById('selectDivisa');
const cartIcon = document.getElementById('cartIcon');
const cartSidebar = document.getElementById('cartSidebar');
const cartOverlay = document.getElementById('cartOverlay');
const cerrarCarrito = document.getElementById('cerrarCarrito');
const cartContent = document.getElementById('cartContent');
const cartCount = document.getElementById('cartCount');
const cartTotal = document.getElementById('cartTotal');
const cartVaciar = document.getElementById('cartVaciar');
const cartNotification = document.getElementById('cartNotification');
const cartError = document.getElementById('cartError');

// Verificar elementos del DOM
console.log('🔍 Verificando elementos del DOM...');
console.log('📦 productGrid:', productGrid);
console.log('⏳ loadingProducts:', loadingProducts);
console.log('❌ mensajeNoEncontrado:', mensajeNoEncontrado);
console.log('🔍 buscarInput:', buscarInput);
console.log('💰 selectDivisa:', selectDivisa);
console.log('🛒 cartIcon:', cartIcon);

// Inicialización
document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 DOM cargado, inicializando aplicación...');
    
    // Prueba simple para verificar si los elementos funcionan
    console.log('🧪 Iniciando prueba simple...');
    if (productGrid) {
        console.log('✅ productGrid encontrado, agregando producto de prueba...');
        productGrid.innerHTML = `
            <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                <div class="product-card">
                    <img src="/img/default.jpg" alt="Producto de prueba" class="product-image">
                    <div class="product-body">
                        <h5 class="product-title">Producto de Prueba</h5>
                        <div class="product-price">$10,000</div>
                        <button class="btn-add-cart">
                            <i class="fas fa-cart-plus me-2"></i>Agregar al Carrito
                        </button>
                    </div>
                </div>
            </div>
        `;
        console.log('✅ Producto de prueba agregado');
    } else {
        console.error('❌ productGrid NO encontrado!');
    }
    
    cargarProductos();
    actualizarCarrito();
    configurarEventos();
});

// Configurar eventos
function configurarEventos() {
    // Búsqueda
    buscarInput.addEventListener('input', filtrarProductos);
    
    // Selector de divisa
    selectDivisa.addEventListener('change', cambiarDivisa);
    
    // Carrito
    cartIcon.addEventListener('click', abrirCarrito);
    cartOverlay.addEventListener('click', cerrarCarritoFuncion);
    cerrarCarrito.addEventListener('click', cerrarCarritoFuncion);
    cartVaciar.addEventListener('click', vaciarCarrito);
}

// Cargar productos desde el backend
async function cargarProductos() {
    try {
        console.log('🚀 Iniciando carga de productos...');
        console.log('📡 Haciendo petición a /api/productos/con-precios...');
        
        const response = await fetch('/api/productos/con-precios');
        
        console.log('📊 Response status:', response.status);
        console.log('📊 Response ok:', response.ok);
        console.log('📊 Response headers:', response.headers);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        console.log('📥 Parseando JSON...');
        productos = await response.json();
        console.log('✅ Productos cargados exitosamente');
        console.log('📦 Número de productos:', productos.length);
        console.log('📋 Primeros 3 productos:', productos.slice(0, 3));
        
        if (productos.length === 0) {
            console.log('⚠️ No hay productos disponibles');
            mostrarMensajeNoEncontrado('No hay productos disponibles');
        } else {
            console.log('🎨 Llamando a mostrarProductos...');
            mostrarProductos(productos);
        }
        
        console.log('🔚 Ocultando loading...');
        ocultarLoading();
    } catch (error) {
        console.error('❌ Error al cargar productos:', error);
        console.error('❌ Error stack:', error.stack);
        mostrarMensajeNoEncontrado('Error al cargar productos: ' + error.message);
        ocultarLoading();
    }
}

// Mostrar productos en la grilla
function mostrarProductos(productosAMostrar) {
    console.log('🎨 Iniciando mostrarProductos con', productosAMostrar.length, 'productos');
    console.log('🎯 Elemento productGrid:', productGrid);
    
    if (!productGrid) {
        console.error('❌ Elemento productGrid no encontrado!');
        return;
    }
    
    productGrid.innerHTML = '';
    console.log('🧹 ProductGrid limpiado');
    
    productosAMostrar.forEach((producto, index) => {
        console.log(`📦 Procesando producto ${index + 1}:`, producto);
        
        // Obtener el precio más reciente del producto
        let precio = 0;
        if (producto.precios && producto.precios.length > 0) {
            // Ordenar por fecha y tomar el más reciente
            const preciosOrdenados = producto.precios.sort((a, b) => 
                new Date(b.fecha) - new Date(a.fecha)
            );
            precio = preciosOrdenados[0].valor;
            console.log('💰 Precio encontrado:', precio);
        } else {
            console.log('⚠️ No se encontraron precios para el producto');
        }
        
        const precioFormateado = formatearPrecio(precio * tasaCambio, divisaActual);
        console.log('💵 Precio formateado:', precioFormateado);
        
        // Generar nombre de imagen basado en el código del producto
        const imagenProducto = producto.imagen || generarNombreImagen(producto.codigo);
        console.log('🖼️ Imagen generada:', imagenProducto);
        
        const productCard = `
            <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                <div class="product-card">
                    <img src="/img/${imagenProducto}" alt="${producto.nombre}" class="product-image" onerror="this.src='/img/default.jpg'">
                    <div class="product-body">
                        <h5 class="product-title">${producto.nombre}</h5>
                        <div class="product-price">${precioFormateado}</div>
                        <button class="btn-add-cart" onclick="agregarAlCarrito('${producto.codigo}')">
                            <i class="fas fa-cart-plus me-2"></i>Agregar al Carrito
                        </button>
                    </div>
                </div>
            </div>
        `;
        
        productGrid.innerHTML += productCard;
        console.log(`✅ Producto ${index + 1} agregado al HTML`);
    });
    
    console.log('🎉 HTML final generado. Longitud:', productGrid.innerHTML.length);
    console.log('🔍 Primeros 500 caracteres del HTML:', productGrid.innerHTML.substring(0, 500));
}

// Filtrar productos por búsqueda
function filtrarProductos() {
    const busqueda = buscarInput.value.toLowerCase().trim();
    
    if (busqueda === '') {
        mostrarProductos(productos);
        ocultarMensajeNoEncontrado();
        return;
    }
    
    const productosFiltrados = productos.filter(producto => 
        producto.nombre.toLowerCase().includes(busqueda)
    );
    
    if (productosFiltrados.length === 0) {
        mostrarMensajeNoEncontrado('No se encontraron productos con ese nombre');
        productGrid.innerHTML = '';
    } else {
        mostrarProductos(productosFiltrados);
        ocultarMensajeNoEncontrado();
    }
}

// Cambiar divisa
async function cambiarDivisa() {
    const nuevaDivisa = selectDivisa.value;
    
    if (nuevaDivisa === divisaActual) return;
    
    try {
        if (nuevaDivisa === 'CLP') {
            tasaCambio = 1;
        } else {
            const response = await fetch(`/api/divisa/convertir-generico?amount=1&desde=CLP&hacia=${nuevaDivisa}`);
            if (!response.ok) throw new Error('Error en conversión');
            const resultado = await response.json();
            
            if (resultado.error) {
                throw new Error(resultado.error);
            }
            
            tasaCambio = resultado.valor_convertido;
        }
        
        divisaActual = nuevaDivisa;
        mostrarProductos(productos);
        actualizarCarrito();
        
    } catch (error) {
        console.error('Error al cambiar divisa:', error);
        alert('Error al cambiar la divisa: ' + error.message);
    }
}

// Agregar producto al carrito
function agregarAlCarrito(codigoProducto) {
    const producto = productos.find(p => p.codigo === codigoProducto);
    
    if (!producto) {
        mostrarNotificacion(cartError, 'Producto no encontrado');
        return;
    }
    
    // Obtener el precio más reciente del producto
    let precio = 0;
    if (producto.precios && producto.precios.length > 0) {
        const preciosOrdenados = producto.precios.sort((a, b) => 
            new Date(b.fecha) - new Date(a.fecha)
        );
        precio = preciosOrdenados[0].valor;
    }
    
    const itemExistente = carrito.find(item => item.codigo === codigoProducto);
    
    if (itemExistente) {
        itemExistente.cantidad += 1;
    } else {
        carrito.push({
            codigo: producto.codigo,
            nombre: producto.nombre,
            precio: precio,
            imagen: producto.imagen || generarNombreImagen(producto.codigo),
            cantidad: 1
        });
    }
    
    guardarCarrito();
    actualizarCarrito();
    mostrarNotificacion(cartNotification, 'Producto agregado al carrito');
}

// Actualizar vista del carrito
function actualizarCarrito() {
    // Actualizar contador
    const totalItems = carrito.reduce((total, item) => total + item.cantidad, 0);
    cartCount.textContent = totalItems;
    
    // Actualizar contenido del carrito
    if (carrito.length === 0) {
        cartContent.innerHTML = '<p class="text-center text-muted">El carrito está vacío</p>';
        cartTotal.textContent = `Total: ${formatearPrecio(0, divisaActual)}`;
        return;
    }
    
    let contenidoHTML = '';
    let total = 0;
    
    carrito.forEach(item => {
        const precioItem = item.precio * item.cantidad * tasaCambio;
        total += precioItem;
        
        contenidoHTML += `
            <div class="cart-item">
                <img src="/img/${item.imagen}" alt="${item.nombre}" class="cart-item-image">
                <div class="cart-item-details">
                    <h6 class="cart-item-title">${item.nombre}</h6>
                    <div class="cart-item-price">${formatearPrecio(item.precio * tasaCambio, divisaActual)}</div>
                    <div class="cart-item-quantity">
                        <button onclick="cambiarCantidad('${item.codigo}', -1)" class="btn-quantity">-</button>
                        <span>${item.cantidad}</span>
                        <button onclick="cambiarCantidad('${item.codigo}', 1)" class="btn-quantity">+</button>
                    </div>
                </div>
                <button onclick="eliminarDelCarrito('${item.codigo}')" class="cart-item-remove">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        `;
    });
    
    cartContent.innerHTML = contenidoHTML;
    cartTotal.textContent = `Total: ${formatearPrecio(total, divisaActual)}`;
}

// Cambiar cantidad en el carrito
function cambiarCantidad(codigoProducto, cambio) {
    const item = carrito.find(item => item.codigo === codigoProducto);
    
    if (!item) return;
    
    item.cantidad += cambio;
    
    if (item.cantidad <= 0) {
        eliminarDelCarrito(codigoProducto);
    } else {
        guardarCarrito();
        actualizarCarrito();
    }
}

// Eliminar producto del carrito
function eliminarDelCarrito(codigoProducto) {
    carrito = carrito.filter(item => item.codigo !== codigoProducto);
    guardarCarrito();
    actualizarCarrito();
}

// Vaciar carrito
function vaciarCarrito() {
    carrito = [];
    guardarCarrito();
    actualizarCarrito();
    cerrarCarritoFuncion();
}

// Guardar carrito en localStorage
function guardarCarrito() {
    localStorage.setItem('carrito', JSON.stringify(carrito));
}

// Abrir carrito
function abrirCarrito() {
    cartSidebar.classList.add('open');
    cartOverlay.classList.add('show');
}

// Cerrar carrito
function cerrarCarritoFuncion() {
    cartSidebar.classList.remove('open');
    cartOverlay.classList.remove('show');
}

// Formatear precio según divisa
function formatearPrecio(precio, divisa) {
    const simbolos = {
        'CLP': '$',
        'USD': '$',
        'EUR': '€'
    };
    
    const simbolo = simbolos[divisa] || '$';
    
    if (divisa === 'CLP') {
        // Formato chileno: $4.990 (sin decimales)
        const precioEntero = Math.round(precio);
        const precioFormateado = precioEntero.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        return `${simbolo}${precioFormateado}`;
    } else {
        // Formato internacional: $5,38 (con decimales)
        const precioFormateado = precio.toFixed(2);
        const partes = precioFormateado.split('.');
        return `${simbolo}${partes.join(',')}`;
    }
}

// Mostrar notificación
function mostrarNotificacion(elemento, mensaje) {
    elemento.textContent = mensaje;
    elemento.classList.add('show');
    
    setTimeout(() => {
        elemento.classList.remove('show');
    }, 3000);
}

// Mostrar mensaje de no encontrado
function mostrarMensajeNoEncontrado(mensaje) {
    mensajeNoEncontrado.textContent = mensaje;
    mensajeNoEncontrado.style.display = 'block';
}

// Ocultar mensaje de no encontrado
function ocultarMensajeNoEncontrado() {
    mensajeNoEncontrado.style.display = 'none';
}

// Ocultar loading
function ocultarLoading() {
    loadingProducts.style.display = 'none';
}

// Generar nombre de imagen basado en el código del producto
function generarNombreImagen(codigo) {
    // Mapeo de códigos a nombres de archivo de imagen basado en los productos reales de la BD
    const mapeoImagenes = {
        // Herramientas Manuales
        'FER-001': 'martillo carpintero.jpg',           // Martillo de Carpintero 16oz
        'FER-002': 'martillo de goma.jpg',              // Martillo de Goma 12oz
        'FER-003': 'martillo bola 20 oz.jpg',           // Martillo de Bola 20oz
        'FER-004': 'destornillasor plano.jpg',          // Destornillador Plano 1/4"
        'FER-005': 'destornillador cruz.jpg',           // Destornillador Phillips #2
        'FER-006': 'juego destornilladores.jpg',        // Juego Destornilladores 12 Piezas
        'FER-007': 'llave inglesa.jpg',                 // Llave Inglesa 12"
        'FER-008': 'juego de llaves.jpg',               // Juego Llaves Mixtas 8-19mm
        'FER-009': 'llave de tubo.jpg',                 // Llave de Tubo 1/2"
        
        // Herramientas Eléctricas
        'FER-010': 'taladro percutor.jpg',              // Taladro Percutor 1/2"
        'FER-011': 'taladro inalambrico.jpg',           // Taladro Inalámbrico 18V
        'FER-014': 'sierra caladora.jpg',               // Sierra Caladora 650W
        'FER-015': 'sierra de sable.jpg',               // Sierra de Sable 1100W
        'FER-016': 'lijadora orbital.jpg',              // Lijadora Orbital 240W
        'FER-017': 'lijadora de banda.jpg',             // Lijadora de Banda 950W
        'FER-018': 'lijadora angular.jpg',              // Lijadora Angular 4-1/2"
        
        // Materiales Básicos
        'MAT-001': 'cemento.jpg',                       // Cemento Portland 25kg
        'MAT-002': 'arena.jpg',                         // Arena Gruesa m³
        'MAT-003': 'ladrillo.jpg',                      // Ladrillo Fiscal Rojo
        'MAT-004': 'gravilla.jpg',                      // Gravilla 1/2" m³
        'MAT-005': 'cal.jpg',                           // Cal Hidratada 25kg
        
        // Acabados
        'MAT-006': 'pintura blanca latex.jpg',          // Pintura Látex Blanco 4L
        'MAT-007': 'pintura azul.jpg',                  // Pintura Esmalte Azul 1L
        'MAT-008': 'barniz.jpg',                        // Barniz Marino 1L
        'MAT-009': 'ceramica piso.jpg',                 // Cerámica Piso 30x30cm
        'MAT-010': 'ceramica muro.jpg',                 // Cerámica Muro 20x30cm
        
        // Equipos de Medición
        'MED-001': 'huincha medir.jpg',                 // Huincha Métrica 5m
        'MED-002': 'nivel burbuja.jpg',                 // Nivel Burbuja 60cm
        'MED-003': 'escuadra.jpg',                      // Escuadra Metálica 30cm
        'MED-004': 'calibrador digital.jpg',            // Calibrador Digital 150mm
        'MED-005': 'laser nivel automatico.jpg',        // Láser Nivel Automático
        
        // Equipos de Seguridad
        'SEG-001': 'casco seguridad.jpg',               // Casco Seguridad Blanco
        'SEG-002': 'guantes.jpg',                       // Guantes Cuero Soldador
        'SEG-003': 'lentes.jpg',                        // Lentes Seguridad Claros
        'SEG-004': 'chaleco reflectante.jpg',           // Chaleco Reflectante
        'SEG-005': 'zapato seguridad.jpg',              // Zapatos Seguridad Punta Acero
        
        // Tornillos y Anclajes
        'ACC-001': 'tornillo madera.jpg',               // Tornillo Madera 3x25mm (100u)
        'ACC-002': 'tornillo autoperforante.jpg',       // Tornillo Autoperforante 3x16mm (100u)
        'ACC-003': 'anclaje quimico.jpg',               // Anclaje Químico 12x110mm
        'ACC-004': 'perno anclaje.jpg',                 // Perno de Anclaje 8x60mm
        
        // Fijaciones y Adhesivos
        'ACC-005': 'silicona neutra.jpg',               // Silicona Neutra Transparente
        'ACC-006': 'adhesivo epoxico.jpg',              // Adhesivo Epóxico 50ml
        'ACC-007': 'cinta americana.jpg',               // Cinta Americana Negra
        'ACC-008': 'sellador.jpg'                       // Sellador Poliuretano Gris
    };
    
    return mapeoImagenes[codigo] || 'default.jpg';
}

// Cerrar carrito con Escape
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        cerrarCarritoFuncion();
    }
});

console.log('JavaScript cargado correctamente');

// Función para procesar el pago con WebPay
async function procesarPago() {
    if (carrito.length === 0) {
        alert('El carrito está vacío');
        return;
    }

    try {
        // Preparar datos del carrito
        const carritoData = carrito.map(item => ({
            codigo: item.codigo,
            nombre: item.nombre,
            precio: item.precio,
            cantidad: item.cantidad
        }));

        // Datos para enviar al backend
        const requestData = {
            carrito: carritoData,
            divisa: divisaActual,
            tasaCambio: tasaCambio,
            clienteId: null // Por ahora null, se puede conectar con login después
        };

        console.log('🚀 Iniciando pago con WebPay...');
        console.log('📦 Datos del carrito:', requestData);

        const response = await fetch('/api/pago/iniciar-carrito', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestData)
        });

        if (!response.ok) {
            // Verificar si la respuesta es HTML en lugar de JSON
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('text/html')) {
                throw new Error('El servidor devolvió una página HTML en lugar de JSON. Esto puede indicar un problema de autenticación o configuración.');
            }
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Verificar que la respuesta sea JSON
        const contentType = response.headers.get('content-type');
        if (!contentType || !contentType.includes('application/json')) {
            throw new Error('El servidor no devolvió JSON válido. Verifica la configuración del servidor.');
        }

        const resultado = await response.json();
        console.log('✅ Respuesta de WebPay:', resultado);

        if (resultado.error) {
            throw new Error(resultado.error);
        }

        // Redirigir a WebPay
        if (resultado.full_url) {
            console.log('🌐 Redirigiendo a WebPay:', resultado.full_url);
            window.location.href = resultado.full_url;
        } else {
            throw new Error('No se recibió URL de WebPay');
        }

    } catch (error) {
        console.error('❌ Error al procesar pago:', error);
        alert('Error al procesar el pago: ' + error.message);
    }
}

// Función para mostrar resumen del carrito antes del pago
function mostrarResumenPago() {
    if (carrito.length === 0) {
        alert('El carrito está vacío');
        return;
    }

    const total = carrito.reduce((sum, item) => sum + (item.precio * item.cantidad), 0);
    const totalFormateado = formatearPrecio(total, divisaActual);

    const confirmacion = confirm(
        `Resumen de la compra:\n\n` +
        `Productos: ${carrito.length}\n` +
        `Total: ${totalFormateado}\n\n` +
        `¿Deseas proceder con el pago?`
    );

    if (confirmacion) {
        procesarPago();
    }
} 