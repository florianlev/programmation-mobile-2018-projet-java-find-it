package ca.qc.cgmatane.informatique.findit.accesseur;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import ca.qc.cgmatane.informatique.findit.modele.Utilisateur;

public class UtilisateurDAO {

    String xml = null;
    private static UtilisateurDAO instance = null;

    private BaseDeDonnees accesseurBaseDeDonnees;




    public static UtilisateurDAO getInstance() {

        if(null == instance)
        {
            instance = new UtilisateurDAO();
        }
        return instance;
    }

    public UtilisateurDAO() {
        this.accesseurBaseDeDonnees = BaseDeDonnees.getInstance();
    }

    public String afficherUtilisateur(String pseudo){
        String LISTER_EVENEMENTS = "SELECT mdp FROM utilisateur WHERE pseudo ='"+pseudo+"';";
        Cursor curseur = accesseurBaseDeDonnees.getReadableDatabase().rawQuery(LISTER_EVENEMENTS,
                null);
        String mdp;
        for(curseur.moveToFirst();!curseur.isAfterLast();curseur.moveToNext()) {
            mdp = curseur.getString(curseur.getColumnIndex("mdp"));
            return mdp;
        }
        return null;
    }

    public int verifConnecction(String pseudo,String mdp){
        String LISTER_EVENEMENTS = "SELECT count(utilisateur_id) as compteur FROM utilisateur WHERE pseudo ='"+pseudo+"' AND mdp ='"+mdp+"';";
        Cursor curseur = accesseurBaseDeDonnees.getReadableDatabase().rawQuery(LISTER_EVENEMENTS,
                null);
        int compteur;
        for(curseur.moveToFirst();!curseur.isAfterLast();curseur.moveToNext()) {
            compteur = curseur.getInt(curseur.getColumnIndex("compteur"));
            return compteur;
        }
        return 0;
    }

    public void ajouterUtilisateurSQL(Utilisateur utilisateur){

        try{
            String url = "http://158.69.113.110/findItServeur/utilisateur/ajouter/index.php?pseudo="+utilisateur.getPseudo()+"&mail="+utilisateur.getMail()+"&mdp="+utilisateur.getMdp();

            String resultat;
            HttpGetRequete getRequete = new HttpGetRequete();
            resultat = getRequete.execute(url).get();
        }catch(InterruptedException e){
            System.out.println("got interrupted!");
        }catch(ExecutionException e){
            System.out.println("got interrupted!");
        }
    }
}
