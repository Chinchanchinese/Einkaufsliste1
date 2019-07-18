package de.rg.einkaufsliste;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;
import android.widget.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Zutaten.*;



public class ZutatenActivity extends AppCompatActivity {

    private ArrayList<Zutat> Zutaten;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
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
        buildStandradlayout();


    }

    private void buildStandradlayout(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(ZutatenActivity.this, ZutatenEingabeActivity.class);
                startActivity(intent);*/
                Intent intent = new Intent(getApplicationContext(), ZutatenEingabeActivity.class);
                startActivityForResult(intent, INPUT_ACTIVITY_RESULT);
            }
        });
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.ListeZutaten);
        recyclerView.setHasFixedSize(true);
        layoutmanager = new LinearLayoutManager(this);
        adapter = new RecyclerViewAdapter(Zutaten);

        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
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
                insertZutat(data.getStringExtra("Zutatenname"));
                Toast.makeText(this,"Zutat hinzugefügt",Toast.LENGTH_LONG).show();
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

}
