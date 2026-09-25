package sk.uniza.fri.banka;

import sk.uniza.fri.IBAN;
import sk.uniza.fri.osoby.Zakaznik;
import sk.uniza.fri.osoby.Zamestnanec;
import sk.uniza.fri.ucty.Podnikatelsky;
import sk.uniza.fri.ucty.Sporiaci;
import sk.uniza.fri.ucty.Studentsky;
import sk.uniza.fri.ucty.Ucet;
import sk.uniza.fri.ucty.Zakladny;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Trieda Banka slúži na reprezentáciu banky, má kód krajiny, kód banky, kontrolne číslo, všetky financie, databázu a správcu poplatkov
 */
public class Banka {
    private String krajina;
    private String kod;
    private String kontrolneCislo;
    private String vsetkyFinancie;
    private final StringBuilder sb;
    private final Databaza databaza;
    private final SpravcaPoplatkov spravcaPoplatkov;

    /**
     * Parametrický konštruktor trieda Banka
     *
     * @param krajina        kód krajiny, v ktorej je banka situovaná
     * @param kontrolneCislo kontrolné číslo banky
     * @param kod            kód banky
     */
    public Banka(String krajina, String kontrolneCislo, String kod) {
        this.setKrajina(krajina);
        this.setKod(kod);
        this.setKontrolneCislo(kontrolneCislo);
        this.sb = new StringBuilder();
        this.databaza = new Databaza();
        this.vsetkyFinancie = this.databaza.vsetkyFinancieVBanke();
        this.spravcaPoplatkov = new SpravcaPoplatkov(this.databaza);
    }

    public SpravcaPoplatkov getSpravcaPoplatkov() {
        return this.spravcaPoplatkov;
    }
    public Databaza getDatabaza() {
        return this.databaza;
    }
    public String getVsetkyFinancie() {
        this.vsetkyFinancie = this.databaza.vsetkyFinancieVBanke();
        return this.vsetkyFinancie;
    }

    private void setKontrolneCislo(String kontrolneCislo) {
        if (kontrolneCislo.length() == 2) {
            this.kontrolneCislo = kontrolneCislo;
        }
    }

    private void setKrajina(String krajina) {
        if (krajina.length() == 2 && !this.suCisla(krajina)) {
            this.krajina = krajina;
        }
    }

    private void setKod(String kod) {
        if (kod.length() == 4 && this.suCisla(kod)) {
            this.kod = kod;
        }
    }

    private boolean suCisla(String retazec) {
        return retazec.matches("[0-9]+");
    }

    /**
     * Vytvorenie základného účtu v banke
     *
     * @param zakaznik zákazník, kzorý vytvára účet
     * @return vytvorený účet
     */
    public Ucet vytvorZakladnyUcet(Zakaznik zakaznik) {
        IBAN vygenerovanyIBAN = this.generujIBAN(this.krajina, this.kontrolneCislo, this.kod);
        Ucet novyZakladnyUcet = new Zakladny(vygenerovanyIBAN, "0");

        this.pridajNovyUcetDoZoznamu(zakaznik, vygenerovanyIBAN, novyZakladnyUcet);

        return novyZakladnyUcet;
    }

    /**
     * Vytvorenie podnikateľského účtu v banke v prípade, že zákazník už mal 18 rokov
     *
     * @param zakaznik         zákazník, ktorý vytvára účet
     * @param identifikacia    identifikácia podnikateľského účtu
     * @param limitPrecerpania nastavenie limitu prečerpania pre účet
     * @return vytvorený účet
     */
    public Ucet vytvorPodnikatelskyUcet(Zakaznik zakaznik, String identifikacia, String limitPrecerpania) {

        if (zakaznik.getVek() >= 18) {
            double vyskaUroku = this.dajRandomCisloDouble(0.04, 0.08);
            double poplatokZaVedenie = this.dajRandomCisloDouble(5, 10);

            IBAN vygenerovanyIBAN = this.generujIBAN(this.krajina, this.kontrolneCislo, this.kod);
            Ucet novyPodnikatelskyUcet = new Podnikatelsky(vygenerovanyIBAN, "0", identifikacia, vyskaUroku, poplatokZaVedenie, limitPrecerpania);

            this.pridajNovyUcetDoZoznamu(zakaznik, vygenerovanyIBAN, novyPodnikatelskyUcet);

            return novyPodnikatelskyUcet;
        }

        return null;
    }

