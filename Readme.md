# Manual de Usuario

## Requisitos Iniciales

- **Java 17**: Asegúrate de tener instalada la versión 17 de Java en tu máquina.
- **JavaFX SDK 22.0.2**: Este SDK es necesario para la interfaz gráfica. Si no se carga automáticamente, puedes añadirlo manualmente como una librería. La ruta por defecto es `Fudu/java-sdk-22.0.2`.
- **MySQL**: Se requiere una instancia de MySQL con un Schema previamente creado. Las tablas necesarias se cargarán automáticamente durante la ejecución inicial.

## Primera Ejecución

1. **Configuración de `application.properties`**:
    - Debes agregar la información de conexión a tu base de datos en el archivo `application.properties`. Los campos a configurar son:
      ```properties
      spring.datasource.url=coloca_aquí_la_información_del_Schema_usado
      spring.datasource.username=tu_usuario_de_MySQL
      spring.datasource.password=tu_contraseña_de_MySQL
      ```
    - Asegúrate de que esta configuración apunte al Schema que contiene o contendrá las tablas necesarias.

2. **Cargar Datos Iniciales**:
    - Una vez configurado, ejecuta la aplicación. Después de la primera ejecución, puedes cargar datos iniciales (Dummy) en las tablas ejecutando el archivo `DatosInicio.sql` en tu instancia de MySQL.
    - **Nota**: Si prefieres usar la aplicación en línea, puedes descomentar la sección en `application.properties` que menciona `#en supabase(online)` y comentar la que dice `#en mySql (Local)`. Esto evitará la necesidad de configurar una base de datos local, aunque puede ralentizar el rendimiento de la aplicación debido a la latencia de las cargas en línea.

## Diseño del Programa

El programa se construyó utilizando **Spring Boot** con **Java 17** para la lógica del negocio. Implementa un modelo de entidad-relación y un servicio que se conecta con dos controles:

- **ControllerJavaFx**: Se encarga de la implementación de la interfaz gráfica utilizando **JavaFX**.
- **Controller**: Proporciona acceso a través de endpoints sin interfaz gráfica, útil para herramientas como Postman.

Para simplificar el despliegue y reducir dependencias, se decidió usar JavaFX para la interfaz visual, haciendo del programa un monolito que se integra bien en diferentes entornos sin requerir múltiples configuraciones adicionales.

## Uso del Programa

Una vez que el programa se ejecuta, se muestra la pantalla inicial con las siguientes opciones de búsqueda:

### Personas

- **Ver Personas**: Muestra una lista de todas las personas en la base de datos.
- **Agregar Persona**: Permite crear nuevas personas utilizando el botón "Agregar" y completando los datos requeridos.
- **Actualizar/Modificar/Eliminar**: Puedes actualizar, modificar o eliminar la información de una persona haciendo clic sobre ella y utilizando las opciones disponibles.
- **Consultar Negocios**: Muestra los negocios asociados a una persona seleccionada. Desde allí, puedes asociar nuevos negocios a la persona utilizando la opción "Agregar" e ingresando el ID del negocio.

### Negocios

- **Ver Negocios**: Muestra una lista de todos los negocios en la base de datos.
- **Agregar Negocio**: Permite crear nuevos negocios utilizando el botón "Agregar" y completando los datos requeridos.
- **Actualizar/Modificar/Eliminar**: Puedes actualizar, modificar o eliminar la información de un negocio haciendo clic sobre él y utilizando las opciones disponibles.
- **Consultar Obligaciones**: Muestra las obligaciones asociadas a un negocio seleccionado. Desde allí, puedes asociar nuevas obligaciones al negocio utilizando la opción "Agregar" e ingresando el ID de la obligación.

### Obligaciones

- **Ver Obligaciones**: Muestra una lista de todas las obligaciones en la base de datos.
- **Agregar Obligación**: Permite crear nuevas obligaciones utilizando el botón "Agregar" y completando los datos requeridos.
- **Actualizar/Modificar/Eliminar**: Puedes actualizar, modificar o eliminar la información de una obligación haciendo clic sobre ella y utilizando las opciones disponibles.

### Información Adicional

- **Errores y Depuración**:

  - Si encuentras errores durante la ejecución, verifica que los requisitos iniciales están correctamente instalados y configurados.
  - Revisa el archivo application.properties para asegurarte de que la configuración de la base de datos es correcta.