package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONObject;

import java.util.List;

import model.Estado;
import model.Malha;
import model.Municipios;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ServiceEstadoMunicipio {

    @GET("{idEstado}")
    Call<List<Estado>> buscaEstado(@Path("idEstado") String idEstado);

    @GET("{idEstado}/municipios")
    Call<List<Municipios>> buscaMunicipios(@Path("idEstado") String idEstado);

    @GET("{id}?formato=application/json")
    Call<JsonObject> buscaMalha(@Path("id") String id);

}
