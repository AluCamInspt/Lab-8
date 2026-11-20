package com.mycompany.laboratorio8.consultas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.mycompany.laboratorio8.datos.Ejemplar;

public class PrestamoDAO {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public static boolean registrar(Connection con, int idSocio, String isbn, int nro,LocalDate fechaPrestamo, LocalDate fechaPrevista) throws SQLException {
        if (SocioDAO.buscarPorId(con, idSocio) == null) {
            System.out.println("No existe un socio con ese ID.");
            return false;
        }
        if (LibroDAO.buscar(con, isbn) == null) {
            System.out.println("No se encontró un libro con ese ISBN.");
            return false;
        }
        boolean autoCommitOriginal = con.getAutoCommit();
        con.setAutoCommit(false);
        try {
            String estado = null;
            String sqlEstado = "SELECT estado FROM EJEMPLAR WHERE ISBN = ? AND nro_ejemplar = ? FOR UPDATE";
            try (PreparedStatement ps = con.prepareStatement(sqlEstado)) {
                ps.setString(1, isbn);
                ps.setInt(2, nro);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        estado = rs.getString("estado");
                    }
                }
            }
            if (estado == null) {
                con.rollback();
                System.out.println("No existe ese ejemplar para el libro indicado.");
                return false;
            }
            if (!Ejemplar.DISPONIBLE.equalsIgnoreCase(estado)) {
                con.rollback();
                System.out.println("El ejemplar ya se encuentra prestado.");
                return false;
            }

            String sqlInsert = "INSERT INTO PRESTAMO (id_socio, ISBN, nro_ejemplar, fecha_prestamo, "
                    + "fecha_devolucion_prevista) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                ps.setInt(1, idSocio);
                ps.setString(2, isbn);
                ps.setInt(3, nro);
                ps.setDate(4, Date.valueOf(fechaPrestamo));
                ps.setDate(5, Date.valueOf(fechaPrevista));
                ps.executeUpdate();
            }
            EjemplarDAO.actualizarEstado(con, isbn, nro, Ejemplar.PRESTADO);

            con.commit();
            return true;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(autoCommitOriginal);
        }
    }

    public static boolean registrarDevolucion(Connection con, int idPrestamo) throws SQLException {
        boolean autoCommitOriginal = con.getAutoCommit();
        con.setAutoCommit(false);
        try {
            String isbn = null;
            int nro = 0;
            boolean devuelto = false;

            String sqlBuscar = "SELECT ISBN, nro_ejemplar, fecha_devolucion_real FROM PRESTAMO "
                    + "WHERE id_prestamo = ? FOR UPDATE";
            try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
                ps.setInt(1, idPrestamo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        System.out.println("No existe un préstamo con ese ID.");
                        return false;
                    }
                    isbn = rs.getString("ISBN");
                    nro = rs.getInt("nro_ejemplar");
                    devuelto = rs.getDate("fecha_devolucion_real") != null;
                }
            }
            if (devuelto) {
                con.rollback();
                System.out.println("Ese préstamo ya fue devuelto anteriormente.");
                return false;
            }

            String sqlUpdate = "UPDATE PRESTAMO SET fecha_devolucion_real = ? WHERE id_prestamo = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setInt(2, idPrestamo);
                ps.executeUpdate();
            }
            EjemplarDAO.actualizarEstado(con, isbn, nro, Ejemplar.DISPONIBLE);

            con.commit();
            return true;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(autoCommitOriginal);
        }
    }

    public static void listar(Connection con) throws SQLException {

        String sql = "SELECT p.id_prestamo, s.nombre, l.titulo, p.nro_ejemplar, p.fecha_prestamo, "
                + "p.fecha_devolucion_prevista, p.fecha_devolucion_real "
                + "FROM PRESTAMO p "
                + "JOIN SOCIO s ON s.id_socio = p.id_socio "
                + "JOIN LIBRO l ON l.ISBN = p.ISBN "
                + "ORDER BY p.id_prestamo";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.printf("%-5s %-20s %-26s %-5s %-12s %-12s %-12s%n",
                    "ID", "SOCIO", "LIBRO", "EJ.", "F. PRÉSTAMO", "F. PREVISTA", "F. DEVOL.");
            System.out.println("-".repeat(98));
            boolean hay = false;
            while (rs.next()) {
                hay = true;
                Date real = rs.getDate("fecha_devolucion_real");
                System.out.printf("%-5d %-20s %-26s %-5d %-12s %-12s %-12s%n",
                        rs.getInt("id_prestamo"),
                        recortar(rs.getString("nombre"), 20),
                        recortar(rs.getString("titulo"), 26),
                        rs.getInt("nro_ejemplar"),
                        rs.getDate("fecha_prestamo").toLocalDate().format(FORMATO),
                        rs.getDate("fecha_devolucion_prevista").toLocalDate().format(FORMATO),
                        real == null ? "Pendiente" : real.toLocalDate().format(FORMATO));
            }
            if (!hay) {
                System.out.println("No hay préstamos registrados.");
            }
        }
    }

    private static String recortar(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
