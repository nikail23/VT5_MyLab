package horseraceapp.controller.command;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.util.dao.ContestantHorseDao;
import horseraceapp.util.dao.RaceDao;
import horseraceapp.util.dao.entity.ContestantHorse;
import horseraceapp.util.dao.entity.Race;
import horseraceapp.util.dao.entity.RaceInfo;

public class GetRaceInfoCommand extends AbstractCommand {
    static final String COMMAND = "race_info";
    private static Integer lastRaceId = null;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Integer raceId = getRaceId(request);
        List<ContestantHorse> horsesInRace = getHorsesInRace(raceId);
        Race race = getRaceById(raceId);
        RaceInfo raceInfo = new RaceInfo(race, horsesInRace);
        request.setAttribute(RACE_INFO, raceInfo);
        request.setAttribute(REQ_ATTRIBUTE, RACE_INFO);
        request.setAttribute(COM_ATTRIBUTE, COMMAND);

        return RACE_INFO_PAGE;
    }

    private Integer getRaceId(HttpServletRequest request) {
        Integer raceId = null;
        try {
            //If got from JSP
            raceId = Integer.valueOf(request.getParameter(RACE_ID));
        } catch (NumberFormatException ex) {
            //If got from other command
            raceId = (Integer) request.getAttribute(RACE_ID);
        }

        if (raceId == null) {
            //If can't get race identificator - than got it from 'changing language'
            raceId = lastRaceId;
        } else {
            //cache race identificator
            lastRaceId = raceId;
        }
        return raceId;
    }

    private List<ContestantHorse> getHorsesInRace(Integer raceId) {
        ContestantHorseDao contestantHorseDao = factory.createContestantHorseDao();
        return contestantHorseDao.findAllHorsesByRaceId(raceId);
    }

    private Race getRaceById(Integer raceId) {
        RaceDao raceDao = factory.createRaceDao();
        return raceDao.findRaceById(raceId);
    }
}
