  const inputBusqueda = document.getElementById('buscar');
  const productos = document.querySelectorAll('.product-card');
  const mensaje = document.getElementById('mensaje-no-encontrado');

  inputBusqueda.addEventListener('input', () => {
    const valor = inputBusqueda.value.toLowerCase();
    let hayCoincidencias = false;

    productos.forEach(card => {
      const nombre = card.querySelector('h2').textContent.toLowerCase();
      if (nombre.includes(valor)) {
        card.style.display = 'block';
        hayCoincidencias = true;
      } else {
        card.style.display = 'none';
      }
    });

    mensaje.style.display = hayCoincidencias ? 'none' : 'block';
  });

