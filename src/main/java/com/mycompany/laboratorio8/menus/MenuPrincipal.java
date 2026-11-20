package com.mycompany.laboratorio8.menus;

import java.sql.Connection;

import com.mycompany.laboratorio8.entrada.Entrada;

public class MenuPrincipal {

    public static void ejecutar(Connection con) {
        int op;
        do {
            System.out.println();
            System.out.println("====================================");
            System.out.println("       SISTEMA DE BIBLIOTECA");
            System.out.println("====================================");
            System.out.println();
            System.out.println("1. Gestionar libros");
            System.out.println("2. Gestionar socios");
            System.out.println("3. Gestionar ejemplares");
            System.out.println("4. Gestionar préstamos");
            System.out.println("0. Salir");
            op = Entrada.leerOpcion("Seleccione una opción: ");
            switch (op) {
                case 1 -> MenuLibro.ejecutar(con);
                case 2 -> MenuSocio.ejecutar(con);
                case 3 -> MenuEjemplar.ejecutar(con);
                case 4 -> MenuPrestamo.ejecutar(con);
                case 0 -> { }
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }
}
