package ca.qc.cgmatane.informatique.findit.accesseur;

import android.database.Cursor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import ca.qc.cgmatane.informatique.findit.modele.Utilisateur;

public class UtilisateurDAO implements UtilisateurURL {

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
    public void ajouterUtilisateur(Utilisateur utilisateur)
    {
        //listeEvenements.add(evenement);
        System.out.println("ajouterUtilisateur");
        String AJOUTER_UTILISATEUR;
        AJOUTER_UTILISATEUR = "INSERT INTO utilisateur (pseudo,mail,mdp) VALUES ('"+ utilisateur.getPseudo() +"','"+ utilisateur.getMdp()+"',' "+utilisateur.getMail()+"');";
        //accesseurBaseDeDonnees.getWritableDatabase().execSQL(AJOUTER_UTILISATEUR);

    }

    /*public void ajouterUtilisateur(Utilisateur utilisateur){
        String xml;

        try {
            URL urlAjouterUtilisateur = new URL(URL_AJOUTER_UTILISATEUR);
            HttpURLConnection connection = (HttpURLConnection) urlAjouterUtilisateur.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            OutputStream fluxEcriture = connection.getOutputStream();
            OutputStreamWriter envoyeur = new OutputStreamWriter(fluxEcriture);

            envoyeur.write("pseudo="+ utilisateur.getPseudo() + "&mail=" + utilisateur.getMail() + "&mdp=" + utilisateur.getMdp());
            envoyeur.close();

            int codeReponse = connection.getResponseCode();
            System.out.print("Code de reponse " + codeReponse);

            InputStream fluxLecture = connection.getInputStream();
            Scanner lecture = new Scanner(fluxLecture);

            String derniereBalise = "</action>";
            lecture.useDelimiter(derniereBalise);
            xml = lecture.next() + derniereBalise;
            lecture.close();
            System.out.println("XML : " + xml);

            connection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
}
