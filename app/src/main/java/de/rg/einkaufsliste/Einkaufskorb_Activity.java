package de.rg.einkaufsliste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Einkaufskorb.Adapter_Einkaufskorb;
import Zutaten.Zutat;
import Zutaten.ZutatenActivity;

public class Einkaufskorb_Activity extends AppCompatActivity {

    private ArrayList<Zutat> ZutatenGesamt;
    private ArrayList<Zutat> Zutatentemp;
    private RecyclerView recyclerView;
    private Adapter_Einkaufskorb adapter;
    private RecyclerView.LayoutManager layoutmanager;
    private int INPUT_ACTIVITY_RESULT;
    private String Daten;
    private ArrayList<ZutatenActivity.Gericht> Gerichte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einkaufskorb);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ZutatenGesamt = new ArrayList<>();
        Bundle extra = getIntent().getBundleExtra("extra");
        Gerichte= (ArrayList<ZutatenActivity.Gericht>) extra.getSerializable("object");
        createEinkaufsliste(Gerichte);
        buildRecyclerView();

    }

    private void createEinkaufsliste(ArrayList<ZutatenActivity.Gericht> mGerichte) {
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
                removeZutat(position);
            }
        });
    }

    public void insertZutat(String position){
        ZutatenGesamt.add(ZutatenGesamt.size(),new Zutat("Neu",position));
        speichern();
        adapter.notifyItemInserted(ZutatenGesamt.size());
    }
    public void removeZutat(int position){

        ZutatenGesamt.remove(position);
        speichern();
        adapter.notifyItemRemoved(position);
    }
    private void speichern() {
        SharedPreferences sharedPreferences= getSharedPreferences(Daten,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ZutatenGesamt);
        editor.putString("Zutatenliste",json);
        editor.apply();
    }

    private ArrayList<Zutat> loadData(String data) {
        Zutatentemp = new ArrayList<>();
        SharedPreferences sharedPreferences= getSharedPreferences(data,0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Zutatenliste", null);
        Type type = new TypeToken<ArrayList<Zutat>>() {}.getType();
        Zutatentemp = gson.fromJson(json, type);
        //Zutat zutat=Zutaten.get(0);

        return Zutatentemp;

    }

}
