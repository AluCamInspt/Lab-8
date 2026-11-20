package com.mycompany.laboratorio8.menus;

import java.sql.Connection;
import java.sql.SQLException;

import com.mycompany.laboratorio8.consultas.EjemplarDAO;
import com.mycompany.laboratorio8.consultas.LibroDAO;
import com.mycompany.laboratorio8.datos.Ejemplar;
import com.mycompany.laboratorio8.datos.Libro;
import com.mycompany.laboratorio8.entrada.Entrada;

public class MenuEjemplar {
    public static void ejecutar(Connection con) {
        int op;
        do {
            System.out.println();
            System.out.println("--- GESTIÓN DE EJEMPLARES ---");
            System.out.println("1. Agregar ejemplar");
            System.out.println("2. Listar ejemplares de un libro");
            System.out.println("0. Volver");
            op = Entrada.leerOpcion("Seleccione una opción: ");
            try {
                switch (op) {
                    case 1 ->
                        agregarEjemplar(con);
                    case 2 ->
                        listarEjemplares(con);
                    case 0 -> {}
                    default ->
                        System.out.println("Opción inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static void agregarEjemplar(Connection con) throws SQLException {
        String isbn = Entrada.leerISBN("ISBN del libro: ");
        Libro l = LibroDAO.buscar(con, isbn);
        if (l == null) {
            System.out.println("No se encontró un libro con ese ISBN.");
        } else {
            int nro = Entrada.leerEnteroPositivo("Número de ejemplar: ");
            if (EjemplarDAO.buscar(con, isbn, nro) != null) {
                System.out.println("Ya existe el ejemplar " + nro + " de \"" + l.getTitulo() + "\".");
            } else {
                EjemplarDAO.insertar(con, isbn, nro);
                System.out.println("Ejemplar agregado correctamente (estado: " + Ejemplar.DISPONIBLE + ").");
            }
        }
    }

    private static void listarEjemplares(Connection con) throws SQLException {
        String isbn = Entrada.leerISBN("ISBN del libro: ");
        Libro l = LibroDAO.buscar(con, isbn);
        if (l == null) {
            System.out.println("No se encontró un libro con ese ISBN.");
        } else {
            EjemplarDAO.listarPorLibro(con, l);
        }
    }

    /*private static void modificarEstadoEjemplar(Connection con) throws SQLException {
        // Paso 1: validar libro y ejemplar.
        String isbn = Entrada.leerTexto("ISBN del libro: ", true);
        if (LibroDAO.buscar(con, isbn) == null) {
            System.out.println("No se encontró un libro con ese ISBN.");
        } else {
            int nro = Entrada.leerEnteroPositivo("Número de ejemplar: ");
            Ejemplar e = EjemplarDAO.buscar(con, isbn, nro);
            if (e == null) {
                System.out.println("No existe ese ejemplar para el libro indicado.");
            } else {
                // Paso 2: mostrar el estado actual y ofrecer los dos estados posibles.
                System.out.println("Estado actual: " + e.getEstado());
                System.out.println("1. " + Ejemplar.DISPONIBLE);
                System.out.println("2. " + Ejemplar.PRESTADO);
                int op = Entrada.leerOpcion("Nuevo estado: ");
                String nuevo;
                if (op != 1 && op != 2) {
                    System.out.println("Opción inválida.");
                } else {
                    if (op == 1) {
                        nuevo = Ejemplar.DISPONIBLE;
                    } else {
                        nuevo = Ejemplar.PRESTADO;
                    }
                    // Paso 3: no dejar "Disponible" un ejemplar que tiene un préstamo sin devolver,
                    // porque quedaría inconsistente. Para eso existe "Registrar devolución".
                    if (nuevo.equals(Ejemplar.DISPONIBLE) && EjemplarDAO.tienePrestamoAbierto(con, isbn, nro)) 
                   {
                        System.out.println("El ejemplar tiene un préstamo sin devolver. Registre la devolución desde "
                                + "\"Gestionar préstamos\".");

                    } else {
                        // Paso 4: actualizar.
                        EjemplarDAO.actualizarEstado(con, isbn, nro, nuevo);
                        System.out.println("Estado actualizado a " + nuevo + ".");
                    }
                }
            }
        }
    }
    */
}
