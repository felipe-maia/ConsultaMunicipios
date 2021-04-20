package model;

import com.google.gson.JsonObject;

import java.util.List;

public class Municipios {

    private String id;
    private String nome;
    private JsonObject microrregiao;

    public Municipios() {
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

    public JsonObject getMicrorregiao() {
        return microrregiao;
    }

    public void setMicrorregiao(JsonObject microrregiao) {
        this.microrregiao = microrregiao;
    }
}
