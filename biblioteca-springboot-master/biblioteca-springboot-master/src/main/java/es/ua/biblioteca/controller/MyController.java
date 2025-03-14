package es.ua.biblioteca.controller;

import es.ua.biblioteca.model.Book;
import es.ua.biblioteca.service.IBookService;
import es.ua.biblioteca.service.WikidataService;
import es.ua.biblioteca.util.GeneratePdfReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
@RestController
@RequestMapping("/api")
public class MyController {

    @Autowired
    private IBookService bookService;

    @Autowired
    private WikidataService wikidataService;

    @RequestMapping(value = "/book/{id}")
    public Book getBook(@PathVariable long id) throws Exception {

        Optional<Book> book = bookService.findById(id);

        if(!book.isPresent())
        	throw new Exception("Entity not found");

        return book.get();
    }

    @DeleteMapping(value = "/book/{id}")
    public String deleteBook(@PathVariable long id) throws Exception {
        return bookService.delete(id);
    }

    @RequestMapping("/book")
    public List<Book> getBook() {
       return bookService.findAll();
    }

    @PostMapping("/book")
    public String createBook(@RequestBody Book book) {
       return bookService.create(book);
    }

    @PutMapping("/book/{id}")
    public String updateBook(@PathVariable("id") long id, @RequestBody Book book) {
    	Optional<Book> bookData = bookService.findById(id);

        if (bookData.isPresent()) {
            Book _book = bookData.get();
            _book.setTitle(book.getTitle());
            _book.setAuthor(book.getAuthor());
            return bookService.update(_book);
        }

        return "The book is not in the database";
     }

    @RequestMapping(value = "/pdfreport", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> booksReport() {

        var books = (List<Book>) bookService.findAll();

        ByteArrayInputStream bis = GeneratePdfReport.booksReport(books);

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=booksreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
