package com.mycompany.laboratorio8.menus;

import java.sql.Connection;
import java.sql.SQLException;

import com.mycompany.laboratorio8.consultas.LibroDAO;
import com.mycompany.laboratorio8.datos.Libro;
import com.mycompany.laboratorio8.entrada.Entrada;

public class MenuLibro {

    public static void ejecutar(Connection con) {
        int op;
        do {
            System.out.println();
            System.out.println("--- GESTIÓN DE LIBROS ---");
            System.out.println("1. Agregar libro");
            System.out.println("2. Listar libros");
            System.out.println("3. Buscar libro");
            System.out.println("4. Modificar libro");
            System.out.println("5. Eliminar libro");
            System.out.println("0. Volver");
            op = Entrada.leerOpcion("Seleccione una opción: ");
            try {
                switch (op) {
                    case 1 -> agregarLibro(con);
                    case 2 -> LibroDAO.listar(con);
                    case 3 -> buscarLibro(con);
                    case 4 -> modificarLibro(con);
                    case 5 -> eliminarLibro(con);
                    case 0 -> { }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        } while (op != 0);
    }


    private static void agregarLibro(Connection con) throws SQLException {
        String isbn = Entrada.leerISBN("ISBN: ");
        String titulo = Entrada.leerTexto("Título: ", true);
        String autor = Entrada.leerNombre("Autor: ", true);
        String editorial = Entrada.leerTexto("Editorial (ENTER para omitir): ", false);
        Integer anio = Entrada.leerAnioOpcional("Año de publicación (ENTER para omitir): ");

        Libro libro = new Libro(isbn, titulo, autor, editorial.isEmpty() ? null : editorial, anio);

        if (LibroDAO.insertar(con, libro)) {
            System.out.println("Libro agregado correctamente.");
        }
    }

    private static void buscarLibro(Connection con) throws SQLException {
        String isbn = Entrada.leerISBN("ISBN: ");
        Libro l = LibroDAO.buscar(con, isbn);
        if (l == null) {
            System.out.println("No se encontró un libro con ese ISBN.");
            return;
        }
        mostrarLibro(l);
    }

    private static void modificarLibro(Connection con) throws SQLException {
        String isbn = Entrada.leerISBN("ISBN del libro a modificar: ");
        Libro actual = LibroDAO.buscar(con, isbn);
        if (actual == null) {
            System.out.println("No se encontró un libro con ese ISBN.");
            return;
        }
        mostrarLibro(actual);
        System.out.println("(ENTER mantiene el valor actual. El ISBN no se puede modificar.)");

        String titulo = Entrada.leerTexto("Nuevo título [" + actual.getTitulo() + "]: ", false);
        String autor = Entrada.leerTexto("Nuevo autor [" + actual.getAutor() + "]: ", false);
        String editorial = Entrada.leerTexto("Nueva editorial [" + Entrada.valor(actual.getEditorial()) + "]: ", false);
        Integer anio = Entrada.leerAnioOpcional("Nuevo año [" + Entrada.valor(actual.getAnioPublicacion()) + "]: ");

        Libro nuevo = new Libro(isbn,
                titulo.isEmpty() ? actual.getTitulo() : titulo,
                autor.isEmpty() ? actual.getAutor() : autor,
                editorial.isEmpty() ? actual.getEditorial() : editorial,
                anio == null ? actual.getAnioPublicacion() : anio);

        LibroDAO.actualizar(con, nuevo);
        System.out.println("Libro modificado correctamente.");
    }

    private static void eliminarLibro(Connection con) throws SQLException {
        String isbn = Entrada.leerISBN("ISBN del libro a eliminar: ");
        Libro l = LibroDAO.buscar(con, isbn);
        if (l == null) {
            System.out.println("No se encontró un libro con ese ISBN.");
            return;
        }
        int prestamos = LibroDAO.contarPrestamos(con, isbn);
        if (prestamos > 0) {
            System.out.println("No se puede eliminar \"" + l.getTitulo() + "\": sus ejemplares tienen "
                    + prestamos + " préstamo(s) registrado(s).");
            return;
        }
        int ejemplares = LibroDAO.contarEjemplares(con, isbn);
        if (ejemplares > 0) {
            System.out.println("Atención: también se eliminarán sus " + ejemplares + " ejemplar(es).");
        }
        if (Entrada.confirmar("¿Está seguro de eliminar el libro \"" + l.getTitulo() + "\"? (S/N): ")) {
            LibroDAO.eliminar(con, isbn);
            System.out.println("Libro eliminado correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private static void mostrarLibro(Libro l) {
        System.out.println("ISBN:      " + l.getIsbn());
        System.out.println("Título:    " + l.getTitulo());
        System.out.println("Autor:     " + l.getAutor());
        System.out.println("Editorial: " + Entrada.valor(l.getEditorial()));
        System.out.println("Año:       " + Entrada.valor(l.getAnioPublicacion()));
    }
}
