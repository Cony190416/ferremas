# 🛠️ Ferremas - Proyecto de Integración de Plataformas

Este proyecto fue desarrollado por **Constanza Mena Aldana** y **John Zapata** para la asignatura **Integración de Plataformas**, impartida por el profesor **Hernán López Gonzales**.

📅 Fecha de entrega: **29 de mayo de 2025**

---

## 🧾 Descripción

**Ferremas** es un sistema de gestión construido con **Spring Boot**, que utiliza **MySQL** como base de datos y expone una API RESTful con documentación integrada mediante **Swagger**. 

Incluye pruebas básicas en **Postman** para validar los endpoints de manera sencilla.

---

## ⚙️ Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.6
- Spring Data JPA
- Spring Web / WebFlux
- Hibernate
- MySQL
- Lombok
- Swagger (springdoc-openapi)
- Maven
- Visual Studio Code

---

## 🧩 Configuración del entorno

### 🔌 Base de datos (MySQL)

Debes crear una base de datos llamada `ferremas_db`.

#### Configuración en `application.properties`:
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/ferremas_db
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
```

⚠️ Asegúrate de tener MySQL corriendo en tu equipo y con el usuario `root` sin contraseña (o modifícalo según tu configuración).

---

## 🚀 Ejecución del proyecto

Puedes ejecutar este proyecto de dos formas:

### 1. Desde Visual Studio Code

- Abre la carpeta del proyecto
- Ejecuta desde el archivo `FerremasApplication.java` con el botón "Run"
- O bien, desde la terminal:
```bash
./mvnw spring-boot:run
```

### 2. Desde línea de comandos con Maven

```bash
mvn spring-boot:run
```

---
desde que se corre el proyecto funcionan los siguientes localhost en el buscador directamente si necesidad de postman:
http://localhost:8080/
http://localhost:8080/divisa/convertir?amount=1000 html
http://localhost:8080/api/divisa/convertir?amount=1000 
http://localhost:8080/divisa/dolar html
http://localhost:8080/api/divisa/dolar
http://localhost:8080/api/divisa/convertir-inverso?amount=150
http://localhost:8080/divisa/convertir-inverso?amount=150 html

## 📬 Endpoints disponibles
Se recomienda utilizar postman para revisar los crud dadi que en swangger no esta tan clarificado.
La API está documentada automáticamente con **Swagger**.

📌 Accede desde:  
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  

### Ejemplos de rutas típicas (ajustar según el código):

- `GET /api/productos`
- `GET /api/productos/{id}`
- `POST /api/productos`
- `PUT /api/productos/{id}`
- `DELETE /api/productos/{id}`

---

## 🧪 Pruebas en Postman

Se incluye una colección de pruebas Postman con los siguientes endpoints:

✅ CRUD de productos  

📁 La colección se encuentra en la raíz del proyecto o en la carpeta `/postman`.

---

## 📚 Dependencias principales del proyecto

Revisar el archivo `pom.xml` para ver las dependencias detalladas, incluyendo:

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-validation`
- `mysql-connector-j`
- `lombok`
- `springdoc-openapi-starter-webmvc-ui`

---
## 🧪 Pruebas en Postman

La carpeta [`PRUEBAS`](./PRUEBAS) contiene las pruebas realizadas en Postman para todos los endpoints desarrollados.

### 👉 ¿Cómo usarlas?

1. Abre Postman.
2. Haz clic en `Import` (Importar).
3. Selecciona los archivos `.json` que están dentro de la carpeta `PRUEBAS`.
4. Postman cargará automáticamente todas las colecciones.

📦 Endpoints principales (Puerto 8080)

Funcionalidad	Método	Endpoint
Listar productos	GET	http://localhost:8080/api/productos
Buscar por código	GET	http://localhost:8080/api/productos/{codigo}
Buscar por nombre	GET	http://localhost:8080/api/productos/buscar?nombre=XXX
Stock bajo	GET	http://localhost:8080/api/productos/stock?limite=30
Crear producto	POST	http://localhost:8080/api/productos
Eliminar producto	DELETE	http://localhost:8080/api/productos/{codigo}
Agregar precio	POST	http://localhost:8080/api/productos/{codigo}/precios

🟡 Dólar
Función	Endpoint
Obtener valor actual del dólar	http://localhost:8080/api/divisa/dolar
Convertir pesos a dólares	http://localhost:8080/api/divisa/convertir?amount=10000
Convertir dólares a pesos	http://localhost:8080/api/divisa/convertir-inverso?amount=100

🟢 Webpay (Entorno de pruebas)
Función	Endpoint
Iniciar pago	http://localhost:8080/api/pago/iniciar?monto=1000
Confirmar pago	http://localhost:8080/api/pago/confirmar?token_ws={{token_ws}}
debe utilizar el token generado automaticamente en iniciar pago y levantar el servidor personalizado
## 🧠 Autores

- 👩‍💻 Constanza Mena Aldana  
- 👨‍💻 John Zapata  

---

## 📌 Notas finales

- El proyecto crea las tablas automáticamente gracias a `spring.jpa.hibernate.ddl-auto=update`
- Asegúrate de tener corriendo MySQL en `localhost:3306`
- Si usas Docker o alguna herramienta externa, recuerda ajustar el `application.properties`

---

¡Gracias por revisar este proyecto!
