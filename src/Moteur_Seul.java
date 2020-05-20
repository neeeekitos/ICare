import java.util.Scanner;
import java.util.*;

public class Moteur_Seul {

    //Caractéristique propre du moteur
    private String nom;             // Nom du moteur ( haut ou bas )
    private double capacite     ;   // Capacité en tours/min du moteur
    private double  angle_initial ; // La valeur de l'angle initial
    private double laps ;			// La valeur d'intervalle de temps pendant lequel est calculé le moteur

    //Les paramètres variables du moteur
    private boolean marche ;   // Le moteur est en marche ou non
    private double  angle ;    // La valeur courante de l'angle
    private double Puissance ; // La valeur de la puissance envoyer au moteur en %

    //Constante pour l'asservissement PID
    private double Kp ;
    private double Ki ;
    private double Kd ;

    public Moteur_Seul (String name , double angle_ini, double cap , double l){       // On initialise notre moteur au angles de référence et sans puissance
        nom = name;
        capacite = cap;
        angle_initial = angle_ini ;
        laps = l;
        angle = angle_initial;
        Puissance = 0;
        marche = false ;
        Kp =1;
        Ki =0;
        Kd =0;
    }

    public void setMoteur(double Angle_Objectif){
        double erreur = Angle_Objectif - this.getAngle() ;
        double erreurAvant = erreur ;	                     //Stockage de la variable erreur pour le Kd
        double variation_erreur = (erreurAvant - erreur)/laps;    //On mets à jour la correction dérivé
        double somme_erreur = 0;
        somme_erreur = somme_erreur + erreur ;
        Puissance =  erreur*Kp +Ki*somme_erreur + Kd*variation_erreur ; //PID
        setPuissance(Puissance);
    }

    public void setPuissance ( double Puis ) {
        angle = angle + ((capacite*360.0*laps)/(60000.0*100))*Puis;	// 60 000= 1min , laps de temps fixé à 100ms et on reflechit en %
    }
    public double getAngle (){
        return angle ;
    }
    public double getPuissance (){
        return Puissance ;
    }
    public void setPID ( double kp , double ki, double kd){
        Kp = kp;
        Ki = ki;
        Kd = kd;
    }

    public void Stop(){
        this.setPuissance(0);
    }

    public String toString(){
        return "Moteur " + nom +" : Puissance= "+Puissance+" / Angle= " + angle ;
    }
}