package horseraceapp.controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import horseraceapp.util.dao.BetDao;
import horseraceapp.util.dao.RaceDao;
import horseraceapp.util.dao.UserDao;
import horseraceapp.util.dao.entity.User;

public class MakeBetCommand extends AbstractCommand {
    static final String COMMAND = "make_bet";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        Integer contestantHorseId = Integer.valueOf(request.getParameter(CONTESTANT_HORSE_ID));

        Integer betAmount = 0;
        try {
            betAmount = Integer.valueOf(request.getParameter(AMOUNT));
        } catch (NumberFormatException ex) {
            return setErrorAndReloadPage(request, response, ERR_EMPTY_BET_AMOUNT_FIELD, contestantHorseId);
        }
        Integer userBalance = user.getBalance();

        if (betAmount <= 0) {
            return setErrorAndReloadPage(request, response, ERR_NONPOSITIVE_BET_AMOUNT, contestantHorseId);
        } else if (betAmount > userBalance) {
            return setErrorAndReloadPage(request, response, ERR_BET_AMOUNT_GREATER_THEN_USER_BALANCE, contestantHorseId);
        }

        boolean betDone = makeBet(user.getId(), betAmount, contestantHorseId);

        if (betDone) {
            resetUserInSession(session, user.getId());
            return getCommand(GetUserBetsCommand.COMMAND).execute(request, response);
        } else {
            return setErrorAndReloadPage(request, response, ERR_CANT_MAKE_BET, contestantHorseId);
        }
    }

    private boolean makeBet(Integer userId, Integer betAmount, Integer contestantHorseId) {
        BetDao betDao = factory.createBetDao();
        return betDao.makeBet(userId, betAmount, contestantHorseId);
    }

    private User getUserById(Integer userId) {
        UserDao userDao = factory.createUserDao();
        return userDao.getUserById(userId);
    }

    private void resetUserInSession(HttpSession session, Integer userId) {
        User user = getUserById(userId);
        session.setAttribute(USER, user);
    }

    private String setErrorAndReloadPage(HttpServletRequest request, HttpServletResponse response,
            String error, Integer contestantHorseId) {
        request.setAttribute(ERROR_ATTRIBUTE, error);
        RaceDao raceDao = factory.createRaceDao();
        Integer raceId = raceDao.getRaceIdByContestantHorseId(contestantHorseId);
        request.setAttribute(RACE_ID, raceId);
        return getCommand(GetRaceInfoCommand.COMMAND).execute(request, response);
    }
}
