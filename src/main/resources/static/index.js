// Selecciona el input de búsqueda por su ID 'buscar'
const inputBusqueda = document.getElementById('buscar');

// Selecciona todos los elementos con la clase 'product-card' (tarjetas de productos)
const productos = document.querySelectorAll('.product-card');

// Selecciona el elemento donde se mostrará el mensaje cuando no haya coincidencias
const mensaje = document.getElementById('mensaje-no-encontrado');

// Agrega un evento que se ejecuta cada vez que el usuario escribe algo en el input
inputBusqueda.addEventListener('input', () => {
  // Obtiene el texto escrito en el input y lo convierte a minúsculas para comparación insensible a mayúsculas
  const valor = inputBusqueda.value.toLowerCase();

  // Variable que indica si se encontró alguna coincidencia con la búsqueda
  let hayCoincidencias = false;

  // Recorre cada tarjeta de producto para verificar si el nombre coincide con el texto buscado
  productos.forEach(card => {
    // Obtiene el texto del título del producto (suponiendo que está en un <h2>) y lo convierte a minúsculas
    const nombre = card.querySelector('h2').textContent.toLowerCase();

    // Si el nombre incluye el texto buscado, muestra la tarjeta y marca que hay coincidencias
    if (nombre.includes(valor)) {
      card.style.display = 'block'; // Muestra la tarjeta
      hayCoincidencias = true;       // Marca que hay al menos una coincidencia
    } else {
      // Si no coincide, oculta la tarjeta
      card.style.display = 'none';
    }
  });

  // Muestra el mensaje "no encontrado" solo si no hay coincidencias, de lo contrario lo oculta
  mensaje.style.display = hayCoincidencias ? 'none' : 'block';
});
