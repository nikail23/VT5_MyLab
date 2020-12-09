package horseraceapp.controller.command;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.util.dao.BetDao;
import horseraceapp.util.dao.entity.Bet;

public class GetUnviewedBetsCommand extends AbstractCommand {
    static final String COMMAND = "unviewed_bets";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        List<Bet> unviewedBets = getAllUnviewedBets();
        request.setAttribute(UNVIEWED_BETS, unviewedBets);
        request.setAttribute(REQ_ATTRIBUTE, UNVIEWED_BETS);
        request.setAttribute(COM_ATTRIBUTE, COMMAND);

        return UNVIEWED_BETS_PAGE;
    }

    private List<Bet> getAllUnviewedBets() {
        BetDao betDao = factory.createBetDao();
        return betDao.findUnviewedBets();
    }
}
