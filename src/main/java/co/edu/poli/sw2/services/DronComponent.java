package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Componente del patron Decorator: contrato comun que implementan tanto el
 * decorador base ({@link DronWrapper}) como los decoradores concretos que lo
 * extienden (por ejemplo, {@link BateriaAdicional}), de modo que un
 * decorador pueda envolver indistintamente un {@link Drone} recien adaptado
 * o a otro decorador ya aplicado, permitiendo encadenar varias
 * caracteristicas sobre el mismo drone.
 */
public interface DronComponent {

    /**
     * Describe el drone envuelto, incluyendo lo que haya agregado cada
     * decorador aplicado.
     *
     * @return una descripcion legible del drone decorado.
     */
    String describir();

    /**
     * Adapta un {@link Drone} existente (del paquete {@code model}, sin
     * modificarlo) a un {@code DronComponent} con su descripcion basica,
     * punto de partida para encadenar decoradores como {@link DronWrapper}
     * y {@link BateriaAdicional}.
     *
     * @param drone drone existente a adaptar.
     * @return un componente que describe el drone recibido.
     */
    static DronComponent of(Drone drone) {
        return () -> drone.getModelo() + " (" + drone.getFabricante() + ")";
    }
}
