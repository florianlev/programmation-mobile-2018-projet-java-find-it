package ca.qc.cgmatane.informatique.findit.modele;

import java.util.HashMap;

public class Score {
    protected int id_score;
    protected int valeur;
    protected int id_utilisateur;

    public Score(int id_score, int valeur, int id_utilisateur) {
        super();
        this.id_score = id_score;
        this.valeur = valeur;
        this.id_utilisateur = id_utilisateur;
    }
    public Score(int id_score, int valeur) {
        super();
        this.id_score = id_score;
        this.valeur = valeur;
    }
    public Score(){
        super();
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public int getId_score() {
        return id_score;
    }

    public void setId_score(int id_score) {
        this.id_score = id_score;
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
        scorePourAdapteur.put("id_score",""+this.id_score);
        return scorePourAdapteur;
    }
}
