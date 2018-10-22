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


        public ImageGalerie trouverImage(int id_image)
        {
            for(ImageGalerie imageGalerie : this.listeImage)
            {
                if(imageGalerie.getId_image() == id_image) return imageGalerie;
            }
            return null;
        }

    public String[] recuperereListeScorePourImageAdapteur() {
        List<String> listeImageAfficher =new ArrayList<>();
        for(ImageGalerie image : listerImage()){
            listeImageAfficher.add(image.getUrl());
        }

        Object[] objNames = listeImageAfficher.toArray();

        String[] listeUrl = Arrays.copyOf(objNames, objNames.length, String[].class);
    //liste de test car service worker non disponible donc recuperation a partir bdd imposible pour le moment
        // String[] listeUrl =  {"https://www.debian.org/logos/openlogo-nd-100.png", "https://files.newsnetz.ch/story/1/8/5/18521241/11/topelement.jpg", "https://www.microdepot.com/wp-content/blogs.dir/92/files/2014/02/icon-27046_640.png", "https://images.frandroid.com/wp-content/uploads/2017/09/logo-apple-special-event-sept-12-2017.jpg"};
        return listeUrl;
    }


    public void envoyerPhoto(String cheminImage){
        //FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);

        /*try {

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://158.69.113.110/findItServeur/galerie/ajouter/index.php");
            FileBody bin = new FileBody( new File(cheminImage));
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("image", bin);
            builder.addBinaryBody("image", new File(cheminImage));
            HttpEntity entity = builder.build();
            httppost.setEntity(entity);

            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String page = EntityUtils.toString(resEntity);
                System.out.println("PAGE :" + page);

            }

        } catch (IOException ex) {
        Logger.getLogger(GalerieDAO.class.getName()).log(Level.SEVERE, null, ex);
    }*/
        System.out.println(cheminImage);

    }


    //todo implementer methode d'envoie d'image sur le serveur
    }


