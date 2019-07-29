package Gerichte;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.util.Base64;

import de.rg.einkaufsliste.R;
import Zutaten.ZutatenActivity;

public class Adapter_Gerichte extends RecyclerView.Adapter<Adapter_Gerichte.MyViewholder>{

    private ArrayList<Gericht> GerichteListe;
    private Adapter_Gerichte.OnItemClickListener myListener;
    private Context context;
    private LayoutInflater inflater;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onZutatenClick(int position);
    }

    public void setOnItemClickListener(Adapter_Gerichte.OnItemClickListener listener) {
        myListener = listener;
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {

        public TextView name;//löschen
        public ImageView Bild;
        public ImageView imageEinkaufskorbHaken;


        public MyViewholder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            name= itemView.findViewById(R.id.textViewNameGericht);//löschen
            Bild= itemView.findViewById(R.id.imageViewGericht);
            imageEinkaufskorbHaken = itemView.findViewById(R.id.imageViewEinkaufskorb);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public void updateList(ArrayList Gerichte) {
        this.GerichteListe.clear();
        this.GerichteListe.addAll(Gerichte);
    }

    public Adapter_Gerichte(Context context,ArrayList<Gericht> mGerichte){
        GerichteListe = mGerichte;
        inflater = LayoutInflater.from(context);
        this.context=context;
    }


    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_gericht,parent,false);
        MyViewholder myvh = new MyViewholder(v, myListener);
        return myvh;
    }


    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        Gericht gericht = GerichteListe.get(position);
        holder.name.setText(gericht.getName());
        if (gericht.getFoto() !=1){
            holder.Bild.setImageResource(gericht.getFoto());
        }else{
            holder.Bild.setImageBitmap(getBitmapFromString(gericht.getBilduri()));
        }
        if (gericht.getHaken()==true) {
            holder.imageEinkaufskorbHaken.setVisibility(View.VISIBLE);
        }
        else{
            holder.imageEinkaufskorbHaken.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return GerichteListe.size();
    }
    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}

