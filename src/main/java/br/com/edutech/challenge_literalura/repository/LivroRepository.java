package br.com.edutech.challenge_literalura.repository;

import br.com.edutech.challenge_literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findAll();

    @Query("SELECT l FROM Livro l WHERE l.idioma = :livroBusca")
    List<Livro> buscaLivrosPorIdiomas(String livroBusca);

    List<Livro> findTop5ByOrderByNumeroDownloadDesc();

    @Query("SELECT l FROM Livro l WHERE LOWER(l.titulo) = LOWER(:titulo)")
    Livro findByTituloCaseInsensitive(@Param("titulo") String titulo);
}
