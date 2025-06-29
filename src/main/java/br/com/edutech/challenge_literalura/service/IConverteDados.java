package br.com.edutech.challenge_literalura.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
