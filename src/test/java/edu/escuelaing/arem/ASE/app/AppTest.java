package edu.escuelaing.arem.ASE.app;

import com.google.gson.JsonObject;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

/*
 * Documentación de la clase AppTest
 *
 * La clase AppTest implementa pruebas unitarias para verificar el funcionamiento
 * correcto de las clases HttpMovie y Cache en la aplicación.
 */

public class AppTest {
    /**
     * Prueba unitaria para verificar la obtención de información de películas.
     * @throws IOException Si ocurre un error de entrada/salida durante la ejecución de la prueba.
     */
    @Test
    public void testGetMovie() throws IOException {
        HttpMovie httpMovie = new HttpMovie();
        JsonObject movieInfo = httpMovie.get("/movie?name=Avengers");
        assertNotNull(movieInfo);
        assertEquals("The Avengers", movieInfo.get("Title").getAsString());
    }

    /**
     * Prueba unitaria para verificar la adición y recuperación de información en la caché.
     */
    @Test
    public void testAddAndGet() {
        Cache cache = Cache.getInstance();
        JsonObject movieInfo = new JsonObject();
        movieInfo.addProperty("Title", "The Matrix");
        cache.add("The Matrix", movieInfo);
        assertTrue(cache.contains("The Matrix"));
        JsonObject cachedInfo = cache.get("The Matrix");
        assertTrue(cachedInfo.has("Title"));
    }

    /**
     * Prueba unitaria para verificar la presencia de una película en la caché.
     */
    @Test
    public void testContains() {
        Cache cache = Cache.getInstance();
        assertFalse(cache.contains("Inception"));
    }
}
