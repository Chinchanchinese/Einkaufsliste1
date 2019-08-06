package Gericht_bearbeiten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Gerichte.Gericht;
import Zutaten.Zutat;
import ZutatenEingabe.ZutatenEingabeActivity;
import de.rg.einkaufsliste.R;
import de.rg.einkaufsliste.*;


public class GerichtBearbeitenActivity extends AppCompatActivity {

    //Empfangene Daten
    private ArrayList<Gerichte.Gericht> Gerichte;
    private int localposition;
    private  String Datensatz;
    private String DatenGericht;

    private ArrayList<Zutat> Zutaten;
    private RecyclerView recyclerView;
    private Adapter_Zutatenliste_bearbeiten adapter;
    private RecyclerView.LayoutManager layoutmanager;
    private int INPUT_ACTIVITY_RESULT;
    private String Daten;
    private Gericht gericht;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gericht_bearbeiten);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Daten=getIntent().getStringExtra("Daten");

        recieveData();
        loadData(Datensatz);
        buildRecyclerView();
        buildStandardlayout();


    }
    private void recieveData() {
        Gerichte = new ArrayList<>();
        Bundle extra = getIntent().getBundleExtra("extra");
        Gerichte = (ArrayList<Gerichte.Gericht>) extra.getSerializable("object");
        localposition = getIntent().getIntExtra("position", 0);
        Gerichte.Gericht gericht = Gerichte.get(localposition);
        Datensatz = gericht.getDatensatz();
    }

    private void buildStandardlayout(){
        final TextView Zutatenname= findViewById(R.id.editTextZutat2);
        Button Hinzufügen= findViewById(R.id.buttonHinzufügen2);
        Hinzufügen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertZutat(Zutatenname.getText().toString());
                Zutatenname.setText("");
                Toast.makeText(getApplicationContext(),"Zutat hinzugefügt",Toast.LENGTH_LONG).show();
            }
        });

        final TextView Nameändern= findViewById(R.id.editTextNameändern);
        Button Namespeichern= findViewById(R.id.buttonNameändern);
        Namespeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gerichte.Gericht gericht = Gerichte.get(localposition);
                gericht.setName(Nameändern.getText().toString());
                Nameändern.setText("");
                Toast.makeText(getApplicationContext(),"Name geändert",Toast.LENGTH_LONG).show();
                speichernGericht();
            }
        });

        Button Gerichtlöschen= findViewById(R.id.buttonGerichtlöschen);
        Gerichtlöschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeGericht(localposition);
                finish();
            }
        });
    }

    public void removeGericht(int position){

        Gerichte.remove(position);
        speichernGericht();
        adapter.notifyItemRemoved(position);
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.ListeZutaten);
        recyclerView.setHasFixedSize(true);
        layoutmanager = new LinearLayoutManager(this);
        adapter = new Adapter_Zutatenliste_bearbeiten(Zutaten);

        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter_Zutatenliste_bearbeiten.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                removeZutat(position);
            }
        });
    }
    private void speichernGericht() {
        SharedPreferences sharedPreferences= getSharedPreferences(DatenGericht,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Gerichte);
        editor.putString("Gerichteliste",json);
        editor.apply();
    }

    private void speichern() {
        SharedPreferences sharedPreferences= getSharedPreferences(Datensatz,0);
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
                /*insertZutat(data.getStringExtra("Zutatenname"));
                Toast.makeText(this,"Zutat hinzugefügt",Toast.LENGTH_LONG).show();*/
                            }
        }
    }

    public void insertZutat(String name){
        Zutaten.add(Zutaten.size(),new Zutat(name));
        speichern();
        adapter.notifyItemInserted(Zutaten.size());
    }

    public void removeZutat(int position){

        Zutaten.remove(position);
        speichern();
        adapter.notifyItemRemoved(position);
    }
}
