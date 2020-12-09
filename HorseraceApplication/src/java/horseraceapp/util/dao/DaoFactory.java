package horseraceapp.util.dao;

public abstract class DaoFactory {
    public static enum DaoType {
        MySQL("ua.epam.horseraceapp.util.dao.mysql.MySqlDaoFactory"),;
        private final String pathToClass;
        
        DaoType(String pathToClass) {
            this.pathToClass = pathToClass;
        }
    }

    public static DaoFactory getInstance(DaoType type) {
        switch (type) {
            case MySQL:
                try {
                    Class<?> clazz = Class.forName(type.pathToClass);
                    return (DaoFactory) clazz.newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    return null;
                }
            default:
                return null;
        }
    }

    public abstract ContestantHorseDao createContestantHorseDao();
    public abstract RaceDao createRaceDao();
    public abstract BetDao createBetDao();
    public abstract UserDao createUserDao();
}
