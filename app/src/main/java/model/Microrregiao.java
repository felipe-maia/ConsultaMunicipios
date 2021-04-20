package model;

import java.util.List;

class Microrregiao {

    private String id;
    private String nome;
    private List<Mesorregiao> mesorregiao;

    public Microrregiao() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Mesorregiao> getMesorregiao() {
        return mesorregiao;
    }

    public void setMesorregiao(List<Mesorregiao> mesorregiao) {
        this.mesorregiao = mesorregiao;
    }
}
