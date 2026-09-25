package sk.uniza.fri;

import sk.uniza.fri.banka.Banka;
import sk.uniza.fri.gui.prihlasovanie.PrihlasovacieOkno;
import sk.uniza.fri.osoby.Zakaznik;
import sk.uniza.fri.osoby.Zamestnanec;

import javax.swing.UIManager;

/**
 * Trieda BankovaAplikacia reprezenzuje samotnú aplikáciu.
 * Obsahuje iba konštruktor, nemá žiadne atribúty
 */
public class BankovaAplikacia {

    /**
     * Konštruktor triedy BankovaAplikacia, nemá žiadne parametre, nastavuje tému GUIčka,
     * vytvára inštanciu banky, zákazníka a zamestnanca, zároveň ich zaradí do banky a spustí prihlasovacie okno ako
     * hlavnú časť GUI.
     */
    public BankovaAplikacia() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Nepodarilo sa načítať tému :(");
        }

        Banka banka = new Banka("SK", "68", "0720");
        Zakaznik zakaznik = new Zakaznik("Jan", "Karol", 25, "janko", "1111", "0000");
        Zakaznik zakaznik2 = new Zakaznik("Peter", "Karol", 35, "petko", "1111", "0000");
        Zamestnanec zamestnanec = new Zamestnanec(banka, "admin", "admin");

        zakaznik.zriadStudentskyUcet(banka);
        zakaznik2.zriadStudentskyUcet(banka);
        zakaznik2.zriadZakladnyUcet(banka);
        banka.zamestnajZamestnanca(zamestnanec);

        zakaznik.zriadPodnikatelskyUcet(banka, "test s.r.o.", "1000");
        zakaznik.zriadSporiaciUcet(banka);
        zakaznik.zriadZakladnyUcet(banka);

        PrihlasovacieOkno prihlasovacieOkno = new PrihlasovacieOkno(banka);
    }
}
