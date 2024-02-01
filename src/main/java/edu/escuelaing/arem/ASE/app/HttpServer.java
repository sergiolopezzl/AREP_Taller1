package edu.escuelaing.arem.ASE.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.JsonObject;

/*
 * Documentación de la clase HttpServer
 *
 * La clase HttpServer representa un servidor HTTP simple que escucha en el puerto 35000
 * y gestiona las solicitudes entrantes de los clientes. Utiliza un objeto HttpMovie para
 * obtener información sobre películas y generar respuestas HTML para enviar a los clientes.
 */
public class HttpServer {
    private HttpMovie service;

    /**
     * Constructor de la clase HttpServer.
     * @param service Un objeto HttpMovie que se utilizará para obtener información sobre películas.
     */
    public HttpServer(HttpMovie service) {
        this.service = service;
    }

    /**
     * Inicia el servidor HTTP y comienza a escuchar en el puerto 35000 para las solicitudes entrantes.
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(35000)) {
            System.out.println("Server started. Listening on port 35000...");

            while (true) {
                handleClient(serverSocket);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000. " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Maneja la solicitud de un cliente aceptando la conexión del cliente, leyendo la URI solicitada,
     * generando una respuesta HTML correspondiente y enviándola al cliente.
     * @param serverSocket El socket del servidor que acepta conexiones de clientes.
     * @throws IOException Si hay un error al manejar la solicitud del cliente.
     */
    private void handleClient(ServerSocket serverSocket) throws IOException {
        try (Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String uriStr = extractURI(in);
            String outputLine = "";

            if (uriStr.startsWith("/movie")) {
                outputLine = movieHtmlIndex(uriStr);
            } else {
                outputLine = obtainHtml();
            }

            out.println(outputLine);
        } catch (IOException e) {
            System.err.println("Error handling client request: " + e.getMessage());
        }
    }

    /**
     * Extrae la URI solicitada de la solicitud HTTP del cliente.
     * @param in El lector de entrada para leer los datos de la solicitud del cliente.
     * @return La cadena de la URI solicitada por el cliente.
     * @throws IOException Si hay un error al leer los datos de la solicitud del cliente.
     */
    private String extractURI(BufferedReader in) throws IOException {
        String inputLine;
        String uriStr = "";

        boolean isFirstLine = true;
        while ((inputLine = in.readLine()) != null) {
            if (isFirstLine) {
                uriStr = inputLine.split(" ")[1];
                isFirstLine = false;
            }
            if (!in.ready()) {
                break;
            }
        }
        return uriStr;
    }

