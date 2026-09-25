package sk.uniza.fri.ucty;

import sk.uniza.fri.IBAN;
import sk.uniza.fri.Peniaze;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

/**
 * Abstraktná trieda Ucet, reprezentuje účet
 */
public abstract class Ucet {
    private final IBAN iban;
    private Peniaze zostatok;
    private final ArrayList<String> pohyby = new ArrayList<>();

    /**
     * IParametrický konštruktor triedy Ucet
     *
     * @param iban     iban účtu
     * @param zostatok počiatočný zostatok
     */
    public Ucet(IBAN iban, String zostatok) {
        this.iban = iban;
        this.setZostatok(zostatok);
    }

    /**
     * Abstraktná metóda odober peniaze, ktorú prepisujú potomkovia triedy Ucet
     *
     * @param hodnota obnos peňazí, ktorý chceme odobrať
     */
    public abstract void odoberPeniaze(String hodnota);

    /**
     * Abstraktná metóda vypocitajVyskuUroku, ktorú implementujú potomkovia
     *
     * @return vrátime výšku úroku v tvare Peniaze
     */
    protected abstract Peniaze vypocitajVyskuUroku();

    /**
     * Abstraktná metódy, implementujú ju potomkovia
     */
    public abstract void pripocitajUrok();

    /**
     * Abstraktná metódy, implementujú ju potomkovia
     *
     * @return popis účtu
     */
    public abstract String dajPopis();

    public IBAN getIBAN() {
        return this.iban;
    }
    public String getZakladneCisloUctu() {
        return this.iban.getZakladneCisloUctu();
    }
    public Peniaze getZostatok() {
        return this.zostatok;
    }
    public ArrayList<String> getPohyby() {
        return this.pohyby;
    }

    /**
     * Metódy nastaví zostatok účtu na novú hodnotu
     *
     * @param hodnota hodnota nového zostatku
     */
    public void setZostatok(String hodnota) {
        this.zostatok = new Peniaze(hodnota);
    }

    /**
     * Pridá peniaze útu
     *
     * @param hodnota množstvo, ktoré chceme prirátať
     */
    public void pridajPeniaze(String hodnota) {
        this.zostatok.scitaj(hodnota);
        BigDecimal hodnotaBD = new BigDecimal(hodnota);
        BigDecimal hodnotaDveMiesta = hodnotaBD.setScale(2, RoundingMode.HALF_DOWN);

        this.pridajPohyb("+" + hodnotaDveMiesta);
    }

    private String dajDatum() {
        LocalDate datumTeraz = LocalDate.now();
        Date datum = Date.from(datumTeraz.atStartOfDay(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat formatovac = new SimpleDateFormat("dd/MM/yyyy");

        return formatovac.format(datum);
    }

    /**
     * Pridá pohyb to zoznamu pohybov
     *
     * @param hodnota reťazec na pridanie
     */
    public void pridajPohyb(String hodnota) {
        this.pohyby.add(this.dajDatum() + "   " + hodnota);
    }

    /**
     * Vráti príjmy vo forme stringu
     *
     * @return príjmy účtu
     */
    public String dajPrijmy() {
        StringBuilder sb = new StringBuilder();

        for (String platba : this.pohyby) {
            if (platba.contains("+")) {
                sb.append(platba).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Vráti výdavky vo forme stringu
     *
     * @return string výdavkov
     */
    public String dajVydavky() {
        StringBuilder sb = new StringBuilder();

        for (String platba : this.pohyby) {
            if (platba.contains("-")) {
                sb.append(platba).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Vráti zostatok účtu v tvare stringu
     *
     * @return zostatok na účte
     */
    public String dajZostatok() {
        return String.format("%.2f %s", this.zostatok.getMnozstvo(), this.zostatok.getMena());
    }

    @Override
    public String toString() {
        return this.iban + " - " + this.dajZostatok();
    }
}
