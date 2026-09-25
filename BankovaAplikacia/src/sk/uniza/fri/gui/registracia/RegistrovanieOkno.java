package sk.uniza.fri.gui.registracia;

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

/**
 * Trieda reprezentuje okno pre registráciu nového zákazníka
 */
public class RegistrovanieOkno {
    private JLabel nazovOknaL;
    private JLabel menoL;
    private JLabel priezviskoL;
    private JLabel vekL;
    private JLabel prihlMenoL;
    private JLabel prihlHesloL;
    private JLabel pinL;
    private JTextField menoTF;
    private JTextField priezviskoTF;
    private JTextField vekTF;
    private JPasswordField prihlHesloPF;
    private JPasswordField pinPF;
    private JTextField prihlMenoTF;
    private JButton registrovatB;
    private JButton zrusitB;
    private JPanel panel;
    private JFrame okno;
    private final Banka banka;


    /**
     * Parametrický konštruktor
     *
     * @param banka banka, v ktorej chceme vytvoriť nového zákazníka
     */
    public RegistrovanieOkno(Banka banka) {
        this.banka = banka;

        this.nastavOkno();
        this.nastavTlacidla();
    }

    private void nastavTlacidla() {
        this.registrovatB.addActionListener(e -> {

            String meno = this.menoTF.getText();
            String priezvisko = this.priezviskoTF.getText();
            int vek = Integer.parseInt(this.vekTF.getText());
            String prihlMeno = this.prihlMenoTF.getText();
            String prihlHeslo = this.prihlHesloPF.getText();
            String pin = this.pinPF.getText();

            if (this.banka.getDatabaza().menoPouzite(meno)) {
                JOptionPane.showMessageDialog(this.okno, "Meno je už zabrané, skús iné");
            } else {
                Zakaznik novyZakaznik = new Zakaznik(meno, priezvisko, vek, prihlMeno, prihlHeslo, pin);
                this.banka.getDatabaza().pridajZakaznika(novyZakaznik);
                JOptionPane.showMessageDialog(this.okno, "Nové konto bolo vytvorené");
                this.resetujPolia();
            }
        });

        this.zrusitB.addActionListener(e -> {
            this.okno.setVisible(false);
            this.okno.dispose();
        });
    }

    private void resetujPolia() {
        this.menoTF.setText("");
        this.priezviskoTF.setText("");
        this.vekTF.setText("");
        this.prihlMenoTF.setText("");
        this.prihlHesloPF.setText("");
        this.pinPF.setText("");
    }

    private void nastavOkno() {
        this.okno = new JFrame("Vytvorenie Konta");
        this.okno.setContentPane(this.panel);

        this.okno.setVisible(true);
        this.okno.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.okno.setLocationRelativeTo(null);
        this.okno.pack();
    }
}
