package sk.uniza.fri.gui.zakaznik;

import sk.uniza.fri.banka.Banka;
import sk.uniza.fri.osoby.Zakaznik;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Trieda reprezentuje okno, ktoré umožní zákazníkovi vytvoriť účet, ktorý si zvolí
 */
public class ZriadUcetOkno {
    private JFrame okno;
    private JPanel panel;
    private JCheckBox zakladnyCB;
    private JCheckBox sporiaciCB;
    private JCheckBox studentskyCB;
    private JCheckBox podnikatelskyCB;
    private JButton potvrdB;
    private JButton zrusB;
    private ButtonGroup skupina;

    private final Banka banka;
    private final Zakaznik zakaznik;

    /**
     * Parametrický konštruktor triedy
     *
     * @param banka    banka
     * @param zakaznik prihlásený zákazník
     */
    public ZriadUcetOkno(Banka banka, Zakaznik zakaznik) {
        this.banka = banka;
        this.zakaznik = zakaznik;

        this.nastavOkno();
        this.nastavCheckBoxy();
        this.nastavTlacidla();
    }

    private void nastavTlacidla() {
        this.potvrdB.addActionListener(e -> {
            if (this.zakladnyCB.isSelected()) {
                this.zakaznik.zriadZakladnyUcet(this.banka);
            } else if (this.sporiaciCB.isSelected()) {
                this.zakaznik.zriadSporiaciUcet(this.banka);
            } else if (this.studentskyCB.isSelected()) {
                this.zakaznik.zriadStudentskyUcet(this.banka);
            } else if (this.podnikatelskyCB.isSelected()) {
                JTextField identifikacia = new JTextField();
                JTextField limitPrecerpania = new JTextField();

                Object[] zadaneParametre = {"Identifikacia", identifikacia, "Limit Prečerpania", limitPrecerpania};
                JOptionPane.showConfirmDialog(this.okno, zadaneParametre, "Novy Podnikatelsky Ucet", JOptionPane.OK_CANCEL_OPTION);

                this.zakaznik.zriadPodnikatelskyUcet(this.banka, identifikacia.getText(), limitPrecerpania.getText());
            }

            this.resetujCheckBoxy();
        });

        this.zrusB.addActionListener(e -> {
            this.okno.setVisible(false);
            this.okno.dispose();
        });
    }

    private void resetujCheckBoxy() {
        this.skupina.clearSelection();
    }

    private void nastavCheckBoxy() {
        this.skupina = new ButtonGroup();
        this.skupina.add(this.zakladnyCB);
        this.skupina.add(this.sporiaciCB);
        this.skupina.add(this.studentskyCB);
        this.skupina.add(this.podnikatelskyCB);
    }

    private void nastavOkno() {
        this.okno = new JFrame("Zriad ucet v banke");
        this.okno.setContentPane(this.panel);

        this.okno.setVisible(true);
        this.okno.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.okno.setLocationRelativeTo(null);
        this.okno.pack();
    }

}
