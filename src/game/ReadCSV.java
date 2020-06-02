package game;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadCSV {
    private boolean pm;
    private double[][] data; // temps | zenith | azimuth
    private int i;
    private int taille;
    private String filePath = "";
    private File file;

    /***
    * Constructeur de la class game.ReadCSV
     ***/
    public ReadCSV(){

        filePath = openCSV();
        file = new File(filePath);
        System.out.println("The selected file is: " + filePath);
        System.out.println("ouverture d'explorateur des fichiers");
        pm=false;
        taille=96;
        data = new double[3][taille];
        i=0;
    }

    /***
    * Méthode permettant de renvoyer un tableau contenant de renvoyer un tableau contenant les données intéressantes du document csv
     ***/
    public double[][] getData(){
        try{
            Scanner inputStream = new Scanner(file);
            inputStream.next();
            while(inputStream.hasNext()){
                String donnee = inputStream.next();
                String [] values = donnee.split(";");
                if(values.length>1){
                    if(values[0].equals("0:0")){ // cette condition permet de commencer a enregistrer les données à partir de la première ligne de donnée
                        pm=true;
                    }
                }

                if(pm && i<taille) { // la condition "i<taille" permet de ne pas essayer d'enregistrer des données de cases vides
                    data[0][i]=0.25*i;
                    data[1][i]=Double.parseDouble(values[1]);
                    data[2][i]=Double.parseDouble(values[2]);
                    i++;
                }
            }
            inputStream.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return data;
    }

    public String openCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Le fichier csv", "csv"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        String fileRoute = "";

        int option = fileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (file != null) {
                FileFilter fileFilter = fileChooser.getFileFilter();

                FileNameExtensionFilter fileNameExtensionFilter = (FileNameExtensionFilter) fileFilter;
                String newName = file.getName();
                file = new File(file.getParent(), newName);
                fileRoute = file.getAbsolutePath();
            }
        }

        System.out.println("le fichier csv choisi se trouve : " + fileRoute);
        return fileRoute;
    }
}
