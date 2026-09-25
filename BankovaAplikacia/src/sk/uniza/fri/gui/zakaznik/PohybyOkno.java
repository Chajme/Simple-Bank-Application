package sk.uniza.fri.gui.zakaznik;

import sk.uniza.fri.ucty.Ucet;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;

/**
 * Trieda reprezentuje okno pre zobrazenie zoznamu pohybov účtu zákazníka
 */
public class PohybyOkno {
    private JFrame okno;
    private JPanel panel;
    private javax.swing.JList<String> zoznamPohybov;
    private final Ucet ucet;

    /**
     * Parametrický konštruktor triedy
     *
     * @param ucet účet, ktorého pohyby chceme zobraziť
     */
    public PohybyOkno(Ucet ucet) {
        this.ucet = ucet;

        this.nastavOkno();
        this.nacitajZoznamPohybov();
    }

    private void nacitajZoznamPohybov() {
        DefaultListModel<String> model = new DefaultListModel<>();

        this.zoznamPohybov.setModel(model);

        model.clear();

        for (String pohyb : this.ucet.getPohyby()) {
            model.addElement(pohyb + " EUR");
        }
    }

    private void nastavOkno() {
        this.okno = new JFrame("Bankova Aplikacia");
        this.okno.setContentPane(this.panel);

        this.okno.setVisible(true);
        this.okno.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.okno.setLocationRelativeTo(null);
        this.okno.setPreferredSize(new Dimension(200, 300));
        this.okno.pack();
    }
}
