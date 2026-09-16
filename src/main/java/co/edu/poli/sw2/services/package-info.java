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
 *     <li>Patron Bridge: desacopla la jerarquia de modalidades de control
 *     ({@link co.edu.poli.sw2.services.ControlDrone}, el Implementor, con sus
 *     implementaciones concretas {@link co.edu.poli.sw2.services.ControlBasico}
 *     y {@link co.edu.poli.sw2.services.ControlAutonomo}) de la Abstraccion que
 *     las usa ({@link co.edu.poli.sw2.services.ControlVuelo}), la cual asocia en
 *     tiempo de ejecucion cualquier tipo de control con cualquier dron
 *     administrado en el CRUD ({@link co.edu.poli.sw2.model.Drone},
 *     {@link co.edu.poli.sw2.model.Agricultura}, {@link co.edu.poli.sw2.model.Vigilancia}),
 *     sin que esa asociacion se persista ni sea responsabilidad de
 *     {@link co.edu.poli.sw2.model.Drone}.</li>
 *     <li>Patron Adapter: permite exportar una
 *     {@link co.edu.poli.sw2.model.Mision} a un archivo JSON reutilizando un
 *     servicio que solo sabe escribir texto.
 *     {@link co.edu.poli.sw2.services.ExportadorMision} es la interfaz con el
 *     cliente (Target), la unica que conoce el controlador;
 *     {@link co.edu.poli.sw2.services.ArchivoJson} es el servicio adaptado
 *     (Adaptee), con una interfaz incompatible a proposito porque recibe un
 *     String y no sabe nada del modelo; y
 *     {@link co.edu.poli.sw2.services.MisionJsonAdapter} es el adaptador, que
 *     implementa la interfaz del cliente mientras envuelve el servicio:
 *     convierte la mision y sus drones en un String con formato JSON y se lo
 *     entrega al servicio, que es quien crea el archivo.</li>
 * </ul>
 */
package co.edu.poli.sw2.services;
