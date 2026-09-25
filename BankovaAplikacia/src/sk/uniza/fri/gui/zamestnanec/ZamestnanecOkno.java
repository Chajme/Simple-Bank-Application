package sk.uniza.fri.gui.zamestnanec;

import sk.uniza.fri.banka.Banka;
import sk.uniza.fri.gui.prihlasovanie.PrihlasovacieOkno;
import sk.uniza.fri.gui.zakaznik.PohybyOkno;
import sk.uniza.fri.osoby.Zakaznik;
import sk.uniza.fri.osoby.Zamestnanec;
import sk.uniza.fri.ucty.Ucet;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Trieda reprezenzuje okno, ktoré vidí zamestnanec po prihlásení
 */
public class ZamestnanecOkno {
    private JButton odhlasitB;
    private javax.swing.JList<Ucet> zoznamVsUctov;
    private JButton vsetkyFinancieB;
    private JLabel menoL;
    private JPanel panel;
    private javax.swing.JList<Zakaznik> zoznamVsZakaznikov;
    private JButton stiahniPoplatkyB;
    private JButton pripocitajUrokyB;
    private JButton obnovitB;
    private JFrame okno;
    private final DefaultListModel<Ucet> modelUcty = new DefaultListModel<>();
    private final DefaultListModel<Zakaznik> modelZakanici = new DefaultListModel<>();
    private final JPopupMenu menuUcty = new JPopupMenu();
    private final JPopupMenu menuZakaznici = new JPopupMenu();
    private final Banka banka;
    private final Zamestnanec zamestnanec;

    /**
     * Parametrický konštruktor triedy
     *
     * @param banka       banka
     * @param zamestnanec prihlásený zamestnanec
     */
    public ZamestnanecOkno(Banka banka, Zamestnanec zamestnanec) {
        this.banka = banka;
        this.zamestnanec = zamestnanec;

        this.zoznamVsUctov.setModel(this.modelUcty);
        this.zoznamVsZakaznikov.setModel(this.modelZakanici);

        this.nastavOkno();
        this.nastavLable();
        this.nacitajZoznamVsUctov();
        this.nacitajZoznamVsZakaznikov();
        this.nastavTlacidla();
        this.nastavMysUcty();
        this.nastavMenuUcty();
        this.nastavMysZakaznici();
        this.nastavMenuZakaznici();
    }

    private void nastavOkno() {
        this.okno = new JFrame("Bankova Aplikacia");
        this.okno.setContentPane(this.panel);

        this.okno.setVisible(true);
        this.okno.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.okno.setLocationRelativeTo(null);
        this.okno.setPreferredSize(new Dimension(600, 300));
        this.okno.pack();
    }

    private void nastavMenuUcty() {
        JMenuItem zmaz = new JMenuItem("Zmaž účet");
        JMenuItem pohyby = new JMenuItem("Pohyby");
        JMenuItem podrobnosti = new JMenuItem("Podrobnosti");

        zmaz.addActionListener(e -> this.otvorOknoZmazUcetZakaznika());

        pohyby.addActionListener(e -> {
            Ucet vybranyUcet = this.zoznamVsUctov.getSelectedValue();
            PohybyOkno pohybyOkno = new PohybyOkno(vybranyUcet);
        });

        podrobnosti.addActionListener(e -> {
            Ucet vybranyUcet = this.zoznamVsUctov.getSelectedValue();
            JOptionPane.showMessageDialog(this.okno, vybranyUcet.dajPopis());
        });


        this.menuUcty.add(zmaz);
        this.menuUcty.add(pohyby);
        this.menuUcty.add(podrobnosti);
    }

    private void nastavMenuZakaznici() {
        JMenuItem podrobnosti = new JMenuItem("Podrobnosti");
        JMenuItem zmazZakaznika = new JMenuItem("Zmaž");

        podrobnosti.addActionListener(e -> this.otvorOknoPodrobnostiZakaznici());
        zmazZakaznika.addActionListener(e -> this.otvorOknoZmazZakaznika());

        this.menuZakaznici.add(podrobnosti);
        this.menuZakaznici.add(zmazZakaznika);
    }

