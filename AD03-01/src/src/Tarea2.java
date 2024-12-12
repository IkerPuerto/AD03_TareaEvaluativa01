package src;
import java.sql.*;
import java.util.Scanner;

public class Tarea2 {
	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		String baseDatos = "dbeventos";
		String host = "localhost";
		String port = "3306";
		String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + baseDatos;
		String user = "prueba";
		String pwd = "Iker94clave";
		
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		
		System.out.print("Introduce el nombre de la ubicacion: ");
		String nombreUbicacion = teclado.nextLine();
		try {
			c = DriverManager.getConnection(urlConnection, user, pwd);
			s = c.createStatement();
			rs = s.executeQuery("SELECT id_ubicacion, nombre, capacidad FROM ubicaciones WHERE nombre = '"+ nombreUbicacion +"';");
			
			if (rs.next()) {	
				int id = rs.getInt(1);
				System.out.println("La capacidad actual de "+ rs.getString(2) + " es: " + rs.getInt(3));
				System.out.print("Introduce la nueva capacidad maxima: ");
				int nuevaCapacidad = teclado.nextInt();
				
				try {
					int i = s.executeUpdate("UPDATE ubicaciones SET capacidad = "+ nuevaCapacidad +" WHERE id_ubicacion = "+ id +";");
					if(i != 0) {
						System.out.println("Capacidad actualizada correctamente.");
					}
				}catch (Error e) {
					System.out.println("Error: " + e);
				}
			} else {
				System.out.println("La ubicacion no existe");
			}
			
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			try {
				if (c != null) c.close();
				if (s != null) s.close();
				if (rs != null) rs.close();
				teclado.close();
			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
	}
}
