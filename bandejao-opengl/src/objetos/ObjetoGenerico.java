package objetos;

import java.io.File;
import java.io.IOException;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import jwavefront.JWavefrontModel;

/**
 * Classe pai de todos os objetos, ou seja, todos os objetos herdar�o dela.
 * @author matheus
 */
public class ObjetoGenerico {

    public ObjetoGenerico(GLAutoDrawable drawable, String caminho_objeto) {
        GL gl = drawable.getGL();

        String filename = "./data/"+caminho_objeto;

        try {
            model = new JWavefrontModel(new File(filename));
        } catch (IOException ex) {}
        model.facetNormals();

        objectid = gl.glGenLists(1);
        objectid = model.compile(drawable, JWavefrontModel.WF_FLAT | JWavefrontModel.WF_MATERIAL );
    }
    
    public ObjetoGenerico(GLAutoDrawable drawable, float delta_X, float delta_Z, String caminho_objeto) {
        GL gl = drawable.getGL();

        String filename = "./data/"+caminho_objeto;

        try {
            model = new JWavefrontModel(new File(filename));
        } catch (IOException ex) {}
        model.facetNormals();

        objectid = gl.glGenLists(1);
        objectid = model.compile(drawable, JWavefrontModel.WF_FLAT | JWavefrontModel.WF_MATERIAL );
        
        delta_x = delta_X;
        delta_z = delta_Z;
    }

    public void desenha(GLAutoDrawable drawable){
        GL gl = drawable.getGL();

        //Teste para ver se o modelo estiver dentro da visão visão da câmera
        if(model.inside(drawable) == true)
            gl.glCallList(objectid);
    }
    
    public boolean conflito(float x_camera, float z_camera){
        return model.conflito(x_camera - delta_x, z_camera - delta_z);
    }

	public float getDelta_x() {
		return delta_x;
	}

	public float getDelta_z() {
		return delta_z;
	}

	

    private JWavefrontModel model;
    private int objectid;
    
    private float delta_x = 0.0f;
    private float delta_z = 0.0f;
}