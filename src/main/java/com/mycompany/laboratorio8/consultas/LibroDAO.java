package com.mycompany.laboratorio8.consultas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.mycompany.laboratorio8.datos.Libro;

public class LibroDAO {

    public static boolean insertar(Connection con, Libro l) throws SQLException {
        if (buscar(con, l.getIsbn()) != null) {
            System.out.println("Ya existe un libro con ese ISBN.");
            return false;
        }
        String sql = "INSERT INTO LIBRO (ISBN, titulo, autor, editorial, anio_publicacion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, l.getIsbn());
            ps.setString(2, l.getTitulo());
            ps.setString(3, l.getAutor());
            ps.setString(4, l.getEditorial());
            if (l.getAnioPublicacion() == null) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, l.getAnioPublicacion());
            }
            ps.executeUpdate();
        }
        return true;
    }

    public static void listar(Connection con) throws SQLException {
        String sql = "SELECT ISBN, titulo, autor FROM LIBRO ORDER BY titulo";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-15s %-30s %-30s%n", "ISBN", "TÍTULO", "AUTOR");
            System.out.println("-".repeat(77));

            boolean hay = false;
            while (rs.next()) {
                hay = true;
                System.out.printf("%-15s %-30s %-30s%n",
                        rs.getString("ISBN"), recortar(rs.getString("titulo"), 30), recortar(rs.getString("autor"), 30));
            }
            if (!hay) {
                System.out.println("No hay libros registrados.");
            }
        }
    }

    public static Libro buscar(Connection con, String isbn) throws SQLException {
        String sql = "SELECT ISBN, titulo, autor, editorial, anio_publicacion FROM LIBRO WHERE ISBN = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int anio = rs.getInt("anio_publicacion");
                    Integer anioObj = rs.wasNull() ? null : anio;
                    return new Libro(rs.getString("ISBN"), rs.getString("titulo"), rs.getString("autor"),
                            rs.getString("editorial"), anioObj);
                }
            }
        }
        return null;
    }

    public static void actualizar(Connection con, Libro l) throws SQLException {
        String sql = "UPDATE LIBRO SET titulo = ?, autor = ?, editorial = ?, anio_publicacion = ? WHERE ISBN = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getEditorial());
            if (l.getAnioPublicacion() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, l.getAnioPublicacion());
            }
            ps.setString(5, l.getIsbn());
            ps.executeUpdate();
        }
    }

    public static int contarPrestamos(Connection con, String isbn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PRESTAMO WHERE ISBN = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public static int contarEjemplares(Connection con, String isbn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM EJEMPLAR WHERE ISBN = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public static void eliminar(Connection con, String isbn) throws SQLException {
        String sql = "DELETE FROM LIBRO WHERE ISBN = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.executeUpdate();
        }
    }

    private static String recortar(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
