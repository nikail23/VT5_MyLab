package horseraceapp.util.dao;

import java.util.List;
import horseraceapp.util.dao.entity.Bet;

public interface BetDao {
    List<Bet> findUserBets(Integer userId);
    List<Bet> findUnviewedBets();

    boolean makeBet(Integer userId, Integer amount, Integer contestantHorseId);
    boolean acceptBet(Integer betId);
    boolean declineBet(Integer betId);
    boolean loseBet(Integer betId);
    boolean waitForPayBet(Integer betId);
    boolean payBet(Integer betId);
    Integer getBetOnWinAmount(Integer betId);
    Integer getBetAmount(Integer betId);
    Integer getBetOwnerId(Integer betId);
}
