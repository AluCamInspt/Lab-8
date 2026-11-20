package com.mycompany.laboratorio8.menus;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import com.mycompany.laboratorio8.consultas.PrestamoDAO;
import com.mycompany.laboratorio8.entrada.Entrada;

public class MenuPrestamo {

    public static void ejecutar(Connection con) {
        int op;
        do {
            System.out.println();
            System.out.println("--- GESTIÓN DE PRÉSTAMOS ---");
            System.out.println("1. Registrar préstamo");
            System.out.println("2. Registrar devolución");
            System.out.println("3. Listar préstamos");
            System.out.println("0. Volver");
            op = Entrada.leerOpcion("Seleccione una opción: ");
            try {
                switch (op) {
                    case 1 -> registrarPrestamo(con);
                    case 2 -> registrarDevolucion(con);
                    case 3 -> PrestamoDAO.listar(con);
                    case 0 -> { }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        } while (op != 0);
    }


    private static void registrarPrestamo(Connection con) throws SQLException {
        int idSocio = Entrada.leerEntero("ID del socio: ");
        String isbn = Entrada.leerISBN("ISBN: ");
        int nro = Entrada.leerEnteroPositivo("Número de ejemplar: ");
        LocalDate fechaPrestamo = Entrada.leerFecha("Fecha del préstamo (aaaa-mm-dd, ENTER = hoy): ", true);
        LocalDate fechaPrevista = Entrada.leerFecha("Fecha prevista de devolución (aaaa-mm-dd): ", false);

        if (fechaPrevista.isBefore(fechaPrestamo)) {
            System.out.println("La fecha prevista de devolución no puede ser anterior a la del préstamo.");
            return;
        }

        if (PrestamoDAO.registrar(con, idSocio, isbn, nro, fechaPrestamo, fechaPrevista)) {
            System.out.println("Préstamo registrado correctamente. El ejemplar quedó como Prestado.");
        }
    }

    private static void registrarDevolucion(Connection con) throws SQLException {
        int id = Entrada.leerEntero("ID del préstamo: ");
        if (PrestamoDAO.registrarDevolucion(con, id)) {
            System.out.println("Devolución registrada correctamente (" + LocalDate.now()
                    + "). El ejemplar quedó como Disponible.");
        }
    }
}
