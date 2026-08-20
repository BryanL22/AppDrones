/**
 * Contiene el contrato generico de persistencia {@link co.edu.poli.sw2.dao.CRUD}
 * y su unica implementacion, {@link co.edu.poli.sw2.dao.DroneDAO}, que maneja
 * tanto la entidad base {@link co.edu.poli.sw2.model.Drone} como sus
 * especializaciones {@link co.edu.poli.sw2.model.Agricultura} y
 * {@link co.edu.poli.sw2.model.Vigilancia}, decidiendo con {@code instanceof}
 * que tabla adicional leer, escribir o actualizar segun el tipo real del
 * objeto recibido.
 *
<<<<<<< HEAD
 * <p>Todas usan JDBC, a traves de {@link co.edu.poli.sw2.services.Conexion},
=======
 * <p>Sigue un patron de herencia por tabla: los campos comunes se guardan en
 * {@code drone} y los propios de cada especializacion en su propia tabla
 * ({@code agricultura} / {@code vigilancia}), relacionada con {@code drone}
 * mediante {@code id_drone} (con {@code ON DELETE CASCADE}). Usa JDBC, a
 * traves del servicio Singleton {@link co.edu.poli.sw2.services.Conexion},
>>>>>>> origin/bryan
 * para almacenar y recuperar las entidades del paquete
 * {@link co.edu.poli.sw2.model}.</p>
 *
 * <p>{@link co.edu.poli.sw2.dao.CRUD#crearTabla(java.sql.Connection, String)}
 * es un metodo por defecto que cada tabla usa para asegurar su propio
 * esquema antes de operar sobre ella.</p>
 */
package co.edu.poli.sw2.dao;
