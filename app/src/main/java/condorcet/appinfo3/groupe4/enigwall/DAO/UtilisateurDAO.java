package condorcet.appinfo3.groupe4.enigwall.DAO;

import com.sun.jersey.api.client.ClientResponse;
import java.security.MessageDigest;
import javax.ws.rs.core.MultivaluedMap;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;

public class UtilisateurDAO extends DAO<Utilisateur> {

    /**
     * Méthode permettant de récupérer les informations d'un utilisateur grâce à son id
     * @param id de l'utilisateur
     * @return l'utilisateur lu
     */
    @Override
    public Utilisateur read(String id) throws Exception {
        String utl = service.path("infoUser/"+id).get(String.class);
        Utilisateur utilisateur = gson.fromJson(utl, Utilisateur.class);

        return utilisateur;
    }

    /**
     * Méthode permettant de créer un utilisateur dans la base de données
     * @param l'utilisateur à créer
     * @return id de l'utilisateur créé
     * @throws si l'utilisateur existe déjà, une exception est levée
     */
    @Override
    public int create(Utilisateur obj) throws Exception {
        Utilisateur utilisateur = obj;
        utilisateur.setMdp(sha1Encryption(obj.getMdp()));
        String json = "";

        try{
            json = gson.toJson(utilisateur);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("createUser/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("L'utilisateur existe déjà !");
        }
        else {
            return Integer.valueOf((String) h.getFirst("id"));
        }
    }

    /**
     * Méthode permettant de mettre à jour un utilisateur
     * @param l'utilisateur à mettre à jour
     * @throws si l'utilisateur n'existe pas, une exception est levée
     */
    @Override
    public void update(Utilisateur obj) throws Exception {
        Utilisateur utilisateur = obj;
        String json = "";

        try{
            json = gson.toJson(utilisateur);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("updateUser/").type("application/json").put(ClientResponse.class, json);
        int status = response.getStatus();

        if(status >=400){
            throw new Exception("L'utilisateur n'existe pas !");
        }
    }

    /**
     * Méthode permettant de supprimer un utilisateur
     * @param l'utilisateur à supprimer
     * @throws si l'utilisateur n'existe pas, une exception est levée
     */
    @Override
    public void delete(Utilisateur obj) throws Exception {
        Utilisateur utilisateur = obj;
        String json = "";

        try{
            json = gson.toJson(utilisateur);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("deleteUser/").type("application/json").delete(ClientResponse.class, json);
        int status = response.getStatus();

        if(status >=400){
            throw new Exception("L'utilisateur n'existe pas !");
        }
    }

    /**
     * Méthode permettant de vérifier que le pseudo de l'utilisateur n'existe pas dans la base de données
     * @param l'utilisateur à vérifier
     * @return false si le pseudo n'existe pas et true s'il existe
     */
    public boolean checkPseudo(Utilisateur obj) throws Exception {
        Utilisateur utilisateur = obj;
        String json = "";

        try{
            json = gson.toJson(utilisateur);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("checkPseudo/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();

        if(status == 500){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Méthode permettant de vérifier que l'adresse email de l'utilisateur n'existe pas dans la base de données
     * @param l'utilisateur à vérifier
     * @return false si le mail n'existe pas et true s'il existe
     */
    public boolean checkMail(Utilisateur obj) throws Exception {
        Utilisateur utilisateur = obj;
        String json = "";

        try{
            json = gson.toJson(utilisateur);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("checkMail/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();

        if(status == 500){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Méthode permettant de vérifier que le pseudo et le mot de passe concordent via la base de données
     * @param l'utilisateur à vérifier
     * @return l'utilisateur qui a été vérifié
     * @throws si le couple <pseudo/mdp> n'existe pas, une exception est levée
     */
    public Utilisateur connexion(Utilisateur obj) throws Exception {

        Utilisateur utilisateur;

        try {
            String utl = service.path("connexionUser/" + obj.getPseudo() + "-" + sha1Encryption(obj.getMdp())).get(String.class);
            utilisateur = gson.fromJson(utl, Utilisateur.class);
        } catch (Exception e) {
            throw new Exception("L'utilisateur n'existe pas !");
        }

        return utilisateur;
    }

    /**
     * Méthode permettant de remettre à 0 l'avancement de l'utilisateur
     * @param l'utilisateur à mettre à jour
     * @throws si l'utilisateur n'existe pas, une exception est levée
     */
    public void updateAvancement(Utilisateur obj) throws Exception {
        Utilisateur utilisateur = obj;
        String json = "";

        try{
            json = gson.toJson(utilisateur);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("updateAvancementUser/").type("application/json").put(ClientResponse.class, json);
        int status = response.getStatus();

        if(status >=400){
            throw new Exception("L'utilisateur n'existe pas !");
        }
    }

    /**
     * Méthode permettant d'encrypter le mot de passe en SHA1
     * @param le mot de passe à encrypter
     * @return le mot de passe encrypté
     */
    private String sha1Encryption(String mdp) throws Exception {
        StringBuilder buffer = new StringBuilder();

        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(mdp.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            for (byte b : bytes)
            {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
        }
        catch (Exception e)
        {
            throw new Exception("Erreur dans l'encodage du mot de passe !");
        }

        return buffer.toString();
    }
}