    /**
     * Vytvorenie študentskeho účtu
     *
     * @param zakaznik    zákazník, ktorý vytvára účet
     * @param vekStudenta vek zákazníka
     * @return vytvorený účet
     */
    public Ucet vytvorStudentskyUcet(Zakaznik zakaznik, int vekStudenta) {
        double poplatokZaVedenie = this.dajRandomCisloDouble(10, 15);

        IBAN vygenerovanyIBAN = this.generujIBAN(this.krajina, this.kontrolneCislo, this.kod);
        Ucet novyStudentskyUcet = new Studentsky(vygenerovanyIBAN, "0", vekStudenta, poplatokZaVedenie);

        this.pridajNovyUcetDoZoznamu(zakaznik, vygenerovanyIBAN, novyStudentskyUcet);

        return novyStudentskyUcet;
    }

    /**
     * Vytvorenie šporiacehu účtu
     *
     * @param zakaznik zákazník, ktorý vytvára účet
     * @return vytvorený účet
     */
    public Ucet vytvorSporiaciUcet(Zakaznik zakaznik) {
        double urokovaSadzba = this.dajRandomCisloDouble(0.03, 0.05);

        IBAN vygenerovanyIBAN = this.generujIBAN(this.krajina, this.kontrolneCislo, this.kod);
        Ucet novySporiaciUcet = new Sporiaci(vygenerovanyIBAN, "0", urokovaSadzba);

        this.pridajNovyUcetDoZoznamu(zakaznik, vygenerovanyIBAN, novySporiaciUcet);

        return novySporiaciUcet;
    }

    private void pridajNovyUcetDoZoznamu(Zakaznik zakaznik, IBAN vygenerovanyIBAN, Ucet vytvorenyUcet) {
        this.databaza.pridajUcet(vygenerovanyIBAN, vytvorenyUcet);

        if (!(this.zakaznikMaUcetVBanke(zakaznik))) {
            this.databaza.pridajZakaznika(zakaznik);
        }
    }

    private IBAN generujIBAN(String krajina, String kontrolneCislo, String kod) {
        String predcislie = this.predcislieUctu();
        String cisloUctu = this.cisloUctu();
        return new IBAN(krajina, kontrolneCislo, kod, predcislie, cisloUctu);
    }

    private String predcislieUctu() {
        this.vycistiSb();

        int pocetCisiel = this.dajRandomCislo(6);

        for (int i = 0; i < pocetCisiel; i++) {
            this.sb.append(this.dajRandomCislo(10));
        }

        String vygenerovanePredcislie = this.sb.toString();
        return vygenerovanePredcislie;
    }

    private int dajRandomCislo(int rozsah) {
        Random rnd = new Random();
        int cislo = rnd.nextInt(rozsah);
        return cislo;
    }

    private double dajRandomCisloDouble(double min, double max) {
        Random rnd = new Random();
        return min + (max - min) * rnd.nextDouble();
    }

    private String cisloUctu() {
        String vygenerovaneCisloUctu = this.vygenerujCisloUctu();
        if (this.databaza.ucetExistuje(vygenerovaneCisloUctu)) {
            String noveVygenerovaneCisloUctu = this.vygenerujCisloUctu();
            return noveVygenerovaneCisloUctu;
        }

        return vygenerovaneCisloUctu;
    }

