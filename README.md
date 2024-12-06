Desafio Profesional Backend - Digital Money House

Digital Money House una billetera virtual. El proyecto, en si mismo ,consiste en desarrollar el backend y la API que consume el frontend(ya hecho, desarrollado con React) e integrarlo al mismo. El lenguaje de programacion es Java con Springboot, una arquitectura de microservicios y la seguridad esta desarrollada con JWT token.
Otras tecnologias usadas: MySQL,Docker,Git.

Fucionalidades:

Registro de usuarios en la app (tambien base de datos)
Login de usuarios registrados.
Autenticacion por JWT token.
Confirmacion de registro mediante un codigo previamente enviado al email.
Recuperacion de contraseña mediante enlace previamente enviado al email.
Obtener la informacion del usuario en el dashboard(saldo,avatar con su nombre y apellido,etc).
Registro de Actividades.
Generacion automotica de Alias y CVU para el usuario registrado.
Registro de Tarjetas de Debito/Credito.
Eliminacion de tarjeta.
Cargar dinero mediante Tarjeta de Debito/Credito.
Cargar dinero mediante transferencia de otro usuario utilizando Alias o CVU.
Envio de dinero mediante transferencia a otro usuario utilizando Alias o CVU.
Descarga de comprobante de actividad.
Listado de todas las Actividades. 
Listado de las ultimas 5 Actividades.
Ver detalle de movimiento.

Links para ver todas las funcionalidades de la aplicacion en video:

https://drive.google.com/file/d/12a3iOK3Yqokq_44ui74sezusXET9Gpmt/view?usp=sharing

Recuperacion de contraseña:
https://drive.google.com/file/d/1wqE5vk_NEf613xCrRDaKLXNecKEPIXJg/view?usp=sharing

Microservicios:
eureka-server: servicio de registro y descubrimiento de servicios.
config-server: servicio de configuraciones de los microservicios.
gateway: punto de entrada único para clientes externos que desean acceder a los diferentes servicios.
user-service: Registro, logueo, actualizacion e informacion de usuario.
accounts-service: servicios relacionados a toda la operacion de la billetera virtual.
Base de Datos: MySQL

Pasos para ejecutar el frontend
Clonar repositorio del frontend -> git clone https://github.com/MatiasMansilla55/Digital-Money-House-Desaf-o-Back-Front/tree/main
Se descargara el Backend y Front-End.
Entrar al Front-End con VSC.
ejecutar el comando -> npm install.
ejecutar el comando -> npm start.

Pasos para ejecutar el backend
Clonar repositorio del backend -> git clone https://github.com/MatiasMansilla55/Digital-Money-House-Desaf-o-Back-Front/tree/main
Abrir el proyecto con IntelliJ.
Aveces se tiene que hacer click derecho en el archivo POM de cada microservicios y seleccionar "Ad as Maven" para que reconozca todas las dependencias instaladas.
Correr cada microservicio en el siguiente orden: eureka-server, config-server, gateway, users-service, accounts-service.
Cada microservicio tiene su configuracion en en github y todo se esto se encarga el microservicio config-server, la base de datos se levanta sola. 

Repostiorio del microservicio Config-server, donde estan todas las configuraciones de los microservicios:
https://github.com/MatiasMansilla55/config-server

Aclaracion: Todos mi commits estan en el repositorio de gitlab ya que en  las diapositivas decia que habia que usar gitlab. Igualmente cree otro repositorio en github con este backend y el front-end. 

