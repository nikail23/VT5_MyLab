package horseraceapp.controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import horseraceapp.util.dao.UserDao;
import horseraceapp.util.dao.entity.User;

public class RechargeBalanceCommand extends AbstractCommand {
    static final String COMMAND = "recharge";
    static final Integer DEFAULT_RECHARGE_AMOUNT = 100;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        Integer userId = user.getId();
        Integer rechargeAmount = getRechargeAmount();
        boolean balanceRecharged = rechargeBalance(userId, rechargeAmount);
        if (balanceRecharged) {
            request.setAttribute(MESSAGE_ATTRIBUTE, MSG_BALANCE_SUCCESSFULLY_RECHARGED);
            resetUserInSession(session, userId);
        } else {
            request.setAttribute(ERROR_ATTRIBUTE, ERR_FAILED_RECHARGE_BALANCE);
        }
        return RECHARGE_PAGE;
    }

    private void resetUserInSession(HttpSession session, Integer userId) {
        User user = getUserById(userId);
        session.setAttribute(USER, user);
    }

    private User getUserById(Integer userId) {
        UserDao userDao = factory.createUserDao();
        return userDao.getUserById(userId);
    }

    private boolean rechargeBalance(Integer userId, Integer rechargeAmount) {
        UserDao userDao = factory.createUserDao();
        return userDao.rechargeBalance(userId, rechargeAmount);
    }

    private Integer getRechargeAmount() {
        return DEFAULT_RECHARGE_AMOUNT;
    }
}
