package sk.uniza.fri.osoby;

import sk.uniza.fri.IBAN;
import sk.uniza.fri.banka.Banka;
import sk.uniza.fri.Prihlasenie;
import sk.uniza.fri.ucty.Ucet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Trieda zamestnanec nám reprezenzuje zamestnanca banky, má dva atribúty:
 * 1. prihlasenie - inštancia triedy Prihlasenie - prihlasovacie údaje zamestnanca.
 * 2. banka - inštancia triedy Banka - banka, v ktorej je zamestnanec vedený.
 */
public class Zamestnanec {
    private final Prihlasenie prihlasenie;
    private final Banka banka;

    /**
     * Parametrický konštruktor Zamestnanec, parametrami sú banka obj. typu Banka a String parametre prihlasovacieMeno,
     * prihlasovacieHeslo. V konštruktore nastavuje počiatočné hodnoty atribútov a vytvárame novú inštanciu triedy Prihlasenie.
     *
     * @param banka              banka, v ktorej je zamestnanec vedený
     * @param prihlasovacieMeno  prihlasovacie meno zamestnanca
     * @param prihlasovacieHeslo prihlasovacie heslo zamestnanca
     */
    public Zamestnanec(Banka banka, String prihlasovacieMeno, String prihlasovacieHeslo) {
        this.prihlasenie = new Prihlasenie(prihlasovacieMeno, prihlasovacieHeslo);
        this.banka = banka;
    }

    /**
     * Metóda toString nám vráti String s prihlasovacími údajmi.
     */
    @Override
    public String toString() {
        return this.prihlasenie.getPrihlasovacieMeno() + " " + this.prihlasenie.getPrihlasovacieHeslo();
    }

    public Prihlasenie getPrihlasenie() {
        return this.prihlasenie;
    }

    public HashMap<IBAN, Ucet> getUctyBanky() {
        return this.banka.getDatabaza().getVsetkyUcty();
    }

    public ArrayList<Zakaznik> getVsetkychZakaznikov() {
        return this.banka.getDatabaza().getZakaznici();
    }

    /**
     * Metóda zrusUcetZakaznika s parametrami typu String cisloUctu a prihlasovacieHeslo zruší účet zákazníka na základe
     * parametra cisloUctu, ktoré zadáme, aby sme účet naozaj zrušili musím zadať heslo pre prihlásenie zamestnanca.
     *
     * @param ucet               účet zákazníka, ktorý chceme zrušiť
     * @param prihlasovacieHeslo prihlasovacie heslo zamestnanca pre potvrdenie
     */
    public void zrusUcetZakaznika(Ucet ucet, String prihlasovacieHeslo) {
        if (prihlasovacieHeslo.equals(this.prihlasenie.getPrihlasovacieHeslo())) {
            this.banka.zrusUcet(ucet);
        }
    }

    public void zrusZakaznika(Zakaznik zakaznik, String prihlasovacieHeslo) {
        if (prihlasovacieHeslo.equals(this.prihlasenie.getPrihlasovacieHeslo())) {
            this.banka.zrusZkaznika(zakaznik);
        }
    }

    /**
     * Metóda vsetkyFinancieBanky nám vráti všetky financie, ktoré sa v banke nachádzajú.
     *
     * @return financie banky v tvare stringu
     */
    public String vsetkyFinancieBanky() {
        BigDecimal financieBanky = new BigDecimal(this.banka.getVsetkyFinancie());
        BigDecimal financieBankyDveMiesta = financieBanky.setScale(2, RoundingMode.HALF_DOWN);

        return financieBankyDveMiesta.toPlainString();
    }

    /**
     * Stiahni poplatky.
     */
    public void stiahniPoplatky() {
        this.banka.getSpravcaPoplatkov().stiahniPoplatky();
    }

    public void pripocitajUroky() {
        this.banka.getSpravcaPoplatkov().pripocitajUroky();
    }
}
