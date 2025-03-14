package es.ua.biblioteca.service;

import es.ua.biblioteca.model.Book;
import es.ua.biblioteca.model.Resena;
import es.ua.biblioteca.repository.BookRepository;
import es.ua.biblioteca.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private BookRepository bookRepository;

    // Guardar una reseña
    public String agregarResena(Long bookId, int calificacion, String comentario) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            return "Error: No se encontró un libro con el ID proporcionado.";
        }
        Resena resena = new Resena(book, calificacion, comentario);
        resenaRepository.save(resena);
        return "Reseña añadida correctamente.";
    }


    public List<Resena> obtenerTodasLasResenas() {
        return resenaRepository.findAll(); // Obtener todas las reseñas
    }

    public Map<Book, Double> obtenerLibrosMejorCalificados() {
        // Obtener todas las reseñas
        List<Resena> resenas = resenaRepository.findAll();

        // Crear un mapa para almacenar la suma de calificaciones y el número de reseñas por libro
        Map<Book, Integer> sumaCalificaciones = new HashMap<>();
        Map<Book, Integer> conteoResenas = new HashMap<>();

        for (Resena resena : resenas) {
            Book libro = resena.getBook();
            int calificacion = resena.getCalificacion();

            // Sumar calificaciones
            sumaCalificaciones.put(libro, sumaCalificaciones.getOrDefault(libro, 0) + calificacion);
            // Contar reseñas
            conteoResenas.put(libro, conteoResenas.getOrDefault(libro, 0) + 1);
        }

        // Calcular el promedio de calificaciones para cada libro
        Map<Book, Double> promedios = new HashMap<>();
        for (Map.Entry<Book, Integer> entry : sumaCalificaciones.entrySet()) {
            Book libro = entry.getKey();
            int totalCalificaciones = entry.getValue();
            int totalResenas = conteoResenas.get(libro);
            double promedio = (double) totalCalificaciones / totalResenas;
            promedios.put(libro, promedio);
        }

        // Ordenar los libros por promedio de calificación (de mayor a menor)
        List<Map.Entry<Book, Double>> listaOrdenada = new ArrayList<>(promedios.entrySet());
        listaOrdenada.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue())); // Orden descendente

        // Devolver los 5 libros mejor calificados (o menos si hay menos de 5)
        int limite = Math.min(5, listaOrdenada.size());
        Map<Book, Double> resultado = new LinkedHashMap<>(); // Para mantener el orden
        for (int i = 0; i < limite; i++) {
            Map.Entry<Book, Double> entry = listaOrdenada.get(i);
            resultado.put(entry.getKey(), entry.getValue());
        }

        return resultado;
    }
}