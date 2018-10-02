package ca.qc.cgmatane.informatique.findit.accesseur;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.findit.modele.score;

public class scoreDAO {


    private static scoreDAO instance =null;
    private BaseDeDonnees accesseurBaseDeDonnees;
    protected List<score> listeScores;

    public static scoreDAO getInstance(){
        if (null == instance){
            instance= new scoreDAO();
        }
        return instance;
    }


    public scoreDAO(){
        this.accesseurBaseDeDonnees = BaseDeDonnees.getInstance();
        listeScores =new ArrayList<score>();

    }

    public List<score> listerScore() {
        String LISTER_SCORES = "SELECT * FROM score";
        Cursor curseur = accesseurBaseDeDonnees.getReadableDatabase().rawQuery(LISTER_SCORES,
                null);
        this.listeScores.clear();
        score score;

        int indexId_score = curseur.getColumnIndex("score_id");
        int indexValeur = curseur.getColumnIndex("valeur");



        for(curseur.moveToFirst();!curseur.isAfterLast();curseur.moveToNext()) {
            int id_score = curseur.getInt(indexId_score);
            int valeur = curseur.getInt(indexValeur);


            score = new score(id_score,valeur);
            this.listeScores.add(score);
        }

        return listeScores;
    }

    public score trouverScore(int id_score)
    {
        for(score scoreRecherche : this.listeScores)
        {
            if(scoreRecherche.getId() == id_score) return scoreRecherche;
        }
        return null;
    }


    public void modifierEvenement(score score)
    {

        SQLiteDatabase db = accesseurBaseDeDonnees.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("valeur",score.getValeur());


        db.update("score",value,"score_id = ? ",new String[] {String.valueOf(score.getId())});

    }


    public List<HashMap<String, String>> recuperereListeScorePourAdapteur() {
        List<HashMap<String, String>> listeScorePourAdapteur;
        listeScorePourAdapteur = new ArrayList<HashMap<String, String>>();

        listerScore();
        for(score score:listeScores){
            listeScorePourAdapteur.add(score.obtenirScorePourAdapteur());
        }
        return listeScorePourAdapteur;
    }

    public void ajouterScore(score score)
    {
        SQLiteDatabase db = accesseurBaseDeDonnees.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("valeur",score.getValeur());
        db.insert("score",null,value);


    }



}
