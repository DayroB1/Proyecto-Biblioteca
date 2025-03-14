package es.ua.biblioteca.service;

import java.io.ByteArrayOutputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.springframework.stereotype.Service;
import org.apache.jena.rdf.model.ModelFactory ;

@Service
public class WikidataService {

	public static String sparqlRepository = "https://query.wikidata.org/sparql";

	public String getAuthors(int num) {

		String resultado = "";

		String queryString =
				"PREFIX wdt: <http://www.wikidata.org/prop/direct/> "
						+ "PREFIX wd: <http://www.wikidata.org/entity/> "
						+ "PREFIX wikibase: <http://wikiba.se/ontology#> "
						+ "PREFIX bd: <http://www.bigdata.com/rdf#> "
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
						+ "SELECT * WHERE { "
						+ "   SERVICE <https://query.wikidata.org/sparql> { "
						+ "      SELECT DISTINCT ?autor ?autorLabel ?fechaNacimiento ?lugarNacimiento ?lugarNacimientoLabel "
						+ "      WHERE { "
						+ "         ?autor wdt:P2799 ?idbvmc."  // Relacionado con la Biblioteca Virtual Miguel de Cervantes
						+ "         ?autor wdt:P21 wd:Q6581072."  // Género femenino
						+ "         ?autor wdt:P106 wd:Q36180."  // Ocupación: escritor
						+ "         OPTIONAL { ?autor wdt:P569 ?fechaNacimiento. } "
						+ "         OPTIONAL { ?autor wdt:P19 ?lugarNacimiento. "
						+ "                    ?lugarNacimiento rdfs:label ?lugarNacimientoLabel FILTER(LANG(?lugarNacimientoLabel) = 'es'). } "
						+ "         SERVICE wikibase:label { bd:serviceParam wikibase:language \"es\". } "
						+ "      } LIMIT " + num
						+ "   }"
						+ " }";

		Query query = QueryFactory.create(queryString) ;
		try (QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
			ResultSet rs = qexec.execSelect() ;
			//ResultSetFormatter.out(System.out, rs, query) ;

			// write to a ByteArrayOutputStream
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			ResultSetFormatter.outputAsJSON(outputStream, rs);

			// and turn that into a String
			resultado = new String(outputStream.toByteArray());
		}

		return resultado;
	}
}