package horseraceapp.util.dao.mysql;

import horseraceapp.util.dao.BetDao;
import horseraceapp.util.dao.ContestantHorseDao;
import horseraceapp.util.dao.DaoFactory;
import horseraceapp.util.dao.RaceDao;
import horseraceapp.util.dao.UserDao;

public class MySqlDaoFactory extends DaoFactory {

    @Override
    public ContestantHorseDao createContestantHorseDao() {
        return new MySqlContestantHorseDao();
    }

    @Override
    public RaceDao createRaceDao() {
        return new MySqlRaceDao();
    }

    @Override
    public BetDao createBetDao() {
        return new MySqlBetDao();
    }

    @Override
    public UserDao createUserDao() {
        return new MySqlUserDao();
    }
}
