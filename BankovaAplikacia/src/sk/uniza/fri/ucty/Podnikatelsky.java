package sk.uniza.fri.ucty;


import sk.uniza.fri.IBAN;
import sk.uniza.fri.Peniaze;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Trieda Podnikatelsky, ktorá je potomkom triedy Ucet
 * Má výšku úroku, identifikáciu, poplatok za vedenie, limit prečerpania a vedie aj informáciu o tom o koľko sme prečerpali
 * limit
 */
public class Podnikatelsky extends Ucet {

    private final TypUctu typUctu;
    private String identifikacia;
    private double vyskaUroku;
    private final double poplatokZaVedenie;
    private BigDecimal limitPrecerpania;
    private BigDecimal precerpane;


    /**
     * Parametrický konštruktor triedy Podnikatelsky
     *
     * @param iban              iban
     * @param zostatok          počiatočný zostatok
     * @param identifikacia     identifikácia
     * @param vyskaUroku        výška úroku
     * @param poplatokZaVedenie poplatok za vedenie
     * @param limitPrecerpania  limit prečerpania
     */
    public Podnikatelsky(IBAN iban, String zostatok, String identifikacia, double vyskaUroku, double poplatokZaVedenie, String limitPrecerpania) {
        super(iban, zostatok);
        this.setIdentifikacia(identifikacia);
        this.setVyskaUroku(vyskaUroku);
        this.setLimitPrecerpania(limitPrecerpania);
        this.precerpane = new BigDecimal("0");
        this.poplatokZaVedenie = poplatokZaVedenie;

        this.typUctu = TypUctu.PODNIKATELSKY;
    }

    private String getTypUctu() {
        return this.typUctu.getNazov(this.typUctu);
    }
    public void setIdentifikacia(String identifikacia) {
        this.identifikacia = identifikacia;
    }
    private void setVyskaUroku(double vyskaUroku) {
        if (vyskaUroku >= 0) {
            this.vyskaUroku = vyskaUroku;
        }
    }

    /**
     * Nasatví limit prečerpania
     *
     * @param nastavLimit nový limit, ktorý chceme nastaviť
     */
    public void setLimitPrecerpania(String nastavLimit) {
        if (!nastavLimit.isEmpty()) {
            this.limitPrecerpania = new BigDecimal(nastavLimit);
        }
    }

    /**
     * Stiahne poplatok za vedenie
     */
    public void stiahniPoplatokZaVedenie() {
        this.odoberPeniaze(String.valueOf(this.poplatokZaVedenie));
    }

    /**
     * Odoberie peniaze, ak je hodnota, ktorú zadávame vyššia ako 0, v prípade, že je zostatok po odobratí menej ako 0
     * pripočíta sa limit prečerpania, ak sa presiahne aj ten, ideme do mínusu
     * @param hodnota obnos peňazí, ktorý chceme odobrať
     */
    @Override
    public void odoberPeniaze(String hodnota) {
        BigDecimal hodnotaBD = new BigDecimal(hodnota);
        BigDecimal nula = BigDecimal.ZERO;

        if (hodnotaBD.compareTo(nula) > 0) {
            Peniaze zostatokPoOdobrati = this.getZostatok();
            zostatokPoOdobrati.odcitaj(hodnota);

            BigDecimal hodnotaDveMiesta = hodnotaBD.setScale(2, RoundingMode.HALF_DOWN);
            this.pridajPohyb("-" + hodnotaDveMiesta);

            if (zostatokPoOdobrati.getMnozstvo().compareTo(nula) < 0) {
                BigDecimal zostatok = new BigDecimal(this.getZostatok().dajMnozstvoString());

                if (zostatok.compareTo(nula) < 0) {
                    BigDecimal zostatokPlusLimit = zostatok.add(new BigDecimal(this.limitPrecerpania.toPlainString()));

                    if (this.precerpane.compareTo(nula) <= 0) {
                        this.setZostatok(zostatokPlusLimit.toPlainString());
                    }

                    if (zostatokPlusLimit.compareTo(nula) < 0) {
                        this.precerpane = zostatokPlusLimit.multiply(new BigDecimal("-1"));
                        this.setZostatok(zostatokPlusLimit.toPlainString());
                    }
                }
            }
        }
    }

    /**
     * Vypočíta výšku úroku v prípade, že nie sme v mínuse
     * @return vráti vypočítanú výšku alebo null
     */
    @Override
    protected Peniaze vypocitajVyskuUroku() {
        BigDecimal nula = BigDecimal.ZERO;

        if (this.getZostatok().getMnozstvo().compareTo(nula) >= 0) {
            String vyskaUrokuStr = String.valueOf(this.vyskaUroku);
            Peniaze vypocitanaVyskaUroku = new Peniaze(this.getZostatok().dajMnozstvoString());
            vypocitanaVyskaUroku.vynasob(vyskaUrokuStr);

            return vypocitanaVyskaUroku;
        }

        return null;
    }

    /**
     * Pripočíta výšku úroku, ak nie je null
     */
    @Override
    public void pripocitajUrok() {
        if (this.vypocitajVyskuUroku() != null) {         //Ak vlastník účtu nevyčerpal peniaze nad maximálny limit, bude mu pridelený úrok
            Peniaze naPridanie = this.vypocitajVyskuUroku();
            this.pridajPeniaze(naPridanie.dajMnozstvoString());
        }
    }

    /**
     * Popis účtu
     * @return informácie o účte
     */
    @Override
    public String dajPopis() {
        double vyskaUrokyPercent = this.vyskaUroku * 100;

        return String.format("""
                        %s účet - %s
                        Poplatok za vedenie: %.2f EUR
                        Výška úroku: %.2f percent
                        Limit prečerpania: %s EUR
                        Nad limit: %s EUR""",
                this.getTypUctu(), this.identifikacia, this.poplatokZaVedenie, vyskaUrokyPercent, this.limitPrecerpania, this.precerpane);
    }

    @Override
    public String toString() {
        return this.getTypUctu() + " - " + super.toString();
    }
}
