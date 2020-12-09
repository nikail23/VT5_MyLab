package horseraceapp.controller.command;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.util.dao.RaceDao;
import horseraceapp.util.dao.entity.Race;

public class GetAllRacesCommand extends AbstractCommand {
    static final String COMMAND = "get_races";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        List<Race> allRaces = getAllRaces();
        request.setAttribute(RACES, allRaces);
        request.setAttribute(REQ_ATTRIBUTE, RACES);
        request.setAttribute(COM_ATTRIBUTE, COMMAND);
        return RACES_PAGE;
    }

    private List<Race> getAllRaces() {
        RaceDao raceDao = factory.createRaceDao();
        return raceDao.findAll();
    }
}
