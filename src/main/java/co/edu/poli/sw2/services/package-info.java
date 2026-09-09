/**
 * Contiene los servicios de la aplicacion, cada uno implementando un patron
 * de diseno distinto:
 * <ul>
 *     <li>{@link co.edu.poli.sw2.services.Conexion}: Singleton que
 *     administra la unica conexion JDBC compartida hacia MySQL.</li>
 *     <li>{@link co.edu.poli.sw2.services.DroneFactory}: Factory Method que
 *     define la plantilla reutilizable para crear un
 *     {@link co.edu.poli.sw2.model.Drone}; {@link co.edu.poli.sw2.services.AgriculturaFactory}
 *     y {@link co.edu.poli.sw2.services.VigilanciaFactory} son las fabricas
 *     concretas que construyen, respectivamente, un
 *     {@link co.edu.poli.sw2.model.Agricultura} o una
 *     {@link co.edu.poli.sw2.model.Vigilancia}.</li>
 *     <li>{@link co.edu.poli.sw2.services.DronePrototype}: Prototype que
 *     obtiene una copia de un {@link co.edu.poli.sw2.model.Drone} existente
 *     delegando en {@link co.edu.poli.sw2.model.Drone#clone()}.</li>
 *     <li>{@link co.edu.poli.sw2.services.DroneBuilder}: Builder que arma un
 *     {@link co.edu.poli.sw2.model.Drone} atributo por atributo y decide, al
 *     construirlo, si corresponde una {@link co.edu.poli.sw2.model.Agricultura},
 *     una {@link co.edu.poli.sw2.model.Vigilancia} o un dron sin
 *     especializacion.</li>
 *     <li>Patron Bridge: desacopla la jerarquia de controles
 *     ({@link co.edu.poli.sw2.services.ControlDrone}, con sus abstracciones refinadas
 *     {@link co.edu.poli.sw2.services.ControlBasico} y {@link co.edu.poli.sw2.services.ControlAutonomo})
 *     de la jerarquia de drones administrados en el CRUD
 *     ({@link co.edu.poli.sw2.model.Drone}, {@link co.edu.poli.sw2.model.Agricultura},
 *     {@link co.edu.poli.sw2.model.Vigilancia}), permitiendo asociar e intercambiar
 *     cualquier tipo de control sobre cualquier dron del sistema.</li>
 * </ul>
 */
package co.edu.poli.sw2.services;
