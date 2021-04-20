package model;

import java.util.List;

class Mesorregiao {

    private String id;
    private String nome;
    private List<UF> UF;

    public Mesorregiao() {
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

    public List<model.UF> getUF() {
        return UF;
    }

    public void setUF(List<model.UF> UF) {
        this.UF = UF;
    }
}
