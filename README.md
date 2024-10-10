
### **README - Aplicación de Registro de Usuarios con Spring Boot**

---

## **Descripción de la Aplicación**

Esta aplicación es una API RESTful desarrollada con **Spring Boot**, que expone un endpoint para el **registro de usuarios**. Los usuarios pueden registrarse proporcionando su nombre, correo electrónico, contraseña y una lista de teléfonos. El sistema valida que el correo y la contraseña sigan las reglas establecidas, genera un **token JWT**, y almacena el registro del usuario en una base de datos en memoria **H2**.

---

### **Características Principales:**

- **API RESTful** para el registro de usuarios.
- Validación de correo electrónico y contraseña mediante expresiones regulares configurables.
- Generación y almacenamiento de un **token JWT** junto con los datos del usuario.
- Persistencia de los datos en una base de datos **H2 en memoria**.
- **Manejo global de excepciones** para respuestas consistentes de errores en formato JSON.
- Documentación interactiva de la API a través de **Swagger**.

---

## **Requisitos**

Para ejecutar esta aplicación, necesitas lo siguiente:

- **JDK 17** o superior.
- **Gradle Wrapper** incluido en el proyecto (no necesitas tener Gradle instalado).
- **Postman** o cualquier herramienta para probar APIs (opcional).
- **curl** (opcional para pruebas desde la terminal).

---

## **Instalación y Configuración**

### **1. Clonar el Repositorio**

Clona el repositorio en tu máquina local:

```bash
git clone https://github.com/Feroli/nisum.git
cd nisum
```

### **2. Compilar el Proyecto**

Para compilar el proyecto y ejecutar las pruebas automáticas:

```bash
./gradlew clean build
```

Este comando:

- Limpia cualquier build previo.
- Compila el código.
- Ejecuta las pruebas unitarias y genera un informe de prueba.

### **3. Ejecutar la Aplicación**

Ejecuta la aplicación usando Gradle Wrapper:

```bash
./gradlew bootRun
```

Esto iniciará la aplicación y la API estará disponible en `http://localhost:8080`.

---

## **Uso de la API**

### **1. Endpoints Disponibles**

#### **Registro de Usuarios**

- **URL:** `/registro`
- **Método:** `POST`
- **Descripción:** Registra un nuevo usuario en el sistema.
  
  **Cuerpo de la solicitud (JSON):**

  ```json
  {
    "name": "Juan Rodríguez",
    "email": "juan.rodriguez@example.com",
    "password": "Password123",
    "phones": [
      {
        "number": "1234567",
        "citycode": "1",
        "contrycode": "57"
      }
    ]
  }
  ```

  **Respuesta exitosa (201 CREATED):**

  ```json
  {
    "id": "uuid-generado",
    "created": "2023-10-10T12:34:56.789",
    "modified": "2023-10-10T12:34:56.789",
    "lastLogin": "2023-10-10T12:34:56.789",
    "token": "jwt-token-generado",
    "isActive": true
  }
  ```

  **Respuesta de error (409 CONFLICT) - Correo duplicado:**

  ```json
  {
    "mensaje": "El correo ya está registrado"
  }
  ```

  **Respuesta de error (400 BAD REQUEST) - Validación de email/contraseña:**

  ```json
  {
    "mensaje": "Formato de correo inválido"
  }
  ```

---

## **Pruebas Automáticas**

### **Ejecutar las Pruebas Unitarias**

Para ejecutar las pruebas unitarias incluidas en el proyecto, utiliza el siguiente comando:

```bash
./gradlew test
```

Esto ejecutará las pruebas definidas en el proyecto, las cuales validan el comportamiento de los componentes principales, como el registro de usuarios, validación de datos, y generación de tokens.

### **Reporte de Resultados**

Una vez que las pruebas finalicen, podrás ver el reporte en formato HTML en:

```
build/reports/tests/test/index.html
```

---

## **Pruebas Manuales**

### **1. Probar la API con Postman**

Sigue estos pasos para probar manualmente el endpoint de registro de usuarios usando **Postman**:

1. Abre Postman y crea una nueva solicitud **POST**.
2. Configura la URL como: `http://localhost:8080/registro`.
3. En la pestaña **Body**, selecciona **raw** y el formato **JSON**.
4. Introduce el siguiente cuerpo de solicitud:

```json
{
  "name": "Juan Rodríguez",
  "email": "juan.rodriguez@example.com",
  "password": "Password123",
  "phones": [
    {
      "number": "1234567",
      "citycode": "1",
      "contrycode": "57"
    }
  ]
}
```

5. Envía la solicitud y verifica la respuesta:
   - **201 Created** si el registro es exitoso.
   - **409 Conflict** si el correo ya está registrado.
   - **400 Bad Request** si la validación de los datos falla.

### **2. Probar la API con `curl`**

Si prefieres usar la terminal, puedes enviar solicitudes de prueba usando **curl**. Aquí tienes un ejemplo:

```bash
curl -X POST http://localhost:8080/registro -H "Content-Type: application/json" -d '{
  "name": "Juan Rodríguez",
  "email": "juan.rodriguez@example.com",
  "password": "Password123",
  "phones": [
    {
      "number": "1234567",
      "citycode": "1",
      "contrycode": "57"
    }
  ]
}'
```

### **3. Verificar la Base de Datos H2**

Si deseas verificar que los datos se han guardado correctamente en la base de datos H2, sigue estos pasos:

1. Accede a la consola H2 en tu navegador: `http://localhost:8080/h2-console`.
2. Configura los parámetros de conexión:
   - **JDBC URL**: `jdbc:h2:mem:*****` (busca en los logs por url=jdbc:h2:mem para encontrar el id de la instancia)
   - **Username**: `sa`
   - **Password**: (déjalo vacío)
3. Ejecuta la consulta SQL para verificar los registros de usuarios:

```sql
SELECT * FROM app_user;
```

---

## **Swagger para la Documentación de la API**

La documentación interactiva de la API está disponible a través de **Swagger**. Puedes acceder a la interfaz de Swagger en:

```
http://localhost:8080/swagger-ui.html
```

Esta página te permitirá probar los endpoints de la API directamente desde el navegador.

---

## **Configuraciones Adicionales**

### **Expresiones Regulares Configurables**

Puedes ajustar las expresiones regulares para la validación de correo electrónico y contraseña en el archivo `application.yml`.

---

