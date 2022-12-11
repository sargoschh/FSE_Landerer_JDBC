import java.util.List;

/**
 * CREATE TABLE `zugangsdaten`.`zugangsdaten` ( `id` INT NOT NULL AUTO_INCREMENT , `username` VARCHAR(50) NOT NULL , `password` VARCHAR(50) NOT NULL , `url` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;
 */
public interface ZugangsdatenRepository extends Repository<Zugangsdaten, Long> {
    List<Zugangsdaten> getAllWithUrlContaining(String urlpart); //Alle Zugangsdaten, die in der URL einen String enthalten
}
