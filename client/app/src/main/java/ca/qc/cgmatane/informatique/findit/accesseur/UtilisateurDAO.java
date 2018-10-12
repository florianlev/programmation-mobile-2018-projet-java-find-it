package ca.qc.cgmatane.informatique.findit.accesseur;

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

        if (null == instance) {
            instance = new UtilisateurDAO();
        }

        return instance;
    }

    public UtilisateurDAO() {

        this.accesseurBaseDeDonnees = BaseDeDonnees.getInstance();
        listeUtilisateurs = new ArrayList<>();

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
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();

        } catch (SAXException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();

        }
        return listeUtilisateurs;
    }

    public boolean verifierConnection(Utilisateur utilisateur){

        try{
            String url = "http://158.69.113.110/findItServeur/utilisateur/compteur/index.php?pseudo="+utilisateur.getPseudo()+"&mdp="+utilisateur.getMdp();
            String xml;
            String nombre;
            String derniereBalise = "</compteurs>";
            HttpPostRequete postRequete = new HttpPostRequete();
            xml = postRequete.execute(url, derniereBalise).get();

            DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            @SuppressWarnings("deprecation")
            Document document = parseur.parse(new StringBufferInputStream(xml));
            String racine = document.getDocumentElement().getNodeName();
            NodeList listeNoeudCompteur = document.getElementsByTagName("compteur");
            for (int position = 0; position < listeNoeudCompteur.getLength(); position++) {
                Element noeudCompteur = (Element) listeNoeudCompteur.item(position);
                nombre = noeudCompteur.getElementsByTagName("nombre").item(0).getTextContent();
                if(Integer.parseInt(nombre) >=1){
                    return true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();

        } catch (SAXException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();

        }
        return false;
    }

    public int recupererIdUtilisateur(Utilisateur utilisateur){
        try{
            String url = "http://158.69.113.110/findItServeur/utilisateur/recupererId/index.php?pseudo="+utilisateur.getPseudo()+"&mdp="+utilisateur.getMdp();
            String xml;
            String id;
            String derniereBalise = "</Utilisateurs>";
            HttpPostRequete postRequete = new HttpPostRequete();
            xml = postRequete.execute(url, derniereBalise).get();

            DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            @SuppressWarnings("deprecation")
            Document document = parseur.parse(new StringBufferInputStream(xml));
            String racine = document.getDocumentElement().getNodeName();
            NodeList listeNoeudUtilisateur = document.getElementsByTagName("utilisateur");
            for (int position = 0; position < listeNoeudUtilisateur.getLength(); position++) {
                Element noeudUtilisateur = (Element) listeNoeudUtilisateur.item(position);
                id = noeudUtilisateur.getElementsByTagName("id").item(0).getTextContent();
                return Integer.parseInt(id);

            }
        }catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();

        } catch (SAXException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();

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
