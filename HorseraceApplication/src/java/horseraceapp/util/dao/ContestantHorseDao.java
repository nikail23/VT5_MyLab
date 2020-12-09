package horseraceapp.util.dao;

import java.util.List;
import horseraceapp.util.dao.entity.ContestantHorse;

public interface ContestantHorseDao {
    List<ContestantHorse> findAllHorsesByRaceId(Integer raceId);
    List<ContestantHorse> findAllHorsesWithoutResultByRaceId(Integer raceId);
    boolean setResults(List<ContestantHorse> horsesInPositionOrder);
}
