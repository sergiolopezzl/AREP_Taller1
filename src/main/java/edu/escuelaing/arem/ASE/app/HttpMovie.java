package edu.escuelaing.arem.ASE.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * Documentación de la clase HttpMovie
 *
 * La clase HttpMovie proporciona métodos para realizar solicitudes HTTP al servicio OMDB API
 * y recuperar información sobre películas utilizando el título de la película como parámetro de búsqueda.
 * Utiliza un objeto Cache para almacenar en caché las respuestas de las solicitudes HTTP para mejorar
 * el rendimiento y reducir la cantidad de solicitudes al servicio OMDB API.
 */
public class HttpMovie {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String API_KEY = "95abfe39";
    private static final String BASE_URL = "http://www.omdbapi.com/?apikey=" + API_KEY + "&t=";

    /**
     * Realiza una solicitud HTTP al servicio OMDB API para obtener información sobre una película
     * utilizando su título como parámetro de búsqueda.
     * @param URIStr La cadena que contiene la URI con el título de la película.
     * @return Un objeto JsonObject que contiene la información de la película obtenida de la API OMDB.
     * @throws IOException Si ocurre un error durante la conexión o la lectura de la respuesta HTTP.
     */
    public JsonObject get(String URIStr) throws IOException {
        String movieTitle = extractMovieTitle(URIStr);

        if (Cache.getInstance().contains(movieTitle)) {
            System.out.println("The movie is in the cache");
            return Cache.getInstance().get(movieTitle);
        }

        String apiUrl = BASE_URL + movieTitle;
        System.out.println("API URL: " + apiUrl);

        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Response: " + response.toString());
                Cache.getInstance().add(movieTitle, JsonParser.parseString(response.toString()).getAsJsonObject());
            } else {
                System.out.println("GET request failed");
            }
        } catch (IOException e) {
            System.err.println("Error making HTTP request: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        System.out.println("GET DONE");
        return Cache.getInstance().get(movieTitle);
    }

    /**
     * Extrae el título de la película de la URI proporcionada.
     * @param URIStr La cadena de URI que contiene el título de la película.
     * @return El título de la película.
     */
    private String extractMovieTitle(String URIStr) {
        return URIStr.split("=")[1];
    }
}
