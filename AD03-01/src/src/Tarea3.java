package src;
import java.sql.*;
import java.util.Scanner;

public class Tarea3 {
	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		String baseDatos = "dbeventos";
		String host = "localhost";
		String port = "3306";
		String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + baseDatos;
		String user = "prueba";
		String pwd = "Iker94clave";
		String nombrePersona = "";
		
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		
		//Introducir DNI
		boolean formatoCorrecto;
		String dniAsistente;
		do {
			System.out.println("Introduce el DNI del asistente: ");
			dniAsistente = teclado.nextLine();
			formatoCorrecto = dniAsistente.matches("\\d{8}[A-Za-z]");
			if(!formatoCorrecto) {
				System.out.println("El formato del DNI no es correcto.");
			}
		}while(!formatoCorrecto);
		
		//Bucar persona del DNI.
		try {
			c = DriverManager.getConnection(urlConnection, user, pwd);
			s = c.createStatement();
			rs = s.executeQuery("SELECT nombre FROM asistentes WHERE dni = '"+ dniAsistente +"'");
			if(rs.next()) {
				nombrePersona = rs.getString(1);
				System.out.println("Estas realizando la reserva para: " + nombrePersona);
			} else {
				System.out.println("No se econtro un asistente con el DNI proporcionado");
			}
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
		
		//Busca la lista de eventos
		try {
			c = DriverManager.getConnection(urlConnection, user, pwd);
			s = c.createStatement();
			rs = s.executeQuery("SELECT e.id_evento, e.nombre_evento, (u.capacidad - count(ae.dni)) FROM eventos e "
					+ "INNER JOIN asistentes_eventos ae ON e.id_evento = ae.id_evento "
					+ "INNER JOIN asistentes a ON ae.dni = a.dni "
					+ "INNER JOIN ubicaciones u ON u.id_ubicacion = e.id_ubicacion "
					+ "GROUP BY e.id_evento, e.nombre_evento;");
			System.out.println("Lista de eventos:");
			while(rs.next()) {
				System.out.println(rs.getInt(1) + ". " + rs.getString(2) + " - Espacios disponibles: " + rs.getInt(3));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		
		//Comprobar que ese DNI no está yá en el evento y el aforo no está lleno
		System.out.println("Elige el numero del avento al que quieres asistir:");
		int numeroEvento = teclado.nextInt();
		
		//Comprueba que haya aforo disponible
		try {
			c = DriverManager.getConnection(urlConnection, user, pwd);
			s = c.createStatement();
			rs = s.executeQuery("SELECT (u.capacidad - count(ae.dni)) FROM eventos e "
					+ "INNER JOIN asistentes_eventos ae ON e.id_evento = ae.id_evento "
					+ "INNER JOIN asistentes a ON ae.dni = a.dni "
					+ "INNER JOIN ubicaciones u ON u.id_ubicacion = e.id_ubicacion "
					+ "WHERE e.id_evento = "+ numeroEvento + " "
					+ "GROUP BY e.id_evento, e.nombre_evento;");
			if(rs.next()) {
				if (rs.getInt(1) == 0) {
					System.out.println("El aforo está lleno");
					return;
				}
			} 
		} catch (Exception e) {
				System.out.println("Error: "+ e);
		}
			
		//Comprueba que el DNI no esté en el evento
		try{
			boolean dniEnEvento = false;
			c = DriverManager.getConnection(urlConnection, user, pwd);
			s = c.createStatement();
			rs = s.executeQuery("SELECT a.dni FROM asistentes a "
					+ "INNER JOIN asistentes_eventos ae ON a.dni = ae.dni "
					+ "INNER JOIN eventos e ON e.id_evento = ae.id_evento "
					+ "WHERE e.id_evento = "+numeroEvento+";");
			while (rs.next()) {
				if (rs.getString(1).equals(dniAsistente)) {
					dniEnEvento = true;
					break;
				}
			} if (dniEnEvento) {
				System.out.println("En DNI ya está en el evento");
				return;
			}
		}catch(Exception e) {
			System.out.println("Exception: " + e);
		}
		
		//Introduce el DNI en el evento
		try{
			int i = s.executeUpdate("INSERT INTO asistentes_eventos VALUES ('"+dniAsistente+"', "+numeroEvento+");");
			if (i != 0) {
				System.out.println(nombrePersona + " ha sido registrado para el evento seleccionado");
			} else {
				System.out.println("No se ha podido registrar");
			}
		}catch (Exception e) {
				System.out.println("Error: " + e);
		//Cerramos las conexiones
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
