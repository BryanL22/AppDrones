/**
 * Contiene el contrato generico de persistencia {@link co.edu.poli.sw2.dao.CRUD}
 * y sus implementaciones (por ejemplo, {@link co.edu.poli.sw2.dao.DroneDAO}) que
 * usan JDBC, a traves de {@link co.edu.poli.sw2.database.Conexion}, para
 * almacenar y recuperar las entidades del paquete {@link co.edu.poli.sw2.model}.
 *
 * <p>Cada implementacion de {@link co.edu.poli.sw2.dao.CRUD} es responsable de
 * definir la sentencia {@code CREATE TABLE IF NOT EXISTS} propia de su entidad
 * e invocar {@link co.edu.poli.sw2.dao.CRUD#crearTabla(java.sql.Connection, String)}
 * antes de operar sobre la tabla correspondiente.</p>
 */
package co.edu.poli.sw2.dao;
