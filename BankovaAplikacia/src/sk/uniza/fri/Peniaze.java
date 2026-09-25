package sk.uniza.fri;

import java.math.BigDecimal;

/**
 * Trieda Peniaze nám slúži na reprezentáciu peňazí. Má dva atribúty:
 * 1. mnozstvo typu BigDecimal - číslo reprezentujúce hodnotu
 * 2. mena typu String - aby sme vedeli s akou menou pracujeme
 * Táto trieda teda reprezentuje nejakú hodnotu, s ktorou ďalej pracujeme (sčítanie, odčítanie)
 */
public class Peniaze {
    private BigDecimal mnozstvo;
    private final String mena;

    /**
     * Parametrický konštruktor, kde parametrami sú mnozstvo typu String a mena typu String.
     *
     * @param mnozstvo mnozstvo peňazí
     * @param mena     mena
     */
    public Peniaze(String mnozstvo, String mena) {
        this.setMnozstvo(mnozstvo);
        this.mena = mena;
    }

    /**
     * Preťažený parametrický konštruktor bez jedného parametra na rozdiel od predošlého konštruktora.
     * Jediným parametrom je množstvo a mena sa automatický nastaví na eurá.
     *
     * @param mnozstvo mnozstvo peňazí
     */
    public Peniaze(String mnozstvo) {
        this.setMnozstvo(mnozstvo);
        this.mena = "EUR";
    }

    private void setMnozstvo(String mnozstvo) {
        BigDecimal hodnota = new BigDecimal(mnozstvo);
        this.mnozstvo = hodnota;
    }

    public String getMena() {
        return this.mena;
    }
    public BigDecimal getMnozstvo() {
        return this.mnozstvo;
    }

    /**
     * Metóda scitaj slúži na sčítanie alebo pridávanie peňazí do celkového množstva.
     *
     * @param hodnota udáva hodnotu, ktorú chceme pridať/sčítať.
     * @return vráti výsledov vo forme BigDecimal
     */
    public BigDecimal scitaj(String hodnota) {
        if (!hodnota.isEmpty()) {
            BigDecimal hodnotaBD = new BigDecimal(hodnota);
            BigDecimal vysledok = this.mnozstvo.add(hodnotaBD);
            this.mnozstvo = vysledok;
            return vysledok;
        }

        return null;
    }

    /**
     * Metóda odcitaj slúži na odčítanie/odoberanie pežaní z celkového množstva.
     *
     * @param hodnota udávame hodnotu, ktorú chcem odčítať/odobrať.
     * @return výsledok vo forme BigDecimal
     */
    public BigDecimal odcitaj(String hodnota) {
        if (!hodnota.isEmpty()) {
            BigDecimal hodnotaBD = new BigDecimal(hodnota);
            BigDecimal vysledok = this.mnozstvo.subtract(hodnotaBD);
            this.mnozstvo = vysledok;
            return vysledok;
        }

        return null;
    }

    /**
     * Slúži na vynásobenie zadanej hodnoty s hodnotou triedy
     *
     * @param hodnota hodnota, ktorou chceme vynásobiť hodnotu mnozstvo
     * @return výsledok v tvare BigDecimal
     */
    public BigDecimal vynasob(String hodnota) {
        if (!hodnota.isEmpty()) {
            BigDecimal hodnotaBD = new BigDecimal(hodnota);
            BigDecimal vysledok = this.mnozstvo.multiply(hodnotaBD);
            this.mnozstvo = vysledok;
            return vysledok;
        }

        return null;
    }

    public String dajMnozstvoString() {
        return this.mnozstvo.toPlainString();
    }
}
