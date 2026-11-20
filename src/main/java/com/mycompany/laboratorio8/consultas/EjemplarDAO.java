package com.mycompany.laboratorio8.consultas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.laboratorio8.datos.Ejemplar;
import com.mycompany.laboratorio8.datos.Libro;

public class EjemplarDAO {

    public static Ejemplar buscar(Connection con, String isbn, int nro) throws SQLException {
        String sql = "SELECT ISBN, nro_ejemplar, estado FROM EJEMPLAR WHERE ISBN = ? AND nro_ejemplar = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);   // primer ?
            ps.setInt(2, nro);       // segundo ?
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Ejemplar(rs.getString("ISBN"), rs.getInt("nro_ejemplar"), rs.getString("estado"));
                }
            }
        }
        return null;
    }

    public static void insertar(Connection con, String isbn, int nro) throws SQLException {
        String sql = "INSERT INTO EJEMPLAR (ISBN, nro_ejemplar, estado) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.setInt(2, nro);
            ps.setString(3, Ejemplar.DISPONIBLE);
            ps.executeUpdate();
        }
    }

    public static void listarPorLibro(Connection con, Libro libro) throws SQLException {
        String sql = "SELECT nro_ejemplar, estado FROM EJEMPLAR WHERE ISBN = ? ORDER BY nro_ejemplar";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, libro.getIsbn());
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Libro: " + libro.getTitulo());
                System.out.println();
                System.out.printf("%-12s %-12s%n", "Ejemplar", "Estado");
                System.out.println("-".repeat(25));
                boolean hay = false;
                while (rs.next()) {
                    hay = true;
                    System.out.printf("%-12d %-12s%n", rs.getInt("nro_ejemplar"), rs.getString("estado"));
                }
                if (!hay) {
                    System.out.println("Este libro todavía no tiene ejemplares.");
                }
            }
        }
    }

    public static void actualizarEstado(Connection con, String isbn, int nro, String nuevoEstado) throws SQLException {
        String sql = "UPDATE EJEMPLAR SET estado = ? WHERE ISBN = ? AND nro_ejemplar = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, isbn);
            ps.setInt(3, nro);
            ps.executeUpdate();
        }
    }

    public static boolean tienePrestamoAbierto(Connection con, String isbn, int nro) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PRESTAMO WHERE ISBN = ? AND nro_ejemplar = ? AND fecha_devolucion_real IS NULL";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.setInt(2, nro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
