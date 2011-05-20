package objetos;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        } catch (IOException ex) {
            Logger.getLogger(Mesa.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.facetNormals();

        objectid = gl.glGenLists(1);
        objectid = model.compile(drawable, JWavefrontModel.WF_FLAT | JWavefrontModel.WF_MATERIAL );
    }

    public void desenha(GLAutoDrawable drawable){
        GL gl = drawable.getGL();

        //Teste para ver se o modelo estiver dentro da visão visão da câmera
        if(model.inside(drawable) == true)
            gl.glCallList(objectid);
    }

    private JWavefrontModel model;
    private int objectid;
}