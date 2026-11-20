package com.mycompany.laboratorio8.datos;

import java.time.LocalDate;

public class Prestamo {

    private int idPrestamo;
    private int idSocio;
    private String isbn;
    private int nroEjemplar;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;
    
    public Prestamo(int idPrestamo, int idSocio, String isbn, int nroEjemplar,
                    LocalDate fechaPrestamo, LocalDate fechaDevolucionPrevista, LocalDate fechaDevolucionReal) {
        this.idPrestamo = idPrestamo;
        this.idSocio = idSocio;
        this.isbn = isbn;
        this.nroEjemplar = nroEjemplar;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public int getIdPrestamo() { return idPrestamo; }
    public int getIdSocio() { return idSocio; }
    public String getIsbn() { return isbn; }
    public int getNroEjemplar() { return nroEjemplar; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaDevolucionPrevista() { return fechaDevolucionPrevista; }
    public LocalDate getFechaDevolucionReal() { return fechaDevolucionReal; }
}
