package edu.escuelaing.arem.ASE.app;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.JsonObject;

/*
 * Documentación de la clase Cache
 *
 * La clase Cache implementa un mecanismo de almacenamiento en caché para objetos JsonObject
 * asociados con claves de tipo String. Utiliza un ConcurrentHashMap para garantizar la
 * concurrencia segura en entornos multiproceso.
 */
public class Cache {
    private static final Cache instance = new Cache();
    private final Map<String, JsonObject> data;

    /**
     * Constructor privado de la clase Cache.
     * Se inicializa una instancia única de Cache y se utiliza un ConcurrentHashMap
     * para almacenar los datos de forma segura en entornos multiproceso.
     */
    private Cache() {
        this.data = new ConcurrentHashMap<>();
    }

    /**
     * Método estático para obtener la instancia única de Cache.
     * @return La instancia única de Cache.
     */
    public static Cache getInstance() {
        return instance;
    }

    /**
     * Agrega un objeto JsonObject al caché asociado con la clave especificada.
     * Si la clave ya está presente en el caché, no se realiza ninguna acción.
     * @param movie La clave asociada al objeto JsonObject.
     * @param json El objeto JsonObject a almacenar en el caché.
     */
    public void add(String movie, JsonObject json) {
        data.putIfAbsent(movie, json);
    }

    /**
     * Obtiene el objeto JsonObject asociado con la clave especificada del caché.
     * @param movie La clave del objeto JsonObject que se va a recuperar.
     * @return El objeto JsonObject asociado con la clave especificada, o null si no existe.
     */
    public JsonObject get(String movie) {
        return data.get(movie);
    }

    /**
     * Verifica si el caché contiene una entrada asociada con la clave especificada.
     * @param movie La clave cuya presencia en el caché se va a comprobar.
     * @return true si el caché contiene una entrada para la clave especificada, false de lo contrario.
     */
    public boolean contains(String movie) {
        return data.containsKey(movie);
    }
}
