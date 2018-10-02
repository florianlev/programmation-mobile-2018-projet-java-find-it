package ca.qc.cgmatane.informatique.findit.accesseur;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.findit.modele.Score;

public class ScoreDAO {


    private static ScoreDAO instance =null;
    private BaseDeDonnees accesseurBaseDeDonnees;
    protected List<Score> listeScores;

    public static ScoreDAO getInstance(){
        if (null == instance){
            instance= new ScoreDAO();
        }
        return instance;
    }


    public ScoreDAO(){
        this.accesseurBaseDeDonnees = BaseDeDonnees.getInstance();
        listeScores =new ArrayList<Score>();

    }

    public List<Score> listerScore() {
        String LISTER_SCORES = "SELECT * FROM score";
        Cursor curseur = accesseurBaseDeDonnees.getReadableDatabase().rawQuery(LISTER_SCORES,
                null);
        this.listeScores.clear();
        Score Score;

        int indexId_score = curseur.getColumnIndex("score_id");
        int indexValeur = curseur.getColumnIndex("valeur");



        for(curseur.moveToFirst();!curseur.isAfterLast();curseur.moveToNext()) {
            int id_score = curseur.getInt(indexId_score);
            int valeur = curseur.getInt(indexValeur);


            Score = new Score(id_score,valeur);
            this.listeScores.add(Score);
        }

        return listeScores;
    }

    public Score trouverScore(int id_score)
    {
        for(Score scoreRecherche : this.listeScores)
        {
            if(scoreRecherche.getId() == id_score) return scoreRecherche;
        }
        return null;
    }


    public void modifierEvenement(Score Score)
    {

        SQLiteDatabase db = accesseurBaseDeDonnees.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("valeur", Score.getValeur());


        db.update("Score",value,"score_id = ? ",new String[] {String.valueOf(Score.getId())});

    }


    public List<HashMap<String, String>> recuperereListeScorePourAdapteur() {
        List<HashMap<String, String>> listeScorePourAdapteur;
        listeScorePourAdapteur = new ArrayList<HashMap<String, String>>();

        listerScore();
        for(Score Score :listeScores){
            listeScorePourAdapteur.add(Score.obtenirScorePourAdapteur());
        }
        return listeScorePourAdapteur;
    }

    public void ajouterScore(Score Score)
    {
        SQLiteDatabase db = accesseurBaseDeDonnees.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("valeur", Score.getValeur());
        db.insert("Score",null,value);
        System.out.println("SUCCES AJOUT");


    }



}
