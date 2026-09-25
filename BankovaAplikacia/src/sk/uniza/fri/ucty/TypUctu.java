package sk.uniza.fri.ucty;

/**
 * Enum trieda, ktorá definuje typy účtov
 */
public enum TypUctu {
    /**
     * Zakladny typ uctu.
     */
    ZAKLADNY,
    /**
     * Podnikatelsky typ uctu.
     */
    PODNIKATELSKY,
    /**
     * Studentsky typ uctu.
     */
    STUDENTSKY,
    /**
     * Sporiaci typ uctu.
     */
    SPORIACI;

    /**
     * Vráti nám názov podľa typu účtu
     *
     * @param typUctu typ účtu
     * @return názov
     */
    public String getNazov(TypUctu typUctu) {
        switch (typUctu) {
            case ZAKLADNY -> {
                return "Základný";
            }
            case PODNIKATELSKY -> {
                return "Podnikateľský";
            }
            case STUDENTSKY -> {
                return "Študentský";
            }
            case SPORIACI -> {
                return "Šporiaci";
            }
        }
        return null;
    }
}
