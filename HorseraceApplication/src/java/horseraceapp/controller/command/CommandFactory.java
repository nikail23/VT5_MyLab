package horseraceapp.controller.command;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class CommandFactory {
    private static final CommandFactory instance = new CommandFactory();
    private final Map<String, AbstractCommand> commandsMap;

    private CommandFactory() {
        commandsMap = new HashMap<>();
        commandsMap.put(AcceptBetCommand.COMMAND, new AcceptBetCommand());
        commandsMap.put(CreateResultCommand.COMMAND, new CreateResultCommand());
        commandsMap.put(DeclineBetCommand.COMMAND, new DeclineBetCommand());
        commandsMap.put(DetermineBetResultCommand.COMMAND, new DetermineBetResultCommand());
        commandsMap.put(GetAllRacesCommand.COMMAND, new GetAllRacesCommand());
        commandsMap.put(GetRaceInfoCommand.COMMAND, new GetRaceInfoCommand());
        commandsMap.put(GetUnresultedRacesCommand.COMMAND, new GetUnresultedRacesCommand());
        commandsMap.put(GetUnviewedBetsCommand.COMMAND, new GetUnviewedBetsCommand());
        commandsMap.put(GetUserBetsCommand.COMMAND, new GetUserBetsCommand());
        commandsMap.put(LogInCommand.COMMAND, new LogInCommand());
        commandsMap.put(LogOutCommand.COMMAND, new LogOutCommand());
        commandsMap.put(MakeBetCommand.COMMAND, new MakeBetCommand());
        commandsMap.put(PayBetCommand.COMMAND, new PayBetCommand());
        commandsMap.put(RechargeBalanceCommand.COMMAND, new RechargeBalanceCommand());
        commandsMap.put(RegisterCommand.COMMAND, new RegisterCommand());
    }
    
    public static CommandFactory getInstance() {
        return instance;
    }

    public AbstractCommand getCommand(String commandName) {
        AbstractCommand command = commandsMap.get(commandName);
        if (command == null) {
            Logger log = Logger.getLogger(CommandFactory.class);
            log.warn("Unknown command string was given: " + commandName);
            
            return new AbstractCommand() {

                @Override
                public String execute(HttpServletRequest request, HttpServletResponse response) {
                    return "index.jsp";
                }
            };
        } else {
            return command;
        }
    }
}
