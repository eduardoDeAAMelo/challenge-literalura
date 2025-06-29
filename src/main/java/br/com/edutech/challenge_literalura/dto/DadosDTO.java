package br.com.edutech.challenge_literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosDTO(
        @JsonAlias("results")List<LivroDTO> resultados
        ) {
}
