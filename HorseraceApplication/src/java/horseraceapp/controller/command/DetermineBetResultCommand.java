package horseraceapp.controller.command;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.util.dao.BetDao;

public class DetermineBetResultCommand extends AbstractCommand {
    static final String COMMAND = "determine_bet_result";
    static final Integer WINNER_POSITION = 1;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Integer betId = Integer.valueOf(request.getParameter(BET_ID));
        Integer position = Integer.valueOf(request.getParameter(POSITION));
        boolean betResultDetermined = determineBetResult(betId, position);
        if (betResultDetermined) {
            request.setAttribute(MESSAGE_ATTRIBUTE, MSG_BET_RESULT_DETERMINED_SUCCESSFULLY);
        } else {
            request.setAttribute(ERROR_ATTRIBUTE, ERR_BET_RESULT_DETERMINATION_FAILED);
        }
        return getCommand(GetUnviewedBetsCommand.COMMAND).execute(request, response);
    }

    boolean determineBetResult(Integer betId, Integer position) {
        BetDao betDao = factory.createBetDao();
        if (Objects.equals(position, WINNER_POSITION)) {
            return betDao.waitForPayBet(betId);
        } else {
            return betDao.loseBet(betId);
        }
    }

}
