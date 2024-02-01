### Sergio Daniel Lopez Vargas
# AREP_Taller1
Este Taller implementa un servidor web en Java que permite a los usuarios 
buscar información sobre películas utilizando la API gratuita OMDb API. 
La aplicación proporciona una interfaz simple a través de un navegador 
web donde los usuarios pueden ingresar el título de la película y obtener 
detalles sobre la misma en formato JSON.
Proporciona una base sólida para desarrollar un servidor web 
modular y extensible para consultar información sobre películas, 
utilizando patrones de diseño y buenas prácticas de programación.

### Funcionamiento
La aplicación funciona al ejecutarla localmente en un entorno Java 
con Maven instalado y conexión a internet. Los usuarios pueden acceder 
al servidor a través de un navegador web y utilizar el formulario en la 
página principal para realizar búsquedas de películas.

### Usa patrones
Se hace uso del patrón de diseño de inversión de dependencias y
el uso de las clases HttpMovie y HttpServer. Esto permite que el servidor
HTTP (HttpServer) no dependa directamente de la implementación
concreta de la API de películas.

### Modular
El proyecto está organizado en módulos claros y distintos:
el servidor web (HttpServer) y la API para consultar películas
(HttpMovie).
Cada uno de estos módulos tiene su responsabilidad específica,
lo que facilita la comprensión y el mantenimiento del código.


### Instrucciones de Ejecución
* Clone el repositorio desde GitHub:

```git clone https://github.com/tu_usuario/servidor-peliculas.git```

* Navegue al directorio del proyecto: 

```cd servidor-peliculas```

* Compile el proyecto y descargue las dependencias con Maven: 

```mvn clean package```

* Ejecute el servidor utilizando el siguiente comando: 

```java -cp target/classes edu.escuelaing.arem.ASE.app.HttpServer```

Una vez que el servidor esté en funcionamiento, acceda a 
http://localhost:35000/ desde su navegador para comenzar a buscar películas.

### Diseño y Extensibilidad
El proyecto está diseñado de manera modular y extensible para permitir futuras expansiones y cambios en la funcionalidad del servidor. Algunas consideraciones sobre el diseño y la extensibilidad incluyen:

* Interfaz de Servicio: El servidor utiliza la clase HttpMovie para abstraer la lógica de consulta de películas. Esto permite que diferentes implementaciones de servicios de consulta puedan ser fácilmente intercambiadas.

* Implementación de Proveedores de Servicios: Se pueden crear nuevas clases que implementen a HttpMovie para manejar diferentes proveedores de servicios de consulta de películas. Por ejemplo, se puede implementar un nuevo proveedor de servicio que utilice una API diferente para obtener información sobre películas.

* Configurabilidad: La aplicación está configurada para escuchar en el puerto 35000, pero este valor se puede cambiar fácilmente modificando la configuración en la clase HttpServer.

* Manejo de Errores: El servidor está diseñado para manejar errores de consulta de manera adecuada y proporcionar retroalimentación al usuario en caso de que no se encuentre la película solicitada.



