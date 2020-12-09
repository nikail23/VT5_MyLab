package horseraceapp.controller.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import horseraceapp.util.dao.UserDao;
import horseraceapp.util.dao.entity.User;
import horseraceapp.util.dao.entity.UserType;

public class LogInCommand extends AbstractCommand {
    static final String COMMAND = "login";
    static final Map<UserType, String> mainPageMap = new HashMap<UserType, String>() {
        {
            put(UserType.USER, USER_MAIN_PAGE);
            put(UserType.ADMIN, ADMIN_MAIN_PAGE);
            put(UserType.BOOKMAKER, BOOKMAKER_MAIN_PAGE);
        }
    };

    private final Integer USER_SESSION_MAX_INACTIVE_INTERVAL = 60 * 60 * 2;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String email = getEmailFromRequest(request);
        if (nullOrEmpty(email)) {
            request.setAttribute(ERROR_ATTRIBUTE, ERR_EMPTY_EMAIL_POLE);
            return LOG_IN_PAGE;
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email);

        if (!matcher.matches()) {
            request.setAttribute(ERROR_ATTRIBUTE, ERR_INCORRECT_EMAIL);
            return LOG_IN_PAGE;
        }

        User user = getUserByEmail(email);

        if (user == null) {
            request.setAttribute(ERROR_ATTRIBUTE, ERR_USER_NOT_FOUND);
            return LOG_IN_PAGE;
        }

        String inputedPassword = getPasswordFromRequest(request);

        if (!Objects.equals(inputedPassword, user.getPassword())) {
            Logger log = Logger.getLogger(LogInCommand.class);
            log.info("Tried to log in as user with id " + user.getId() + " with non-matching password.");
            
            request.setAttribute(ERROR_ATTRIBUTE, ERR_INCORRECT_PASSWORD);
            return LOG_IN_PAGE;
        }

        HttpSession session = request.getSession();
        session.setAttribute(USER, user);
        session.setMaxInactiveInterval(USER_SESSION_MAX_INACTIVE_INTERVAL);

        String mainPage = mainPageMap.get(user.getType());
        return mainPage;

    }

    private String getPasswordFromRequest(HttpServletRequest request) {
        String password = request.getParameter(PASSWORD);
        if (password == null) {
            password = (String) request.getAttribute(PASSWORD);
        }
        return password;
    }

    private String getEmailFromRequest(HttpServletRequest request) {
        String email = request.getParameter(EMAIL);
        if (email == null) {
            email = (String) request.getAttribute(EMAIL);
        }
        return email;
    }

    private boolean nullOrEmpty(String string) {
        return (string == null) || ("".equals(string));
    }

    private User getUserByEmail(String email) {
        UserDao userDao = factory.createUserDao();
        return userDao.getUserByEmail(email);
    }
}
