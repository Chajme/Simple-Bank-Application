package sk.uniza.fri;

/**
 * Trieda IBAN nám reprezentuje iban účtu, ktorý sa ďalej využíva ako identifikátor účtu. Má 5 parametrov typu String:
 * 1. kodKrajiny
 * 2. kontrolneCislice
 * 3. kodBanky
 * 4. predcislie
 * 5. zakladneCisloUctu
 * - každý atribút reprezentuje časť celého ibanu
 */
public class IBAN {
    private String kodKrajiny;
    private String kontrolneCislice;
    private String kodBanky;
    private String predcislie;
    private String zakladneCisloUctu;

    /**
     * Parametrický konštruktor IBAN, s parametrami typu String kodKrajiny, kontrolneCislice, kodBanky, predcislie,
     * zakladneCisloUctu nám nastaví počiatočne hodnoty atribútov.
     *
     * @param kodKrajiny        kod krajiny
     * @param kontrolneCislice  kontrolne cislice
     * @param kodBanky          kod banky
     * @param predcislie        predcislie
     * @param zakladneCisloUctu zakladne cislo uctu
     */
    public IBAN(String kodKrajiny, String kontrolneCislice, String kodBanky, String predcislie, String zakladneCisloUctu) {
        this.setKodKrajiny(kodKrajiny);                 //1.
        this.setKontrolneCislice(kontrolneCislice);     //2.
        this.setKodBanky(kodBanky);                     //3.
        this.setPredcislie(predcislie);                 //4.
        this.setZakladneCisloUctu(zakladneCisloUctu);   //5.
    }

    /**
     * Preťažený parametrický konštruktor IBAN, kde používame jediný parameter typu String iban. Tento parameter reprezentuje
     * celý iban, ktorý je následne rozdelený na časti a sú nastavené atribúty.
     *
     * @param iban iban, ktorý chceme nastaviť
     */
    public IBAN(String iban) {
        this.setIBAN(iban);
    }

    @Override
    public String toString() {
        String medzery = "4";
        return this.celyIBAN().replaceAll("(.{" + medzery + "})", "$1 ").trim();
    }

    private String celyIBAN() {
        String celyIBAN = this.kodKrajiny + this.kontrolneCislice + this.kodBanky + this.predcislie + this.zakladneCisloUctu;
        return celyIBAN;
    }

    public String getZakladneCisloUctu() {
        return this.zakladneCisloUctu;
    }

    /**
     * Metóda ibanJeRovnaky s inštanciou porovnanie triedy IBAN ako parametrom nám porovná inštancie a vráti true alebo false.
     *
     * @param porovnanie iban, ktorý chceme porovnať
     * @return true alebo false, podľa totho či je iban rovnaky
     */
    public boolean ibanJeRovnaky(IBAN porovnanie) {
        if (this == porovnanie) {
            return true;
        }

        if (porovnanie == null) {
            return false;
        }

        if (this.kodKrajiny.equals(porovnanie.kodKrajiny) &&
                this.kontrolneCislice.equals(porovnanie.kontrolneCislice) &&
                this.kodBanky.equals(porovnanie.kodBanky) &&
                this.predcislie.equals(porovnanie.predcislie) &&
                this.zakladneCisloUctu.equals(porovnanie.zakladneCisloUctu)) {

            return true;
        }

        return false;
    }

    private void setKodKrajiny(String kodKrajiny) {
        if (kodKrajiny.length() == 2) {
            this.kodKrajiny = kodKrajiny;
        }
    }

    private void setKontrolneCislice(String kontrolneCislice) {
        if (kontrolneCislice.length() == 2) {
            this.kontrolneCislice = kontrolneCislice;
        }
    }

    private void setKodBanky(String kodBanky) {
        if (kodBanky.length() == 4) {
            this.kodBanky = kodBanky;
        }
    }

    private void setPredcislie(String predcislie) {
        if (predcislie.length() == 6) {
            this.predcislie = predcislie;
        } else {
            StringBuilder sb = new StringBuilder();
            int pocetNul = 6 - predcislie.length();
            for (int i = 0; i < pocetNul; i++) {
                sb.append("0");
            }
            sb.append(predcislie);
            this.predcislie = sb.toString();
        }
    }

    private void setZakladneCisloUctu(String zakladneCisloUctu) {
        if (zakladneCisloUctu.length() == 10) {
            this.zakladneCisloUctu = zakladneCisloUctu;
        }
    }

    private void setIBAN(String iban) {
        String kodKrajinyL = "";
        String kontrolneCisliceL = "";
        String kodBankyL = "";
        String predcislieL = "";
        String zakladneCisloUctuL = "";

        if (iban.length() == 24) {
            char[] znaky = iban.toCharArray();
            for (int i = 0; i < znaky.length; i++) {
                if (i >= 0 && i <= 1) {
                    kodKrajinyL += znaky[i];
                } else if (i >= 2 && i <= 3) {
                    kontrolneCisliceL += znaky[i];
                } else if (i >= 4 && i <= 7) {
                    kodBankyL += znaky[i];
                } else if (i >= 8 && i <= 13) {
                    predcislieL += znaky[i];
                } else if (i >= 14 && i <= 23) {
                    zakladneCisloUctuL += znaky[i];
                }
            }
            this.kodKrajiny = kodKrajinyL;
            this.kontrolneCislice = kontrolneCisliceL;
            this.kodBanky = kodBankyL;
            this.predcislie = predcislieL;
            this.zakladneCisloUctu = zakladneCisloUctuL;
        }
    }
}
