package game;

public class Unthread extends Thread {
    private Rotation rotateMoteurs;
    private boolean isManualMode;
    private int time;

    public Unthread(Rotation r){
        time=1500;
        rotateMoteurs=r;
    }

    public void run(){
        double[][] data = rotateMoteurs.getData();
        int i=0;
        while(i<data[1].length) {
            if (!isManualMode) {
                rotateMoteurs.calcul((int) data[1][i], (int) data[2][i]);
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

    public void setTime(int time){ this.time=time; }
}
