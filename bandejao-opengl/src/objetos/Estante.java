package objetos;

import javax.media.opengl.GLAutoDrawable;

public class Estante extends ObjetoGenerico {
	public Estante(GLAutoDrawable drawable) {
		super(drawable,"Estante.obj");
	}
	public Estante(GLAutoDrawable drawable, float delta_x, float delta_z) {
		super(drawable, delta_x, delta_z,"Estante.obj");
	}
}