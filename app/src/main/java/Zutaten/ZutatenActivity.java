package Zutaten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Gericht_bearbeiten.GerichtBearbeitenActivity;
import Gerichte.Gericht;
import de.rg.einkaufsliste.R;


public class ZutatenActivity extends AppCompatActivity {

    //Empfangene Daten
    private ArrayList<Gericht> Gerichte;
    private int localposition;
    private  String Datensatz;
    private Gericht gericht;

    private ArrayList<Zutat> Zutaten;
    private RecyclerView recyclerView;
    private Adapter_Zutatenliste adapter;
    private RecyclerView.LayoutManager layoutmanager;
    private int INPUT_ACTIVITY_RESULT;

    private TextView Rezept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zutaten);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recieveData();
        loadData(Datensatz);
        buildRecyclerView();
        buildStandardlayout();




    }

    private void recieveData() {
        Gerichte = new ArrayList<>();
        Bundle extra = getIntent().getBundleExtra("extra");
        Gerichte = (ArrayList<Gericht>) extra.getSerializable("object");
        localposition = getIntent().getIntExtra("position", 0);
        gericht = Gerichte.get(localposition);
        Datensatz = gericht.getDatensatz();
    }

    private void buildStandardlayout(){
        // Gericht bearbeiten öffnen
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), GerichtBearbeitenActivity.class);
                Bundle extra = new Bundle();
                extra.putSerializable("object",Gerichte);
                intent2.putExtra("extra", extra);
                intent2.putExtra("position", localposition);
                finish();
                startActivityForResult(intent2, INPUT_ACTIVITY_RESULT);
            }
        });

        //Rezept
        Rezept = findViewById(R.id.textViewRezept);
        Rezept.setText(gericht.getRezept());

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
            public void onDeleteClick(int position){
            }
        });
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
            }
        }
    }
}
