package de.rg.einkaufsliste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import Einkaufskorb.Adapter_Einkaufskorb;
import Gerichte.Gericht;
import Zutaten.Zutat;
import Zutaten.ZutatenActivity;

public class Einkaufskorb_Activity extends AppCompatActivity {

    private ArrayList<Zutat> ZutatenGesamt;
    private ArrayList<Zutat> Zutatentemp;
    private ArrayList<Zutat> Artikelliste;
    private RecyclerView recyclerView;
    private Adapter_Einkaufskorb adapter;
    private RecyclerView.LayoutManager layoutmanager;
    private int INPUT_ACTIVITY_RESULT;
    private String Daten;
    private String DatenArtikel;
    private ArrayList<Gericht> Gerichte;
    private TextView Artikelname;
    private Button hinzufügen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einkaufskorb);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ZutatenGesamt = new ArrayList<>();
        Bundle extra = getIntent().getBundleExtra("extra");
        Gerichte= (ArrayList<Gericht>) extra.getSerializable("object");
        createEinkaufsliste(Gerichte);
        buildRecyclerView();

        loadDataArtikel();
        createGesamtliste(Artikelliste);

        Artikelname = findViewById(R.id.editTextArtikel);
        hinzufügen = findViewById(R.id.buttonArtikelHinzufügen);
        hinzufügen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String artikel = Artikelname.getText().toString();
                Artikelname.setText("");
                insertArtikel(artikel);
            }
        });

    }

    private void createGesamtliste(ArrayList<Zutat> artikelliste) {
        if (artikelliste != null){
            for (int i=0; i<artikelliste.size();i++) {
             ZutatenGesamt.add(artikelliste.get(i));
            }
        }
        else{
            Artikelliste = new ArrayList<>();
        }

    }

    private void createEinkaufsliste(ArrayList<Gericht> mGerichte) {
        for (int i=0; i < mGerichte.size();i++){
            if(mGerichte.get(i).getHaken()==true) {
                String Datensatz=mGerichte.get(i).getDatensatz();
                ZutatenGesamt.addAll(loadData(Datensatz));
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INPUT_ACTIVITY_RESULT) {
            if (resultCode == RESULT_OK) {
            }
        }
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.ListeEinkaufskorb);
        recyclerView.setHasFixedSize(true);
        layoutmanager = new LinearLayoutManager(this);
        adapter = new Adapter_Einkaufskorb(ZutatenGesamt);

        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter_Einkaufskorb.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                removeArtikel(position);
            }
        });
    }

    public void insertArtikel(String position){
        ZutatenGesamt.add(ZutatenGesamt.size(),new Zutat(position));
        Artikelliste.add(new Zutat(position));
        speichernArtikel();
        adapter.notifyItemInserted(ZutatenGesamt.size());
    }
    public void removeArtikel(int position){

        Artikelliste.remove(position);
        speichernArtikel();
        adapter.notifyItemRemoved(position);
    }

    private ArrayList<Zutat> loadData(String data) {
        Zutatentemp = new ArrayList<>();
        SharedPreferences sharedPreferences= getSharedPreferences(data,0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Zutatenliste", null);
        Type type = new TypeToken<ArrayList<Zutat>>() {}.getType();
        Zutatentemp = gson.fromJson(json, type);

        return Zutatentemp;

    }
    private void speichernArtikel() {
        SharedPreferences sharedPreferences= getSharedPreferences("DatenArtikel",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Artikelliste);
        editor.putString("Artikelliste",json);
        editor.commit();
    }

    private void loadDataArtikel() {
        SharedPreferences sharedPreferences= getSharedPreferences("DatenArtikel",0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Artikelliste", null);
        Type type = new TypeToken<ArrayList<Zutat>>() {}.getType();
        Artikelliste = gson.fromJson(json, type);
    }

}
