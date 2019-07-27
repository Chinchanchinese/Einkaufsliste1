package ZutatenEingabe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.rg.einkaufsliste.R;

public class ZutatenEingabeActivity extends AppCompatActivity {

    private EditText TextHinzufügen;
    private Button ButtonHinzufügen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zutaten_eingabe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextHinzufügen = findViewById(R.id.editTextZutat);
        ButtonHinzufügen = findViewById(R.id.buttonHinzufügen);
        ButtonHinzufügen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                senden();
            }
        });

    }


    public void senden() {
        String inhalt= TextHinzufügen.getText().toString();
        TextHinzufügen = findViewById(R.id.editTextZutat);
        Intent data = new Intent();
        data.putExtra("Zutatenname", inhalt);
        setResult(RESULT_OK, data);
        super.finish();
    }

}
