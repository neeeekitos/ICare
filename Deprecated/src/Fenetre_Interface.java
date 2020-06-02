import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Fenetre_Interface implements ActionListener {

    private JFrame frame;
    private JPanel panel1;
    private JLabel jLabelEntrer;
    private JLabel jLabelZenith;
    private JPanel panel2;
    private JButton manuelButton;
    private JButton fichierExcelButton;
    private JButton exitButton;
    private JTextField textField1;
    private boolean manuel;
    private boolean affichage;

    private boolean methode_Excel;

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
        frame.setSize(400, 400);
        frame.setVisible(true);
        //pour centrer sur l'ecran
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(this.panel1);
        action();

    }

    public  void action (){
        manuelButton.addActionListener(this);
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
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public boolean isManuel() {
        return manuel;
    }

    public void setManuel(boolean manuel) {
        this.manuel = manuel;
    }

    public boolean isAffichage() {
        return affichage;
    }

    public void setAffichage(boolean affichage) {
        this.affichage = affichage;
    }

    public boolean isMethode_Excel() {
        return methode_Excel;
    }

    public void setMethode_Excel(boolean methode_Excel) {
        this.methode_Excel = methode_Excel;
    }

    public static void main(String[] args)
    {
        Fenetre_Interface fen = new Fenetre_Interface();

    }
}
