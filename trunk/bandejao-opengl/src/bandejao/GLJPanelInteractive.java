package bandejao;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

/**
 * Onde ocorre todas as definições do JFrame,GLJPanel,e eventos do mouse e teclado
 *
 */

public abstract class GLJPanelInteractive extends GLJPanel {
    public static final float WINDOW_SIZE = 5;

    //Adicionamos os eventos do GLListener, teclado, mouse ao GLJPanel
    public GLJPanelInteractive(GLCapabilities glcap,JFrame frame) {
        glcap = null;
       
        addGLEventListener(new GLListener());
        
        RotacaoListener rotacaoListener = new RotacaoListener();
        frame.addMouseMotionListener(rotacaoListener);
        frame.addKeyListener(rotacaoListener);
		//Posicoes iniciais para testar sem ter que fica andando

		//x_camera = -30f;
		//z_camera = -20f;
    }

    public abstract void init(GLAutoDrawable drawable);
    public abstract void display(GLAutoDrawable drawable);
    public abstract void reshape(GLAutoDrawable drawable, int x, int y, int w, int h);
    public abstract void displayChanged(GLAutoDrawable drawable, boolean bln, boolean bln1);

    //Define a projeção da nossa cena
    private void defineVisualParameters(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(90.0f, width/height, 0.1, 100.0);
    }

    public class GLListener implements GLEventListener {

        public void init(GLAutoDrawable drawable) {
            viewport = new int[4];
            mvmatrix = new double[16];
            projmatrix = new double[16];

            //Transfere a seta do mouse para o centro
            try {
                center = new Point();
                robot = new java.awt.Robot();
            } catch (AWTException ex) { }

            Toolkit toolkit =  Toolkit.getDefaultToolkit ();
            Dimension dim = toolkit.getScreenSize();

            center.x = dim.width / 2;
            center.y = dim.height / 2;
            robot.mouseMove(center.x, center.y);

            //Oculta o ponteiro do mouse
            Image cursorImage = Toolkit.getDefaultToolkit().getImage("xparent.gif");
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point( 0, 0), "" );
            setCursor( blankCursor );

            GLJPanelInteractive.this.init(drawable);
        }

        //Eventos que ocorrem na nossa cena
        public void display(GLAutoDrawable drawable) {
            GL gl = drawable.getGL();
            GLU glu = new GLU();

            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glLoadIdentity();

            //Movimentação da câmera
            glu.gluLookAt(x_camera, 2.4, z_camera, (raio)*(Math.sin(angle))+(z_camera)*(Math.sin(angle)), y_camera, (-raio)*(Math.cos(angle))+(z_camera)*(Math.cos(angle)),  0, 1, 0);

            gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
            gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, mvmatrix, 0);
            gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projmatrix, 0);

            defineVisualParameters(drawable);

            GLJPanelInteractive.this.display(drawable);
        }

        //Onde ocorre os tratamentos para quando mudamos a resolução do JFrame
        public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
            GL gl = drawable.getGL();
            if (h == 0) {
                h = 1;
            }

            width = w;
            height = h;

            gl.glViewport(0, 0, w, h);            
            defineVisualParameters(drawable);

            GLJPanelInteractive.this.reshape(drawable, x, y, w, h);
        }

        public void displayChanged(GLAutoDrawable drawable, boolean bln, boolean bln1) {
            GLJPanelInteractive.this.displayChanged(drawable, bln, bln1);
        }

    }

    //Eventos do mouse e do teclado
    public class RotacaoListener extends KeyAdapter implements MouseMotionListener {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
		case KeyEvent.VK_W://faz zoom-in                    
                    z_cameraAux = (float) (z_camera - 0.035 * (Math.cos(angle)));
                    x_cameraAux = (float) (x_camera + 0.035 * (Math.sin(angle)));
                    
                    delta = z_cameraAux - z_camera;
                    
                    conflito = BandejaoOpengl.conflitoModelo(x_cameraAux, z_cameraAux, delta);

                    if(!conflito){
                        z_camera -= 0.0315*(Math.cos(angle));
                        x_camera += 0.0315*(Math.sin(angle));
                        
                        repaint();
                    }
                    break;
		case KeyEvent.VK_S://faz zoom-out
                    z_cameraAux = (float) (z_camera + 0.035 * (Math.cos(angle)));
                    x_cameraAux = (float) (x_camera - 0.035 * (Math.sin(angle)));

                    delta = z_cameraAux - z_camera;
                    
                    conflito = BandejaoOpengl.conflitoModelo(x_cameraAux, z_cameraAux, delta);

                    if(!conflito){
                        z_camera += 0.0315*(Math.cos(angle));
                        x_camera -= 0.0315*(Math.sin(angle));
                        
                        repaint();
                    }
                    break;
            }
        }

        public void mouseMoved(MouseEvent e) {
            if(x0 == -1){
                x0 = e.getX();
            }
            else{
                x1 = e.getX();
                angle += 0.0085*(x1-x0);
                x0 = x1;
            }

            if(y0 == -1){
                y0 = e.getY();
            }
            else{
                y1 = e.getY();
                y_camera -= 500*(y1-y0);
                y0 = y1;
            }

            repaint();
        }

        public void mouseDragged(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

	public float getX_camera() {
		return x_camera;
	}

	public float getZ_camera() {
		return z_camera;
	}


	
    public float delta;
    public float x_cameraAux = 0.0f;
    public float z_cameraAux = 4.4f;
    public float x_camera = 0.0f;
    public float y_camera = 0.0f;
    public float z_camera = 4.4f;
    public float angle = 0.0f;
    private float raio = 100000;
    private int x0 = -1;
    private int x1;
    private int y0 = -1;
    private int y1;
    private Robot robot;
    private Point center;
    private float height;
    private float width;
    private int viewport[];
    private double mvmatrix[];
    private double projmatrix[];
    private boolean conflito;
}
