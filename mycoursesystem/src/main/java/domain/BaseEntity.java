package domain;

public abstract class BaseEntity {

    public Long id;

    public BaseEntity(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        if(id == null || id >= 0) {
            this.id = id;

        } else {
            throw new InvalidValueException("Kurs-ID muss größer gleich 0!");
        }
    }
}
