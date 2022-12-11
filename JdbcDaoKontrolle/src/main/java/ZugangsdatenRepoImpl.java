import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZugangsdatenRepoImpl implements ZugangsdatenRepository {

    Connection con;

    public ZugangsdatenRepoImpl() throws SQLException, ClassNotFoundException {
        this.con = MySqLDBVerbindung.getConnection("jdbc:mysql://localhost:3306/zugangsdaten", "root", "");
    }

    @Override
    public Optional<Zugangsdaten> insert(Zugangsdaten entity) {
        EigeneAsserts.notNull(entity);

        String sql = "INSERT INTO `zugangsdaten` (`username`, `password`, `url`) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPasswort());
            preparedStatement.setString(3, entity.getUrl());

            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows==0) {
                return Optional.empty();
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                return this.getById(generatedKeys.getLong(1));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Zugangsdaten> getById(Long id) {
        EigeneAsserts.notNull(id);

        if(countColumnsById(id)==0) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM `zugangsdaten` WHERE `id` LIKE ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            Zugangsdaten zugangsdaten = newZugangsdaten(resultSet);

            return Optional.of(zugangsdaten);
        } catch (SQLException | InvalidValueException e) {
            throw new DatenbankException(e.getMessage());
        }
    }

    private int countColumnsById(Long id) {
        try {
            String sql = "SELECT COUNT(*) FROM `zugangsdaten` WHERE `id`=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new DatenbankException(e.getMessage());
        }
    }

    @Override
    public List<Zugangsdaten> getAll() {

        String sql = "SELECT * FROM `zugangsdaten`";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            return getZugangsdatenList(resultSet);
        } catch (SQLException e) {
            throw new DatenbankException(e.getMessage());
        }
    }

    @Override
    public List<Zugangsdaten> getAllWithUrlContaining(String urlpart) {
        EigeneAsserts.notNull(urlpart);

        String sql = "SELECT * FROM `zugangsdaten` WHERE LOWER(`url`) LIKE LOWER(?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+urlpart+"%");

            ResultSet resultSet = preparedStatement.executeQuery();

            return getZugangsdatenList(resultSet);
        } catch (SQLException e) {
            throw new DatenbankException(e.getMessage());
        }
    }

    private List<Zugangsdaten> getZugangsdatenList(ResultSet resultSet) {
        try {
            ArrayList<Zugangsdaten> zugangsdatenList = new ArrayList<>();

            while (resultSet.next()) {
                zugangsdatenList.add(newZugangsdaten(resultSet));
            }
            return zugangsdatenList;
        } catch (SQLException | InvalidValueException e) {
            throw new DatenbankException(e.getMessage());
        }
    }

    private Zugangsdaten newZugangsdaten(ResultSet resultSet) throws SQLException, InvalidValueException {
        return new Zugangsdaten(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4)
        );
    }
}
