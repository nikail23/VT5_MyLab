package horseraceapp.util.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import horseraceapp.util.connection.ConnectionManager;
import horseraceapp.util.connection.MySqlConnectionManager;
import horseraceapp.util.dao.RaceDao;
import horseraceapp.util.dao.entity.ContestantHorse;
import horseraceapp.util.dao.entity.Race;

public class MySqlRaceDao implements RaceDao {
    private final String SELECT_ALL_QUERY = "SELECT * FROM race";
    private final String SELECT_ALL_UNRESULTED_RACES_QUERY = "SELECT DISTINCT "
            + "race.* FROM race JOIN contestant_horse AS ch ON ch.race_id = "
            + "race.id WHERE ch.position IS NULL";
    private final String SELECT_RACE_BY_ID_QUERY = "SELECT * FROM race WHERE id = ?";
    private final String SELECT_RACE_ID_BY_CONTESTANT_HORSE_ID_QUERY = "SELECT "
            + "race_id FROM contestant_horse WHERE id = ?";

    @Override
    public List<Race> findAll() {
        List<Race> allRaces = new ArrayList<>();
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            try (Statement query = connection.createStatement()) {
                ResultSet rs = query.executeQuery(SELECT_ALL_QUERY);

                while (rs.next()) {
                    allRaces.add(new Race(rs.getInt(1), rs.getTimestamp(2), rs.getString(3), rs.getInt(4)));
                }
            }
        } catch (SQLException ex) {
        }
        return allRaces;
    }

    @Override
    public List<Race> findUnresultedRaces() {
        List<Race> unresultedRaces = new ArrayList<>();
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();
        try (Connection connection = connectionManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(SELECT_ALL_UNRESULTED_RACES_QUERY);

                while (rs.next()) {
                    Integer raceId = rs.getInt(1);
                    Timestamp raceTime = rs.getTimestamp(2);
                    String racePlace = rs.getString(3);
                    Integer raceDistance = rs.getInt(4);

                    Race race = new Race(raceId, raceTime, racePlace, raceDistance);

                    unresultedRaces.add(race);
                }
            }
        } catch (SQLException ex) {
        }
        return unresultedRaces;
    }

    @Override
    public Race findRaceById(Integer raceId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_RACE_BY_ID_QUERY)) {
                statement.setInt(1, raceId);

                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    Timestamp raceTime = rs.getTimestamp(2);
                    String racePlace = rs.getString(3);
                    Integer raceDistance = rs.getInt(4);

                    Race race = new Race(raceId, raceTime, racePlace, raceDistance);

                    return race;
                }
            }
        } catch (SQLException ex) {
        }
        return null;
    }

    @Override
    public Integer getRaceIdByContestantHorseId(Integer contestantHorseId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_RACE_ID_BY_CONTESTANT_HORSE_ID_QUERY)) {
                statement.setInt(1, contestantHorseId);

                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    Integer raceId = rs.getInt(1);
                    return raceId;
                }
            }
        } catch (SQLException ex) {
        }
        return null;
    }
}
