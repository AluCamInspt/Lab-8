package com.mycompany.laboratorio8.entrada;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Entrada {

    private static final Scanner sc = new Scanner(System.in);

    public static String leerTexto(String mensaje, boolean obligatorio) {
        String s;
        do {
            System.out.print(mensaje);
            s = sc.nextLine().trim();
            if (obligatorio && s.isEmpty()) {
                System.out.println("Este dato es obligatorio.");
            }
        } while (obligatorio && s.isEmpty());

        return s;
    }

    public static int leerOpcion(String mensaje) {
        System.out.println();
        System.out.print(mensaje);
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static int leerEntero(String mensaje) {
        int numero = 0;
        boolean esValido = false;

        while (!esValido) {
            System.out.print(mensaje);
            try {
                numero = Integer.parseInt(sc.nextLine().trim());
                esValido = true;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
        return numero;
    }

    public static int leerEnteroPositivo(String mensaje) {
        int n = leerEntero(mensaje);
        while (n <= 0) {
            System.out.println("El número debe ser mayor que cero.");
            n = leerEntero(mensaje);
        }
        return n;
    }

    public static Integer leerAnioOpcional(String mensaje) {
        Integer anio = null;
        boolean esValido = false;
        int anioActual = LocalDate.now().getYear();

        while (!esValido) {
            System.out.print(mensaje);
            String s = sc.nextLine().trim();

            if (s.isEmpty()) {
                esValido = true;
            } else {
                try {
                    int valor = Integer.parseInt(s);
                    if (valor < 1 || valor > anioActual) {
                        System.out.println("Ingrese un año válido (entre 1 y " + anioActual + ").");
                    } else {
                        anio = valor;
                        esValido = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ingrese un año válido (solo números).");
                }
            }
        }
        return anio;
    }

    public static String leerDni(String mensaje) {
        boolean esDatoCorrecto;
        String dni = leerTexto(mensaje, true);
        esDatoCorrecto = dni.matches("\\d{6,10}");
        while (!esDatoCorrecto) {
            System.out.println("El DNI debe contener solo números (entre 6 y 10 dígitos).");
            dni = leerTexto(mensaje, true);
            esDatoCorrecto = dni.matches("\\d{6,10}");
        }
        return dni;
    }

    public static LocalDate leerFecha(String mensaje, boolean permiteHoy) {
        LocalDate fecha = null;
        boolean esValido = false;

        while (!esValido) {
            System.out.print(mensaje);
            String s = sc.nextLine().trim();

            if (s.isEmpty()) {
                if (permiteHoy) {
                    fecha = LocalDate.now();
                    esValido = true;
                } else {
                    System.out.println("Este dato es obligatorio.");
                }
            } else {
                try {
                    fecha = LocalDate.parse(s); //formato aaaa-mm-dd
                    esValido = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Fecha inválida. Use el formato aaaa-mm-dd (ej: 2026-05-01).");
                }
            }
        }
        return fecha;
    }


    public static boolean confirmar(String mensaje) {
        String s;
        do {
            System.out.print(mensaje);
            s = sc.nextLine().trim();
            if (!s.equalsIgnoreCase("S") && !s.equalsIgnoreCase("N")) {
                System.out.println("Responda S o N.");
            }
        } while (!s.equalsIgnoreCase("S") && !s.equalsIgnoreCase("N"));

        return s.equalsIgnoreCase("S");
    }

    //aprendizaje de uso de ternario
    /*
        String devolver;
        if(o==null)
            devolver="-";
        else
            devolver=o.toString();
        return devolver;
    */
    
    public static String valor(Object o) {
        return o == null ? "-" : o.toString();
    }

    public static String leerNombre(String mensaje, boolean obligatorio) {
        String s;
        boolean esValido;
        do {
            s = leerTexto(mensaje, obligatorio);
            esValido = s.isEmpty() || s.matches("[\\p{L} .'-]{1,100}");

            if (!esValido) {
                System.out.println("El nombre solo puede contener letras, espacios, puntos, apóstrofes y guiones.");
            }
        } while (!esValido);

        return s;
    }
    public static String leerISBN(String mensaje) {
    String s;
    boolean esValido;
    do {
        s = leerTexto(mensaje, true);
        esValido = s.matches("\\d{10,13}");
        
        if (!esValido) {
            System.out.println("El ISBN debe contener obligatoriamente entre 10 y 13 números (sin guiones ni letras).");
        }
    } while (!esValido);

    return s;
}

}
