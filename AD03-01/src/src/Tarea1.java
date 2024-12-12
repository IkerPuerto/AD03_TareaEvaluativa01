package src;
import java.sql.*;

public class Tarea1 {
	public static void main(String[] args) {
		String baseDatos = "dbeventos";
		String host = "localhost";
		String port = "3306";
		String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + baseDatos;
		String user = "prueba";
		String pwd = "Iker94clave";
		
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		
		try {
			c = DriverManager.getConnection(urlConnection, user, pwd);
			s = c.createStatement();
			rs = s.executeQuery("SELECT e.nombre_evento, COUNT(ae.dni), u.nombre, u.direccion FROM eventos e "
								+ "INNER JOIN ubicaciones u ON u.id_ubicacion = e.id_ubicacion "
								+ "INNER JOIN asistentes_eventos ae ON ae.id_evento = e.id_evento "
								+ "GROUP BY e.nombre_evento, u.nombre, u.direccion");
			
			System.out.println(String.format("%-30s | %-10s | %-35s | %-25s", "Evento", "Asistentes", "Ubicación", "Dirección"));
			System.out.println("--------------------------------------------------------------------------------------------------------------");
			while(rs.next()) {
				System.out.println(String.format("%-30s | %-10s | %-35s | %-25s", rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			try {
				if (c != null) c.close();
				if (s != null) s.close();
				if (rs != null) rs.close();
			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
	}
}
