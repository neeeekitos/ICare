package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadCSV {
    private boolean pm;
    private double[][] data; // temps | zenith | azimuth
    private int i;
    private int taille;
    private String fileName;
    private File file;

    /***
    * Constructeur de la class game.ReadCSV
     ***/
    public ReadCSV(){
        fileName = "doc/solarOrbit.csv";
        file = new File(fileName);
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
}
