package ca.qc.cgmatane.informatique.findit.accesseur;

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
    }
}
