package horseraceapp.controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.util.dao.BetDao;

public class DeclineBetCommand extends AbstractCommand {
    static final String COMMAND = "decline_bet";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Integer betId = Integer.valueOf(request.getParameter(BET_ID));
        boolean betDeclined = declineBet(betId);
        if (betDeclined) {
            request.setAttribute(MESSAGE_ATTRIBUTE, MSG_BET_SUCCESSFULLY_DECLINED);
        } else {
            request.setAttribute(ERROR_ATTRIBUTE, ERR_FAILED_DECLINE_STAKE);
        }
        return getCommand(GetUnviewedBetsCommand.COMMAND).execute(request, response);
    }

    private boolean declineBet(Integer betId) {
        BetDao betDao = factory.createBetDao();
        return betDao.declineBet(betId);
    }
}
