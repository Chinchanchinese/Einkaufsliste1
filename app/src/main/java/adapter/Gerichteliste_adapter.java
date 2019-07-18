package adapter;

import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.*;

import androidx.annotation.NonNull;

import BilderGerichte.*;
import de.rg.einkaufsliste.MainActivity;
import de.rg.einkaufsliste.R;
import de.rg.einkaufsliste.ZutatenActivity;

import java.util.*;


public class Gerichteliste_adapter extends ArrayAdapter<Gericht>{
    private Context context;
    private List<Gericht> Gerichte;
    private Button button_zutaten;


    public Gerichteliste_adapter(Context context, List<Gericht> Gerichte){

        super (context, R.layout.gerichteliste_layout, Gerichte);
        this.context = context;
        this.Gerichte = Gerichte;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.gerichteliste_layout, parent, false);

        ImageView imageviewfoto = (ImageView) view.findViewById((R.id.imageView2));
        imageviewfoto.setImageResource(Gerichte.get(position).getFoto());
        TextView textviewname = (TextView) view.findViewById(R.id.textViewName);
        textviewname.setText(Gerichte.get(position).getName());


        /*button_zutaten.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Toast.makeText(getContext(),"Funktioniert" + position, Toast.LENGTH_SHORT).show();
            }
        });*/


        return view;
    }
}

