package game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;

public class Fenetre_Interface implements ActionListener {

    private JFrame frame;
    private JPanel panelPrincipal;
    private JLabel jLabelEntrer;
    private JLabel jLabelZenith;
    private JPanel panel2;
    private JButton manuelButton;
    private JButton fichierExcelButton;
    private JButton exitButton;
    private JTextField textField1;
    private boolean manuel;
    private boolean affichage;
    private ReadCSV lectureExcel;

    private double [][] tableauExcel;

    public Fenetre_Interface() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame = new JFrame();
        frame.setSize(700, 400);
        frame.setVisible(true);
        //pour centrer sur l'ecran
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(this.panelPrincipal);
        action();

    }

    public  void action (){
        manuelButton.addActionListener(this);
        exitButton.addActionListener(this);
        fichierExcelButton.addActionListener(this);
        /*jLabelEntrer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //new JOptionPane().showMessageDialog(null, "Vous avez entré l'azimut : " + jFieldAzimut.getText() + " et le zénith : " + jFieldZenith.getText() + " .");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //jLabelEntrer.setOpaque(true);
            }
        });*/
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == manuelButton) {
            manuel = true;
            affichage = true;
        }
        if (e.getSource() == fichierExcelButton) {
            manuel = false;
            affichage = true;
            lectureExcel = new ReadCSV() ;
            tableauExcel = lectureExcel.getData();
//            lectureExcel.lecture();
        }
        if (e.getSource() == exitButton) {
            System.exit(-1);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public boolean getManuel() {
        return manuel;
    }

    public void setManuel(boolean manuel) {
        this.manuel = manuel;
    }

    public boolean getAffichage() {
        return affichage;
    }

    public void setAffichage(boolean affichage) {
        this.affichage = affichage;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public double[][] getTableauExcel() {
        return tableauExcel;
    }

    public void setTableauExcel(double[][] tableauExcel) {
        this.tableauExcel = tableauExcel;
    }

    public static void main(String[] args) {
        Fenetre_Interface fen = new Fenetre_Interface();
    }
}
