package horseraceapp.util.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import horseraceapp.util.connection.ConnectionManager;
import horseraceapp.util.connection.MySqlConnectionManager;
import horseraceapp.util.dao.ContestantHorseDao;
import horseraceapp.util.dao.entity.ContestantHorse;
import horseraceapp.util.dao.entity.Race;

public class MySqlContestantHorseDao implements ContestantHorseDao {

    private final String SELECT_ALL_HORSES_BY_RACE_ID_QUERY = "SELECT ch.id, "
            + "horse.name, race.start_time, race.place, race.distance, ch.position, "
            + "ch.coefficient FROM contestant_horse AS ch JOIN race ON "
            + "ch.race_id = race.id JOIN horse ON ch.horse_id = horse.id "
            + "WHERE race.id = ?";

    private final String SELECT_ALL_HORSES_WITHOUT_RESULT_BY_RACE_ID_QUERY = "SELECT ch.id, "
            + "horse.name, race.start_time, race.place, race.distance, ch.position, "
            + "ch.coefficient FROM contestant_horse AS ch JOIN race ON "
            + "ch.race_id = race.id JOIN horse ON ch.horse_id = horse.id "
            + "WHERE race.id = ? AND ch.position IS NULL";

    private final String SET_RESULT_QUERY = "UPDATE contestant_horse SET "
            + "position = ? WHERE id = ?";

    @Override
    public List<ContestantHorse> findAllHorsesByRaceId(Integer raceId) {
        List<ContestantHorse> horses = new ArrayList<>();
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_HORSES_BY_RACE_ID_QUERY)) {
                statement.setInt(1, raceId);

                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    Integer id = rs.getInt(1);
                    String horseName = rs.getString(2);
                    Timestamp raceTime = rs.getTimestamp(3);
                    String racePlace = rs.getString(4);
                    Integer raceDistance = rs.getInt(5);
                    Integer position = rs.getInt(6);
                    Double coefficient = rs.getDouble(7);

                    ContestantHorse horse = new ContestantHorse(id, horseName, raceTime, racePlace, raceDistance, position, coefficient);

                    horses.add(horse);
                }
            }
        } catch (SQLException ex) {
        }
        return horses;
    }

    @Override
    public List<ContestantHorse> findAllHorsesWithoutResultByRaceId(Integer raceId) {
        List<ContestantHorse> horses = new ArrayList<>();
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_HORSES_WITHOUT_RESULT_BY_RACE_ID_QUERY)) {
                statement.setInt(1, raceId);

                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    Integer id = rs.getInt(1);
                    String horseName = rs.getString(2);
                    Timestamp raceTime = rs.getTimestamp(3);
                    String racePlace = rs.getString(4);
                    Integer raceDistance = rs.getInt(5);
                    Integer position = rs.getInt(6);
                    Double coefficient = rs.getDouble(7);

                    ContestantHorse horse = new ContestantHorse(id, horseName, raceTime, racePlace, raceDistance, position, coefficient);

                    horses.add(horse);
                }
            }
        } catch (SQLException ex) {
        }
        return horses;
    }

    @Override
    public boolean setResults(List<ContestantHorse> horsesInPositionOrder) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(SET_RESULT_QUERY)) {
                Iterator<ContestantHorse> iterator = horsesInPositionOrder.iterator();
                int currentPosition = 1;
                while (iterator.hasNext()) {
                    ContestantHorse next = iterator.next();
                    Integer contestantHorseId = next.getId();
                    Integer position = currentPosition++;
                    statement.setInt(1, position);
                    statement.setInt(2, contestantHorseId);
                    statement.addBatch();
                }
                statement.executeBatch();
                connection.commit();
                return true;
            } catch (SQLException ex) {
                connection.rollback();
            }
        } catch (SQLException ex) {
        }
        return false;
    }
}
