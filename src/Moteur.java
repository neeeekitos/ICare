public class Moteur {
    private final float  Theta_ini = 0 ; //La valeur des angles initiaux est une référence elle est fixe
    private final float  Phi_ini = 0 ;

    private float  Theta ;    // La valeur de l'angle du moteur bas
    private float  Phi ;      // La valeur de l'angle du moteur haut
    private float Puissance_bas ; // La valeur de la puissance envoyer au moteur bas
    private float Puissance_haut ; // La valeur de la puissance envoyer au moteur haut

    private float Kp_bas = 1;       // Les constante servant à l'asservissement des moteurs
    private float Ki_bas = 0;
    private float Kd_bas = 0;
    private float Kp_haut = 1;
    private float Ki_haut = 0;
    private float Kd_haut = 0;

    public Moteur (){       // On initialise notre moteur au angles de référence et sans puissance
        Theta = Theta_ini;
        Phi = Phi_ini ;
        Puissance_bas = 0;
        Puissance_haut = 0;
    }

    public void incrementation_bas (){
        float  coef_bas = 2;
        Theta = Theta + Puissance_bas*coef_bas;
    }
    public void incrementation_haut (){
        float coef_haut =1;
        Phi = Phi + Puissance_haut*coef_haut;
    }

    public void setMoteur_bas(float Azimut){
        float erreur = Azimut - this.getTheta() ;
        float erreurAvant = erreur ;	                     //Stockage de la variable erreur pour le Kd
        float variation_erreur = erreurAvant - erreur;    //On mets à jour la correction dérivé
        float somme_erreur = 0;
        somme_erreur = somme_erreur + erreur ;
        Puissance_bas =  erreur*Kp_bas +Ki_bas*somme_erreur + Kd_bas*variation_erreur ; //PID
        this.incrementation_bas();
    }

    public void setMoteur_haut( float Zenith){
        float erreur = Zenith - this.getPhi() ;
        float erreurAvant = erreur ;	                     //Stockage de la variable erreur pour le Kd
        float variation_erreur = erreurAvant - erreur;    //On mets à jour la correction dérivé
        float somme_erreur = 0;
        somme_erreur = somme_erreur + erreur ;
        Puissance_haut =  erreur*Kp_haut +Ki_haut*somme_erreur + Kd_haut*variation_erreur ; //PID
        this.incrementation_haut();
    }

    public float getTheta (){
        return Theta ;
    }
    public float getPhi (){
        return Phi ;
    }
    public float getPuissance_bas (){
        return Puissance_bas ;
    }
    public float getPuissance_haut (){
        return Puissance_haut ;
    }

    public String toString(){
        return "Puissance (bas: " + Puissance_bas+";haut: "+ Puissance_haut+") / Angle (theta: " + Theta+";phi"+Phi+")";
    }
}
