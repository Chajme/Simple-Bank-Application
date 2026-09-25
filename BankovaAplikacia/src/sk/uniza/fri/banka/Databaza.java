package sk.uniza.fri.banka;

import sk.uniza.fri.IBAN;
import sk.uniza.fri.Prihlasenie;
import sk.uniza.fri.osoby.Zakaznik;
import sk.uniza.fri.osoby.Zamestnanec;
import sk.uniza.fri.ucty.Ucet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/**
 * Trieda Databaza, slúži ako databáza na uchovávanie zoznamov
 * Zoznam všetkých zamestnancov, zákazníkov a účtov
 */
public class Databaza {
    private final ArrayList<Zakaznik> zakaznici;
    private final ArrayList<Zamestnanec> zamestnanci;

    private final HashMap<IBAN, Ucet> vsetkyUcty;

    /**
     * Konštruktor triedy Databaza, inicializuje atribúty
     */
    public Databaza() {
        this.zakaznici = new ArrayList<>();
        this.vsetkyUcty = new HashMap<>();
        this.zamestnanci = new ArrayList<>();
    }

    public ArrayList<Zakaznik> getZakaznici() {
        return this.zakaznici;
    }
    public HashMap<IBAN, Ucet> getVsetkyUcty() {
        return this.vsetkyUcty;
    }
    public ArrayList<Zamestnanec> getZamestnanci() {
        return this.zamestnanci;
    }

    /**
     * Pridá zákazníka do zoznamu zákazníkov vedených v banke, ak nie je null
     *
     * @param zakaznik zákazník, ktorého chceme pridať
     */
    public void pridajZakaznika(Zakaznik zakaznik) {
        this.zakaznici.add(zakaznik);
    }

    /**
     * Pridá účet do hash mapy, kde bude ako kľúč jeho iban a hodnota daný účet
     *
     * @param iban iban účtu
     * @param ucet daný účet
     */
    public void pridajUcet(IBAN iban, Ucet ucet) {
        this.vsetkyUcty.put(iban, ucet);
    }

    /**
     * Pridá zamestnanca do zoznamu zamestnancov, ak nie je null
     *
     * @param zamestnanec zamestnanec, ktorého chceme pridaj
     */
    public void pridajZamestnanca(Zamestnanec zamestnanec) {
        if (zamestnanec != null) {
            this.zamestnanci.add(zamestnanec);
        }
    }

    /**
     * Náje účet v hashmape, podľa zadaného ibanu. Postupne prehľadáva kľúče, ak sa kľúč (IBAN) zhoduje s ibanom, ktorý
     * sme zadali ako parameter, vráti účet, ktorý prislúcha tomuto ibanu
     *
     * @param iban iban, ktorý sme zadali
     * @return účet, ktorý prislúcha daménu ibanu
     */
    public Ucet najdiUcet(IBAN iban) {
        for (IBAN ibanVDatabaze : this.vsetkyUcty.keySet()) {
            if (ibanVDatabaze.ibanJeRovnaky(iban)) {
                return this.vsetkyUcty.get(ibanVDatabaze);
            }
        }
        return null;
    }

    /**
     * Odstríni účet s daným ibanom
     *
     * @param iban iban účtu, ktorý chceme odstrániť
     * @return odstránený účet
     */
    public Ucet odstranUcet(IBAN iban) {
        Ucet ucet = this.najdiUcet(iban);
        if (ucet != null) {
            Zakaznik zakaznik = this.ucetZakaznika(iban);
            Objects.requireNonNull(zakaznik).getUcty().remove(ucet.getIBAN());
            this.vsetkyUcty.remove(ucet.getIBAN());
            return ucet;
        }
        return null;
    }

    private Zakaznik ucetZakaznika(IBAN iban) {
        for (Zakaznik zakaznik : this.zakaznici) {
            for (Ucet ucet : zakaznik.getUcty().values()) {
                if (ucet.getIBAN().ibanJeRovnaky(iban)) {
                    return zakaznik;
                }
            }
        }
        return null;
    }

