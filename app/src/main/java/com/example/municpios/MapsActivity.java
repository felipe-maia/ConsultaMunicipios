package com.example.municpios;

import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import model.Estado;
import model.Municipios;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import service.ServiceEstadoMunicipio;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Retrofit retrofit, retrofit2;
    private GoogleMap mMap;
    private List<Municipios> municipiosList;
    private JSONObject jsonObject;
    private String json;
    private LatLng centroide;
    private PolygonOptions polygon;
    private String nomeBusca = "Estado do Paraná";
    private String idMunicipio = "";
    private String sCentroide = "";
    private Address address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        municipiosList = new ArrayList<>();

        retrofit = new Retrofit.Builder().baseUrl("https://servicodados.ibge.gov.br/api/v1/localidades/estados/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit2 = new Retrofit.Builder().baseUrl("https://servicodados.ibge.gov.br/api/v2/malhas/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void buscaMalha(final String id) {
        final ServiceEstadoMunicipio estadoService = retrofit2.create(ServiceEstadoMunicipio.class);
        Call<JsonObject> call = estadoService.buscaMalha(id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonObject = new JSONObject(response.body().toString());
                        json = jsonObject.getString("arcs");
                        sCentroide = jsonObject.getString("objects");
                        JSONObject j = new JSONObject(sCentroide);
                        String sJ = j.getString("foo");
                        j = new JSONObject(sJ);
                        sJ = j.getString("geometries");
                        sJ = sJ.substring(1);
                        j = new JSONObject(sJ);
                        sJ = j.getString("properties");
                        j = new JSONObject(sJ);
                        sJ = j.getString("centroide");
                        sCentroide = sJ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                idMunicipio = "";
                poligono();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("TESTE", "errado");
                Log.i("TESTE", t.toString());
                Log.i("TESTE", call.toString());
            }
        });
    }

    public void setCentroide() {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> enderecos = geocoder.getFromLocationName(nomeBusca, 1);
            if (enderecos.size() > 0 && enderecos != null) {
                address = enderecos.get(0);
                Log.i("TESTE", enderecos.get(0).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LatLng local = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(local)
                .title(nomeBusca)
                .snippet(address.getAddressLine(0)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centroide, 8));
        Toast.makeText(this, address.getAddressLine(0), Toast.LENGTH_LONG).show();
    }

    public void findIDMunicipio() {
        for (Municipios m : municipiosList
        ) {
            if (m.getNome().toLowerCase().equals(nomeBusca.toLowerCase())) {
                idMunicipio = m.getId();
                Log.i("TESTE", idMunicipio);
            }
        }
        if (idMunicipio.equals("")) {
            Toast.makeText(this, "Nenhum município encontrado", Toast.LENGTH_LONG).show();

        } else {
            buscaMalha(idMunicipio);
        }
    }

    public void buscaMunicipios() {
        final ServiceEstadoMunicipio estadoService = retrofit.create(ServiceEstadoMunicipio.class);
        Call<List<Municipios>> call = estadoService.buscaMunicipios("41");
        call.enqueue(new Callback<List<Municipios>>() {
            @Override
            public void onResponse(Call<List<Municipios>> call, Response<List<Municipios>> response) {
                if (response.isSuccessful()) {
                    municipiosList = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Municipios>> call, Throwable t) {
                Log.i("TESTE", "errado");
                Log.i("TESTE", t.toString());
                Log.i("TESTE", call.toString());
            }
        });
    }

    public void poligono() {

        sCentroide = sCentroide.replace("[", "");
        sCentroide = sCentroide.replace("]", "");
        String[] cent = sCentroide.split(",");
        Double c1 = Double.valueOf(cent[0]);
        Double c2 = Double.valueOf(cent[1]);
        centroide = new LatLng(c2, c1);

        polygon = new PolygonOptions();
        String coord = json.substring(2);
        coord = coord.replace("]]]", "");
        Log.i("TESTE", coord);

        String[] coord2 = coord.split("],");
        for (int i = 0; i < coord2.length - 1; i++) {

            String coord1 = coord2[i].replace("[", "");
            String[] vetCoord = coord1.split(",");

            Double d1 = Double.valueOf(vetCoord[0]);
            Double d2 = Double.valueOf(vetCoord[1]);


            LatLng latLng = new LatLng(d2, d1);
            polygon.add(latLng);
        }
        mMap.addPolygon(polygon);
        setCentroide();
    }

    public void alert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_busca);

        final EditText editNomeMunicipio = dialog.findViewById(R.id.editNomeMunicipio);
        Button btnBuscar = dialog.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeBusca = editNomeMunicipio.getText().toString();
                findIDMunicipio();
                dialog.dismiss();
            }
        });

        Button btnCancelar = dialog.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void mostraDados() {
        Toast.makeText(this, address.getLatitude() + "," + address.getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        buscaMunicipios();
        buscaMalha("41");

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                alert();
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //mMap.clear();
                mostraDados();
            }
        });

        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney and move the camera -25.4749, -49.2788
        // mMap.addMarker(new MarkerOptions().position(casa).title("Casa").snippet("Minha casa"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(casa));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pontoInicial, 5));

        /*CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(casa);
        circleOptions.radius(50);
        circleOptions.fillColor(Color.argb(25,255,0,0));
        mMap.addCircle(circleOptions);
         */
    }
}
