package ca.qc.cgmatane.informatique.findit.modele;

public class ImageGalerie {
    private int id_image;
    private String urlCoordonnees;
    private int   utilisateur_id;

    public ImageGalerie(int id_image, String urlCoordonnees, int utilisateur_id) {
        super();
        this.id_image = id_image;
        this.urlCoordonnees = urlCoordonnees;
        this.utilisateur_id = utilisateur_id;
    }

    public ImageGalerie() {
        super();
    }

    public ImageGalerie(String urlCoordonnees, int utilisateur_id) {
        this.urlCoordonnees = urlCoordonnees;
        this.utilisateur_id = utilisateur_id;
    }

    public int getId_image() {
        return id_image;
    }

    public void setId_image(int id_image) {
        this.id_image = id_image;
    }

    public String getUrlCoordonnees() {
        return urlCoordonnees;
    }

    public void setUrlCoordonnees(String urlCoordonnees) {
        this.urlCoordonnees = urlCoordonnees;
    }

    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }
}
