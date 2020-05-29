package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.lwjgl.*;

public class CommandWindow {
    private JTextField jFieldZenith;
    private JPanel panel1;
    private JTextField jFieldAzimut;
    private JLabel jLabelEntrer;
    private JLabel jLabelZenith;
    private JLabel jLabelAzimut;

    public CommandWindow() {

        jLabelEntrer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new JOptionPane().showMessageDialog(null, "Vous avez entré l'azimut : " + jFieldAzimut.getText() + " et le zénith : " + jFieldZenith.getText() + " .");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                jLabelEntrer.setOpaque(true);
            }
        });
        jLabelEntrer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args)
    {
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

        JFrame frame = new JFrame();
        frame.getContentPane().add(new CommandWindow().panel1);
        frame.setSize(400,400);
        frame.setVisible(true);
        //pour centrer sur l'ecran
        frame.setLocationRelativeTo(null);
    }
}