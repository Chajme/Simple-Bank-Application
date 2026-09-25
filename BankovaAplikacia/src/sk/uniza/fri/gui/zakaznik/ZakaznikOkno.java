package sk.uniza.fri.gui.zakaznik;

import sk.uniza.fri.banka.Banka;
import sk.uniza.fri.gui.prihlasovanie.PrihlasovacieOkno;
import sk.uniza.fri.osoby.Zakaznik;
import sk.uniza.fri.ucty.Podnikatelsky;
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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Trieda reprezenzuje okno, ktoré vidí prihlásený zákazník po príklásení
 */
public class ZakaznikOkno {
    private JButton odhlasitB;
    private JButton zmenUdajeB;
    private JButton zmenPinB;
    private javax.swing.JList<Ucet> zoznamUctovList;
    private JLabel zostatokL;
    private JLabel menoL;
    private JPanel panel;
    private JButton zriadUcetB;
    private JButton obnovitB;
    private JFrame okno;
    private final JPopupMenu menu;
    private final DefaultListModel<Ucet> model;
    private final Banka banka;
    private final Zakaznik prihlZakaznik;

    /**
     * Parametrický konštruktor triedy
     *
     * @param banka    banka
     * @param zakaznik prihlásený zákazník
     */
    public ZakaznikOkno(Banka banka, Zakaznik zakaznik) {
        this.banka = banka;
        this.prihlZakaznik = zakaznik;

        this.menu = new JPopupMenu();
        this.model = new DefaultListModel<>();
        this.zoznamUctovList.setModel(this.model);

        this.nastavOkno();
        this.nacitajZoznam();
        this.nastavMys();
        this.nastavMenu();
        this.nastavTlacidla();
        this.nastavLable();
    }

    private void nastavOkno() {
        this.okno = new JFrame("Bankova Aplikacia");
        this.okno.setContentPane(this.panel);

        this.okno.setVisible(true);
        this.okno.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.okno.setLocationRelativeTo(null);
        this.okno.setPreferredSize(new Dimension(500, 300));
        this.okno.pack();
    }

    private void nastavTlacidla() {
        this.odhlasitB.addActionListener(e -> {
            this.vypniOkno();
            PrihlasovacieOkno prihlasovacieOkno = new PrihlasovacieOkno(this.banka);
        });

        this.zmenUdajeB.addActionListener(e -> {
            ZmenaUdajovOkno zmenaUdajovOkno = new ZmenaUdajovOkno(this.prihlZakaznik, this.banka);
        });

        this.zmenPinB.addActionListener(e -> {
            ZmenPinOkno zmenPinOkno = new ZmenPinOkno(this.prihlZakaznik);
        });

        this.zriadUcetB.addActionListener(e -> {
            ZriadUcetOkno zriadUcetOkno = new ZriadUcetOkno(this.banka, this.prihlZakaznik);
        });

        this.obnovitB.addActionListener(e -> this.nacitajZoznam());
    }

    private void nastavLable() {
        this.menoL.setText(this.prihlZakaznik.getCeleMeno());
        this.zostatokL.setText(this.prihlZakaznik.getCelkovyZostatok() + " EUR");
    }

    private void nastavMenu() {
        JMenuItem vyber = new JMenuItem("Výber");
        JMenuItem vklad = new JMenuItem("Vklad");
        JMenuItem posielanie = new JMenuItem("Pošli");
        JMenuItem zmaz = new JMenuItem("Zmaž účet");
        JMenuItem pohyby = new JMenuItem("Pohyby");
        JMenuItem podrobnosti = new JMenuItem("Podrobnosti");

        vyber.addActionListener(e -> this.otvorOknoVyber());

        vklad.addActionListener(e -> this.otvorOknoVklad());

        posielanie.addActionListener(e -> this.otvorOknoPosli());

        pohyby.addActionListener(e -> this.otvorOknoPohyby());

        podrobnosti.addActionListener(e -> this.otvorOknoPodrobnosti());

        zmaz.addActionListener(e -> {
            if (this.prihlZakaznik.getUcty().size() == 1) {
                JOptionPane.showMessageDialog(this.okno, "Nemôžeš zrušiť účet, ak máš iba jeden!");
            } else {
                Ucet vybranyUcet = this.zoznamUctovList.getSelectedValue();
                int volba = JOptionPane.showConfirmDialog(this.okno, "Naozaj chceš odstrániť tento účet?");

                if (volba == JOptionPane.YES_OPTION) {
                    this.prihlZakaznik.zrusUcetVBanke(this.banka, vybranyUcet);
                    this.model.removeElement(vybranyUcet);
                    this.aktualizujZostatok();
                }
            }
        });

        this.menu.add(vyber);
        this.menu.add(vklad);
        this.menu.add(posielanie);
        this.menu.add(zmaz);
        this.menu.add(pohyby);
        this.menu.add(podrobnosti);
    }

