package de.rg.einkaufsliste;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GerichteEingabeActivitySave extends AppCompatActivity {


    private Button btn1;
    private ImageView iv1;
    private EditText NameGericht;
    private Intent bildintent;
    private String DatenGericht;
    private String Datensatz;
    private Bitmap bitmap;
    private Button Hinzufügen;


    File bildfile = new File(Environment.getExternalStorageDirectory() + "/FotoApp/bild.jpg");
    Uri bilduri = Uri.fromFile(bildfile);

    int Kameracode = 15;

    Bitmap bm1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerichte_eingabe);

        NameGericht = findViewById(R.id.editTextNameGericht);
        btn1 = (Button) findViewById(R.id.buttonFotoaufnehmen);
        iv1 = (ImageView) findViewById(R.id.imageViewFotoaufnehmen);
        Hinzufügen = findViewById(R.id.buttonGerichtHinzufügen);

        Hinzufügen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                senden();
            }
        });



        /*if (bildfile.exists()) {
            bm1 = BitmapFactory.decodeFile(bildfile.getAbsolutePath());
            iv1.setImageBitmap(bm1);
        }*/




        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                /*ContentValues values = new ContentValues();
                bilduri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/
                bildintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                /*File dir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/FotoApp/bild.png")));
                String pictureName= getPictureName();
                bildfile=new File (dir,pictureName);
                Uri imageuri=Uri.fromFile(bildfile);*/

                //bildintent.putExtra(MediaStore.EXTRA_OUTPUT, bildfile);
                startActivityForResult(bildintent, Kameracode);

                /*catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Kamera nicht unterstützt!", Toast.LENGTH_SHORT).show();

                }*/

            }
        });
    }

    /*private void speichern() {

        String inhalt= NameGericht.getText().toString();
        Gericht gericht=new Gericht(inhalt,bitmap,new ArrayList<Zutat>(),inhalt);
        SharedPreferences sharedPreferences= getSharedPreferences(DatenGericht,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gericht);
        editor.putString("Zutatenliste",json);
        editor.apply();
    }*/

    private String getPictureName() {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp=sdf.format(new Date());
        return "Image"+timestamp+".jpg";
    }

    public void senden() {
        String inhalt= NameGericht.getText().toString();
        NameGericht = findViewById(R.id.editTextZutat);
        Intent data = new Intent();
        data.putExtra("Gericht_Name", inhalt);
        data.putExtra("Gericht_Bild", bitmap);
        setResult(RESULT_OK, data);
        super.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if (resultCode == RESULT_OK) {

            if (requestCode == Kameracode) {

                //Toast.makeText(getApplicationContext(), "Bild gespeichert unter: " + bildfile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

                //bm1 = BitmapFactory.decodeFile(bildfile.getAbsolutePath());
                bitmap= (Bitmap)data.getExtras().get("data");
                iv1.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext(), "Bild gesetzt " , Toast.LENGTH_SHORT).show();

            }


        //}


        super.onActivityResult(requestCode, resultCode, data);
    }
}
