package ca.qc.cgmatane.informatique.findit.accesseur;


import android.database.Cursor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import java.io.StringBufferInputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ca.qc.cgmatane.informatique.findit.modele.Utilisateur;

public class UtilisateurDAO {


    private static UtilisateurDAO instance = null;
    private BaseDeDonnees accesseurBaseDeDonnees;

    List<Utilisateur> listeUtilisateurs;


    public static UtilisateurDAO getInstance() {

        if(null == instance)
        {
            instance = new UtilisateurDAO();
        }

        return instance;
    }

    public UtilisateurDAO() {

        this.accesseurBaseDeDonnees = BaseDeDonnees.getInstance();
        listeUtilisateurs = new ArrayList<>();

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

    public List<Utilisateur> listerUtilisateur() {
        try {
            String url = "http://158.69.113.110/findItServeur/utilisateur/liste/index.php";

            String xml;
            String derniereBalise = "</utilisateurs>";
            HttpPostRequete postRequete = new HttpPostRequete();
            xml = postRequete.execute(url, derniereBalise).get();

            DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            @SuppressWarnings("deprecation")
            Document document = parseur.parse(new StringBufferInputStream(xml));
            String racine = document.getDocumentElement().getNodeName();
            System.out.println(racine);


            NodeList listeNoeudUtilisateur = document.getElementsByTagName("utilisateur");
            for (int position = 0; position < listeNoeudUtilisateur.getLength(); position++) {

                Element noeudUtilisateur = (Element) listeNoeudUtilisateur.item(position);

                //System.out.println(noeudUtilisateur.getElementsByTagName("pseudo").item(0).getTextContent());
                Utilisateur utilisateur = new Utilisateur();
                String id = noeudUtilisateur.getElementsByTagName("utilisateur_id").item(0).getTextContent();
                utilisateur.setId(Integer.parseInt(id));

                String pseudo = noeudUtilisateur.getElementsByTagName("pseudo").item(0).getTextContent();
                utilisateur.setPseudo(pseudo);
                String mail = noeudUtilisateur.getElementsByTagName("mail").item(0).getTextContent();
                utilisateur.setMail(mail);
                String mdp = noeudUtilisateur.getElementsByTagName("mdp").item(0).getTextContent();
                utilisateur.setMdp(mdp);


                //listeVoyage.add(voyage);

            }
        }catch(InterruptedException e){
            e.printStackTrace();

        }catch(ParserConfigurationException e){
            e.printStackTrace();

        }catch(SAXException e){
            e.printStackTrace();

        }catch(IOException e){
            e.printStackTrace();

        }catch(ExecutionException e){
            e.printStackTrace();

        }
        return listeUtilisateurs;
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