    private void otvorOknoZmazZakaznika() {
        JPasswordField heslo = new JPasswordField();

        Object[] parametre = {"Heslo", heslo};
        int volba = JOptionPane.showConfirmDialog(this.okno, parametre, "Naozaj chceš odstrániť zákazníka?\nTento krok zruší všetky jeho účty v banke!\nZadaj heslo pre pokračovanie.", JOptionPane.OK_CANCEL_OPTION);

        if (volba == JOptionPane.OK_OPTION && !(heslo.getText().equals(""))) {
            Zakaznik vybranyZakaznik = this.zoznamVsZakaznikov.getSelectedValue();
            this.zamestnanec.zrusZakaznika(vybranyZakaznik, heslo.getText());
            this.nacitajZoznamVsZakaznikov();
            this.nacitajZoznamVsUctov();
        }
    }

    private void otvorOknoZmazUcetZakaznika() {
        JPasswordField heslo = new JPasswordField();

        Object[] parametre = {"Heslo", heslo};
        int volba = JOptionPane.showConfirmDialog(this.okno, parametre, "Naozaj chceš odstrániť zákazníka?\nTento krok zruší všetky jeho účty v banke!\nZadaj heslo pre pokračovanie.", JOptionPane.OK_CANCEL_OPTION);

        if (volba == JOptionPane.OK_OPTION && !(heslo.getText().equals(""))) {
            Ucet vybranyUcet = this.zoznamVsUctov.getSelectedValue();
            this.zamestnanec.zrusUcetZakaznika(vybranyUcet, heslo.getText());
        }
    }

    private void otvorOknoPodrobnostiZakaznici() {
        Zakaznik vybranyZakaznik = this.zoznamVsZakaznikov.getSelectedValue();
        JOptionPane.showMessageDialog(this.okno, vybranyZakaznik.dajPopis() + "\nVlastní " + vybranyZakaznik.getUcty().size() + " účtov");
    }

    private void nastavMysUcty() {
        this.zoznamVsUctov.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = ZamestnanecOkno.this.zoznamVsUctov.locationToIndex(e.getPoint());
                    ZamestnanecOkno.this.zoznamVsUctov.setSelectedIndex(index);
                    ZamestnanecOkno.this.menuUcty.show(ZamestnanecOkno.this.zoznamVsUctov, e.getX(), e.getY());
                }
            }
        });
    }

    private void nastavMysZakaznici() {
        this.zoznamVsZakaznikov.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = ZamestnanecOkno.this.zoznamVsZakaznikov.locationToIndex(e.getPoint());
                    ZamestnanecOkno.this.zoznamVsZakaznikov.setSelectedIndex(index);
                    ZamestnanecOkno.this.menuZakaznici.show(ZamestnanecOkno.this.zoznamVsZakaznikov, e.getX(), e.getY());
                }
            }
        });
    }

    private void nastavTlacidla() {
        this.odhlasitB.addActionListener(e -> {
            PrihlasovacieOkno prihlasovacieOkno = new PrihlasovacieOkno(this.banka);
            this.vypniOkno();
        });

        this.vsetkyFinancieB.addActionListener(e -> JOptionPane.showMessageDialog(this.okno,
                "Banka momentálne disponuje s financiami vo výške: " + this.zamestnanec.vsetkyFinancieBanky() + " EUR"));

        this.pripocitajUrokyB.addActionListener(e -> {
            this.zamestnanec.pripocitajUroky();
            JOptionPane.showMessageDialog(this.okno, "Úroky boli pripočítané");
        });

        this.stiahniPoplatkyB.addActionListener(e -> {
            this.zamestnanec.stiahniPoplatky();
            JOptionPane.showMessageDialog(this.okno, "Poplatky boli stiahnuté");
        });

        this.obnovitB.addActionListener(e -> {
            this.nacitajZoznamVsUctov();
            this.nacitajZoznamVsZakaznikov();
        });
    }

    private void nastavLable() {
        this.menoL.setText(this.zamestnanec.getPrihlasenie().getPrihlasovacieMeno());
    }

    private void nacitajZoznamVsUctov() {
        this.modelUcty.clear();

        for (Ucet ucet : this.banka.getDatabaza().getVsetkyUcty().values()) {
            this.modelUcty.addElement(ucet);
        }
    }

    private void nacitajZoznamVsZakaznikov() {
        this.modelZakanici.clear();

        for (Zakaznik zakaznik : this.banka.getDatabaza().getZakaznici()) {
            this.modelZakanici.addElement(zakaznik);
        }
    }

    private void vypniOkno() {
        this.okno.setVisible(false);
        this.okno.dispose();
    }

}