    /**
     * Genera y devuelve una respuesta HTML que muestra la información de una película solicitada.
     * @param uriStr La URI de la solicitud HTTP que contiene el título de la película.
     * @return La respuesta HTML que muestra la información de la película.
     * @throws IOException Si hay un error al obtener la información de la película.
     */
    private String movieHtmlIndex(String uriStr) throws IOException {
        JsonObject response = service.get(uriStr);
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n"
                + "<html lang=\"en\">\r\n"
                + "<head>\r\n"
                + "    <meta charset=\"UTF-8\">\r\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                + "    <title>Pelicula:</title>\r\n"
                + "    <style>\r\n"
                + "        body {\r\n"
                + "            font-family: Arial, sans-serif;\r\n"
                + "            background-color: #f4f4f4;\r\n"
                + "            margin: 0;\r\n"
                + "            padding: 0;\r\n"
                + "        }\r\n"
                + "        .container {\r\n"
                + "            max-width: 800px;\r\n"
                + "            margin: 20px auto;\r\n"
                + "            padding: 20px;\r\n"
                + "            background-color: #fff;\r\n"
                + "            border-radius: 10px;\r\n"
                + "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\r\n"
                + "        }\r\n"
                + "        .poster {\r\n"
                + "            max-width: 100%;\r\n"
                + "            height: auto;\r\n"
                + "            border-radius: 10px;\r\n"
                + "            margin-bottom: 20px;\r\n"
                + "        }\r\n"
                + "        .movie-details {\r\n"
                + "            display: flex;\r\n"
                + "            align-items: flex-start;\r\n"
                + "        }\r\n"
                + "        .details {\r\n"
                + "            flex: 1;\r\n"
                + "        }\r\n"
                + "        .title {\r\n"
                + "            font-size: 24px;\r\n"
                + "            margin: 0 0 10px;\r\n"
                + "        }\r\n"
                + "        .info span {\r\n"
                + "            display: block;\r\n"
                + "            margin-bottom: 5px;\r\n"
                + "        }\r\n"
                + "        .plot {\r\n"
                + "            font-style: italic;\r\n"
                + "            margin-top: 20px;\r\n"
                + "        }\r\n"
                + "        .clear-button {\r\n"
                + "            text-align: center;\r\n"
                + "        }\r\n"
                + "        .clear-button button {\r\n"
                + "            padding: 10px 20px;\r\n"
                + "            background-color: #4CAF50;\r\n"
                + "            color: white;\r\n"
                + "            border: none;\r\n"
                + "            border-radius: 5px;\r\n"
                + "            cursor: pointer;\r\n"
                + "            transition: background-color 0.3s;\r\n"
                + "        }\r\n"
                + "        .clear-button button:hover {\r\n"
                + "            background-color: #45a049;\r\n"
                + "        }\r\n"
                + "    </style>\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "    <div class=\"container\">\r\n"
                + "        <div class=\"movie-details\">\r\n"
                + "            <img src=\"" + response.get("Poster").getAsString() + "\" alt=\"Movie Poster\" class=\"poster\">\r\n"
                + "            <div class=\"details\">\r\n"
                + "                <h2 class=\"title\">" + response.get("Title").getAsString() + "</h2>\r\n"
                + "                <div class=\"info\">\r\n"
                + "                    <span>Released: " + response.get("Released") + "</span>\r\n"
                + "                    <span>Genre: " + response.get("Genre") + "</span>\r\n"
                + "                    <span>Director: " + response.get("Director") + "</span>\r\n"
                + "                    <span>Actors: " + response.get("Actors") + "</span>\r\n"
                + "                    <span>Language: " + response.get("Language") + "</span>\r\n"
                + "                </div>\r\n"
                + "                <p class=\"plot\">" + response.get("Plot") + "</p>\r\n"
                + "            </div>\r\n"
                + "        </div>\r\n"
                + "        <div class=\"clear-button\">\r\n"
                + "            <a href=\"/\">\r\n"
                + "                <button>Limpiar</button>\r\n"
                + "            </a>\r\n"
                + "        </div>\r\n"
                + "    </div>\r\n"
                + "</body>\r\n"
                + "</html>";
        return outputLine;
    }

    /**
     * Genera y devuelve una página HTML para el formulario de búsqueda de películas.
     * @return La página HTML del formulario de búsqueda de películas.
     */
    private String obtainHtml() {
        String outputLine = """
                HTTP/1.1 200 OK\r
                Content-Type: text/html\r
                \r
                <!DOCTYPE html>\r
                <html lang="en">\r
                <head>\r
                    <meta charset="UTF-8">\r
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">\r
                    <title>Buscador de peliculas</title>\r
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                            display: flex;
                            flex-direction: column;
                            align-items: center;
                            justify-content: center;
                            height: 100vh;
                        }
                        h1 {
                            font-size: 2rem;
                            margin-bottom: 20px;
                        }
                        form {
                            display: flex;
                            flex-direction: column;
                            align-items: center;
                        }
                        label, input[type="text"], input[type="button"] {
                            margin-bottom: 10px;
                        }
                        input[type="text"] {
                            padding: 8px;
                            border: 1px solid #ccc;
                            border-radius: 5px;
                        }
                        input[type="button"] {
                            padding: 10px 20px;
                            background-color: #4CAF50;
                            color: white;
                            border: none;
                            border-radius: 5px;
                            cursor: pointer;
                            transition: background-color 0.3s;
                        }
                        input[type="button"]:hover {
                            background-color: #45a049;
                        }
                        #getrespmsg {
                            margin-top: 20px;
                            font-weight: bold;
                        }
                    </style>
                </head>
                <body>
                    <h1>Buscador de peliculas</h1>
                    <form action="/movie">
                        <label for="name">Nombre de la pelicula:</label>
                        <input type="text" id="name" name="name" placeholder="Escribe aqui">
                        <input type="button" value="Buscar" onclick="loadGetMsg()">
                    </form>
                    <div id="getrespmsg"></div>
                    <script>
                        function loadGetMsg() {
                            let nameVar = document.getElementById("name").value;
                            const xhttp = new XMLHttpRequest();
                            xhttp.onload = function() {
                                document.getElementById("getrespmsg").innerHTML = this.responseText;
                            }
                            xhttp.open("GET", "/movie?name="+nameVar);
                            xhttp.send();
                        }
                    </script>
                </body>
                </html>""";
        return outputLine;
    }
}
