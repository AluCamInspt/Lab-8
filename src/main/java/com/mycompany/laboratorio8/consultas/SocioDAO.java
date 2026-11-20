package com.mycompany.laboratorio8.consultas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mycompany.laboratorio8.datos.Socio;

public class SocioDAO {

    public static int insertar(Connection con, Socio s) throws SQLException {
        if (buscarPorDni(con, s.getDni()) != null) {
            System.out.println("Ya existe un socio con ese DNI.");
            return -1;
        }
        String sql = "INSERT INTO SOCIO (nombre, DNI, telefono, fecha_alta) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDni());
            ps.setString(3, s.getTelefono());
            ps.setDate(4, Date.valueOf(s.getFechaAlta()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    public static void listar(Connection con) throws SQLException {
        String sql = "SELECT id_socio, nombre, DNI, telefono FROM SOCIO ORDER BY id_socio";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.printf("%-5s %-25s %-12s %-15s%n", "ID", "NOMBRE", "DNI", "TELÉFONO");
            System.out.println("-".repeat(60));
            boolean hay = false;
            while (rs.next()) {
                hay = true;
                String tel = rs.getString("telefono");
                System.out.printf("%-5d %-25s %-12s %-15s%n",
                        rs.getInt("id_socio"), rs.getString("nombre"), rs.getString("DNI"), tel == null ? "" : tel);
            }
            if (!hay) {
                System.out.println("No hay socios registrados.");
            }
        }
    }

    public static Socio buscarPorDni(Connection con, String dni) throws SQLException {
        return buscar(con, "SELECT id_socio, nombre, DNI, telefono, fecha_alta FROM SOCIO WHERE DNI = ?", dni);
    }

    public static Socio buscarPorId(Connection con, int id) throws SQLException {
        return buscar(con, "SELECT id_socio, nombre, DNI, telefono, fecha_alta FROM SOCIO WHERE id_socio = ?", id);
    }

    private static Socio buscar(Connection con, String sql, Object parametro) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Socio(rs.getInt("id_socio"), rs.getString("nombre"), rs.getString("DNI"),
                            rs.getString("telefono"), rs.getDate("fecha_alta").toLocalDate());
                }
            }
        }
        return null;
    }

    public static void actualizar(Connection con, Socio s) throws SQLException {
        String sql = "UPDATE SOCIO SET nombre = ?, telefono = ? WHERE id_socio = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getTelefono());
            ps.setInt(3, s.getIdSocio());
            ps.executeUpdate();
        }
    }

    public static int contarPrestamos(Connection con, int idSocio) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PRESTAMO WHERE id_socio = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSocio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public static void eliminar(Connection con, int idSocio) throws SQLException {
        String sql = "DELETE FROM SOCIO WHERE id_socio = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSocio);
            ps.executeUpdate();
        }
    }
}
