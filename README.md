# 🛠️ Ferremas - Sistema de Gestión para Ferretería

Este proyecto fue desarrollado por **Constanza Mena Aldana** y **John Zapata** para la asignatura **Integración de Plataformas**, impartida por el profesor **Hernán López Gonzales**.

📅 Fecha de entrega: **29 de mayo de 2025**

---

## 🎯 Descripción del Proyecto

**Ferremas** es un sistema completo de gestión para ferretería desarrollado con **Spring Boot** que incluye:

- **Sistema de roles y autenticación** (Admin, Bodeguero, Vendedor, Contador, Cliente)
- **Gestión de productos** con precios dinámicos
- **Carrito de compras** funcional
- **Integración con WebPay** para pagos online
- **Conversión de divisas** en tiempo real
- **Panel administrativo** completo
- **Interfaz web responsiva** con Bootstrap

---

## ⚙️ Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.4.6**
- **Spring Security** (Autenticación y autorización)
- **Spring Data JPA** (Persistencia de datos)
- **Hibernate** (ORM)
- **MySQL** (Base de datos)
- **Maven** (Gestión de dependencias)

### Frontend
- **Thymeleaf** (Motor de plantillas)
- **Bootstrap 5** (Framework CSS)
- **JavaScript** (Interactividad)
- **HTML5/CSS3**

### APIs Externas
- **WebPay API** (Procesamiento de pagos)
- **mindicador.cl** (Conversión de divisas)

---

## 🚀 Instalación y Configuración

### 📋 Requisitos Previos

1. **Java 17** o superior
2. **MySQL 8.0** o superior
3. **Maven 3.6** o superior
4. **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### 🔧 Configuración de la Base de Datos

1. **Instalar MySQL** y asegurarse de que esté corriendo en `localhost:3306`

2. **Crear la base de datos:**
```sql
CREATE DATABASE ferremas_db;
```

3. **Configurar `application.properties`:**
```properties
# Configuración del servidor
server.port=8080

# Configuración de la base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/ferremas_db
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

# Configuración de seguridad
spring.security.user.name=admin
spring.security.user.password=admin123
```

⚠️ **Nota:** Ajusta el `username` y `password` según tu configuración de MySQL.

### 🏃‍♂️ Ejecutar el Proyecto

#### Opción 1: Desde la línea de comandos
```bash
# Navegar al directorio del proyecto
cd ferremas-main

# Compilar y ejecutar
mvn spring-boot:run
```

#### Opción 2: Desde un IDE
1. Abrir el proyecto en tu IDE
2. Ejecutar la clase `FerremasApplication.java`
3. El servidor se iniciará en `http://localhost:8080`

---

## 👥 Roles de Usuario y Credenciales

### 🔐 Credenciales de Acceso

| Rol | Email | Contraseña | Funcionalidades |
|-----|-------|------------|-----------------|
| **ADMINISTRADOR** | `admin@ejemplo.com` | `admin123` | Gestión completa del sistema |
| **BODEGUERO** | `bodeguero@ejemplo.com` | `bodeguero123` | Gestión de inventario |
| **VENDEDOR** | `vendedor@ejemplo.com` | `vendedor123` | Gestión de ventas |
| **CONTADOR** | `contador@ejemplo.com` | `contador123` | Reportes financieros |
| **CLIENTE** | debe crearse  una cuenta que se almacenara en bbdd y podra ingresar con credenciales creadas

### 🎯 Funcionalidades por Rol

#### 👑 **ADMINISTRADOR**
- Gestión completa de productos (CRUD)
- Gestión de usuarios y roles
- Reportes de ventas
- Configuración del sistema
- Acceso a todas las funcionalidades

#### 📦 **BODEGUERO**
- Gestión de inventario
- Control de stock
- Edición de productos
- Reportes de inventario

#### 💼 **VENDEDOR**
- Gestión de ventas
- Atención al cliente
- Reportes de ventas
- Procesamiento de pedidos

#### 📊 **CONTADOR**
- Reportes financieros
- Análisis de ventas
- Gestión de precios
- Reportes de rentabilidad

#### 🛒 **CLIENTE**
- Navegación de productos
- Carrito de compras
- Proceso de pago con WebPay
- Historial de compras

---

## 🌐 Funcionalidades del Sistema

