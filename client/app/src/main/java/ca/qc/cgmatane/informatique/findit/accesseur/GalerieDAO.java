package ca.qc.cgmatane.informatique.findit.accesseur;

import android.graphics.Bitmap;
import android.net.Uri;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringBufferInputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ca.qc.cgmatane.informatique.findit.modele.ImageGalerie;

public class GalerieDAO {

    private static final String UPLOAD_URL = "http://158.69.113.110/findItServeur/galerie/ajouter/index.php";
    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;
    private static GalerieDAO instance =null;
    private BaseDeDonnees accesseurBaseDeDonnees;
    protected List<ImageGalerie> listeImage;

    public static GalerieDAO getInstance(){
        if (null == instance){
            instance= new GalerieDAO();
        }
        return instance;
    }


    public GalerieDAO(){
        this.accesseurBaseDeDonnees = BaseDeDonnees.getInstance();
        listeImage = new ArrayList<>();

    }



    public List<ImageGalerie> listerImage() {

        try {
            String url = "http://158.69.113.110/findItServeur/galerie/liste/indexGalerie.php";
            String xml;
            String derniereBalise = "</photos>";
            HttpPostRequete postRequete = new HttpPostRequete();
            xml = postRequete.execute(url, derniereBalise).get();

            DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            @SuppressWarnings("deprecation")

            Document document = parseur.parse(new StringBufferInputStream(xml));
            String racine = document.getDocumentElement().getNodeName();
            NodeList listeNoeudScore = document.getElementsByTagName("photo");
            listeImage.clear();
            for (int position = 0; position < listeNoeudScore.getLength(); position++) {
                Element noeudScore = (Element) listeNoeudScore.item(position);
                ImageGalerie imageGalerie = new ImageGalerie();
                String id = noeudScore.getElementsByTagName("id").item(0).getTextContent();
                imageGalerie.setId_image(Integer.parseInt(id));
                String valeur = noeudScore.getElementsByTagName("url").item(0).getTextContent();
                imageGalerie.setUrl(valeur);

                listeImage.add(imageGalerie);

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
        return listeImage;
    }


    public ImageGalerie trouverImage(int id_image) {
        for(ImageGalerie imageGalerie : this.listeImage) {
            if(imageGalerie.getId_image() == id_image) return imageGalerie;
        }
        return null;
    }

    public String[] recuperereListePourImageAdapteur() {
        List<String> listeImageAfficher =new ArrayList<>();
        for(ImageGalerie image : listerImage()){
            listeImageAfficher.add(image.getUrl());
        }

        Object[] objNames = listeImageAfficher.toArray();

        String[] listeUrl = Arrays.copyOf(objNames, objNames.length, String[].class);
        return listeUrl;
    }
}


