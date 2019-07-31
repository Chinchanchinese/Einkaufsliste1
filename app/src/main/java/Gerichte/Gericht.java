package Gerichte;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.List;

import Zutaten.Zutat;

public class Gericht implements Serializable {

    private String id;
    private String name;
    private int Foto = 1;
    private List<Zutat> Zutatenliste;
    private String Datensatz;
    private Bitmap Fotobm;
    private Uri bilduri;
    private Boolean Haken = false;
    private String path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFoto() {
        return Foto;

    }

    public void setFoto(int foto) {
        Foto = foto;
    }

    public Bitmap getFotobm() {
        return Fotobm;
    }

    public void setFotobm(Bitmap fotobm) {
        Fotobm = fotobm;
    }

    public String getDatensatz() {
        return Datensatz;
    }

    public void setDatensatz(String datensatz) {
        Datensatz = datensatz;
    }

    public List<Zutat> getZutatenliste() {
        return Zutatenliste;
    }

    public void setZutatenliste(List<Zutat> zutatenliste) {
        Zutatenliste = zutatenliste;
    }

    public Uri getBilduri() {
        return bilduri;
    }

    public void setBilduri(Uri bilduri) {
        this.bilduri = bilduri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getHaken() {
        return Haken;
    }

    public void setHaken(Boolean haken) {
        Haken = haken;
    }

    public Gericht(String name, int bm, List<Zutat> zutatenliste, String Datensatz) {
        this.id = id;
        this.name = name;
        Foto = bm;
        this.Zutatenliste = zutatenliste;
        this.Datensatz = Datensatz;
    }

    public Gericht(String name, Uri Bilduri, List<Zutat> zutatenliste, String Datensatz) {
        this.id = id;
        this.name = name;
        bilduri = Bilduri;
        this.Zutatenliste = zutatenliste;
        this.Datensatz = Datensatz;
    }

    public Gericht(String name, String mpath, List<Zutat> zutatenliste, String Datensatz) {
        this.id = id;
        this.name = name;
        this.path = mpath;
        this.Zutatenliste = zutatenliste;
        this.Datensatz = Datensatz;
    }


    public Gericht() {
    }
}
