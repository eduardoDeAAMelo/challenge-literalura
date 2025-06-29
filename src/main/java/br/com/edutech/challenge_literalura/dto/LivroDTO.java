package br.com.edutech.challenge_literalura.dto;


import br.com.edutech.challenge_literalura.model.Livro;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LivroDTO(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<AutorDTO> autor,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Double numeroDownload
        ) {
}
