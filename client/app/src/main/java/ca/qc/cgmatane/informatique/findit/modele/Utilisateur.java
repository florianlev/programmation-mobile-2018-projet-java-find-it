package ca.qc.cgmatane.informatique.findit.modele;

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
        this.mdp = mdp;
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
        this.mdp = mdp;
    }
}
