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
 *     <li>Patron Bridge: desacopla las modalidades de control
 *     ({@link co.edu.poli.sw2.services.ControlDrone}, con sus implementaciones
 *     concretas {@link co.edu.poli.sw2.services.ControlBasico} y
 *     {@link co.edu.poli.sw2.services.ControlAutonomo}) de los drones administrados
 *     en el CRUD ({@link co.edu.poli.sw2.model.Drone}, {@link co.edu.poli.sw2.model.Agricultura},
 *     {@link co.edu.poli.sw2.model.Vigilancia}). El control se relaciona con el
 *     dron mediante una dependencia en tiempo de ejecucion (recibiendo el objeto
 *     {@link co.edu.poli.sw2.model.Drone} como parametro en
 *     {@link co.edu.poli.sw2.services.ControlDrone#ejecutarAccion(co.edu.poli.sw2.model.Drone)}),
 *     sin persistencia ni acoplamiento estructural en el modelo.</li>
 * </ul>
 */
package co.edu.poli.sw2.services;
