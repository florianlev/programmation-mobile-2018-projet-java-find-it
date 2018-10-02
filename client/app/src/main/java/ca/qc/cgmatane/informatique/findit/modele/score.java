package ca.qc.cgmatane.informatique.findit.modele;

import java.util.HashMap;

public class score {
    protected int id;
    protected int valeur;

    public score(int id, int valeur) {
        this.id = id;
        this.valeur = valeur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }
    public HashMap<String, String> obtenirScorePourAdapteur()
    {
        HashMap<String, String> scorePourAdapteur = new HashMap<String,String>();
        scorePourAdapteur.put("valeur", ""+this.valeur);
        scorePourAdapteur.put("id",""+this.id);
        return scorePourAdapteur;
    }
}
