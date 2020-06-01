package game;

import engine.GameEngine;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.cert.Extension;

import static java.awt.SystemColor.desktop;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Fenetre_Interface implements ActionListener {

    private JFrame frame;
    private JPanel panelPrincipal;
    private JLabel jLabelEntrer;
    private JLabel jLabelZenith;
    private JPanel panel2;
    private JButton manuelButton;
    private JButton fichierExcelButton;
    private JButton exitButton;
    private JSpinner spinner;
    private boolean manuel;
    private boolean affichage;
    private ReadCSV lectureExcel;
    private GameEngine gameEngine;

    private double [][] tableauExcel;

    public Fenetre_Interface(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
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
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //pour centrer sur l'ecran
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(this.panelPrincipal);
        action();

    }

    public  void action (){
        manuelButton.addActionListener(this);
        exitButton.addActionListener(this);
        fichierExcelButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == manuelButton) {
            manuel = true;
            affichage = true;
            gameEngine.setAffichage(affichage);
            frame.dispose();
        }
        if (e.getSource() == fichierExcelButton) {
            manuel = false;
            affichage = true;
            gameEngine.setAffichage(affichage);
            Unthread.setIntervalUpdate((Integer) spinner.getValue()); // non null
            frame.dispose();
        }
        if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void stop(){
        System.exit(0);
    }

    public boolean getManuel() {
        return manuel;
    }

    public void setManuel(boolean manuel) {
        this.manuel = manuel;
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
        Fenetre_Interface fen = new Fenetre_Interface(null);
    }
}
