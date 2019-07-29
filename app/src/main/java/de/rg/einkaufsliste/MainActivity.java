package de.rg.einkaufsliste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Handler;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;

import android.widget.*;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.*;

import Gerichte.Adapter_Gerichte;
import Gerichte.Gericht;
import GerichteEingabe.GerichteEingabeActivity;
import Zutaten.Zutat;
import Zutaten.ZutatenActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private ArrayList<Gericht> Gerichte;
    private ArrayList<Zutat> Zutaten;
    private ImageView Bild;
    private TextView Name;
    private Button buttonZutaten;
    private String DatenGericht;
    private String Daten;
    private int i=0;

    private RecyclerView recyclerView;
    private Adapter_Gerichte adapterGerichte;
    private RecyclerView.LayoutManager layoutmanager;
    private int INPUT_ACTIVITY_RESULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData(DatenGericht);


        buildRecyclerView();
        buildStandardlayout();


    }


    private void buildStandardlayout(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GerichteEingabeActivity.class);
                startActivityForResult(intent, INPUT_ACTIVITY_RESULT);
            }
        });
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Einkaufskorb_Activity.class);
                Bundle extra = new Bundle();
                extra.putSerializable("object",Gerichte);
                intent.putExtra("extra", extra);
                startActivityForResult(intent, INPUT_ACTIVITY_RESULT);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.ListeGerichte);
        recyclerView.setHasFixedSize(true);
        layoutmanager = new GridLayoutManager(this,3);
        adapterGerichte = new Adapter_Gerichte(this,Gerichte);

        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(adapterGerichte);

        adapterGerichte.setOnItemClickListener(new Adapter_Gerichte.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                i++;
                Handler handler=new Handler();
                Runnable runn = new Runnable() {
                    @Override
                    public void run() {
                        if(i==1) {
                            Hakensetzen(position);
                        }
                        i=0;
                    }
                };
                if (i==1) {
                    handler.postDelayed(runn,400);
                }
                else if(i==2){
                    Intent intent2 = new Intent(getApplicationContext(), ZutatenActivity.class);
                    Bundle extra = new Bundle();
                    extra.putSerializable("object",Gerichte);
                    intent2.putExtra("extra", extra);
                    intent2.putExtra("position", position);
                    startActivityForResult(intent2, INPUT_ACTIVITY_RESULT);
                }
            }

            @Override
            public void onDeleteClick(int position) {
                removeGericht(position);
            }

            @Override
            public void onZutatenClick(int position) {
            }
        });
    }

    private void Hakensetzen(int position) {
        Gericht gericht = Gerichte.get(position);
        if (gericht.getHaken() == false) {
            gericht.setHaken(true);
        } else {
            gericht.setHaken(false);
        }
        adapterGerichte.notifyItemChanged(position);
        speichern();
    }

    public void onResume() {
        // fetch updated data
        super.onResume();
        loadData(DatenGericht);
        adapterGerichte.updateList(Gerichte);
        adapterGerichte.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void speichern() {
        SharedPreferences sharedPreferences= getSharedPreferences(DatenGericht,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Gerichte);
        editor.putString("Gerichteliste",json);
        editor.apply();
    }

    private void loadData(String data) {
        //this.getSharedPreferences(data, 0).edit().clear().commit();
        SharedPreferences sharedPreferences= getSharedPreferences(data,0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Gerichteliste", null);
        Type type = new TypeToken<ArrayList<Gericht>>() {}.getType();
        Gerichte = gson.fromJson(json, type);

        if(Gerichte==null){
            Gerichte = new ArrayList<>();
            Gerichte.add(new Gericht("Morning Glory", R.drawable.morning_glory, new ArrayList<Zutat>(), "Data1"));
            Gerichte.add(new Gericht("Bier", R.drawable.bier,new ArrayList<Zutat>(),"Data2"));
            Gerichte.add(new Gericht("Schoppe", R.drawable.schoppe,new ArrayList<Zutat>(),"Data3"));
            Gerichte.add(new Gericht("Wrap", R.drawable.wrap,new ArrayList<Zutat>(),"Data4"));
            Gerichte.add(new Gericht("Croissant", R.drawable.croissant,new ArrayList<Zutat>(),"Data5"));
            Gerichte.add(new Gericht("Eklig", R.drawable.eklig,new ArrayList<Zutat>(),"Data6"));
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INPUT_ACTIVITY_RESULT) {
            if (resultCode == RESULT_OK) {
                GerichtHinzufügen(data);
            }
        }
    }
    public void GerichtHinzufügen(Intent data){
        String name= data.getStringExtra("Gericht_Name");
        Bitmap bildbm = data.getParcelableExtra("Gericht_Bild");
        String Bildstring= getStringFromBitmap(bildbm);
        Gericht gericht=new Gericht(name,Bildstring,new ArrayList<Zutat>(),name);
        Gerichte.add (gericht);
        speichern();
        adapterGerichte.notifyItemInserted(Gerichte.size());
        Toast.makeText(this,"Gericht hinzugefügt",Toast.LENGTH_LONG).show();
    }
    public void removeGericht(int position){

        Gerichte.remove(position);
        speichern();
        adapterGerichte.notifyItemRemoved(position);
    }
    private String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
}
