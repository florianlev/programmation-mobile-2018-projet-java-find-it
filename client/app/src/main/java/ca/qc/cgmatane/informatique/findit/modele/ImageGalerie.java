package ca.qc.cgmatane.informatique.findit.modele;

public class ImageGalerie {
    private int id_image;
    private String url;


    public ImageGalerie(int id_image, String url, int utilisateur_id) {
        super();
        this.id_image = id_image;
        this.url = url;

    }

    public ImageGalerie() {
        super();
    }

    public ImageGalerie(String url, int utilisateur_id) {
        this.url = url;

    }

    public int getId_image() {
        return id_image;
    }

    public void setId_image(int id_image) {
        this.id_image = id_image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