### 🏠 **Página Principal**
- **URL:** `http://localhost:8080/`
- **Descripción:** Catálogo de productos con diseño responsivo
- **Funcionalidades:**
  - Visualización de productos con precios
  - Filtros por categoría
  - Búsqueda de productos
  - Agregar productos al carrito

### 🔐 **Sistema de Autenticación**
- **URL:** `http://localhost:8080/login`
- **Funcionalidades:**
  - Login con email y contraseña
  - Redirección automática según rol
  - Logout que redirige al index

### 🛒 **Carrito de Compras**
- **Funcionalidades:**
  - Agregar/eliminar productos
  - Modificar cantidades
  - Cálculo automático de totales
  - Integración con WebPay

### 💳 **Sistema de Pagos (WebPay)**
- **Funcionalidades:**
  - Generación de tokens de pago
  - Redirección a WebPay
  - Confirmación de pagos
  - Manejo de transacciones exitosas/fallidas

### 💱 **Conversión de Divisas**
- **Endpoints:**
  - `GET /api/divisa/dolar` - Obtener valor actual del dólar
  - `GET /api/divisa/convertir?amount=1000` - Convertir pesos a dólares
  - `GET /api/divisa/convertir-inverso?amount=100` - Convertir dólares a pesos

### 📊 **Panel Administrativo**
- **URL:** `http://localhost:8080/admin/home` (requiere login como ADMIN)
- **Funcionalidades:**
  - Dashboard con estadísticas
  - Gestión de productos (editar/eliminar)
  - Gestión de usuarios
  - Reportes de ventas

---

## 🔌 APIs y Endpoints

### 📦 **Gestión de Productos**
```http
GET    /api/productos                    # Listar todos los productos
GET    /api/productos/{codigo}          # Obtener producto por código
GET    /api/productos/con-precios       # Productos con precios incluidos
POST   /api/productos                   # Crear nuevo producto
PUT    /api/productos/{codigo}          # Actualizar producto
DELETE /api/productos/{codigo}          # Eliminar producto
```

### 👥 **Gestión de Usuarios**
```http
GET    /api/usuarios                    # Listar usuarios (ADMIN)
PUT    /api/usuarios/{id}/toggle       # Activar/desactivar usuario
POST   /api/clientes/registrar         # Registrar nuevo cliente
POST   /api/clientes/login             # Login de cliente
```

### 💳 **Sistema de Pagos**
```http
POST   /api/pago/iniciar               # Iniciar transacción WebPay
POST   /api/pago/confirmar             # Confirmar pago
GET    /api/pago/pago-exitoso          # Página de pago exitoso
GET    /api/pago/pago-fallido          # Página de pago fallido
```

### 💱 **Conversión de Divisas**
```http
GET    /api/divisa/dolar               # Valor actual del dólar
GET    /api/divisa/convertir           # Convertir pesos a dólares
GET    /api/divisa/convertir-inverso   # Convertir dólares a pesos
```

### 🛒 **Carrito de Compras**
```http
GET    /api/carrito                    # Obtener carrito actual
POST   /api/carrito/agregar            # Agregar producto al carrito
DELETE /api/carrito/eliminar/{id}      # Eliminar producto del carrito
PUT    /api/carrito/actualizar         # Actualizar cantidades
```

---

## 🧪 Pruebas con Postman

### 📁 Colecciones Incluidas

El proyecto incluye colecciones de Postman en la carpeta `PRUEBAS/`:

1. **FERREMAS - API COMPLETA.postman_collection.json**
   - Endpoints principales del sistema
   - Pruebas de autenticación
   - Gestión de productos

2. **FERREMAS-DOLAR.postman_collection.json**
   - Pruebas de conversión de divisas
   - Endpoints de mindicador.cl

3. **FERREMAS-WEBPAY.postman_collection.json**
   - Pruebas del sistema de pagos
   - Flujo completo de WebPay

### 🔧 Cómo Importar las Colecciones

1. Abrir **Postman**
2. Hacer clic en **"Import"**
3. Seleccionar los archivos `.json` de la carpeta `PRUEBAS/`
4. Las colecciones se cargarán automáticamente

### 🚀 Ejemplos de Pruebas

#### Probar Login de Admin
```http
POST http://localhost:8080/login
Content-Type: application/x-www-form-urlencoded

username=admin@ferremas.com&password=admin123
```

