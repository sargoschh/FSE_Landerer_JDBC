

public class Zugangsdaten extends Entitaet {

    private String username;
    private String passwort;
    private String url;

    public Zugangsdaten(Long id, String username, String passwort, String url) throws FalscherWertException, InvalidValueException {
        super(id);
        this.setUsername(username);
        this.setPasswort(passwort);
        this.setUrl(url);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws InvalidValueException {
        if(username!=null && username.length() > 2) {
            this.username = username;
        } else {
            throw new InvalidValueException("Username muss mindestens 2 Zeichen lang sein!");
        }

    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) throws InvalidValueException {
        if(passwort!=null && passwort.length() > 8) {
            this.passwort = passwort;
        } else {
            throw new InvalidValueException("Passwort muss mindestens 8 Zeichen lang sein!");
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) throws InvalidValueException {
        if(url!=null && url.length() > 10) {
            this.url = url;
        } else {
            throw new InvalidValueException("URL muss mindestens 10 Zeichen lang sein!");
        }
    }
}
