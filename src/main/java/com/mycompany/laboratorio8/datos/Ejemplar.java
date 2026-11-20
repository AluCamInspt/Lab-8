package com.mycompany.laboratorio8.datos;

public class Ejemplar {

    public static final String DISPONIBLE = "Disponible";
    public static final String PRESTADO = "Prestado";

    private String isbn;
    private int nroEjemplar;
    private String estado;

    public Ejemplar(String isbn, int nroEjemplar, String estado) {
        this.isbn = isbn;
        this.nroEjemplar = nroEjemplar;
        this.estado = estado;
    }

    public String getIsbn() { return isbn; }
    public int getNroEjemplar() { return nroEjemplar; }
    public String getEstado() { return estado; }
}
