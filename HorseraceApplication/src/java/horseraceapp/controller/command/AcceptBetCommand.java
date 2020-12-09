package horseraceapp.controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.util.dao.BetDao;

public class AcceptBetCommand extends AbstractCommand {
    static final String COMMAND = "accept_bet";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Integer betId = Integer.valueOf(request.getParameter(BET_ID));
        boolean betAccepted = acceptBet(betId);
        if (betAccepted) {
            request.setAttribute(MESSAGE_ATTRIBUTE, MSG_BET_SUCCESSFULLY_ACCEPTED);
        } else {
            request.setAttribute(ERROR_ATTRIBUTE, ERR_FAILED_ACCEPT_STAKE);
        }
        return getCommand(GetUnviewedBetsCommand.COMMAND).execute(request, response);
    }

    private boolean acceptBet(Integer betId) {
        BetDao betDao = factory.createBetDao();
        return betDao.acceptBet(betId);
    }
}
