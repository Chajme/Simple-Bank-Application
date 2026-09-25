package sk.uniza.fri.gui.zakaznik;

import sk.uniza.fri.IBAN;
import sk.uniza.fri.banka.Banka;
import sk.uniza.fri.osoby.Zakaznik;
import sk.uniza.fri.ucty.Ucet;

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
 * Trieda reprezenzuje posielanie peňazí medzi účtami
 */
public class PosielaniePenaziOkno {
    private JButton posliB;
    private JButton zrusB;
    private JPasswordField pinPF;
    private JTextField kodKrajinyTF;
    private JTextField kontrolneCisliceTF;
    private JTextField kodBankyTF;
    private JTextField predcislieTF;
    private JTextField zaklCisloUctuTF;
    private JLabel ibanL;
    private JLabel pinL;
    private JPanel panel;
    private JLabel obnosL;
    private JTextField obnosTF;
    private JFrame okno;
    private final Banka banka;
    private final Zakaznik zakaznik;
    private final Ucet vybranyUcet;

    /**
     * Parametrický konštruktor triedy
     *
     * @param banka       banka
     * @param zakaznik    prihlásený zákazník
     * @param vybranyUcet vybraný účet, z ktorého posielame peniaze
     */
    public PosielaniePenaziOkno(Banka banka, Zakaznik zakaznik, Ucet vybranyUcet) {
        this.banka = banka;
        this.zakaznik = zakaznik;
        this.vybranyUcet = vybranyUcet;

        this.nastavOkno();
        this.nastavTlacidla();
    }

    private void nastavTlacidla() {
        this.posliB.addActionListener(e -> {
            String kodKrajiny = this.kodKrajinyTF.getText();
            String kontrolneCislice = this.kontrolneCisliceTF.getText();
            String kodBanky = this.kodBankyTF.getText();
            String predcislie = this.predcislieTF.getText();
            String cisloUctu = this.zaklCisloUctuTF.getText();

            String pin = this.pinPF.getText();

            String obnos = this.obnosTF.getText();

            IBAN ibanPrijemcu = new IBAN(kodKrajiny, kontrolneCislice, kodBanky, predcislie, cisloUctu);

            if (this.banka.prevodNaUcet(this.zakaznik, pin, this.vybranyUcet, ibanPrijemcu, obnos) != null) {
                JOptionPane.showMessageDialog(this.okno, "Peniaze boli poslané");
                this.vymazPolia();
            } else {
                JOptionPane.showMessageDialog(this.okno, "Peniaze sa nepodarilo odoslať");
            }
        });

        this.zrusB.addActionListener(e -> this.vymazPolia());
    }

    private void nastavOkno() {
        this.okno = new JFrame("Zmena údajov");
        this.okno.setContentPane(this.panel);

        this.okno.setVisible(true);
        this.okno.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.okno.setLocationRelativeTo(null);
        this.okno.setPreferredSize(new Dimension(700, 200));
        this.okno.pack();
    }

    private void vymazPolia() {
        this.kodKrajinyTF.setText("");
        this.kontrolneCisliceTF.setText("");
        this.kodBankyTF.setText("");
        this.predcislieTF.setText("");
        this.kontrolneCisliceTF.setText("");
        this.zaklCisloUctuTF.setText("");
        this.pinPF.setText("");
        this.obnosTF.setText("");
    }
}
