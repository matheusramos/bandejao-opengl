package bandejao;

import java.io.IOException;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import objetos.*;

/**
 * BandejaoOpengl.java <BR>
 * PROCURAR LIVRO DA ISABEL MANSOURR
 */
public class BandejaoOpengl extends GLJPanelInteractive{

	public BandejaoOpengl(GLCapabilities glcaps, JFrame frame) {
        //Chamada para o construtor de GLJPanelInteractive
        super(glcaps, frame);
	}

    public void init(GLAutoDrawable drawable) {
		//OpenGl Par�metros: Mudaremos
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_DEPTH_TEST);

        //Compila todos os modelos da cena
        mesa_ = new Mesa(drawable);
        piso_ = new Piso(drawable);
        suco_ = new Suco(drawable);
        pilar_ = new Pilar(drawable);
        grade_ = new Grade(drawable);
        estante_ = new Estante(drawable);
        catraca_ = new Catraca(drawable);
        maquinaCartao_ = new MaquinaCartao(drawable);
        carrinhoComida_ = new CarrinhoComida(drawable);
		camera_ = new Camera(drawable);

        //Devemos colocar lighting em init e em display pois sen�o a luz iria rotacionar junto com a c�mera
        lighting(drawable);
    }

    public void display(GLAutoDrawable drawable) {
         GL gl = drawable.getGL();

        //Cor de Fundo
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        //O m�todo lighting tamb�m � colocado aqui pois sen�o a luz iria girar junto com a c�mera
        lighting(drawable);

        gl.glMatrixMode(GL.GL_MODELVIEW);

        gl.glTranslatef(0.0f, 0.0f, -5.0f);
        piso_.desenha(drawable);
        gl.glTranslatef(0.0f, 0.0f, 5.0f);

        gl.glTranslatef(-8.0f, 0.0f, -2.0f);
        suco_.desenha(drawable);
        gl.glTranslatef(8.0f, 0.0f, 2.0f);

        gl.glTranslatef(-8.0f, 0.0f, -6.0f);
        mesa_.desenha(drawable);
        gl.glTranslatef(8.0f, 0.0f, 6.0f);

        gl.glTranslatef(-4.0f, 0.0f, -8.0f);
        pilar_.desenha(drawable);
        gl.glTranslatef(4.0f, 0.0f, 8.0f);

        gl.glTranslatef(0.0f, 0.0f, -8.0f);
        carrinhoComida_.desenha(drawable);
        gl.glTranslatef(0.0f, 0.0f, 8.0f);

        gl.glTranslatef(-4.0f, 0.0f, 3.0f);
        grade_.desenha(drawable);
        gl.glTranslatef(4.0f, 0.0f, -3.0f);

        gl.glTranslatef(4.0f, 0.0f, 3.0f);
        estante_.desenha(drawable);
        gl.glTranslatef(-4.0f, 0.0f, -3.0f);

		gl.glTranslatef(-3.5f,6.0f,-8.0f);
		camera_.desenha(drawable);
		gl.glTranslatef(3.5f,-6.0f,8.0f);

        gl.glTranslatef(0.0f, 1.627f, 0.0f);
        maquinaCartao_.desenha(drawable);
		


        angle_ += 10;
        gl.glRotatef(angle_, -1.0f, -1.0f, 0.0f);
        catraca_.desenha(drawable);

        gl.glRotatef(120, -1.0f, -1.0f, 0.0f);
        catraca_.desenha(drawable);

        gl.glRotatef(-240, -1.0f, -1.0f, 0.0f);
        catraca_.desenha(drawable);



        gl.glFlush(); //execute all commands
    }
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {   
    }
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    private void lighting(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        //N�o foi preciso colocar a luz ambiente pois nossos modelos j� possuem cor,
        //a luz difusa define qual a cor e a intensidade que ser� refletida pelos modelos, variando de 0 a 1,
        //a luz especular define a intensidade e a cor com que a fonte de luz ser� refletida nos objetos, e essa reflex�o ser�
        //    observada de acordo com a posi��o da luz,
        //e na posi��o da luz, como queremos projetar os raios de luz do sol na cena, o quarto par�metro ser� zero; e os 3 primeiros par�metros definem
        //    onde a luz ser� posicionada, sendo que a dire��o e o sentido da luz sempre ser� para o ponto da origem (0,0,0).

        float[] luzDifusa = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        float[] luzEspecular = new float[]{0.5f, 0.5f, 0.5f, 1.0f};
        float[] posicaoLuz = new float[]{-100.0f, 50f, 50.0f, 0.0f};

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, luzDifusa, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, luzEspecular, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posicaoLuz, 0);

        posicaoLuz = new float[]{100.0f, 50f, -50.0f, 0.0f};

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, luzDifusa, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, luzEspecular, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, posicaoLuz, 0);

        //Define que os dois lados de um plano ser�o iluminados
        gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_TRUE);

        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_LIGHT1);
    }

	public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);

        //Cria o painel de proje��o
        GLCapabilities glcaps = new GLCapabilities();

        glcaps.setDoubleBuffered(true);
        glcaps.setHardwareAccelerated(true);

        BandejaoOpengl viewer = new BandejaoOpengl(glcaps, frame);
        viewer.setOpaque(true);

        frame.getContentPane().add(viewer);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

/*
 * V�riavei de Classe -> Termina��o com o caractere "_"
 */
	private Mesa mesa_;
    private Piso piso_;
    private Suco suco_;
    private Pilar pilar_;
    private Grade grade_;
    private Estante estante_;
    private Catraca catraca_;
	private Camera camera_;
    private CarrinhoComida carrinhoComida_;
    private MaquinaCartao maquinaCartao_;
    private float angle_ = 0;
}

