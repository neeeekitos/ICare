package engine.items;

import engine.Scene;
import engine.SceneLight;
import engine.graph.lights.DirectionalLight;
import engine.graph.lights.PointLight;
import org.joml.Vector3f;
import engine.graph.Mesh;

public class Soleil extends GameItem {

    private Mesh mesh;

    private Scene scene;

    private float zenithSoleil;
    private float azimutSoleil ;
    private float zenithSoleilpourAffichage;

    public Soleil(Mesh mesh, Scene sce){
        super(mesh);
        scene = sce;
        zenithSoleil = 90;
        zenithSoleilpourAffichage = zenithSoleil;
        azimutSoleil = 0;
    }

    public void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0,0,0));

        // Directional Light
        float lightIntensity = (this.getPosition().y+1)/3.5f ;;
        Vector3f lightDirection = new Vector3f(0, 0, 0.0f);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        directionalLight.setShadowPosMult(5);
        directionalLight.setOrthoCords(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 20.0f);
        sceneLight.setDirectionalLight(directionalLight);
        //sceneLight.getDirectionalLight().setIntensity(lightIntensity);

        // Point Light Représentant le soleil
        PointLight[] source = new PointLight[2];
        Vector3f PointlightDirection_0 = new Vector3f(0,0,0); // Soleil principal
        PointLight.Attenuation AttenuationSoleil = new PointLight.Attenuation(0,0,0.7f);
        source [0] = new PointLight(new Vector3f(1,1,1),PointlightDirection_0,lightIntensity);
        source [0].setAttenuation(AttenuationSoleil);
        Vector3f PointlightDirection_1 = new Vector3f(0, -0.2f, 0.5f); // Soleil' éclaire la surface par en dessous sinon il y a de l'ombre sur le soleil
        source [1] = new PointLight(new Vector3f(1,1,1),PointlightDirection_1,lightIntensity);
        sceneLight.setPointLightList(source);
    }

    public void angleSoleil( float Ze, float Az ){

        if (Ze >= 360) Ze -= 360;
        if (Ze <= 0) Ze += 360;

        if (Az >= 360) Az -= 360;
        if (Az <= 0) Az += 360;

        float Rayon = 3.5f;
        float radZe = (float) Math.toRadians(Ze);
        float radAz = (float) Math.toRadians(Az);
        float nouvX = (float)( Math.cos(radZe)*Math.cos(radAz)*Rayon);
        float nouvY = (float)( Math.sin(radZe)*Rayon)-2;
        float nouvZ = (float)( Math.cos(radZe)*Math.sin(radAz)*Rayon);
        this.getPosition().x = nouvX;
        this.getPosition().y = nouvY;
        this.getPosition().z = nouvZ;
        setZenithSoleil(Ze);
        setAzimutSoleil(Az);
    }

    public void setZenithSoleil ( float Ze){
        zenithSoleil = Ze;
        System.out.println("classe Soleil : zenith soleil : "+ Ze);
    }

    public void setAzimutSoleil(float Az) {
        azimutSoleil = Az;
    }

    public void mise_a_jour(){
        Vector3f lightDirection = scene.getSceneLight().getDirectionalLight().getDirection();
        Vector3f PointlightDirection = scene.getSceneLight().getPointLightList(0).getPosition();
        Vector3f PointlightDirection_2 = scene.getSceneLight().getPointLightList(0).getPosition();
        float PositionSoleil = (this.getPosition().y+1)/3.5f ;
        if ( PositionSoleil<-0.5){                             // Le soleil est couché
            scene.getSceneLight().getPointLightList(0).setIntensity(0);
            scene.getSceneLight().getPointLightList(1).setIntensity(0.2f); // on garde une petite luminosité
            scene.getSceneLight().setSkyBoxLight(new Vector3f(0.1f,0.1f,0.1f));
            lightDirection.x = 0;
            lightDirection.y = 0;
            lightDirection.z = 0;
        } else {
            scene.getSceneLight().getPointLightList(0).setIntensity(PositionSoleil/2 + 0.5f); // l'intensité du soleil oscille entre 1 et 0.75
            scene.getSceneLight().getPointLightList(1).setIntensity(PositionSoleil/2 +0.5f);   // l'intensité du soleil' oscille entre 0.6 et 0.2
            lightDirection.x = this.getPosition().x;
            lightDirection.y = PositionSoleil;
            lightDirection.z = this.getPosition().z ;
            PointlightDirection.x = this.getPosition().x;
            PointlightDirection.y = this.getPosition().y;
            PointlightDirection.z = this.getPosition().z;
            PointlightDirection_2.x = this.getPosition().x;
            PointlightDirection_2.y = -0.5f*this.getPosition().y;
            PointlightDirection_2.z = - 0.5f*this.getPosition().z;

        }
        scene.getSceneLight().getPointLightList(0).getAttenuation().setExponent(Math.abs((zenithSoleilpourAffichage/90)+1));
        scene.getSceneLight().getDirectionalLight().setIntensity(PositionSoleil);

       PointlightDirection.normalize();
       lightDirection.normalize();
    }

    public void coucherDeSoleil (){
        float PositionSoleil = this.getPosition().y+1;
        float Xsoleil = this.getPosition().x*0.05f;
        float Zsoleil = this.getPosition().z*0.05f;
        float Coef = PositionSoleil/4.5f;
        Vector3f vecLum = new Vector3f(Coef+Xsoleil+0.6f,Coef+Zsoleil+0.6f,Coef+Zsoleil*Xsoleil+0.6f);
        scene.getSceneLight().setSkyBoxLight(vecLum);
    }

    public float getZenithSoleil() {
        return zenithSoleil;
    }

    public float getAzimutSoleil() {
        return azimutSoleil;
    }

    public float getZenithSoleilpourAffichage() {
        return zenithSoleilpourAffichage;
    }

    public void setZenithSoleilpourAffichage(float zenithSoleilpourAffichage) {
        this.zenithSoleilpourAffichage = zenithSoleilpourAffichage;
    }


}
