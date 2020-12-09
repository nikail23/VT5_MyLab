package horseraceapp.controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogOutCommand extends AbstractCommand {
    static final String COMMAND = "logout";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute(USER);
        return SITE_HOME_PAGE;
    }
}
