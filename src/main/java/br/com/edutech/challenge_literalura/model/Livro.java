package br.com.edutech.challenge_literalura.model;

import br.com.edutech.challenge_literalura.dto.LivroDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String titulo;
    private String idioma;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;
    @Column(name = "numero_download")
    private Double numeroDownload;

    public Livro(){
    }

    public Livro(LivroDTO livroDTO, Autor autor) {
        this.titulo = livroDTO.titulo();
        this.autor = autor;
        this.idioma = livroDTO.idiomas().get(0);
        this.numeroDownload = livroDTO.numeroDownload();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Double getNumeroDownload() {
        return numeroDownload;
    }

    public void setNumeroDownload(Double numeroDownload) {
        this.numeroDownload = numeroDownload;
    }

    @Override
    public String toString() {
        return
                "********* LIVRO *********" +
                        "\nTitulo: " + titulo +
                        "\nIdioma: " + idioma +
                        "\nAutor: " + autor.getNome() +
                        "\nNúmero de download: " + numeroDownload +
                        "\n************************* ";
    }
}
