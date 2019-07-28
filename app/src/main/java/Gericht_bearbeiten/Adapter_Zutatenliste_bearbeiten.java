package Gericht_bearbeiten;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Zutaten.Zutat;
import de.rg.einkaufsliste.R;

public class Adapter_Zutatenliste_bearbeiten extends RecyclerView.Adapter<Adapter_Zutatenliste_bearbeiten.MyViewholder> {

    private ArrayList<Zutat> Zutatenliste;
    private OnItemClickListener myListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView delete;


        public MyViewholder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            textView= itemView.findViewById(R.id.textViewCard);
            delete = itemView.findViewById(R.id.imageViewdelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public Adapter_Zutatenliste_bearbeiten(ArrayList<Zutat> mZutatenliste){
        Zutatenliste = mZutatenliste;
    }


    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_zutaten_bearbeiten,parent,false);
        MyViewholder myvh = new MyViewholder(v, myListener);
        return myvh;
    }


    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        Zutat zutat = Zutatenliste.get(position);
        holder.textView.setText(zutat.getName());
    }

    @Override
    public int getItemCount() {
        return Zutatenliste.size();
    }
}
