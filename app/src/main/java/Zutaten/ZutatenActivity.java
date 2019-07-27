package Zutaten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.*;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Gericht_bearbeiten.GerichtBearbeitenActivity;
import de.rg.einkaufsliste.R;
import ZutatenEingabe.ZutatenEingabeActivity;


public class ZutatenActivity extends AppCompatActivity {

    private ArrayList<Zutat> Zutaten;
    private RecyclerView recyclerView;
    private Adapter_Zutatenliste adapter;
    private RecyclerView.LayoutManager layoutmanager;
    private int INPUT_ACTIVITY_RESULT;
    private String Daten;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zutaten);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Daten=getIntent().getStringExtra("Gericht_Zutatenliste");

        loadData(Daten);
        buildRecyclerView();
        buildStandardlayout();


    }

    private void buildStandardlayout(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GerichtBearbeitenActivity.class);
                intent.putExtra("Daten", Daten);
                startActivityForResult(intent, INPUT_ACTIVITY_RESULT);
            }
        });
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.ListeZutaten);
        recyclerView.setHasFixedSize(true);
        layoutmanager = new LinearLayoutManager(this);
        adapter = new Adapter_Zutatenliste(Zutaten);

        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter_Zutatenliste.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                removeZutat(position);
            }
        });
    }

    private void speichern() {
        SharedPreferences sharedPreferences= getSharedPreferences(Daten,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Zutaten);
        editor.putString("Zutatenliste",json);
        editor.apply();
    }

    private void loadData(String data) {
        SharedPreferences sharedPreferences= getSharedPreferences(data,0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Zutatenliste", null);
        Type type = new TypeToken<ArrayList<Zutat>>() {}.getType();
        Zutaten = gson.fromJson(json, type);

        if(Zutaten==null){
            Zutaten = new ArrayList<>();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INPUT_ACTIVITY_RESULT) {
            if (resultCode == RESULT_OK) {
                String Daten =data.getStringExtra("Zutatenname");
                if (Daten != null) {
                    insertZutat(data.getStringExtra("Zutatenname"));
                    Toast.makeText(this, "Zutat hinzugefügt", Toast.LENGTH_LONG).show();
                }
                else {
                    /*String Daten2 =data.getStringExtra("Positionlist");
                    int daten3= Integer.parseInt(Daten2);*/
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void insertZutat(String position){
        Zutaten.add(Zutaten.size(),new Zutat("Neu",position));
        speichern();
        adapter.notifyItemInserted(Zutaten.size());
    }

    public void removeZutat(int position){

        Zutaten.remove(position);
        speichern();
        adapter.notifyItemRemoved(position);
    }

    public static class Gericht implements Serializable {

        private String id;
        private String name;
        private int Foto =1;
        private List<Zutat> Zutatenliste;
        private String Datensatz;
        private Bitmap Fotobm;
        private String bilduri;
        private Boolean Haken=false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getFoto() {
                return Foto;

        }

        public void setFoto(int foto) {
            Foto = foto;
        }

        public Bitmap getFotobm() {
            return Fotobm;
        }

        public void setFotobm(Bitmap fotobm) {
            Fotobm = fotobm;
        }

        public String getDatensatz() {
            return Datensatz;
        }

        public void setDatensatz(String datensatz) {
            Datensatz = datensatz;
        }

        public List<Zutat> getZutatenliste() {
            return Zutatenliste;
        }

        public void setZutatenliste(List<Zutat> zutatenliste) {
            Zutatenliste = zutatenliste;
        }

        public String getBilduri() {
            return bilduri;
        }

        public void setBilduri(String bilduri) {
            this.bilduri = bilduri;
        }

        public Boolean getHaken() {
            return Haken;
        }

        public void setHaken(Boolean haken) {
            Haken = haken;
        }

        public Gericht(String name, int bm, List<Zutat> zutatenliste, String Datensatz) {
            this.id = id;
            this.name = name;
            Foto = bm;
            this.Zutatenliste=zutatenliste;
            this.Datensatz=Datensatz;
        }

        public Gericht( String name, String Bilduri, List<Zutat> zutatenliste,String Datensatz) {
            this.id = id;
            this.name = name;
            bilduri = Bilduri;
            this.Zutatenliste=zutatenliste;
            this.Datensatz=Datensatz;
        }


        public Gericht() {
        }
    }
}