    private void nastavMys() {
        this.zoznamUctovList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = ZakaznikOkno.this.zoznamUctovList.locationToIndex(e.getPoint());
                    ZakaznikOkno.this.zoznamUctovList.setSelectedIndex(index);
                    ZakaznikOkno.this.menu.show(ZakaznikOkno.this.zoznamUctovList, e.getX(), e.getY());
                }
            }
        });
    }

    private void otvorOknoPodrobnosti() {
        Ucet vybranyUcet = this.zoznamUctovList.getSelectedValue();

        if (vybranyUcet instanceof Podnikatelsky) {
            int volba = JOptionPane.showConfirmDialog(this.okno, "Chceš zmeniť údaje?");
            if (volba == JOptionPane.YES_OPTION) {
                JTextField identifikacia = new JTextField();
                JTextField limitPrecerpania = new JTextField();

                Object[] parametre = {"Identifikácia", identifikacia, "Limit Prečerpania", limitPrecerpania};
                int volba2 = JOptionPane.showConfirmDialog(this.okno, parametre, "Zmena údajov", JOptionPane.OK_CANCEL_OPTION);

                if (volba2 == JOptionPane.OK_OPTION) {
                    ((Podnikatelsky)vybranyUcet).setIdentifikacia(identifikacia.getText());
                    ((Podnikatelsky)vybranyUcet).setLimitPrecerpania(limitPrecerpania.getText());
                }
            }
        }

        JOptionPane.showMessageDialog(this.okno, vybranyUcet.dajPopis());
    }

    private void otvorOknoPohyby() {
        Ucet vybranyUcet = this.zoznamUctovList.getSelectedValue();
        PohybyOkno pohybyOkno = new PohybyOkno(vybranyUcet);
    }

    private void otvorOknoVyber() {
        JTextField obnos = new JTextField();
        JPasswordField pin = new JPasswordField();

        Ucet vybranyUcet = this.zoznamUctovList.getSelectedValue();
        int index = this.zoznamUctovList.getSelectedIndex();

        Object[] zadaneParametre = {"Obnos", obnos, "PIN", pin};
        int volba = JOptionPane.showConfirmDialog(this.okno, zadaneParametre, "Výber", JOptionPane.OK_CANCEL_OPTION);

        if (volba == JOptionPane.OK_OPTION) {
            this.prihlZakaznik.vyberPeniaze(pin.getText(), obnos.getText(), vybranyUcet);

            this.aktualizujZostatok();
            this.aktualizujZoznam(vybranyUcet, index);
        }
    }

    private void otvorOknoVklad() {
        Ucet vybranyUcet = this.zoznamUctovList.getSelectedValue();
        int index = this.zoznamUctovList.getSelectedIndex();

        String obnos = JOptionPane.showInputDialog(this.okno, "Zadaj obnos");

        try {
            this.prihlZakaznik.vlozPeniaze(obnos, vybranyUcet);
            this.aktualizujZostatok();
            this.aktualizujZoznam(vybranyUcet, index);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.okno, "Nezadal si obnos");
        }
    }

    private void otvorOknoPosli() {
        Ucet vybranyUcet = this.zoznamUctovList.getSelectedValue();
        int index = this.zoznamUctovList.getSelectedIndex();

        PosielaniePenaziOkno posielaniePenaziOkno = new PosielaniePenaziOkno(this.banka, this.prihlZakaznik, vybranyUcet);
        this.aktualizujZostatok();
        this.aktualizujZoznam(vybranyUcet, index);
    }

    private void nacitajZoznam() {
        this.model.clear();

        for (Ucet ucet : this.prihlZakaznik.getUcty().values()) {
            this.model.addElement(ucet);
        }
    }

    private void aktualizujZostatok() {
        this.zostatokL.setText(this.prihlZakaznik.getCelkovyZostatok() + " EUR");
    }

    private void aktualizujZoznam(Ucet vybranyUcet, int index) {
        this.model.removeElement(vybranyUcet);
        this.model.add(index, vybranyUcet);
    }

    private void vypniOkno() {
        this.okno.setVisible(false);
        this.okno.dispose();
    }
}
