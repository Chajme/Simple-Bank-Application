package sk.uniza.fri.ucty;

import sk.uniza.fri.IBAN;
import sk.uniza.fri.Peniaze;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Trieda Zakladny, ktorá je potomkom triedy Ucet
 * Reprezentuje najzákladnejší typ účtu, ktorý má pekne dané atribúty ako stalyUrok a garantovanyPoplatok, ktoré sú nemenné
 * a ich hodnota je garantovaná bankou
 */
public class Zakladny extends Ucet {

    private final TypUctu typUctu;
    private final BigDecimal minimalnyZostatok;
    private final double stalyUrok;
    private final double garantovanyPoplatok;

    /**
     * Parametrický konštruktor triedy Zakladny
     *
     * @param iban     iban účtu
     * @param zostatok počiatočný zostatok
     */
    public Zakladny(IBAN iban, String zostatok) {
        super(iban, zostatok);
        this.typUctu = TypUctu.ZAKLADNY;
        this.stalyUrok = 0.02;
        this.garantovanyPoplatok = 2;
        this.minimalnyZostatok = new BigDecimal("50");
    }

    private String getTypUctu() {
        return this.typUctu.getNazov(this.typUctu);
    }

    /**
     * Stiahne poplatok za vedenie účtu
     */
    public void stiahniPoplatokZaVedenie() {
        this.odoberPeniaze(String.valueOf(this.garantovanyPoplatok));
    }

    /**
     * Odoberie peniaze z útu, ak hodnota, ktorú zadávané je väčšia ako 0 a zostatok po odobratí bude väčší alebo rovný 0
     * @param hodnota obnos peňazí, ktorý chceme odobrať
     */
    @Override
    public void odoberPeniaze(String hodnota) {
        Peniaze zostatokPoOdobrati = new Peniaze(this.getZostatok().dajMnozstvoString());
        zostatokPoOdobrati.odcitaj(hodnota);

        BigDecimal hodnotaBD = new BigDecimal(hodnota);
        BigDecimal hodnotaDveMiesta = hodnotaBD.setScale(2, RoundingMode.HALF_DOWN);

        BigDecimal aktualnyZostatok = new BigDecimal(zostatokPoOdobrati.dajMnozstvoString());
        BigDecimal zero = BigDecimal.ZERO;

        if (hodnotaBD.compareTo(zero) > 0 && aktualnyZostatok.compareTo(this.minimalnyZostatok) >= 0) {
            this.setZostatok(zostatokPoOdobrati.dajMnozstvoString());
            this.pridajPohyb("-" + hodnotaDveMiesta);
        }
    }

    /**
     * Vypočíta výšku úroku na základe percentuálneho úroku
     * @return výšku úroku
     */
    @Override
    protected Peniaze vypocitajVyskuUroku() {
        String stalyUrokStr = String.valueOf(this.stalyUrok);
        Peniaze naPridanie = new Peniaze(this.getZostatok().dajMnozstvoString());
        naPridanie.vynasob(stalyUrokStr);
        return naPridanie;
    }

    /**
     * Pripočíta vypočítaný úrok
     */
    @Override
    public void pripocitajUrok() {
        String vypocitanyUrok = this.vypocitajVyskuUroku().dajMnozstvoString();

        this.pridajPeniaze(vypocitanyUrok);
    }

    /**
     * Vráti string s popisom účtu
     */
    @Override
    public String dajPopis() {
        double stalyUrokPercent = this.stalyUrok * 100;

        return String.format("""
                        %s účet
                        Minimálna výška zostatku %s EUR
                        Stály úrok: %.2f percent
                        Garantovaný poplatok za vedenie: %.2f EUR""",
                this.getTypUctu(), this.minimalnyZostatok, stalyUrokPercent, this.garantovanyPoplatok);
    }

    @Override
    public String toString() {
        return this.getTypUctu() + " - " + super.toString();
    }
}
