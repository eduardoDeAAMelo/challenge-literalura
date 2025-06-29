package br.com.edutech.challenge_literalura.principal;

import br.com.edutech.challenge_literalura.dto.AutorDTO;
import br.com.edutech.challenge_literalura.dto.DadosDTO;
import br.com.edutech.challenge_literalura.dto.LivroDTO;
import br.com.edutech.challenge_literalura.model.Autor;
import br.com.edutech.challenge_literalura.model.Livro;
import br.com.edutech.challenge_literalura.repository.AutorRepository;
import br.com.edutech.challenge_literalura.repository.LivroRepository;
import br.com.edutech.challenge_literalura.service.ConsumoAPI;
import br.com.edutech.challenge_literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    @Autowired
    private final LivroRepository livroRepository;

    @Autowired
    private final AutorRepository autorRepository;


    private static final String URL_BASE = "https://gutendex.com/books/";
    Scanner leitura = new Scanner(System.in);
    ConsumoAPI consumoAPI = new ConsumoAPI();
    ConverteDados conversor = new ConverteDados();
    private String json;

    private String menu = """
            ********************************************************
            Escolha o número de sua opção:
            1- buscar livro pelo título
            2- listar livros registrados
            3- listar autores registrados
            4- listar autores vivos em um determinado ano
            5- listar livros em um determinado idioma pela sigla: pt, en , fr...
            6- top 5 livros mais baixados
            
            0- sair""";

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu() {

        var opcaoEscolhida = -1;
        while (opcaoEscolhida != 0) {
            json = consumoAPI.obterDados(URL_BASE);
            System.out.println(menu);
            opcaoEscolhida = leitura.nextInt();
            leitura.nextLine();
            switch (opcaoEscolhida) {
                case 1 -> buscarLivroPorTitulo();
                case 2 -> listarLivrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> buscarAutoresVivosPorAno();
                case 5 -> listarLivrosPorIdioma();
                case 6 -> top5LivrosMaisBaixados();

                case 0 -> System.out.println("Encerrando aplicação...");
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        System.out.println(livros);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        System.out.println(autores);
    }

    private void buscarAutoresVivosPorAno() {
        System.out.println("Digite o ano para busca");
        var ano = leitura.nextInt();
        leitura.nextLine();
        List<Autor> autores = autorRepository.buscaAutoresVivosPorAno(ano);
        System.out.println(autores);
    }

    private void listarLivrosPorIdioma() {
        System.out.println("Digite o idioma para buscar livro");
        var livroBusca = leitura.next();
        List<Livro> livrosPorIdioma = livroRepository.buscaLivrosPorIdiomas(livroBusca.toLowerCase());
        System.out.println(livrosPorIdioma);
    }

    private void top5LivrosMaisBaixados() {
        List<Livro> livrosTop = livroRepository.findTop5ByOrderByNumeroDownloadDesc();
        System.out.println(livrosTop);
    }

    private void buscarLivroPorTitulo() {
        LivroDTO livroDTO = obterDadosLivro();
        if (livroDTO != null) {
            Livro livroExistente = livroRepository.findByTituloCaseInsensitive(livroDTO.titulo());

            if (livroExistente != null) {
                System.out.println("Livro: " + livroDTO.titulo() + " já consta no banco de dados.");
                System.out.println(livroExistente);
                return;
            }

            AutorDTO autorDTO = livroDTO.autor().get(0);
            Autor autor;
            Autor autorExistente = autorRepository.findByNome(autorDTO.nome());

            if (autorExistente != null) {
                autor = autorExistente;
            } else {
                autor = new Autor(autorDTO);
                autorRepository.save(autor);
            }
            Livro novoLivro = new Livro(livroDTO, autor);
            autor.adicionarLivro(novoLivro);
            try {
                livroRepository.save(novoLivro);
                System.out.println("Livro salvo com sucesso!");
                System.out.println(novoLivro);
            } catch (Exception e) {
                System.out.println("Livro já consta no banco de dados");
            }

        } else {
            System.out.println("Livro não encontrado!");
        }
    }

    private LivroDTO obterDadosLivro() {
        System.out.println("Digite o nome do livro");
        var nomeLivro = leitura.nextLine();
        json = consumoAPI.obterDados(URL_BASE +
                "?search=" +
                nomeLivro.replace(" ", "+")
        );
        var dadosBusca = conversor.obterDados(json, DadosDTO.class);
        Optional<LivroDTO> livroDTO = dadosBusca.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nomeLivro.toUpperCase()))
                .findFirst();
        if (livroDTO.isPresent()) {
            return livroDTO.get();
        } else {
            return null;
        }
    }
}