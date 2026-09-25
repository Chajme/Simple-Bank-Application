package sk.uniza.fri.banka;

import sk.uniza.fri.ucty.Podnikatelsky;
import sk.uniza.fri.ucty.Studentsky;
import sk.uniza.fri.ucty.Ucet;
import sk.uniza.fri.ucty.Zakladny;

/**
 * Trieda SpravdaPoplatkov, spravuje poplatky účtov a pripočítava im úroky
 */
public class SpravcaPoplatkov {

    private final Databaza databaza;

    /**
     * Parametrický konštruktor SpravcaPoplatkov, ktorý zadefinuje atribút this.databaza
     *
     * @param databaza databáza banky
     */
    public SpravcaPoplatkov(Databaza databaza) {
        this.databaza = databaza;
    }

    /**
     * Prechádza účty v banke, ak je účet typu Podnikateľský, Studentsky alebo Zakladny, stiahne mu poplatok za vedenie
     */
    public void stiahniPoplatky() {
        for (Ucet ucet : this.databaza.getVsetkyUcty().values()) {
            if (ucet instanceof Podnikatelsky) {
                ((Podnikatelsky)ucet).stiahniPoplatokZaVedenie();
            } else if (ucet instanceof Studentsky) {
                ((Studentsky)ucet).stiahniPoplatokZaVedenie();
            } else if (ucet instanceof Zakladny) {
                ((Zakladny)ucet).stiahniPoplatokZaVedenie();
            }
        }
    }

    /**
     * Prejde cez všetky účty v databáze banky a pripočíta im úrok
     */
    public void pripocitajUroky() {
        for (Ucet ucet : this.databaza.getVsetkyUcty().values()) {
            ucet.pripocitajUrok();
        }
    }

}