    /**
     * Odstráni zo zoznamu zákazníka, ktorého dávame ako parameter
     *
     * @param zakaznik zákazník, kzorého chceme odstrániť
     * @return vraciame zákazníka
     */
    public Zakaznik odstranZakaznika(Zakaznik zakaznik) {
        if (zakaznik != null) {
            this.zakaznici.remove(zakaznik);
            for (Ucet ucet : zakaznik.getUcty().values()) {
                if (this.vsetkyUcty.containsValue(ucet)) {
                    this.vsetkyUcty.remove(ucet.getIBAN());
                }
            }
            return zakaznik;
        }
        return null;
    }

    /**
     * Zisťujeme či už účet s daným číslom existuje.
     *
     * @param vygenerovaneCisloUctu číslo účtu, ktoré kontrolujeme ako parameter
     * @return true alebo false či už taký účet existuje
     */
    public boolean ucetExistuje(String vygenerovaneCisloUctu) {
        for (Ucet ucet : this.vsetkyUcty.values()) {
            if (ucet.getZakladneCisloUctu().equals(vygenerovaneCisloUctu)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prihlasenie zakaznika na základe údajov, ktoré sme zadali ako parametre.
     * Zadáme prihlasovacie meno a prihlasovacieh heslo, následne prechádzame všetkých zákazníkov v banke, ak sa prihlasovacie
     * údaje zhodujú s prihlasovacími údajmi zákazníka v banke, vrátime jeho inštanciu.
     *
     * @param meno  prihlasovacie meno
     * @param heslo prihlasovacie heslo
     * @return prihlásený zákazník alebo null, ak taký zákazník nie je
     */
    public Zakaznik prihlasenieZakaznika(String meno, String heslo) {
        Prihlasenie zadanePrihlasovacieUdaje = new Prihlasenie(meno, heslo);

        for (Zakaznik zakaznik : this.zakaznici) {
            Prihlasenie prihlasovacieUdajeZakaznika = zakaznik.getPrihlasenie();
            if (prihlasovacieUdajeZakaznika.prihlasovacieUdajeSuSpravne(zadanePrihlasovacieUdaje)) {
                return zakaznik;
            }
        }
        return null;
    }

    /**
     * Prihlasenie zamestnanca na základe mena a hesla, ktoré sme zadali.
     * Prehľadávame zoznam zamestnancov banky, až kým nenarazíme na zamestnanca, ktorého prihlasovacie údaje sa zhodujú
     * so zadanými
     *
     * @param meno  prihlasovacie meno zamestnanca
     * @param heslo prihlasovacie heslo zamestnanca
     * @return vrátime prihláseného zamestnanca alebo null, ak taký zamestnanec nie je
     */
    public Zamestnanec prihlasenieZamestnanca(String meno, String heslo) {
        Prihlasenie zadanePrihlasovacieUdaje = new Prihlasenie(meno, heslo);

        for (Zamestnanec zamestnanec : this.zamestnanci) {
            Prihlasenie prihlasovacieUdajeZakaznika = zamestnanec.getPrihlasenie();

            if (prihlasovacieUdajeZakaznika.prihlasovacieUdajeSuSpravne(zadanePrihlasovacieUdaje)) {
                return zamestnanec;
            }
        }

        return null;
    }

    /**
     * Vsetky financie v banke string.
     *
     * @return všetky financie banky v tvare string
     */
    public String vsetkyFinancieVBanke() {
        BigDecimal financie = BigDecimal.ZERO;

        for (Ucet ucet : this.vsetkyUcty.values()) {
            financie = financie.add(ucet.getZostatok().getMnozstvo());
        }

        return financie.toString();
    }

    /**
     * Vráti true alebo false na základe toho, či je meno, ktoré sme zadali už použité alebo nie
     *
     * @param meno zadané meno
     * @return boolean, či je meno použité
     */
    public boolean menoPouzite(String meno) {
        for (Zakaznik zakaznik : this.zakaznici) {
            String prihlasovacieMeno = zakaznik.getPrihlasenie().getPrihlasovacieMeno();
            if (prihlasovacieMeno.equals(meno)) {
                return true;
            }
        }
        return false;
    }

}

