/**
 * Contiene el contrato generico de persistencia {@link co.edu.poli.sw2.dao.CRUD}
 * y sus implementaciones: {@link co.edu.poli.sw2.dao.DroneDAO} para la tabla
 * base {@code drone}, y {@link co.edu.poli.sw2.dao.AgriculturaDAO} /
 * {@link co.edu.poli.sw2.dao.VigilanciaDAO} para las especializaciones del
 * dron, siguiendo un patron de herencia por tabla: cada una persiste sus
 * campos propios en su propia tabla ({@code agricultura} / {@code vigilancia})
 * relacionada con {@code drone} mediante {@code id_drone}.
 *
 * <p>Todas usan JDBC, a traves de {@link co.edu.poli.sw2.database.Conexion},
 * para almacenar y recuperar las entidades del paquete
 * {@link co.edu.poli.sw2.model}.</p>
 *
 * <p>Cada implementacion de {@link co.edu.poli.sw2.dao.CRUD} es responsable de
 * definir la sentencia {@code CREATE TABLE IF NOT EXISTS} propia de su entidad
 * e invocar {@link co.edu.poli.sw2.dao.CRUD#crearTabla(java.sql.Connection, String)}
 * antes de operar sobre la tabla correspondiente.</p>
 */
package co.edu.poli.sw2.dao;
