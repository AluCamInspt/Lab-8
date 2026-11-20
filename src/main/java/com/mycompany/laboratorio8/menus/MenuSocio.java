package com.mycompany.laboratorio8.menus;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import com.mycompany.laboratorio8.consultas.SocioDAO;
import com.mycompany.laboratorio8.datos.Socio;
import com.mycompany.laboratorio8.entrada.Entrada;

public class MenuSocio {

    public static void ejecutar(Connection con) {
        int op;
        do {
            System.out.println();
            System.out.println("--- GESTIÓN DE SOCIOS ---");
            System.out.println("1. Agregar socio");
            System.out.println("2. Listar socios");
            System.out.println("3. Buscar socio (por DNI)");
            System.out.println("4. Modificar socio");
            System.out.println("5. Eliminar socio");
            System.out.println("0. Volver");
            op = Entrada.leerOpcion("Seleccione una opción: ");
            try {
                switch (op) {
                    case 1 -> agregarSocio(con);
                    case 2 -> SocioDAO.listar(con);
                    case 3 -> buscarSocio(con);
                    case 4 -> modificarSocio(con);
                    case 5 -> eliminarSocio(con);
                    case 0 -> { }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        } while (op != 0);
    }


    private static void agregarSocio(Connection con) throws SQLException {
        String nombre = Entrada.leerNombre("Nombre: ", true);
        String dni = Entrada.leerDni("DNI: ");   // valida que sean solo números
        String telefono = Entrada.leerTexto("Teléfono (ENTER para omitir): ", false);
        LocalDate alta = Entrada.leerFecha("Fecha de alta (aaaa-mm-dd, ENTER = hoy): ", true);

        Socio socio = new Socio(0, nombre, dni, telefono.isEmpty() ? null : telefono, alta);

        int id = SocioDAO.insertar(con, socio);
        if (id >= 0) {
            System.out.println("Socio agregado correctamente con ID " + id + ".");
        }
    }

    private static void buscarSocio(Connection con) throws SQLException {
        String dni = Entrada.leerDni("DNI: ");
        Socio s = SocioDAO.buscarPorDni(con, dni);
        if (s == null) {
            System.out.println("No se encontró un socio con ese DNI.");
            return;
        }
        mostrarSocio(s);
    }

    private static void modificarSocio(Connection con) throws SQLException {
        String dni = Entrada.leerDni("DNI del socio a modificar: ");
        Socio actual = SocioDAO.buscarPorDni(con, dni);
        if (actual == null) {
            System.out.println("No se encontró un socio con ese DNI.");
            return;
        }
        mostrarSocio(actual);
        System.out.println("(ENTER mantiene el valor actual. El DNI y el ID no se pueden modificar.)");

        String nombre = Entrada.leerTexto("Nuevo nombre [" + actual.getNombre() + "]: ", false);
        String telefono = Entrada.leerTexto("Nuevo teléfono [" + Entrada.valor(actual.getTelefono()) + "]: ", false);

        Socio nuevo = new Socio(actual.getIdSocio(),
                nombre.isEmpty() ? actual.getNombre() : nombre,
                actual.getDni(),
                telefono.isEmpty() ? actual.getTelefono() : telefono,
                actual.getFechaAlta());

        SocioDAO.actualizar(con, nuevo);
        System.out.println("Socio modificado correctamente.");
    }

    private static void eliminarSocio(Connection con) throws SQLException {
        int id = Entrada.leerEntero("ID del socio a eliminar: ");
        Socio s = SocioDAO.buscarPorId(con, id);
        if (s == null) {
            System.out.println("No existe un socio con ese ID.");
            return;
        }
        int prestamos = SocioDAO.contarPrestamos(con, id);
        if (prestamos > 0) {
            System.out.println("No se puede eliminar a " + s.getNombre() + ": tiene " + prestamos
                    + " préstamo(s) asociado(s).");
            return;
        }
        if (Entrada.confirmar("¿Está seguro de eliminar al socio \"" + s.getNombre() + "\"? (S/N): ")) {
            SocioDAO.eliminar(con, id);
            System.out.println("Socio eliminado correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private static void mostrarSocio(Socio s) {
        System.out.println("ID:         " + s.getIdSocio());
        System.out.println("Nombre:     " + s.getNombre());
        System.out.println("DNI:        " + s.getDni());
        System.out.println("Teléfono:   " + Entrada.valor(s.getTelefono()));
        System.out.println("Fecha alta: " + s.getFechaAlta());
    }
}
