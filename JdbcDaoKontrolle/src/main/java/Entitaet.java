

public abstract class Entitaet {
    private Long id;

    public Entitaet(Long id) throws FalscherWertException {
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) throws FalscherWertException {
        if (id == null || id >= 0) { //null ist allowed for new entities ...
            this.id = id;
        } else {
            throw new FalscherWertException("Kurs-ID muss größer gleich 0 sein!");
        }
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
