package sk.uniza.fri;

/**
 * Triedou Prihlasenie reprezenzujeme prihlasovanie, má 3 atribúty.
 * 1. pin typu String - predstavuje pin o dĺžke maximálne 4 číselné znaky.
 * 2. prihlasovacieMeno typu String - prihlasovacie meno o dĺžke maximálne 5 malých písmen.
 * 3. prihlasovacieHeslo typu String - prihlasovacie heslo, bez podmienok, teda môže obsahovať rôzne znaky.
 */
public class Prihlasenie {
    private String pin;
    private String prihlasovacieMeno;
    private String prihlasovacieHeslo;

    /**
     * Parametrický konštruktor Prihlasenie, parametrami sú prihlasovacieMeno, prihlasovacieHeslo, pin.
     * Všetky parametre sú typu string a nastavujeme nimi počiatočnú hodnotu atribútov.
     *
     * @param prihlasovacieMeno  prihlasovacie meno
     * @param prihlasovacieHeslo prihlasovacie heslo
     * @param pin                pin
     */
    public Prihlasenie(String prihlasovacieMeno, String prihlasovacieHeslo, String pin) {
        this.setPrihlasovacieMeno(prihlasovacieMeno);
        this.setPrihlasovacieHeslo(prihlasovacieHeslo);
        this.setPin(pin);
    }

    /**
     * Preťažený parametrický konštruktor Prihlasenie, s dvoma atribútmi: prihlasovacieMeno, prihlasovacieHeslo.
     * Pri využité tohoto konštroktora sa nenastaví atribút pin na žiadnu počiatočnú hodnotu.
     *
     * @param prihlasovacieMeno  prihlasovacie meno
     * @param prihlasovacieHeslo prihlasovacie heslo
     */
    public Prihlasenie(String prihlasovacieMeno, String prihlasovacieHeslo) {
        this.setPrihlasovacieMeno(prihlasovacieMeno);
        this.setPrihlasovacieHeslo(prihlasovacieHeslo);
    }

    private void setPrihlasovacieMeno(String prihlasovacieMeno) {
        if (this.kontrolaZnakov(prihlasovacieMeno) && prihlasovacieMeno.length() == 5) {
            this.prihlasovacieMeno = prihlasovacieMeno.toLowerCase();
        }

    }

    private void setPrihlasovacieHeslo(String prihlasovacieHeslo) {
        if (!prihlasovacieHeslo.isEmpty()) {
            this.prihlasovacieHeslo = prihlasovacieHeslo;
        }
    }

    private void setPin(String pin) {
        if (this.kontrolaSpravnostiPinu(pin)) {
            this.pin = pin;
        }
    }

    public String getPrihlasovacieMeno() {
        return this.prihlasovacieMeno;
    }
    public String getPrihlasovacieHeslo() {
        return this.prihlasovacieHeslo;
    }

    public String getPin() {
        return this.pin;
    }

    private boolean kontrolaZnakov(String retazec) {
        return retazec.matches("[a-z]+");
    }

    private boolean kontrolaSpravnostiPinu(String pin) {
        return pin.matches("[0-9]+") && pin.length() == 4;
    }

    /**
     * Metóda zmenPin s atribútmi staryPin typu String a novyPin typu String nám zmení hodnotu
     * atribútu podľa hodnôt zadaných parametrov. Ak je staryPin, ktorý sme zadali ako parameter rovnaký
     * ako atribút pin, hodnota parametra novyPin sa nastaví ako hodnota atribútu pin.
     *
     * @param staryPin stary pin
     * @param novyPin  novy pin
     */
    public boolean zmenPin(String staryPin, String novyPin) {
        if (staryPin.equals(this.pin)) {
            this.setPin(novyPin);
            return true;
        }
        return false;
    }

    /**
     * Metóda zmenPrihlasovacieMeno s parametrom noveMeno typu String nám zmení prihlasovacieMeno na hodnotu
     * parametra noveMeno
     *
     * @param noveMeno nove meno
     */
    public void zmenPrihlasovacieMeno(String noveMeno) {
        this.setPrihlasovacieMeno(noveMeno);
    }

    /**
     * Metóda zmenPrihlasovacieHeslo so String parametrami stareHeslo a noveHeslo funguje na rovnakom princípe
     * ako metóda zmenPin. Porvnáme zadany parameter stareHeslo s atribútom prilasovacieHeslo, ak sa rovnajú, atribút
     * prihlasovacieHeslo sa zmení a dostane hodnotu parametra noveHeslo.
     *
     * @param stareHeslo stare heslo
     * @param noveHeslo  nove heslo
     * @return true alebo fales, či sa heslo zmenilo
     */
    public boolean zmenPrihlasovacieHeslo(String stareHeslo, String noveHeslo) {
        if (stareHeslo.equals(this.prihlasovacieHeslo)) {
            this.setPrihlasovacieHeslo(noveHeslo);
            return true;
        }
        return false;
    }

    /**
     * Metóda prihlasovacieUdajeSuSpravne s parametrom porovnanie objektového typu Prihlasenie, slúži na porovnanie
     * inštancií prihlasenia, ak sa rovnajú vráti nám true, ak nie false. V metóda porovnáme aj či to je zadaná
     * inštancia tá istá ako naša inštancia a či je zadaná inštancia inštancia triedy Prihlasenie.
     *
     * @param porovnanie prihlasovacie údaje, ktoré chceme porovnať
     * @return true alebo false či sú údaje rovnaké
     */
    public boolean prihlasovacieUdajeSuSpravne(Prihlasenie porovnanie) {
        if (this == porovnanie) {
            return true;
        }

        if (this.prihlasovacieMeno != null && porovnanie.prihlasovacieMeno != null && this.prihlasovacieHeslo != null && porovnanie.prihlasovacieHeslo != null) {
            return this.prihlasovacieMeno.equals(porovnanie.prihlasovacieMeno) &&
                    this.prihlasovacieHeslo.equals(porovnanie.prihlasovacieHeslo);
        }

        return false;
    }

}