#### Obtener Productos
```http
GET http://localhost:8080/api/productos
```

#### Convertir Divisas
```http
GET http://localhost:8080/api/divisa/convertir?amount=10000
```

---

## 🏗️ Estructura del Proyecto

```
ferremas-main/
├── src/
│   ├── main/
│   │   ├── java/cl/duoc/ferremas/
│   │   │   ├── controller/          # Controladores REST
│   │   │   ├── model/              # Entidades JPA
│   │   │   ├── repository/         # Repositorios de datos
│   │   │   ├── service/            # Lógica de negocio
│   │   │   ├── security/           # Configuración de seguridad
│   │   │   └── util/               # Utilidades
│   │   └── resources/
│   │       ├── static/             # Archivos estáticos (CSS, JS, imágenes)
│   │       ├── templates/          # Plantillas Thymeleaf
│   │       └── application.properties
│   └── test/                       # Pruebas unitarias
├── PRUEBAS/                        # Colecciones de Postman
├── pom.xml                         # Dependencias Maven
└── README.md                       # Este archivo
```

---

## 🔧 Configuración de Seguridad

### 🛡️ Rutas Públicas
- `/` - Página principal
- `/login` - Página de login
- `/cliente/registro` - Registro de clientes
- `/api/productos/**` - APIs de productos
- `/api/divisa/**` - APIs de divisas
- `/api/pago/**` - APIs de pagos

### 🔒 Rutas Protegidas
- `/admin/**` - Solo ADMINISTRADOR
- `/bodeguero/**` - Solo BODEGUERO
- `/vendedor/**` - Solo VENDEDOR
- `/contador/**` - Solo CONTADOR
- `/cliente/home` - Solo CLIENTE autenticado

---

## 🚨 Solución de Problemas

### ❌ Error: "Port 8080 already in use"
```bash
# Matar procesos en el puerto 8080
taskkill /f /im java.exe
```

### ❌ Error: "Database connection failed"
1. Verificar que MySQL esté corriendo
2. Verificar credenciales en `application.properties`
3. Verificar que la base de datos `ferremas_db` exista

### ❌ Error: "Authentication failed"
1. Verificar que el usuario exista en la base de datos
2. Verificar que las credenciales sean correctas
3. Verificar que el rol esté asignado correctamente

### ❌ Error: "WebPay integration failed"
1. Verificar conexión a internet
2. Verificar configuración de WebPay
3. Verificar tokens de autenticación

---

## 📈 Características Destacadas

### ✅ **Funcionalidades Implementadas**
- [x] Sistema de autenticación con roles
- [x] Gestión completa de productos
- [x] Carrito de compras funcional
- [x] Integración con WebPay
- [x] Conversión de divisas en tiempo real
- [x] Panel administrativo completo
- [x] Interfaz web responsiva
- [x] APIs RESTful documentadas
- [x] Pruebas con Postman incluidas

### 🎨 **Interfaz de Usuario**
- Diseño moderno con Bootstrap 5
- Navegación intuitiva
- Responsive design para móviles
- Modales para edición de productos
- Notificaciones en tiempo real

### 🔒 **Seguridad**
- Autenticación basada en roles
- Protección CSRF habilitada
- Contraseñas encriptadas con BCrypt
- Sesiones seguras
- Logout automático

---

## 👥 Autores

- 👩‍💻 **Constanza Mena Aldana**
- 👨‍💻 **John Zapata**

---

## 📞 Soporte

Para reportar problemas o solicitar nuevas funcionalidades:

1. Verificar la sección de **Solución de Problemas**
2. Revisar los logs del servidor
3. Probar con las colecciones de Postman incluidas

---

## 📝 Notas de Desarrollo

- El proyecto utiliza **Spring Boot DevTools** para desarrollo
- Las tablas se crean automáticamente con `spring.jpa.hibernate.ddl-auto=update`
- Los logs están configurados para mostrar SQL queries
- El sistema incluye manejo de errores personalizado
- Todas las APIs incluyen validación de datos

---

## 🎯 Próximas Mejoras

- [ ] Implementación de notificaciones push
- [ ] Sistema de cupones y descuentos
- [ ] Reportes avanzados con gráficos
- [ ] Integración con redes sociales
- [ ] App móvil nativa
- [ ] Sistema de fidelización de clientes

---

**¡El sistema está listo para producción! 🚀**
