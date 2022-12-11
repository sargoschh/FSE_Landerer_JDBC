import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class RepoTest {

    ZugangsdatenRepository repo;

    @BeforeEach
    public void setup() {
        try {
            repo = new ZugangsdatenRepoImpl();
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testVerbindungsaufbauDBUeberRepo() {
        Assertions.assertDoesNotThrow(() -> new ZugangsdatenRepoImpl());
    }

    @Test
    public void testZugangsdatenEinfuegen() throws InvalidValueException {
        Random r = new Random();
        int zufall = r.nextInt(100);
        Optional<Zugangsdaten> optEingefuegt = repo.insert(new Zugangsdaten(null, "username" + zufall, "asdfjklö", "http://www.google.at"));
        Assertions.assertTrue(optEingefuegt.isPresent());
        Zugangsdaten zugangsdaten = optEingefuegt.get();
        Assertions.assertEquals("username" + zufall, zugangsdaten.getUsername());
        Assertions.assertNotEquals(null, zugangsdaten.getId());
    }

    @Test
    public void testNeueZugangsdatenSpeichernUndDannHolenMitId() throws InvalidValueException {
        Random r = new Random();
        int zufall = r.nextInt(100);
        Optional<Zugangsdaten> optEingefuegt = repo.insert(new Zugangsdaten(null, "username" + zufall, "asdfjklö", "http://www.google.at"));
        Assertions.assertTrue(optEingefuegt.isPresent());
        Long newID = optEingefuegt.get().getId();
        Optional<Zugangsdaten> z2 = repo.getById(newID);
        Assertions.assertTrue(z2.isPresent());
        Assertions.assertEquals(newID, z2.get().getId());
    }

    @Test
    public void testAlleDatensaetzeHolen() throws InvalidValueException {
        List<Zugangsdaten> eintraege = repo.getAll();
        int anzahl = eintraege.size();
        Optional<Zugangsdaten> optEingefuegt = repo.insert(new Zugangsdaten(null, "username", "asdfjklö", "http://www.google.at"));
        Assertions.assertTrue(optEingefuegt.isPresent());
        Optional<Zugangsdaten> optEingefuegt2 = repo.insert(new Zugangsdaten(null, "username", "asdfjklö", "http://www.google.at"));
        Assertions.assertTrue(optEingefuegt2.isPresent());
        Assertions.assertEquals(anzahl + 2, repo.getAll().size());
    }

    @Test
    public void testAlleDatensaetzeMitUrlTeilHolen() throws InvalidValueException {
        UUID uuid = UUID.randomUUID();
        Optional<Zugangsdaten> optEingefuegt = repo.insert(new Zugangsdaten(null, "username", "asdfjklö", "http://www.google.at" + uuid));
        Assertions.assertTrue(optEingefuegt.isPresent());
        Optional<Zugangsdaten> optEingefuegt2 = repo.insert(new Zugangsdaten(null, "username", "asdfjklö", "http://www.google.at" + uuid));
        Assertions.assertTrue(optEingefuegt2.isPresent());
        Assertions.assertEquals(2, repo.getAllWithUrlContaining(uuid.toString()).size());
    }


}
