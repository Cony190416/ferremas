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

## 📬 Endpoints disponibles

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
