package sk.uniza.fri.gui.zakaznik;

import sk.uniza.fri.osoby.Zakaznik;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.WindowConstants;
import java.awt.Dimension;

/**
 * Trieda reprezenzuje okno, v ktorom si prihlásený zákazním môže zmeniť pin
 */
public class ZmenPinOkno {
    private JFrame okno;
    private JPanel panel;
    private JPasswordField staryPinPF;
    private JPasswordField novyPinPF;
    private JButton zrusB;
    private JButton ulozB;
    private JLabel staryPinL;
    private JLabel novyPinL;
    private final Zakaznik zakaznik;

    /**
     * Parametrický konštruktor triedy
     *
     * @param zakaznik prihlásený zákazník
     */
    public ZmenPinOkno(Zakaznik zakaznik) {
        this.zakaznik = zakaznik;

        this.nastavOkno();
        this.nastavTlacidla();
    }

    private void nastavTlacidla() {
        this.ulozB.addActionListener(e -> {
            String staryPin = this.staryPinPF.getText();
            String novyPin = this.novyPinPF.getText();

            if (!(staryPin.equals("")) && !(novyPin.equals(""))) {
                if (this.zakaznik.zmenPIN(staryPin, novyPin)) {
                    JOptionPane.showMessageDialog(this.okno, "PIN bol úspešne zmenený");
                    this.staryPinPF.setText("");
                    this.novyPinPF.setText("");
                } else {
                    JOptionPane.showMessageDialog(this.okno, "PIN sa nepodarilo zmeniť");
                }
            }
        });

        this.zrusB.addActionListener(e -> {
            this.staryPinPF.setText("");
            this.novyPinPF.setText("");
        });
    }

    private void nastavOkno() {
        this.okno = new JFrame("Bankova Aplikacia");
        this.okno.setContentPane(this.panel);

        this.okno.setVisible(true);
        this.okno.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.okno.setLocationRelativeTo(null);
        this.okno.setPreferredSize(new Dimension(250, 250));
        this.okno.pack();
    }
}
