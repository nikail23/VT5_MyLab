package horseraceapp.controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.util.dao.BetDao;

public class PayBetCommand extends AbstractCommand {

    static final String COMMAND = "pay_bet";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Integer betId = Integer.valueOf(request.getParameter(BET_ID));
        boolean betPayed = payBet(betId);
        if (betPayed) {
            request.setAttribute(MESSAGE_ATTRIBUTE, MSG_BET_SUCCESSFULLY_PAID);
        } else {
            request.setAttribute(ERROR_ATTRIBUTE, ERR_FAILED_PAYED_BET);
        }
        return getCommand(GetUnviewedBetsCommand.COMMAND).execute(request, response);
    }

    private boolean payBet(Integer betId) {
        BetDao betDao = factory.createBetDao();
        return betDao.payBet(betId);
    }
}
