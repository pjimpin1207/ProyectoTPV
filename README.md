Documentación Técnica del Sistema TPV - Restaurante
Este documento detalla la arquitectura del software y la función de cada una de las clases que componen el sistema de Terminal Punto de Venta (TPV). El proyecto sigue un patrón de diseño estructurado en capas (Modelo, Vista, Controlador y DAO) y utiliza persistencia híbrida (MariaDB y ObjectDB).

1. Paquete main (Punto de Entrada)
Main.java: Es la clase principal que arranca la aplicación. Utiliza SwingUtilities.invokeLater para asegurar que la interfaz gráfica se ejecute en el hilo correcto de eventos (EDT), lanzando la VentanaLogin.

2. Paquete modelo (Lógica de Negocio)
Contiene las clases que representan los objetos reales del restaurante.

Usuario.java: Clase abstracta base que define los atributos comunes de cualquier empleado (id, nombre, password).

Administrador.java: Hereda de Usuario. Representa al gerente con acceso total al panel de control.

Camarero.java: Hereda de Usuario. Representa al personal de sala encargado de atender las mesas.

Producto.java: Entidad que representa un artículo de la carta. Contiene su id, nombre, categoría y precio.

Categoria.java: Enumeración (Enum) que clasifica los productos en BEBIDA, COMIDA o POSTRE.

Mesa.java: Representa una mesa física del local. Guarda su número, su estado actual y tiene asignado un Ticket vivo mientras está ocupada.

EstadoMesa.java: Enumeración que define los estados posibles de una mesa (LIBRE, OCUPADA, PENDIENTE_PAGO, RESERVADA).

Ticket.java: Entidad compleja que almacena la cuenta de una mesa. Contiene una lista de productos (ArrayList), un conjunto de camareros (HashSet para evitar repetidos), la fecha y el total. Está mapeada con anotaciones JPA (@Entity) para guardarse en ObjectDB.

3. Paquete controlador (Gestión de Estado)
GestorMesas.java: Aplica el patrón de diseño Singleton. Se encarga de instanciar las 11 mesas al abrir el programa y mantenerlas vivas en la memoria RAM. Gracias a esta clase, las mesas no pierden su información si el camarero entra y sale de la ventana de comandas.

4. Paquete dao (Data Access Object - Persistencia)
Gestiona toda la entrada y salida de datos a las bases de datos aislando las consultas SQL o JPA del resto del código.

ConexionDB.java: Establece la conexión JDBC con la base de datos relacional (MariaDB/MySQL).

ConexionObjectDB.java: Establece la conexión JPA (EntityManagerFactory) con la base de datos orientada a objetos (ObjectDB).

ProductoDAO.java: Realiza las operaciones CRUD (Crear, Leer, Actualizar, Borrar) de la carta de productos en MariaDB.

UsuarioDAO.java: Valida los inicios de sesión y permite crear o despedir camareros en MariaDB.

TicketDAO.java: Guarda un registro básico y relacional de los tickets cobrados en MariaDB (solo total y observaciones).

TicketObjectDBDAO.java: Guarda y recupera los objetos Ticket completos (con toda su lista interna de productos y camareros) en ObjectDB. Es la clase que nutre de datos al Cierre de Caja.

5. Paquete ui (User Interface - Vistas)
Contiene todas las ventanas gráficas diseñadas con la librería Java Swing.

VentanaLogin.java: Pantalla de bienvenida donde se establece la fecha de la sesión antes de abrir la sala.

VentanaMesas.java: Pantalla principal que muestra el mapa de mesas. Se comunica con el GestorMesas para pintar los botones de azul (libres) o rojo (ocupadas). Incluye el acceso seguro al administrador.

DialogoSeleccionCamarero.java: Ventana emergente (Popup) que lee dinámicamente de la base de datos los camareros disponibles para asignarlos a la mesa seleccionada.

VentanaComanda.java: Es el núcleo del TPV. Muestra la tabla interactiva de productos, suma los precios, permite modificar líneas de pedido de forma individual y genera la impresión del recibo en .txt.

DialogoCobro.java: Pantalla de confirmación de pago. Al aceptar, se encarga de llamar a los DAOs para guardar el registro en ambas bases de datos y, finalmente, libera la mesa para dejarla lista para nuevos clientes.

VentanaAdministrador.java: Panel de control general (Dashboard). Permite gestionar la plantilla, actualizar la carta (CRUD de productos) y ejecutar el "Cierre de Caja", que lee todo lo facturado en el día y genera un archivo histórico de contabilidad.

6. Paquete excepciones (Control de Errores)
MesaException.java: Excepción personalizada que salta si se intenta realizar una operación ilegal con una mesa (por ejemplo, intentar liberarla si el cliente aún no ha pagado).

ProductoNoEncontradoException.java: Excepción de seguridad por si se intenta interactuar con un producto que no existe en el registro.
