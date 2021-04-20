package model;

import com.google.gson.JsonObject;

public class Malha {
    private String type;
    private JsonObject objects;

    public Malha() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonObject getObjects() {
        return objects;
    }

    public void setObjects(JsonObject objects) {
        this.objects = objects;
    }
}
