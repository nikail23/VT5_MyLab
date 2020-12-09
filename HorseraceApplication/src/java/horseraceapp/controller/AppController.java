package horseraceapp.controller;

import java.io.File;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.controller.command.AbstractCommand;
import horseraceapp.controller.command.CommandFactory;

public class AppController extends HttpServlet {
    private final static String ENCODING = "UTF-8"; 
    private static final String COMMAND_PARAMETER = "command";
    private final String LOG4J_INIT_FILE_PARAMETER = "log4j-init-file";
    private final CommandFactory factory;
    
    public AppController() {
        factory = CommandFactory.getInstance();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding(ENCODING);
        response.setContentType("text/html;charset=UTF-8");
        String commandString = request.getParameter(COMMAND_PARAMETER);
        
        AbstractCommand command = factory.getCommand(commandString);
        String forward = command.execute(request, response);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(forward);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    @Override
    public void init() throws ServletException {
        String realPath = getServletContext().getRealPath(getInitParameter(LOG4J_INIT_FILE_PARAMETER));
        File log4jInitFile = new File(realPath);
        if(log4jInitFile.exists()) {
            org.apache.log4j.PropertyConfigurator.configure(realPath);
        } else {
            org.apache.log4j.BasicConfigurator.configure();
        }
    }
}
