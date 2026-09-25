package sk.uniza.fri.osoby;

import sk.uniza.fri.Peniaze;
import sk.uniza.fri.banka.Banka;
import sk.uniza.fri.IBAN;
import sk.uniza.fri.Prihlasenie;
import sk.uniza.fri.ucty.Ucet;

import java.util.HashMap;

/**
 * Trieda Zakaznik, ktorá má 6 atribútov, meno, priezvisko, vek, prihlasenie, ucty.
 */
public class Zakaznik {
    private String meno;
    private String priezvisko;
    private int vek;
    private final Prihlasenie prihlasenie;
    private final HashMap<IBAN, Ucet> ucty = new HashMap<>();

    /**
     * Parametrický konštruktor triedy Zakaznik
     *
     * @param meno               meno
     * @param priezvisko         priezvisko
     * @param vek                vek
     * @param prihlasovacieMeno  prihlasovacie meno
     * @param prihlasovacieHeslo prihlasovacie heslo
     * @param pin                pin
     */
    public Zakaznik(String meno, String priezvisko, int vek, String prihlasovacieMeno, String prihlasovacieHeslo, String pin) {
        this.setMeno(meno);
        this.setPriezvisko(priezvisko);
        this.vek = vek;
        this.prihlasenie = new Prihlasenie(prihlasovacieMeno, prihlasovacieHeslo, pin);
    }

    /**
     * Preťažený parametrický konštruktor
     *
     * @param meno       meno
     * @param priezvisko priezvisko
     */
    public Zakaznik(String meno, String priezvisko) {
        this.setMeno(meno);
        this.setPriezvisko(priezvisko);
        this.prihlasenie = new Prihlasenie("novy", "novy", "0000");
    }

    public int getVek() {
        return this.vek;
    }

    public String getCeleMeno() {
        return this.meno + " " + this.priezvisko;
    }

    public Prihlasenie getPrihlasenie() {
        return this.prihlasenie;
    }

    public HashMap<IBAN, Ucet> getUcty() {
        return this.ucty;
    }

    public String getCelkovyZostatok() {
        Peniaze celkZostatok = new Peniaze("0");

        for (Ucet ucet : this.ucty.values()) {
            celkZostatok.scitaj(ucet.getZostatok().dajMnozstvoString());
        }

        return celkZostatok.dajMnozstvoString();
    }

    private void setMeno(String meno) {
        if (this.kontrolaZnakovStringu(meno)) {
            this.meno = meno;
        }
    }

    private void setPriezvisko(String priezvisko) {
        if (this.kontrolaZnakovStringu(priezvisko)) {
            this.priezvisko = priezvisko;
        }
    }

