package engine.items;

import engine.items.GameItem;

public class MoteurSeul extends GameItem {

    //Caractéristique propre du moteur
    private String nom;             // Nom du moteur ( haut ou bas )
    private double capacite     ;   // Capacité en tours/min du moteur
    private float  angle_initial ; // La valeur de l'angle initial
    private final double laps = 0.5;// La valeur d'intervalle de temps pendant lequel est calculé le moteur

    //Les paramètres variables du moteur
    private boolean marche ;   // Le moteur est en marche ou non
    private float angle ;    // La valeur courante de l'angle
    private double puissance; // La valeur de la puissance envoyer au moteur en %

    //Constante pour l'asservissement PID
    private double Kp ;
    private double Ki ;
    private double Kd ;

    public MoteurSeul(String name , float angle_ini, double cap){       // On initialise notre moteur au angles de référence et sans puissance
        super();
        nom = name;
        capacite = cap;
        angle_initial = angle_ini;
        angle = angle_initial;
        puissance = 0;
        marche = false;
        Kp =1;
        Ki =0;
        Kd =0;
    }

    public void tournerHorizontalement(GameItem[] itemsToTurn) {
        System.out.println("classe MoteurSeul zenith : " + angle);
        for (GameItem item : itemsToTurn) {
            item.getRotation().z = angle;
        }
    }

    public void tournerVerticalement(GameItem[] itemsToTurn) {
       // System.out.println("Azimut nouvelle position angle : " + angle);
        for (GameItem item : itemsToTurn) {
            item.getRotation().y = angle;
        }
    }

    public void setMoteur(double Angle_Objectif){
        double erreur = Angle_Objectif - this.getAngle() ;
        double erreurAvant = erreur ; //Stockage de la variable erreur pour le Kd
        double variation_erreur = (erreurAvant - erreur)/laps;    //On mets à jour la correction dérivé
        double somme_erreur = 0;
        somme_erreur = somme_erreur + erreur ;
        puissance =  erreur*Kp +Ki*somme_erreur + Kd*variation_erreur ; //PID
        setPuissance(puissance);
    }

    public void setPuissance ( double Puis ) {
        angle = (float) (angle + ((capacite*360.0*laps)/(60000.0*100))*Puis);	// 60 000= 1min , laps de temps fixé à 100ms et on reflechit en %
    }
    public float getAngle (){
        return angle ;
    }

    public double getPuissance (){
        return puissance;
    }

    public void setPID ( double kp , double ki, double kd){
        Kp = kp;
        Ki = ki;
        Kd = kd;
    }

    public void setAngle(float angle) {
        if (angle >= 360) angle -= 360;
        if (angle <= 0) angle += 360;
        this.angle = angle;
    }

    public void Stop(){
        this.setPuissance(0);
    }

    public String toString(){
        return "Moteur " + nom +" : Puissance= "+ puissance +" / Angle= " + angle ;
    }
}