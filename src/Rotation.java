
public class Rotation {
    private int azimuthIni;
    private int zenithIni;
    private int azimuthAppliquer;
    private int zenithAppliquer;

    //constructeur de la classe Rotation
    public Rotation(int a, int h) { // a et h sont des angles qui sont définis lors de la calibration de nos moteurs
        this.azimuthIni=a;
        this.zenithIni=h;
    }

    /***
     * La classe calcul permet, à partir des angles fournis en entrée et de la position initial du moteur, de calculer l'angle à appliquer au moteur en sorti
     ***/

    public void calcul(int azimuth, int zenith ) {
        azimuthAppliquer=azimuth-azimuthIni;
        zenithAppliquer=zenith-zenithIni;



        zenithIni=zenithAppliquer;
        azimuthIni=azimuthAppliquer;
    }


}