package sk.uniza.fri.ucty;

import sk.uniza.fri.IBAN;
import sk.uniza.fri.Peniaze;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Šporiaci účet, ktorý má úrokovú sadzbu generovanú bakou, úroková sadzba nemôže byť vyššia ako 5 percent
 * Pri vyberaní peňazí z účtu, a presiahnutí minimálneho zostatku sa nám zaúčtuje poplatok 10 EUR
 */
public class Sporiaci extends Ucet {

    private final TypUctu typUctu;
    private double urokovaSadzba;
    private final BigDecimal minimalnyZostatok;

    /**
     * Parametrický konštruktor triedy Sporiaci
     *
     * @param iban          iban účtu
     * @param zostatok      počiatočný zostatok
     * @param urokovaSadzba úroková sadzba generovaná bankou
     */
    public Sporiaci(IBAN iban, String zostatok, double urokovaSadzba) {
        super(iban, zostatok);
        this.setUrokovaSadzba(urokovaSadzba);
        this.typUctu = TypUctu.SPORIACI;
        this.minimalnyZostatok = new BigDecimal("500");
    }

    private String getTypUctu() {
        return this.typUctu.getNazov(this.typUctu);
    }

    private void setUrokovaSadzba(double urokovaSadzba) {
        if (urokovaSadzba >= 0 && urokovaSadzba <= 0.05) {
            this.urokovaSadzba = urokovaSadzba;
        }
    }

    /**
     * Odoberie peniaze z účtu, v prípade, ze presiahneme limit, penalizuje nás v podobe poplatku vo výške 10 EUR
     * @param hodnota obnos peňazí, ktorý chceme odobrať
     */
    @Override
    public void odoberPeniaze(String hodnota) {
        BigDecimal aktualnyZostatokBD = this.getZostatok().getMnozstvo();
        BigDecimal hodnotaBD = new BigDecimal(hodnota);

        BigDecimal zostatokPoOdobratiBD = aktualnyZostatokBD.subtract(hodnotaBD);

        if (hodnotaBD.compareTo(BigDecimal.ZERO) > 0 && zostatokPoOdobratiBD.compareTo(BigDecimal.ZERO) > 0) {
            this.setZostatok(zostatokPoOdobratiBD.toString());
            BigDecimal hodnotaDveMiesta = hodnotaBD.setScale(2, RoundingMode.HALF_DOWN);
            this.pridajPohyb("-" + hodnotaDveMiesta);
            if (zostatokPoOdobratiBD.compareTo(this.minimalnyZostatok) < 0) {
                this.setZostatok(zostatokPoOdobratiBD.subtract(new BigDecimal("10")).toPlainString());
            }
        }
    }

    /**
     * Vypočíta výšku mesačného úroku, ktorý následne vráti ako inštanciu triedy Peniaze
     * @return výška mesačného úroku
     */
    @Override
    protected Peniaze vypocitajVyskuUroku() {
        BigDecimal mesacnyUrok = BigDecimal.valueOf(this.urokovaSadzba).divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
        BigDecimal aktualnyZostatok = new BigDecimal(this.getZostatok().dajMnozstvoString());
        BigDecimal urok = aktualnyZostatok.multiply(mesacnyUrok);

        return new Peniaze(urok.toPlainString());
    }

    /**
     * Pripočíta vypočítaný úrok
     */
    @Override
    public void pripocitajUrok() {
        Peniaze urokTentoMesiac = this.vypocitajVyskuUroku();
        this.pridajPeniaze(urokTentoMesiac.dajMnozstvoString());
    }

    /**
     * Vráti popis účtu
     * @return popis účtu
     */
    @Override
    public String dajPopis() {
        double urokovaSadzbaPercent = this.urokovaSadzba * 100;

        return String.format("%s účet,\nvýška úroku: %.2f percent", this.getTypUctu(), urokovaSadzbaPercent);
    }

    @Override
    public String toString() {
        return this.getTypUctu() + " - " + super.toString();
    }
}
