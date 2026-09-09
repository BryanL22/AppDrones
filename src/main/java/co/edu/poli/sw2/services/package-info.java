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
 *     <li>{@link co.edu.poli.sw2.services.DronComponent}: Decorator -
 *     interfaz que declara el contrato comun ({@code describir()}) e incluye
 *     el metodo estatico {@code of(Drone)} que adapta un
 *     {@link co.edu.poli.sw2.model.Drone} real a un componente inicial.
 *     {@link co.edu.poli.sw2.services.DronWrapper} la implementa como
 *     decorador base, envolviendo cualquier {@code DronComponent} (un drone
 *     adaptado u otro decorador); {@link co.edu.poli.sw2.services.BateriaAdicional}
 *     extiende {@code DronWrapper} como decorador concreto que agrega la
 *     caracteristica de una bateria adicional, y al recibir un
 *     {@code DronComponent} generico puede encadenarse sobre el drone base
 *     o sobre otro decorador ya aplicado (incluida otra bateria), sin
 *     necesitar una clase abstracta intermedia.</li>
 * </ul>
 */
package co.edu.poli.sw2.services;
