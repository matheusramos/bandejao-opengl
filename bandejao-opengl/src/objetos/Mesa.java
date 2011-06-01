package objetos;

import javax.media.opengl.GLAutoDrawable;

public class Mesa extends ObjetoGenerico {
	public Mesa(GLAutoDrawable drawable, float delta_x, float delta_z) {
		super(drawable, delta_x, delta_z,"Mesa.obj");
	}
}
