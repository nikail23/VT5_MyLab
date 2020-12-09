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
import horseraceapp.util.dao.BetDao;
import horseraceapp.util.dao.DaoFactory;
import horseraceapp.util.dao.UserDao;
import horseraceapp.util.dao.entity.Bet;
import horseraceapp.util.dao.entity.BetState;
import horseraceapp.util.dao.entity.User;

public class MySqlBetDao implements BetDao {
    private final String SELECT_ALL_BETS_BY_USER_ID_QUERY = "SELECT bet.id, bet_state.state, "
            + "ch.coefficient, bet.amount, horse.name, race.place, "
            + "race.start_time, bet.place_time, ch.position FROM bet JOIN bet_state ON bet.state_id "
            + "= bet_state.id JOIN contestant_horse AS ch ON bet.contestant_horse_id "
            + "= ch.id JOIN horse ON ch.horse_id = "
            + "horse.id JOIN race ON ch.race_id = race.id JOIN "
            + "user ON bet.owner_id = user.id WHERE user.id = ? "
            + "ORDER BY race.start_time";

    private final String GET_UNVIEWED_BETS_QUERY = "SELECT bet.id, bs.state, "
            + "bet.owner_id, bet.amount, bet.place_time, ch.coefficient, "
            + "race.place, race.start_time, horse.name, ch.position FROM bet JOIN "
            + "bet_state AS bs ON bet.state_id = bs.id JOIN "
            + "contestant_horse AS ch ON bet.contestant_horse_id = ch.id "
            + "JOIN horse ON ch.horse_id = horse.id JOIN race ON ch.race_id = "
            + "race.id WHERE bs.state IN ('WAITING_FOR_ACCEPT', "
            + "'WON_WAITING_FOR_PAY') OR ((bs.state IN ('ACCEPTED')) AND "
            + "ch.position IS NOT NULL)";

    private final String CREATE_BET_QUERY = "INSERT INTO bet (state_id, "
            + "owner_id, amount, contestant_horse_id) VALUES ( (SELECT bet_state.id "
            + "FROM bet_state WHERE bet_state.state = ?), ?, ?, ?)";

    private final String ACCEPT_BET_BY_ID_QUERY = "UPDATE bet SET "
            + "bet.state_id = (SELECT bs.id FROM bet_state AS bs WHERE "
            + "state = 'ACCEPTED') WHERE bet.id = ? AND bet.state_id = "
            + "(SELECT bs.id FROM bet_state AS bs WHERE state = "
            + "'WAITING_FOR_ACCEPT')";

    private final String DECLINE_BET_BY_ID_QUERY = "UPDATE bet SET "
            + "bet.state_id = (SELECT bs.id FROM bet_state AS bs WHERE "
            + "state = 'DECLINED') WHERE bet.id = ? AND bet.state_id = "
            + "(SELECT bs.id FROM bet_state AS bs WHERE state = "
            + "'WAITING_FOR_ACCEPT')";

    private final String LOSE_BET_BY_ID_QUERY = "UPDATE bet SET "
            + "bet.state_id = (SELECT bs.id FROM bet_state AS bs WHERE "
            + "state = 'LOSE') WHERE bet.id = ? AND bet.state_id = "
            + "(SELECT bs.id FROM bet_state AS bs WHERE state = "
            + "'ACCEPTED') AND (SELECT ch.position FROM contestant_horse AS ch "
            + "WHERE ch.id = bet.contestant_horse_id) IS NOT NULL";

    private final String UPDATE_TO_WON_WAITING_FOR_PAY_BET_BY_ID_QUERY = "UPDATE bet SET "
            + "bet.state_id = (SELECT bs.id FROM bet_state AS bs WHERE "
            + "state = 'WON_WAITING_FOR_PAY') WHERE bet.id = ? AND bet.state_id = "
            + "(SELECT bs.id FROM bet_state AS bs WHERE state = "
            + "'ACCEPTED') AND (SELECT ch.position FROM contestant_horse AS ch "
            + "WHERE ch.id = bet.contestant_horse_id) IS NOT NULL";

    private final String WON_PAYED_BET_BY_ID_QUERY = "UPDATE bet SET "
            + "bet.state_id = (SELECT bs.id FROM bet_state AS bs WHERE "
            + "state = 'WON_PAYED') WHERE bet.id = ? AND bet.state_id = "
            + "(SELECT bs.id FROM bet_state AS bs WHERE state = "
            + "'WON_WAITING_FOR_PAY')";