    private boolean kontrolaZnakovStringu(String retazec) {
        char[] znaky = retazec.toCharArray();

        for (char znak : znaky) {
            if (!Character.isLetter(znak)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Zákazník si vytvorí Základný účet v banke, ktorú zadávame ako parameter
     * vytvorený účet zaradíme to zoznamu účtov
     *
     * @param banka banka, v ktorej chceme vytvoriť účet
     * @return vraciame vytvorený účet
     */
    public Ucet zriadZakladnyUcet(Banka banka) {
        Ucet novyZakladnyUcet = banka.vytvorZakladnyUcet(this);
        this.ucty.put(novyZakladnyUcet.getIBAN(), novyZakladnyUcet);
        return novyZakladnyUcet;
    }

    /**
     * Zákazník si vytvorí Podnikateľský účet v banke, ktorú zadávame ako parameter
     * vytvorený účet zaradíme to zoznamu účtov
     *
     * @param banka            banka, v ktorej chceme vytvoriť účet
     * @param identifikacia    identifikácia účtu
     * @param limitPrecerpania limit prečerpania, ktorý chceme, aby účet mal
     * @return vytvorený účet
     */
    public Ucet zriadPodnikatelskyUcet(Banka banka, String identifikacia, String limitPrecerpania) {
        Ucet novyPodnikatelskyUcet = banka.vytvorPodnikatelskyUcet(this, identifikacia, limitPrecerpania);
        this.ucty.put(novyPodnikatelskyUcet.getIBAN(), novyPodnikatelskyUcet);
        return novyPodnikatelskyUcet;
    }

    /**
     * Zákazník si vytvorí Študentský účet v banke, ktorú zadávame ako parameter
     * vytvorený účet zaradíme to zoznamu účtov
     *
     * @param banka banka, v ktorej chceme vytvoriť účet
     * @return vraciame vytvorený účet
     */
    public Ucet zriadStudentskyUcet(Banka banka) {
        Ucet novyStudentskyUcet = banka.vytvorStudentskyUcet(this, this.vek);
        this.ucty.put(novyStudentskyUcet.getIBAN(), novyStudentskyUcet);
        return novyStudentskyUcet;
    }

    /**
     * Zákazník si vytvorí Šporiaci účet v banke, ktorú zadávame ako parameter
     * vytvorený účet zaradíme to zoznamu účtov
     *
     * @param banka banka, v ktorej chceme vytvoriť účet
     * @return vraciame vytvorený účet
     */
    public Ucet zriadSporiaciUcet(Banka banka) {
        Ucet novySporiaciUcet = banka.vytvorSporiaciUcet(this);
        this.ucty.put(novySporiaciUcet.getIBAN(), novySporiaciUcet);
        return novySporiaciUcet;
    }

    /**
     * Rušenie účtu v banke
     *
     * @param banka banka
     * @param ucet  účet, ktorý chceme zrušiť
     */
    public void zrusUcetVBanke(Banka banka, Ucet ucet) {
        Ucet zrusenyUcet = banka.zrusUcet(ucet);
        this.ucty.remove(zrusenyUcet.getIBAN());
    }

    /**
     * Vloženie peňazí na účet, ktorý zadávame ako parameter
     *
     * @param mnozstvo obnos, ktorý chceme vložiť
     * @param ucet     účet, do ktorého chceme vložiť peniaze
     */
    public void vlozPeniaze(String mnozstvo, Ucet ucet) {
        if (ucet != null && this.ucty.containsKey(ucet.getIBAN())) {
            ucet.pridajPeniaze(mnozstvo);
        }
    }

    /**
     * Výber peňazí z účtu
     *
     * @param pin      pin pre potvrdenie, že sme to my
     * @param mnozstvo obnos, ktorý chceme vybrať
     * @param ucet     účet, z ktorého vyberáme
     */
    public void vyberPeniaze(String pin, String mnozstvo, Ucet ucet) {
        if (ucet != null && pin.equals(this.prihlasenie.getPin()) && this.ucty.containsKey(ucet.getIBAN())) {
            ucet.odoberPeniaze(mnozstvo);
        }
    }

    /**
     * Posielanie peňazí na účet
     *
     * @param banka            banka, v ktorej je účet vedený
     * @param pin              pin na kontrolu
     * @param ucetOdosielatela účet, z ktorého posielame peniaze
     * @param ibanPrijemcu     iban účtu, ktorému posielame peniaze
     * @param obnos            obnos peňazí
     * @return vrátime, či účet príjemcu, ktorý sme zadali existuje alebo nie
     */
    public boolean posliPeniazeNaUcet(Banka banka, String pin, Ucet ucetOdosielatela, IBAN ibanPrijemcu, String obnos) {
        Ucet ucet = banka.prevodNaUcet(this, pin, ucetOdosielatela, ibanPrijemcu, obnos);
        return ucet != null;
    }

    /**
     * Zmena pinu
     *
     * @param staryPin stary pin pre kontrolu
     * @param novyPin  pin na ktorý chceme zmeniť
     */
    public boolean zmenPIN(String staryPin, String novyPin) {
        return this.prihlasenie.zmenPin(staryPin, novyPin);
    }

    /**
     * Zmena prihlasovacieho mena
     *
     * @param banka    banka, v ktorje je zákazník vedený
     * @param noveMeno nové meno, na ktoré chceme zmeniť staré
     */
    public void zmenPrihlasovacieMeno(Banka banka, String noveMeno) {
        if (!(banka.getDatabaza().menoPouzite(noveMeno))) {
            this.prihlasenie.zmenPrihlasovacieMeno(noveMeno);
        }
    }

    /**
     * Zmena prihlasovacieho hesla
     *
     * @param stareHeslo staré heslo pre potvrdenie
     * @param noveHeslo  nové heslo, na ktoré chceme zmeniť staré
     * @return vraciame, či sa zmena podarila alebo nie
     */
    public boolean zmenPrihlasovacieHeslo(String stareHeslo, String noveHeslo) {
        return this.prihlasenie.zmenPrihlasovacieHeslo(stareHeslo, noveHeslo);
    }

    /**
     * Popis zákazníka
     *
     * @return vraciame meno, priezvisko a vek zákazníka
     */
    public String dajPopis() {
        return this.meno + " " + this.priezvisko + "\nVek: " + this.vek;
    }

    @Override
    public String toString() {
        return this.meno + " " + this.priezvisko;
    }
}

