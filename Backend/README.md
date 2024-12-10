# Desafío Profesional Backend - Digital Money House

**Digital Money House** es una billetera virtual.  
El proyecto consiste en desarrollar el backend y la API que consume el frontend (ya hecho, desarrollado con React) e integrarlo al mismo.  
El lenguaje de programación es **Java con Spring Boot**, una arquitectura de microservicios y la seguridad está desarrollada con **JWT Token**.  
**Otras tecnologías usadas**: MySQL, Docker, Git.

## Funcionalidades

- Registro de usuarios en la app (también base de datos).
- Login de usuarios registrados.
- Autenticación por JWT token.
- Confirmación de registro mediante un código previamente enviado al email.
- Recuperación de contraseña mediante enlace previamente enviado al email.
- Obtener la información del usuario en el dashboard (saldo, avatar con su nombre y apellido, etc.).
- Registro de actividades.
- Generación automática de alias y CVU para el usuario registrado.
- Registro de tarjetas de débito/crédito.
- Eliminación de tarjeta.
- Cargar dinero mediante tarjeta de débito/crédito.
- Cargar dinero mediante transferencia de otro usuario utilizando alias o CVU.
- Envío de dinero mediante transferencia a otro usuario utilizando alias o CVU.
- Descarga de comprobante de actividad.
- Listado de todas las actividades.
- Listado de las últimas 5 actividades.
- Ver detalle de movimiento.

## Links

### Funcionalidades de la aplicación en video
- [Demostración completa](https://drive.google.com/file/d/12a3iOK3Yqokq_44ui74sezusXET9Gpmt/view?usp=sharing)
- [Recuperación de contraseña](https://drive.google.com/file/d/1wqE5vk_NEf613xCrRDaKLXNecKEPIXJg/view?usp=sharing)

## Microservicios

- **eureka-server**: Servicio de registro y descubrimiento de servicios.
- **config-server**: Servicio de configuraciones de los microservicios.
- **gateway**: Punto de entrada único para clientes externos que desean acceder a los diferentes servicios.
- **user-service**: Registro, logueo, actualización e información de usuario.
- **accounts-service**: Servicios relacionados a toda la operación de la billetera virtual.
- **Base de Datos**: MySQL.

## Pasos para ejecutar el frontend

1. **Clonar el repositorio del frontend**:  
   ```bash
   git clone https://github.com/MatiasMansilla55/Digital-Money-House-Desaf-o-Back-Front/tree/main
2. Entrar al Front-End con Visual Studio Code.

3. Ejecutar los siguientes comandos:

bash
Copiar código
   npm install
   npm start
## Pasos para ejecutar el backend

1. Clonar el repositorio del backend:
   ```bash
   git clone https://github.com/MatiasMansilla55/Digital-Money-House-Desaf-o-Back-Front/tree/main
2. Abrir el proyecto con IntelliJ.

3. Correr cada microservicio en el siguiente orden:

- eureka-server
- config-server
- gateway
- user-service
- accounts-service
4. Cada microservicio tiene su configuración en el repositorio de GitHub gestionado por el microservicio config-server.

5. La base de datos se levanta automáticamente.

## Repositorio de configuraciones (config-server)
Todas las configuraciones de los microservicios se encuentran aquí:
Config-server en GitHub
## Link hacia POSTMAN
https://web.postman.co/workspace/My-Workspace~0b688a8a-3650-4874-80f7-53698b23dc8f/request/19386776-32ef3e6d-f670-4020-9d2f-fa10289dcda0

## Links a la documentacion de SWAGGER
1. Accounts-Service:
http://localhost:8084/swagger-ui/index.html
2. User-Service
http://localhost:8087/swagger-ui/index.html
## Aclaración
Todos mis commits están en el repositorio de GitLab, ya que en las diapositivas del desafío se indicaba usar GitLab.
Sin embargo, también creé otro repositorio en GitHub con este backend y el frontend.
