package engine.items;

import engine.items.GameItem;
import engine.graph.Material;
import engine.graph.Mesh;
import engine.graph.OBJLoader;
import engine.graph.Texture;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class SkyBox extends GameItem {

    public SkyBox(String objModel, String textureFile) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        Texture skyBoxtexture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.0f));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }
    public SkyBox(String objModel, Texture skyBoxtexture ) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        skyBoxMesh.setMaterial(new Material(skyBoxtexture, 1f));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }
}
