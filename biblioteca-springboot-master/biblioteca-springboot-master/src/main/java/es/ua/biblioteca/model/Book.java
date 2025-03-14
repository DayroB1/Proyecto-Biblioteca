package es.ua.biblioteca.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    private String author;
    
    private Date publication;

    private String link;

    public Book() {
    }
    
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Book(Long id, String title, String author) {
    	this.id = id;
        this.title = title;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Book other = (Book) obj;
        if (this.title != other.title) {
            return false;
        }
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {

        var builder = new StringBuilder();
        builder.append("Book{id=").append(id).append(", title=")
                .append(title).append(", author=")
                .append(author).append("}")
                .append(link).append("}");
        return builder.toString();
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getPublication() {
		return publication;
	}

	public void setPublication(Date publication) {
		this.publication = publication;
	}

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
}