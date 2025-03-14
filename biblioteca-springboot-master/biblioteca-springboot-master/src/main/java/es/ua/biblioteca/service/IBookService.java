package es.ua.biblioteca.service;

import java.util.List;
import java.util.Optional;

import es.ua.biblioteca.model.Book;

public interface IBookService {

    List<Book> findAll();
    Optional<Book> findById(long id);
	String create(Book book);
	String update(Book book);
	String delete(long id);
	List<Book> search(String title);
}  