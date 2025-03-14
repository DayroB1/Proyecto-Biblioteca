package es.ua.biblioteca.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private int calificacion; // Puntuación de 1 a 5
    private String comentario; // Comentario de la reseña
    private Date fecha;

    public Resena() {
        this.fecha = new Date(); // Fecha actual por defecto
    }

    // Constructor
    public Resena(Book book, int calificacion, String comentario) {
        this.book = book;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fecha = new Date();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}