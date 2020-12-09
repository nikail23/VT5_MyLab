package horseraceapp.util.dao;

import java.util.List;
import horseraceapp.util.dao.entity.Race;

public interface RaceDao {
    List<Race> findAll();
    List<Race> findUnresultedRaces();
    Race findRaceById(Integer raceId);
    Integer getRaceIdByContestantHorseId(Integer contestantHorseId);
}
