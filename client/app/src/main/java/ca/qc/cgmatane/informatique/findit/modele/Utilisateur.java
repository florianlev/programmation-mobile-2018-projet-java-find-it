package ca.qc.cgmatane.informatique.findit.modele;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilisateur {

    protected int id;
    protected String pseudo;
    protected String mail;
    protected String mdp;


    public Utilisateur() {
        super();

    }
    public Utilisateur(String pseudo, String mail, String mdp) {
        super();
        this.pseudo = pseudo;
        this.mail = mail;
        try {
            this.mdp = SHA256(mdp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public Utilisateur(String pseudo, String mdp) {
        super();
        this.pseudo = pseudo;
        try {
            this.mdp = SHA256(mdp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        try {
            this.mdp = SHA256(mdp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String SHA256 (String text) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes());
        byte[] digest = md.digest();

        return Base64.encodeToString(digest, Base64.DEFAULT);
    }
}
