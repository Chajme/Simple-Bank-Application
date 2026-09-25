package sk.uniza.fri.gui.zakaznik;

import sk.uniza.fri.banka.Banka;
import sk.uniza.fri.osoby.Zakaznik;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Dimension;

/**
 * Trieda reprezentuje okno, ktoré umožní prihlásenému zákazníkovi zmeniť svoje prihlasovacie údaje
 */
public class ZmenaUdajovOkno {
    private JFrame okno;
    private JPanel panel;
    private JButton potvrdB;
    private JButton zrusB;
    private JLabel prihlMenoL;
    private JLabel prihlHesloL;
    private JLabel prihlHesloKontrolaL;
    private JTextField noveMenoTF;
    private JPasswordField stareHesloTF;
    private JPasswordField noveHesloTF;

    private final Zakaznik zakaznik;
    private final Banka banka;

    /**
     * Parametrický konštruktor triedy
     *
     * @param zakaznik prihlásený zákazník
     * @param banka    banka
     */
    public ZmenaUdajovOkno(Zakaznik zakaznik, Banka banka) {
        this.zakaznik = zakaznik;
        this.banka = banka;

        this.nastavOkno();
        this.nastavTlacidla();
    }

    private void nastavOkno() {
        this.okno = new JFrame("Zmena údajov");
        this.okno.setContentPane(this.panel);

        this.okno.setVisible(true);
        this.okno.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.okno.setLocationRelativeTo(null);
        this.okno.setPreferredSize(new Dimension(325, 250));
        this.okno.pack();

    }

    private void nastavTlacidla() {
        this.potvrdB.addActionListener(e -> {
            String noveMeno = this.noveMenoTF.getText();
            String noveHeslo = this.noveHesloTF.getText();
            String stareHeslo = this.stareHesloTF.getText();

            if (!(noveMeno.equals(""))) {
                this.zakaznik.zmenPrihlasovacieMeno(this.banka, noveMeno);
                JOptionPane.showMessageDialog(this.okno, "Meno bolo úspešne zmenené");
                this.noveMenoTF.setText("");
            }

            if (!(noveHeslo.equals("") && !(stareHeslo.equals("")))) {
                if (this.zakaznik.zmenPrihlasovacieHeslo(stareHeslo, noveHeslo)) {
                    JOptionPane.showMessageDialog(this.okno, "Heslo bolo úspešne zmenené");
                    this.noveHesloTF.setText("");
                    this.stareHesloTF.setText("");
                } else {
                    JOptionPane.showMessageDialog(this.okno, "Zadal si zlé staré heslo!");
                }
            }
        });

        this.zrusB.addActionListener(e -> {
            this.noveMenoTF.setText("");
            this.noveHesloTF.setText("");
            this.stareHesloTF.setText("");
        });
    }
}
