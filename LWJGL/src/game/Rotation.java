package game;

import engine.items.Soleil;

import java.util.Timer;
import java.util.TimerTask;

public class Rotation {
    private int azimuthIni;
    private int zenithIni;
    private int temps;
    private double[][] data;
    private ReadCSV r;
    private int i;
    private boolean manuel;
    MoteurSeul moteurHaut;
    MoteurSeul moteurBas;
    Soleil soleil;

    /**
    * constructeur de la classe game.Rotation
     **/
    public Rotation(int zIni, int aIni, boolean manuel,Soleil soleil) {
        temps=100;
        r = new ReadCSV();
        data = r.getData();
        zenithIni=zIni;
        azimuthIni=aIni;
        moteurHaut = new MoteurSeul("moteurHaut", zenithIni, 170);
        moteurBas = new MoteurSeul("moteurBas", azimuthIni, 170);
        i=0;
        this.manuel = manuel;
        this.soleil = soleil ;

        Timer time= new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(i<96 && manuel == false ) {
                    calcul((int) data[1][i], (int) data[2][i]);
                    soleil.angleSoleil((int) data[1][i], (int) data[2][i]);
                    i++;
                } else {
                    time.cancel();
                }
            }
        },temps,temps);
    }

    /***
     * La classe calcul permet, à partir des angles fournis en entrée et de la position initial du moteur, de calculer l'angle à appliquer au moteur en sorti
     ***/
    public void calcul(int zenith, int azimuth ) {
        System.out.println("zenith : "+zenithIni+" ; azimuth : "+azimuthIni);
        int azimuthAppliquer=azimuth-azimuthIni;
        int zenithAppliquer=zenith-zenithIni;



        zenithIni=zenithIni+zenithAppliquer;
        azimuthIni=azimuthIni+azimuthAppliquer;

        moteurHaut.setAngle(zenithIni);
        moteurBas.setAngle(azimuthIni);
    }

    public int getAzimuthIni(){
        return azimuthIni;
    }

    public int getZenithIniIni(){
        return zenithIni;
    }

    public MoteurSeul getMoteurHaut() {
        return moteurHaut;
    }

    public MoteurSeul getMoteurBas() {
        return moteurBas;
    }

    public static void main(String[] args) {

        Rotation r = new Rotation(40,50, false, null);
    }
}