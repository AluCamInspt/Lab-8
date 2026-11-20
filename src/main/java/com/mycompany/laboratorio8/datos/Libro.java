package com.mycompany.laboratorio8.datos;

public class Libro {

    private String isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private Integer anioPublicacion;
    
    public Libro(String isbn, String titulo, String autor, String editorial, Integer anioPublicacion) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public Integer getAnioPublicacion() { return anioPublicacion; }
}
