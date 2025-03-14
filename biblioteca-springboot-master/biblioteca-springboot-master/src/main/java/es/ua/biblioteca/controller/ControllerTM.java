package es.ua.biblioteca.controller;

import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.ua.biblioteca.model.Resena;
import es.ua.biblioteca.service.ResenaService;
import es.ua.biblioteca.service.WikidataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.ua.biblioteca.model.Book;
import es.ua.biblioteca.service.IBookService;

@Controller
public class ControllerTM {

	@Autowired
	private WikidataService wikidataService;
	@Autowired
    private IBookService bookService;

	@Autowired
	private ResenaService resenaService;
	
	@RequestMapping("/books")
	public String libros(Model modelo) {
		
		List<Book> books = bookService.findAll();

		modelo.addAttribute("books", books);
		return "biblioteca";
	}
	
	@RequestMapping("/")
	public String Inicio(Model modelo) {

		modelo.addAttribute("mensaje", "¡Hola!, bienvenido a la biblioteca en Spring Boot, donde puedes acceder al listado de obras almacenadas, realizar un CRUD completo de la misma, realizar búsqueda, generar un PDF personalizado con el contenido de la biblioteca y acceder a un servicio de búsqueda adicional que consulta a un repositorio externo (Wikidata). En la parte de abajo puedes seleccionar la opción que prefieras para poder acceder a ese servicio.");
		return "index";
	}
	
	@RequestMapping("/createBook")
	public String createBook(Model model) {
		model.addAttribute("book", new Book());
	    return "form";
	}
	
	@RequestMapping("/searchBook")
	public String searchBook(@RequestParam(value = "texto", required = false) String texto, Model model) {

		// añadir servicio de busqueda llamada y logica para mostrar los resultados en el formulario
		model.addAttribute("libros", bookService.search(texto));

	    return "searchForm";
	}
	
	@PostMapping("/createBook")
	public String createBook(@ModelAttribute Book book, Model model) {
		String result = bookService.create(book);
	    model.addAttribute("book", book);
	    model.addAttribute("result", result);
	    return "result";
	}

	// Nuevo método para mostrar el formulario de eliminación
	@RequestMapping("/deleteBook")
	public String deleteBookForm(Model model) {
		return "formDelete";
	}

	// Nuevo método para manejar la eliminación de un libro
	@PostMapping("/deleteBook")
	public String deleteBook(@RequestParam("id") long id, Model model) {
		String result = bookService.delete(id);
		model.addAttribute("result", result);
		return "deleteResult";
	}

	// Nuevo método para mostrar el formulario de edición
	@RequestMapping("/editBook")
	public String editBookForm(Model model) {
		return "formEdit";
	}

	// Nuevo método para manejar la edición de un libro
	@PostMapping("/editBook")
	public String editBook(@RequestParam("id") long id, @RequestParam("title") String title, @RequestParam("author") String author, Model model) {
		Optional<Book> bookData = bookService.findById(id);
		if (bookData.isPresent()) {
			Book book = bookData.get();
			book.setTitle(title);
			book.setAuthor(author);
			String result = bookService.update(book);
			model.addAttribute("result", result);
		} else {
			model.addAttribute("result", "No se encontró un libro con el ID proporcionado.");
		}
		return "editResult";
	}

	@RequestMapping("/authorsbvmc")
	public String getAuthorsWikidata(Model model) {
		String jsonResponse = wikidataService.getAuthors(100);
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(jsonResponse);
			JsonNode bindings = root.path("results").path("bindings");

			List<Map<String, String>> autores = new ArrayList<>();
			for (JsonNode binding : bindings) {
				Map<String, String> autor = new HashMap<>();
				autor.put("autor", binding.path("autor").path("value").asText());
				autor.put("autorLabel", binding.path("autorLabel").path("value").asText());

				// Fecha de nacimiento
				if (binding.has("fechaNacimiento")) {
					autor.put("fechaNacimiento", binding.path("fechaNacimiento").path("value").asText());
				} else {
					autor.put("fechaNacimiento", "Desconocida");
				}

				if (binding.has("lugarNacimientoLabel")) {
					autor.put("lugarNacimiento", binding.path("lugarNacimientoLabel").path("value").asText());
				} else {
					autor.put("lugarNacimiento", "Desconocido");
				}

				autores.add(autor);
			}

			model.addAttribute("autores", autores);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Error al procesar los datos de Wikidata");
		}
		return "authorsList";
	}

	@GetMapping("/agregar")
	public String mostrarFormularioAgregarResena(Model model) {
		return "agregarResena";
	}

	// Procesar el formulario de reseña
	@PostMapping("/agregar")
	public String agregarResena(@RequestParam Long bookId, @RequestParam int calificacion, @RequestParam String comentario, Model model) {
		String resultado = resenaService.agregarResena(bookId, calificacion, comentario);
		model.addAttribute("resultado", resultado);
		return "resultadoResena";
	}

	// Mostrar reseñas
	@GetMapping("/verResenas")
	public String verTodasLasResenas(Model model) {
		List<Resena> resenas = resenaService.obtenerTodasLasResenas(); // Obtener todas las reseñas
		model.addAttribute("resenas", resenas); // Pasar las reseñas a la vista
		return "verResenas";
	}

	@GetMapping("/recomendaciones")
	public String mostrarRecomendaciones(Model model) {
		Map<Book, Double> librosConPuntuacion = resenaService.obtenerLibrosMejorCalificados();
		model.addAttribute("librosConPuntuacion", librosConPuntuacion);
		return "recomendaciones";
	}

}