    private final String GET_BET_ON_WIN_AMOUNT_BY_BET_ID_QUERY = "SELECT "
            + "floor(bet.amount * ch.coefficient) FROM bet JOIN "
            + "contestant_horse AS ch ON bet.contestant_horse_id = ch.id "
            + "WHERE bet.id = ?";

    private final String GET_BET_AMOUNT_BY_BET_ID = "SELECT amount FROM "
            + "bet WHERE id = ?";

    private final String GET_BET_OWNER_ID_BY_BET_ID_QUERY = "SELECT owner_id "
            + "FROM bet WHERE id = ?";

    private final String INCREASE_USER_BALANCE_QUERY = "UPDATE user SET "
            + "balance = balance + ? WHERE id = ?";

    private final String DECREASE_USER_BALANCE_QUERY = "UPDATE user SET "
            + "balance = balance - ? WHERE id = ?";

    @Override
    public List<Bet> findUserBets(Integer userId) {
        List<Bet> userBets = new ArrayList<>();
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BETS_BY_USER_ID_QUERY)) {
                statement.setInt(1, userId);

                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    Integer betId = rs.getInt(1);
                    User betOwner = getUserByUserId(userId);
                    BetState betState = BetState.valueOf(rs.getString(2));
                    Double coefficient = rs.getDouble(3);
                    Integer amount = rs.getInt(4);
                    String horseName = rs.getString(5);
                    String racePlace = rs.getString(6);
                    Timestamp raceTime = rs.getTimestamp(7);
                    Timestamp betPlaceTime = rs.getTimestamp(8);
                    Integer horsePosition = rs.getInt(9);
                    horsePosition = horsePosition == 0 ? null : horsePosition;

                    Bet bet = new Bet(betId, betOwner, betState, horseName, coefficient, amount, racePlace, raceTime, betPlaceTime, horsePosition);

                    userBets.add(bet);
                }
            }
        } catch (SQLException ex) {
        }
        return userBets;
    }

    @Override
    public List<Bet> findUnviewedBets() {
        List<Bet> unviewedBets = new ArrayList<>();
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();
        try (Connection connection = connectionManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(GET_UNVIEWED_BETS_QUERY);

                while (rs.next()) {
                    Integer id = rs.getInt(1);
                    BetState state = BetState.valueOf(rs.getString(2));
                    User betOwner = getUserByUserId(rs.getInt(3));
                    Integer amount = rs.getInt(4);
                    Timestamp betPlaceTime = rs.getTimestamp(5);
                    Double coefficient = rs.getDouble(6);
                    String racePlace = rs.getString(7);
                    Timestamp raceTime = rs.getTimestamp(8);
                    String horseName = rs.getString(9);
                    Integer position = rs.getInt(10);
                    position = position == 0 ? null : position;

                    Bet bet = new Bet(id, betOwner, state, horseName,
                            coefficient, amount, racePlace, raceTime, betPlaceTime, position);

                    unviewedBets.add(bet);
                }
            }
        } catch (SQLException ex) {
        }
        return unviewedBets;
    }

    @Override
    public boolean makeBet(Integer userId, Integer amount, Integer contestantHorseId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        if (!checkUserBalance(userId, amount)) {
            return false;
        }

        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement createBetStatement = connection.prepareStatement(CREATE_BET_QUERY);
                    PreparedStatement changeUserBalanceStatement = connection.prepareStatement(DECREASE_USER_BALANCE_QUERY)) {

                changeUserBalanceStatement.setInt(1, amount);
                changeUserBalanceStatement.setInt(2, userId);

                createBetStatement.setString(1, BetState.WAITING_FOR_ACCEPT.name());
                createBetStatement.setInt(2, userId);
                createBetStatement.setInt(3, amount);
                createBetStatement.setInt(4, contestantHorseId);

                int userBalanceChangeResult = changeUserBalanceStatement.executeUpdate();
                int createBetResult = createBetStatement.executeUpdate();

                if ((createBetResult > 0) && (userBalanceChangeResult > 0)) {
                    connection.commit();
                    return true;
                }
            } catch (SQLException ex) {
            }
            connection.rollback();
        } catch (SQLException ex) {
        }
        return false;
    }

    @Override
    public boolean acceptBet(Integer betId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(ACCEPT_BET_BY_ID_QUERY)) {
                statement.setInt(1, betId);
                statement.executeUpdate();
                connection.commit();
                return true;
            } catch (SQLException ex) {
                connection.rollback();
            }
        } catch (SQLException ex) {
        }
        return false;
    }

    @Override
    public boolean declineBet(Integer betId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement declineBetStatement = connection.prepareStatement(DECLINE_BET_BY_ID_QUERY);
                    PreparedStatement increaseUserBalanceStatement = connection.prepareStatement(INCREASE_USER_BALANCE_QUERY)) {

                declineBetStatement.setInt(1, betId);

                Integer betAmount = getBetAmount(betId);
                Integer betOwnerId = getBetOwnerId(betId);
                increaseUserBalanceStatement.setInt(1, betAmount);
                increaseUserBalanceStatement.setInt(2, betOwnerId);

                declineBetStatement.executeUpdate();
                increaseUserBalanceStatement.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException ex) {
                connection.rollback();
            }
        } catch (SQLException ex) {
        }
        return false;
    }

    @Override
    public boolean loseBet(Integer betId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(LOSE_BET_BY_ID_QUERY)) {
                statement.setInt(1, betId);
                int updateResult = statement.executeUpdate();
                if (updateResult != 0) {
                    connection.commit();
                    return true;
                }
            } catch (SQLException ex) {
            }
            connection.rollback();
        } catch (SQLException ex) {
        }
        return false;
    }

    @Override
    public boolean waitForPayBet(Integer betId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_TO_WON_WAITING_FOR_PAY_BET_BY_ID_QUERY)) {
                statement.setInt(1, betId);
                int updateResult = statement.executeUpdate();
                if (updateResult != 0) {
                    connection.commit();
                    return true;
                }
            } catch (SQLException ex) {
            }
            connection.rollback();
        } catch (SQLException ex) {
        }
        return false;
    }

    @Override
    public boolean payBet(Integer betId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement changeBetStateStatement = connection.prepareStatement(WON_PAYED_BET_BY_ID_QUERY);
                    PreparedStatement changeUserBalanceStatement = connection.prepareStatement(INCREASE_USER_BALANCE_QUERY)) {

                changeBetStateStatement.setInt(1, betId);

                Integer betOnWinAmount = getBetOnWinAmount(betId);
                Integer betOwnerId = getBetOwnerId(betId);
                changeUserBalanceStatement.setInt(1, betOnWinAmount);
                changeUserBalanceStatement.setInt(2, betOwnerId);

                int betStateChangeResult = changeBetStateStatement.executeUpdate();
                int userBalanceChangeResult = changeUserBalanceStatement.executeUpdate();

                if ((betStateChangeResult > 0) && (userBalanceChangeResult > 0)) {
                    connection.commit();
                    return true;
                }
            } catch (SQLException ex) {
            }
            connection.rollback();
        } catch (SQLException ex) {
        }
        return false;
    }

    @Override
    public Integer getBetOnWinAmount(Integer betId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(GET_BET_ON_WIN_AMOUNT_BY_BET_ID_QUERY)) {
                statement.setInt(1, betId);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    Integer betOnWinAmount = rs.getInt(1);
                    return betOnWinAmount;
                }
            }
        } catch (SQLException ex) {
        }
        return null;
    }

    @Override
    public Integer getBetAmount(Integer betId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(GET_BET_AMOUNT_BY_BET_ID)) {
                statement.setInt(1, betId);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    Integer betAmount = rs.getInt(1);
                    return betAmount;
                }
            }
        } catch (SQLException ex) {
        }
        return null;
    }

    @Override
    public Integer getBetOwnerId(Integer betId) {
        ConnectionManager connectionManager = MySqlConnectionManager.getInstance();

        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(GET_BET_OWNER_ID_BY_BET_ID_QUERY)) {
                statement.setInt(1, betId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    Integer userId = rs.getInt(1);
                    return userId;
                }
            }
        } catch (SQLException ex) {
        }
        return null;
    }

    private User getUserByUserId(Integer userId) {
        DaoFactory factory = DaoFactory.getInstance(DaoFactory.DaoType.MySQL);
        UserDao userDao = factory.createUserDao();
        User user = userDao.getUserById(userId);
        return user;
    }

    private boolean checkUserBalance(Integer userId, Integer sum) {
        DaoFactory factory = DaoFactory.getInstance(DaoFactory.DaoType.MySQL);
        UserDao userDao = factory.createUserDao();
        Integer userBalance = userDao.getUserBalance(userId);
        if (userBalance == null) {
            return false;
        }
        return userBalance >= sum;
    }
}
