package horseraceapp.controller.command;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.util.dao.BetDao;
import horseraceapp.util.dao.entity.Bet;
import horseraceapp.util.dao.entity.User;

public class GetUserBetsCommand extends AbstractCommand {
    static final String COMMAND = "get_user_bets";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute(USER);
        List<Bet> userBets = getUserBetsById(user.getId());

        request.setAttribute(MY_BETS, userBets);
        request.setAttribute(REQ_ATTRIBUTE, MY_BETS);
        request.setAttribute(COM_ATTRIBUTE, COMMAND);

        return USER_BETS_PAGE;
    }

    private List<Bet> getUserBetsById(Integer userId) {
        BetDao betDao = factory.createBetDao();
        return betDao.findUserBets(userId);
    }
}
