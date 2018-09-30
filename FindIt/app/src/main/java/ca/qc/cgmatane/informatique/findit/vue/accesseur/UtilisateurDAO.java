package ca.qc.cgmatane.informatique.findit.vue.accesseur;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ca.qc.cgmatane.informatique.findit.vue.modele.Utilisateur;

public class UtilisateurDAO implements UtilisateurURL {

    String xml = null;
    Utilisateur utilisateur;

    public UtilisateurDAO() {

        utilisateur = new Utilisateur("","","");
    }

    public void ajouterUtilisateur(Utilisateur utilisateur){
        String xml;

        try {
            URL urlAjouterUtilisateur = new URL(URL_AJOUTER_UTILISATEUR);
            HttpURLConnection connection = (HttpURLConnection) urlAjouterUtilisateur.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
