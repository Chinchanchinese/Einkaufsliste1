package GerichteEingabe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.rg.einkaufsliste.R;

public class GerichteEingabeActivity extends AppCompatActivity {


    private Button btn1;
    private ImageView iv1;
    private EditText NameGericht;
    private Bitmap bitmap;
    private Button Hinzufügen;

    int Kameracode = 15;
    int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    private OutputStream outputstream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerichte_eingabe);

        NameGericht = findViewById(R.id.editTextNameGericht);
        //btn1 = (Button) findViewById(R.id.buttonFotoaufnehmen);
        iv1 = (ImageView) findViewById(R.id.imageViewFotoaufnehmen);
        Hinzufügen = findViewById(R.id.buttonGerichtHinzufügen);

        Hinzufügen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                senden();
            }
        });

        /*btn1.setOnClickListener(new OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    invokeCamera();
                }
                else {
                    // let's request permission.
                    String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });*/
        iv1.setOnClickListener(new OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    invokeCamera();
                }
                else {
                    // let's request permission.
                    String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }

    private void invokeCamera() {

        //Uri pictureUri= FileProvider.getUriForFile(this,"de.rg.einkaufsliste.provider", createImageFile());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Wo soll camera speichern
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        // tell the camera to request WRITE permission.
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, 15);
    }

    private File createImageFile() {
        File pictureDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp=sdf.format(new Date());

        File imageFile= new File(pictureDir, "picture" + timestamp + ".jpg");
        return imageFile;
    }


    public void senden() {
        String inhalt= NameGericht.getText().toString();
        NameGericht = findViewById(R.id.editTextZutat);
        Intent data = new Intent();
        data.putExtra("Gericht_Name", inhalt);
        data.putExtra("Gericht_Bild",bitmap );
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // we have heard back from our request for camera and write external storage.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {
                Toast.makeText(this, "Keine Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Kameracode) {

                if (data != null) {
                    bitmap= (Bitmap)data.getExtras().get("data");

                    iv1.setImageBitmap(bitmap);

                    BitmapDrawable drawable= (BitmapDrawable) iv1.getDrawable();
                    Bitmap bitmap2 = drawable.getBitmap();

                    File filepath= Environment.getExternalStorageDirectory();
                    File dir = new File(filepath.getAbsolutePath()+"/Bilder Gerichte/");
                    dir.mkdir();
                    File file = new File(dir, System.currentTimeMillis()+".jpg");
                    try{
                        outputstream = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, outputstream);
                    Toast.makeText(getApplicationContext(), "Bild gespeichert " , Toast.LENGTH_SHORT).show();
                    try {
                        outputstream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        outputstream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}