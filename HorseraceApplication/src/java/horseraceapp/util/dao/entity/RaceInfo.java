package horseraceapp.util.dao.entity;

import java.util.List;

public class RaceInfo {
    private Race race;
    private List<ContestantHorse> horses;

    public RaceInfo() {
    }

    public RaceInfo(Race race, List<ContestantHorse> horses) {
        this.race = race;
        this.horses = horses;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public List<ContestantHorse> getHorses() {
        return horses;
    }

    public void setHorses(List<ContestantHorse> horses) {
        this.horses = horses;
    }
}
