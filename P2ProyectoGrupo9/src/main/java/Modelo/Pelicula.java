package Modelo;

import java.util.Objects;

/** Película con información básica y validación */
public class Pelicula {

    private String titulo;
    private String director;
    private int anio;
    private String genero;
    private String sinopsis;

    public Pelicula(String titulo, String director, int anio, String genero, String sinopsis) {
        setTitulo(titulo);
        setDirector(director);
        setAnio(anio);
        setGenero(genero);
        setSinopsis(sinopsis);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        this.titulo = titulo.trim();
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("El director no puede estar vacío");
        }
        this.director = director.trim();
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        final int ANIO_MIN = 1888;
        final int ANIO_MAX = java.time.Year.now().getValue() + 1;
        
        if (anio < ANIO_MIN || anio > ANIO_MAX) {
            throw new IllegalArgumentException("El año debe estar entre " + ANIO_MIN + " y " + ANIO_MAX);
        }
        this.anio = anio;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("El género no puede estar vacío");
        }
        this.genero = genero.trim();
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = (sinopsis == null) ? "" : sinopsis;
    }

    @Override
    public String toString() {
        return this.titulo + " (" + this.anio + ")";
    }

    public String toStringDetallado() {
        return String.format("Pelicula[titulo='%s', director='%s', anio=%d, genero='%s']", 
                           titulo, director, anio, genero);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return anio == pelicula.anio && Objects.equals(titulo, pelicula.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, anio);
    }
}
