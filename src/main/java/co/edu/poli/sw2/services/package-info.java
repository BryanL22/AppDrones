/**
 * Contiene los servicios de la aplicacion, cada uno implementando un patron
 * de diseno distinto:
 * <ul>
 *     <li>{@link co.edu.poli.sw2.services.Conexion}: Singleton que
 *     administra la unica conexion JDBC compartida hacia MySQL.</li>
 *     <li>{@link co.edu.poli.sw2.services.DroneFactory}: Factory que crea
 *     la especializacion de {@link co.edu.poli.sw2.model.Drone} que
 *     corresponda ({@link co.edu.poli.sw2.model.Agricultura} o
 *     {@link co.edu.poli.sw2.model.Vigilancia}) a partir de un tipo.</li>
 * </ul>
 */
package co.edu.poli.sw2.services;
