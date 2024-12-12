package src;
import java.sql.*;
import java.util.Scanner;

public class Tarea4 {

	   // URL de la base de datos


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

        try {
			c = DriverManager.getConnection(urlConnection, user, pwd);
			s = c.createStatement();
			rs = s.executeQuery("SELECT id_evento, nombre_evento FROM eventos");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + ". " + rs.getString(2));
			}
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        
        System.out.println("Introduce en ID del evento para ver la cantidad de asistentes: ");
        int idEvento = teclado.nextInt();
        
        //almacenada obtener_numero_asistentes
        try {
        	c = DriverManager.getConnection(urlConnection, user, pwd);
			s = c.createStatement();
        	CallableStatement prcProcedimientoAlmacenado = c.prepareCall("{ call obtener_numero_asistentes("+ idEvento +") }");
        	rs = prcProcedimientoAlmacenado.executeQuery();
        	if(rs.next()) {
        		System.out.println("El numero de asistentes al evento seleccionado es: "+ rs.getInt(1));
        	}
        }catch(Exception e) {
        	System.out.println("Error: " + e);
        }finally {
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
