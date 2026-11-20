package com.mycompany.laboratorio8;

import java.sql.Connection;
import java.sql.SQLException;

import com.mycompany.laboratorio8.bd.Conexion;
import com.mycompany.laboratorio8.menus.MenuPrincipal;


public class Main {

    public static void main(String[] args) {
        //codigo para probar singleton
        /*try (Connection con = Conexion.conectar(); Connection con2 = Conexion.conectar()) {

            System.out.println("¿Es el mismo objeto? " + (con == con2));   // false

            // Para comprobarlo en la base: cada conexión tiene su propio número en el servidor
            for (Connection c : new Connection[]{con, con2}) {
                try (PreparedStatement ps = c.prepareStatement("SELECT CONNECTION_ID()"); ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    System.out.println("ID de conexión en el servidor: " + rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo conectar a la base de datos: " + e.getMessage());
        }*/
        try (Connection con = Conexion.conectar()) {
            System.out.println("Conexión exitosa a la base de datos Biblioteca_DB.");
            MenuPrincipal.ejecutar(con);   // se pasa la misma conexión a todos los menús
            con.close();
        } catch (SQLException e) {
            System.out.println("No se pudo conectar a la base de datos: " + e.getMessage());
        }
        System.out.println("Cerrando sistema..");
    }
}
