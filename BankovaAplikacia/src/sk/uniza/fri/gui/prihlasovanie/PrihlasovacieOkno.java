package sk.uniza.fri.gui.prihlasovanie;

import sk.uniza.fri.banka.Banka;
import sk.uniza.fri.gui.registracia.RegistrovanieOkno;
import sk.uniza.fri.gui.zakaznik.ZakaznikOkno;
import sk.uniza.fri.gui.zamestnanec.ZamestnanecOkno;
import sk.uniza.fri.osoby.Zakaznik;
import sk.uniza.fri.osoby.Zamestnanec;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Trieda reprezentuje prihlasovacie okno, cez ktoré sa prihlasuje zamestnanec alebo zákazník
 */
public class PrihlasovacieOkno {

    private JFrame okno;
    private JPanel panel;
    private JLabel nazovOkna;
    private JTextField menoPole;
    private JPasswordField hesloPole;
    private JButton tlacidloPrihlasit;
    private JButton tlacidloZrusit;
    private JButton registrovatB;
    private Zakaznik prihlZakaznik;
    private Zamestnanec prihlZamestnanec;
    private final Banka banka;

    /**
     * Parametrický konštruktor triedy PrihlasovacieOkno
     *
     * @param banka banka
     */
    public PrihlasovacieOkno(Banka banka) {
        this.banka = banka;

        this.nastavOkno();
        this.nastavTlacidla();
    }

    private void nastavOkno() {
        this.okno = new JFrame("Prihlasenie");
        this.okno.setContentPane(this.panel);

        this.okno.setVisible(true);
        this.okno.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.okno.setLocationRelativeTo(null);
        this.okno.pack();
    }

    /**
     * Metódy slúži na vypnutie a zahodenie prihlasovacieho okna
     */
    public void vypniOkno() {
        this.okno.setVisible(false);
        this.okno.dispose();
    }

    private void nastavTlacidla() {
        this.tlacidloPrihlasit.addActionListener(e -> {
            String prihlMeno = this.menoPole.getText();
            String prihlHeslo = this.hesloPole.getText();

            this.prihlZakaznik = this.banka.prihlasZakaznika(prihlMeno, prihlHeslo);
            this.prihlZamestnanec = this.banka.prihlasZamestnanca(prihlMeno, prihlHeslo);

            if (this.prihlZakaznik != null) {
                ZakaznikOkno zakaznikOkno = new ZakaznikOkno(this.banka, this.prihlZakaznik);
                this.vypniOkno();
            } else if (this.prihlZamestnanec != null) {
                ZamestnanecOkno zamestnanecOkno = new ZamestnanecOkno(this.banka, this.prihlZamestnanec);
                this.vypniOkno();
            }
        });

        this.tlacidloZrusit.addActionListener(e -> {
            this.menoPole.setText("");
            this.hesloPole.setText("");
            this.prihlZamestnanec = null;
            this.prihlZakaznik = null;
        });

        this.registrovatB.addActionListener(e -> {
            RegistrovanieOkno registrovanieOkno = new RegistrovanieOkno(this.banka);
        });
    }
}
