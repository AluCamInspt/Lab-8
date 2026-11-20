package com.mycompany.laboratorio8.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String DRIVER = "org.mariadb.jdbc.Driver";
    private static final String URL = "jdbc:mariadb://localhost:3306/Biblioteca_DB";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    private static Conexion instancia;
    private Connection connection;

    private Conexion() throws SQLException {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC de MariaDB (" + DRIVER + ")", e);
        }
    }

    public static synchronized Connection conectar() throws SQLException {
        if (instancia == null || instancia.connection == null || instancia.connection.isClosed()) {
            instancia = new Conexion();
        }
        return instancia.connection;
    }
}

















