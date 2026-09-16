package co.edu.poli.sw2.services;

import java.util.ArrayList;
import java.util.List;

/**
 * Elemento compuesto (Composite) del patron Composite.
 *
 * <p>Agrupa varios {@link Component}, que pueden ser sensores individuales
 * ({@link WrapperSensor}) u otros grupos, de modo que el arbol puede anidarse a
 * cualquier profundidad. No conoce las clases concretas de sus hijos: al pedirle
 * la lectura, delega en cada uno y une los resultados.</p>
 */
public class Composite implements Component {

    List<Component> hijos = new ArrayList<>();

    /**
     * Agrega un elemento al grupo.
     *
     * @param component sensor individual u otro grupo.
     */
    public void add(Component component) {
        hijos.add(component);
    }

    /**
     * Quita un elemento del grupo.
     *
     * @param component elemento a quitar.
     */
    public void remove(Component component) {
        hijos.remove(component);
    }

    /**
     * Delega la lectura en cada hijo y devuelve todas las lecturas juntas,
     * una por linea.
     *
     * @return texto con la lectura de todos los elementos del grupo.
     */
    @Override
    public String leer() {
        List<String> lecturas = new ArrayList<>();
        for (Component hijo : hijos) {
            lecturas.add(hijo.leer());
        }

        return String.join("\n", lecturas);
    }
}
