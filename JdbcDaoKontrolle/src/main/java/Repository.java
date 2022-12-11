import java.util.List;
import java.util.Optional;

public interface Repository<T, I> {

    Optional<T> insert(T entity); //ID wird von der DB vergeben

    Optional<T> getById(I id);

    List<T> getAll();
}
