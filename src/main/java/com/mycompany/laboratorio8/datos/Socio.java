package com.mycompany.laboratorio8.datos;

import java.time.LocalDate;

public class Socio {

    private int idSocio;
    private String nombre;
    private String dni; 
    private String telefono;
    private LocalDate fechaAlta;

    public Socio(int idSocio, String nombre, String dni, String telefono, LocalDate fechaAlta) {
        this.idSocio = idSocio;
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.fechaAlta = fechaAlta;
    }

    public int getIdSocio() { return idSocio; }
    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public LocalDate getFechaAlta() { return fechaAlta; }
}
