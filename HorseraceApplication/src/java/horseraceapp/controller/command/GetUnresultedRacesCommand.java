package horseraceapp.controller.command;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.util.dao.RaceDao;
import horseraceapp.util.dao.entity.Race;

public class GetUnresultedRacesCommand extends AbstractCommand {
    static final String COMMAND = "unresulted_races";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        List<Race> unresultedRaces = getAllUnresultedRaces();
        request.setAttribute(ERROR_ATTRIBUTE, request.getAttribute(ERROR_ATTRIBUTE));
        request.setAttribute(UNRESULTED_RACES, unresultedRaces);
        request.setAttribute(REQ_ATTRIBUTE, UNRESULTED_RACES);
        request.setAttribute(COM_ATTRIBUTE, COMMAND);
        return UNRESULTED_RACES_PAGE;
    }

    private List<Race> getAllUnresultedRaces() {
        RaceDao raceDao = factory.createRaceDao();
        return raceDao.findUnresultedRaces();
    }
}
