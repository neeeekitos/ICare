
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



        zenithIni=zenithIni+zenithAppliquer;
        azimuthIni=azimuthIni+azimuthAppliquer;
    }

    public int getAzimuthIni(){
        return azimuthIni;
    }

    public int getZenithIniIni(){
        return zenithIni;
    }

    public static void main(String[] args) {

        Rotation r = new Rotation(20, 30);
        r.calcul(50,185);
        System.out.println("azimuth : "+r.getAzimuthIni()+" ; zenith : "+r.getZenithIniIni());
        r.calcul(20,-5);
        System.out.println("azimuth : "+r.getAzimuthIni()+" ; zenith : "+r.getZenithIniIni());
    }
}