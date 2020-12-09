package horseraceapp.util.dao.entity;

public enum BetState {
    WON_PAYED,
    WON_WAITING_FOR_PAY,
    LOSE,
    ACCEPTED,
    WAITING_FOR_ACCEPT,;

    public String toBundleString() {
        return name().toLowerCase().replaceAll("_", ".");
    }
}
