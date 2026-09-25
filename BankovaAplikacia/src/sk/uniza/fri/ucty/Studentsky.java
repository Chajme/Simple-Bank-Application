package sk.uniza.fri.ucty;

import sk.uniza.fri.IBAN;
import sk.uniza.fri.Peniaze;

import java.math.BigDecimal;

/**
 * Trieda Studentsky, ktorá je potomtkom triedy Ucet
 * Predstavuje študentský účet, ktorý musí odvádzať poplatky v prípade, že sa zákazníkovi zvýši vek nad 25 rokov, ak má
 * zákazník menej ako 25 rokov neodvádza žiadne poplatky, úrok účtu je pevne daný na 5 EUR, v prípade, že účet disponuje
 * zostatok vyšším ako 1000 EUR, úrok sa zvyšuje na dvojnásobok
 */
public class Studentsky extends Ucet {

    private final TypUctu typUctu;
    private int vekStudenta;
    private double poplatokZaVedenie;

    /**
     * Parametrický konštruktor tridy Studentsky
     *
     * @param iban              iban účtu
     * @param zostatok          počiatočný zostatok
     * @param vekStudenta       vek zákazníka
     * @param poplatokZaVedenie poplatok za vedenie, ktorý je generovaný bankou
     */
    public Studentsky(IBAN iban, String zostatok, int vekStudenta, double poplatokZaVedenie) {
        super(iban, zostatok);
        this.setVekStudenta(vekStudenta);
        this.setPoplatokZaVedenie(poplatokZaVedenie);
        this.pridajPeniaze("20");

        this.typUctu = TypUctu.STUDENTSKY;
    }

    private String getTypUctu() {
        return this.typUctu.getNazov(this.typUctu);
    }
    public int getVekStudenta() {
        return this.vekStudenta;
    }
    public double getPoplatokZaVedenie() {
        return this.poplatokZaVedenie;
    }

    private void setPoplatokZaVedenie(double poplatokZaVedenie) {
        if (this.vekStudenta > 25) {
            this.poplatokZaVedenie = poplatokZaVedenie;
        } else {
            this.poplatokZaVedenie = 0;
        }
    }
    public void setVekStudenta(int vekStudenta) {
        this.vekStudenta = vekStudenta;
    }

    /**
     * Stiahne poplatok za vedenie v prípade, že je poplatok vyšší ako 0, teda zákazník má viac ako 25 rokov
     */
    public void stiahniPoplatokZaVedenie() {
        if (this.poplatokZaVedenie > 0) {
            this.odoberPeniaze(String.valueOf(this.poplatokZaVedenie));
        }
    }

    /**
     * Odoberie peniaze z účtu, ak je hodnota väčšia ako 0 a pridá pohyb medzi pohyby
     * @param hodnota obnos peňazí, ktorý chceme odobrať
     */
    @Override
    public void odoberPeniaze(String hodnota) {
        BigDecimal hodnotaBD = new BigDecimal(hodnota);

        if (!hodnota.isEmpty()) {
            Peniaze naOdobranie = new Peniaze(hodnota);
            Peniaze zostatokPoOdobrati = new Peniaze(this.getZostatok().dajMnozstvoString());
            BigDecimal zostatokPoOdobratiBD = zostatokPoOdobrati.odcitaj(naOdobranie.dajMnozstvoString());

            if (zostatokPoOdobratiBD.compareTo(new BigDecimal("0")) >= 0) {
                this.setZostatok(zostatokPoOdobrati.dajMnozstvoString());
                this.pridajPohyb("-" + zostatokPoOdobrati.dajMnozstvoString());
            }
        }
    }

    /**
     * Vypočíta výšku úroku v prípade, že účet dispouje zostatkom vyšším ako 1000 EUR, inak je výška úroku stanovená na 5 EUR
     * @return vypočítaná výška úroku ako inštancia triedy Peniaze
     */
    @Override
    protected Peniaze vypocitajVyskuUroku() {
        Peniaze vyskaUroku = new Peniaze("5");

        BigDecimal naPorovnanie = new BigDecimal("100");

        if (super.getZostatok().getMnozstvo().compareTo(naPorovnanie) > 0) {
            vyskaUroku.vynasob("2");
        }

        return vyskaUroku;
    }

    /**
     * Pripočíta úrok
     */
    @Override
    public void pripocitajUrok() {
        Peniaze urokNaPripocitanie = this.vypocitajVyskuUroku();

        this.pridajPeniaze(urokNaPripocitanie.dajMnozstvoString());
    }

    /**
     * Vráti popis triedy Studentsky
     * @return popis triedy
     */
    @Override
    public String dajPopis() {
        return String.format("Vek studenta: %d\nPoplatok za vedenie účtu: %.2f EUR", this.vekStudenta, this.poplatokZaVedenie);
    }

    @Override
    public String toString() {
        return this.getTypUctu() + " - " + super.toString();
    }
}
