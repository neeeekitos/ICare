package game;

import engine.items.Soleil;

public class Unthread extends Thread {
    private Rotation rotateMoteurs;
    private Soleil soleil;
    private boolean isManualMode;
    private static int time;

    public Unthread(Rotation r, Soleil soleil){
        rotateMoteurs=r;
        this.soleil = soleil;

    }

    public void run(){
        double[][] data = rotateMoteurs.getData();
        int i=0;
        while(i<data[1].length) {
            if (!isManualMode) {
                int Ze = (int) data[1][i];
                int Az =  (int) data[2][i];

                soleil.angleSoleil(Ze, Az);
                rotateMoteurs.getMoteurHaut().setAngle(90-Ze);
                rotateMoteurs.getMoteurBas().setAngle(Az);

                i++;
                try {
                    this.sleep(time);

                } catch (InterruptedException ex) {
                }
            } else {
                System.out.print("");// cette ligne permet au thread de ne pas "mourir" lorsqu'on met la mode manuel en pause
            }
        }
    }

    public void setIsManualMode(boolean is){
        isManualMode=is;
    }

    public static void setIntervalUpdate(int seconds) {
        System.out.println(seconds + "intervalle");
        if (seconds == 0) seconds = 1; // default value
        time = seconds*1000; //convertir en ms
    }

    public void setTime(int time){ this.time=time; }
}

