package ca.qc.cgmatane.informatique.findit.accesseur;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
        listeScores = new ArrayList<>();

    }



    public List<Score> listerScore() {

        try {
            String url = "http://158.69.113.110/findItServeur/score/liste/indexScore.php";
            String xml;
            String derniereBalise = "</scores>";
            HttpPostRequete postRequete = new HttpPostRequete();
            xml = postRequete.execute(url, derniereBalise).get();

            DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            @SuppressWarnings("deprecation")

            Document document = parseur.parse(new StringBufferInputStream(xml));
            String racine = document.getDocumentElement().getNodeName();
            NodeList listeNoeudScore = document.getElementsByTagName("score");
            listeScores.clear();
            for (int position = 0; position < listeNoeudScore.getLength(); position++) {
                Element noeudScore = (Element) listeNoeudScore.item(position);
                Score score = new Score();
                String id = noeudScore.getElementsByTagName("id").item(0).getTextContent();
                score.setId_score(Integer.parseInt(id));
                String valeur = noeudScore.getElementsByTagName("valeur").item(0).getTextContent();
                score.setValeur(Integer.parseInt(valeur));

                String id_utilisateur = noeudScore.getElementsByTagName("utilisateur_id").item(0).getTextContent();
                score.setId_utilisateur(Integer.parseInt(id_utilisateur));

                listeScores.add(score);
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
        return listeScores;
    }


    public Score trouverScore(int id_score)
    {
        for(Score scoreRecherche : this.listeScores)
        {
            if(scoreRecherche.getId_score() == id_score) return scoreRecherche;
        }
        return null;
    }


    public void modifierEvenement(Score Score)
    {

        SQLiteDatabase db = accesseurBaseDeDonnees.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("valeur", Score.getValeur());


        db.update("Score",value,"score_id = ? ",new String[] {String.valueOf(Score.getId_score())});

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

    public void ajouterScore(Score score)
    {
        try{
            String url = "http://158.69.113.110/findItServeur/score/ajouter/index.php?valeur="+ score.getValeur()+"&utilisateur_id="+score.getId_utilisateur();

            String resultat;
            HttpGetRequete getRequete = new HttpGetRequete();
            resultat = getRequete.execute(url).get();
        }catch(InterruptedException e){
            System.out.println("got interrupted!");
        }catch(ExecutionException e){
            System.out.println("got interrupted!");
        }
    }

    public void modifierScore(Score score)
    {
        try{
            String url = "http://158.69.113.110/findItServeur/score/modifier/index.php?valeur="+ score.getValeur()+"&utilisateur_id="+score.getId_utilisateur();

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
