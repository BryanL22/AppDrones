package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Interfaz del patron Decorator: contrato comun para {@link DronWrapper}
 * y decoradores concretos como {@link BateriaAdicional}, permitiendo
 * encadenar caracteristicas sobre un drone.
 */
public interface DronComponent {

    /**
     * Describe el drone, incluyendo lo agregado por cada decorador.
     *
     * @return descripcion del drone decorado.
     */
    String describir();

    /**
     * Adapta un {@link Drone} a {@code DronComponent} con su descripcion
     * basica, punto de partida para encadenar decoradores.
     *
     * @param drone drone a adaptar.
     * @return componente que describe el drone recibido.
     */
    static DronComponent of(Drone drone) {
        return () -> drone.getModelo() + " (" + drone.getFabricante() + ")";
    }
}
