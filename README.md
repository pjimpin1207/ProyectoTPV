# Proyecto-Final-FP
Proyecto Final PF - Programación 25/26
---
## Idea de Proyecto
La idea del proyecto consiste en desarrollar una aplicación en Java para la gestión de un negocio de hostelería, como un bar o restaurante. El programa permitiría llevar el control de las comandas de las mesas, añadir los pedidos que van haciendo los clientes, modificar o eliminar productos y calcular el total de la cuenta. Además, también podría incluir la gestión de mesas libres u ocupadas, el control del menú disponible y un pequeño registro de ventas del día. La intención es que sea una aplicación práctica y sencilla, pensada para facilitar el trabajo diario en el local.
---
# Resumen del Proyecto: Sistema TPV en Java

## Idea del proyecto
Hemos montado un **TPV de restaurante en Java**, con una estructura clara y lógica parecida a la vida real (mesas, pedidos, cobros, etc.).
---
## Qué hace el sistema
- Login de usuarios  
- Gestión de mesas  
- Creación de tickets (pedidos)  
- Añadir productos  
- Calcular total, descuentos y dividir cuenta  
- Cobrar y controlar la caja  
---
## Clases principales

### SistemaTPV (clase)
Es el “cerebro” del sistema:
- Controla sesiones  
- Guarda tickets  
- Lleva la caja  
---
### Usuario (clase)
Clase base con:
- Datos comunes  
- Login  

#### Camarero (clase)
- Usa el sistema  
- Selecciona mesas  
- Crea tickets  

#### Administrador (clase)
- Gestiona productos  
- Consulta la caja  
---
### Mesa (clase)
- Número de mesa  
- Estado (libre, ocupada, etc.)  
- Se puede cambiar y liberar  
---
### Ticket (clase)
- Guarda productos (`ArrayList`)  
- Calcula total  
- Aplica descuentos  
- Divide cuenta  
- Cobra  
---
### Producto (clase)
- Datos básicos: nombre, precio, categoría  
---
## Enumeraciones

### EstadoMesa (enum)
- LIBRE  
- OCUPADA  
- PENDIENTE_PAGO  
- RESERVADA  
### Categoria (enum)
- BEBIDA  
- COMIDA  
- POSTRE  
---
## Relaciones importantes
- El sistema controla mesas y tickets  
- Los camareros crean tickets  
- Una mesa tiene 0 o 1 ticket  
- Un ticket tiene varios productos  
---
## Lo importante
- Uso de herencia  
- Uso de `ArrayList`  
- Lógica real (no solo guardar datos)  
- Proyecto bastante completo para DAW  
---
