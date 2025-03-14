package es.ua.biblioteca.repository;

import es.ua.biblioteca.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByBookId(Long bookId);
}