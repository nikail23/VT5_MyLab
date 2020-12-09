package horseraceapp.controller.command;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import horseraceapp.util.dao.ContestantHorseDao;
import horseraceapp.util.dao.entity.ContestantHorse;

public class CreateResultCommand extends AbstractCommand {

    static final String COMMAND = "create_result";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Integer raceId = Integer.valueOf(request.getParameter(RACE_ID));
        List<ContestantHorse> unresultedHorses = getAllHorsesWithoutResultInRace(raceId);
        if (unresultedHorses.isEmpty()) {
            request.setAttribute(ERROR_ATTRIBUTE, ERR_CREATING_RESULT_FOR_RACE_WITH_RESULT);
            return getCommand(GetUnresultedRacesCommand.COMMAND).execute(request, response);
        }
        List<ContestantHorse> resultedHorses = createResults(unresultedHorses);
        boolean resultsSeted = setResults(resultedHorses);
        if (resultsSeted) {
            request.setAttribute(RACE_ID, raceId);
            return getCommand(GetRaceInfoCommand.COMMAND).execute(request, response);
        } else {
            request.setAttribute(ERROR_ATTRIBUTE, ERR_CREATING_RESULT);
            return getCommand(GetUnresultedRacesCommand.COMMAND).execute(request, response);
        }
    }

    private List<ContestantHorse> getAllHorsesWithoutResultInRace(Integer raceId) {
        ContestantHorseDao contestantHorseDao = factory.createContestantHorseDao();
        return contestantHorseDao.findAllHorsesWithoutResultByRaceId(raceId);
    }

    private List<ContestantHorse> createResults(List<ContestantHorse> horses) {
        Collections.shuffle(horses);
        return horses;
    }

    private boolean setResults(List<ContestantHorse> resultedHorses) {
        ContestantHorseDao contestantHorseDao = factory.createContestantHorseDao();
        return contestantHorseDao.setResults(resultedHorses);
    }
}