    private String vygenerujCisloUctu() {
        this.vycistiSb();

        for (int i = 0; i < 10; i++) {
            this.sb.append(this.dajRandomCislo(10));
        }

        String vygenerovaneCisloUctu = this.sb.toString();
        return vygenerovaneCisloUctu;
    }

    private void vycistiSb() {
        this.sb.delete(0, this.sb.length());
    }

    /**
     * Kontrola či má zákazník účet v banke alebo nie
     *
     * @param zakaznik zákazník, kzorého účty chceme kontrolovať
     * @return true alebo false poda toho či už má zákazník účet v banke
     */
    public boolean zakaznikMaUcetVBanke(Zakaznik zakaznik) {
        return this.databaza.getZakaznici().contains(zakaznik);
    }

    /**
     * Zrušenie účtu v banke
     *
     * @param ucet účet, ktorý chceme zrušiť
     * @return zrušený účet
     */
    public Ucet zrusUcet(Ucet ucet) {
        Ucet zrusenyUcet = this.databaza.odstranUcet(ucet.getIBAN());
        return zrusenyUcet;
    }

    /**
     * Vymazanie zákazníka z banky
     *
     * @param zakaznik zákazník, ktorého chceme vymazať
     * @return vymazaný zákazník
     */
    public Zakaznik zrusZkaznika(Zakaznik zakaznik) {
        return this.databaza.odstranZakaznika(zakaznik);
    }

    /**
     * Prevod peňazí medzi dvoma účtami
     *
     * @param zakaznik         zákazník, kzorý posiela peniaze
     * @param pin              pin pre kontrolu
     * @param ucetOdosielatela účet, z ktorého zákazník posiela peniaze
     * @param ibanPrijemcu     iban účtu príjemcu
     * @param obnos            obnos peňazí, kzorý posielame
     * @return účet príjemcu
     */
    public Ucet prevodNaUcet(Zakaznik zakaznik, String pin, Ucet ucetOdosielatela, IBAN ibanPrijemcu, String obnos) {
        BigDecimal nula = BigDecimal.ZERO;

        if (pin.equals(zakaznik.getPrihlasenie().getPin()) && ucetOdosielatela.getZostatok().getMnozstvo().compareTo(nula) > 0) {
            Ucet ucetPrijemcu = this.databaza.najdiUcet(ibanPrijemcu);

            if (ucetPrijemcu != null) {
                zakaznik.vyberPeniaze(pin, obnos, ucetOdosielatela);
                ucetPrijemcu.pridajPeniaze(obnos);
                return ucetPrijemcu;
            }
        }
        return null;
    }

    /**
     * Prihlásenie zákazníka do banky
     *
     * @param meno  prihlasovacie meno
     * @param heslo prihlasovacie heslo
     * @return prihlásený zákazník
     */
    public Zakaznik prihlasZakaznika(String meno, String heslo) {
        Zakaznik prihlasenyZakaznik = this.databaza.prihlasenieZakaznika(meno, heslo);
        if (prihlasenyZakaznik != null) {
            return prihlasenyZakaznik;
        }
        return null;
    }

    /**
     * Prihlásenie zamestnanca do banky
     *
     * @param meno  prihlasovacie meno
     * @param heslo prihlasovacie heslo
     * @return prihlásený zamestnanec
     */
    public Zamestnanec prihlasZamestnanca(String meno, String heslo) {
        Zamestnanec prihlasenyZamestnanec = this.databaza.prihlasenieZamestnanca(meno, heslo);
        if (prihlasenyZamestnanec != null) {
            return prihlasenyZamestnanec;
        }
        return null;
    }

    /**
     * Pridanie zamestnanca do banky
     *
     * @param zamestnanec zamestnanec, ktorého chceme pridať do banky
     */
    public void zamestnajZamestnanca(Zamestnanec zamestnanec) {
        if (zamestnanec != null) {
            this.databaza.pridajZamestnanca(zamestnanec);
        }
    }
}

