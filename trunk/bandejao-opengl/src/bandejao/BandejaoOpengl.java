package bandejao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
        piso = new Piso(drawable);      
        catraca = new Catraca(drawable);
		extintor = new Extintor(drawable);
        maquinaCartao = new MaquinaCartao(drawable);
		criarMesas(drawable);
        gradeEntrada = new GradeEntrada(drawable);
        gradeMaquinaCartao = new GradeMaquinaCartao(drawable);
        gradeMaquinaCartao2 = new GradeMaquinaCartao2(drawable);
		lixeira = new Lixeira(drawable);
        portaFechada = new PortaFechada(drawable);
        portaAberta = new PortaAberta(drawable);
        paredeEntrada1 = new ParedeEntrada1(drawable);
        paredeEntrada2 = new ParedeEntrada2(drawable);
		
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
        
        piso.desenha(drawable);
        gradeMaquinaCartao.desenha(drawable);
        gradeMaquinaCartao2.desenha(drawable);
		extintor.desenha(drawable);

		gl.glTranslatef(-5f,0,2.5f);
		lixeira.desenha(drawable);
		gl.glTranslatef(5f,0,-2.5f);


		desenharMesas(drawable);
        
        gl.glTranslatef(0.0f, 1.627f, 0.0f);
        maquinaCartao.desenha(drawable);
        gradeEntrada.desenha(drawable);
        portaFechada.desenha(drawable);
        portaAberta.desenha(drawable);
        paredeEntrada1.desenha(drawable);
        paredeEntrada2.desenha(drawable);


        gl.glRotatef(angle, -1.0f, -1.0f, 0.0f);
        catraca.desenha(drawable);

        gl.glRotatef(120, -1.0f, -1.0f, 0.0f);
        catraca.desenha(drawable);

        gl.glRotatef(-240, -1.0f, -1.0f, 0.0f);
        catraca.desenha(drawable);
              
        gl.glFlush(); //execute all commands


    }
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {   
    }
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
	/**
	 * Cria as mesas no ArrayList
	 * @param drawable
	 */
	private void criarMesas(GLAutoDrawable drawable){
		mesas_ = new ArrayList();
		Mesa mesa;

        mesa = new Mesa(drawable, -10.0f, 3.5f);
		mesas_.add(mesa);

        mesa = new Mesa(drawable, -10.0f, 7.0f);
        mesas_.add(mesa);

	}
	/**
	 * Percorre o ArrayList e desenha (de perto dos �rmarios para perto da cozinha) as mesas.
	 *
	 * @param drawable
	 */
	private void desenharMesas(GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		Iterator it;	//Iterador
		int i=0;		//Vari�vel auxiliar que ajudar� a transladar de volta ao local correto*/
		int fileira=0;	//N�mero da fileira atual
		Mesa mesa;		//V�riavel tempor�ria que armazenar� as mesas

		/*Medidas referentes �s mesas*/
		final float ESPACO_ENTRE_MESAS = 3.5f;
		final float ESPACO_ENTRE_FILEIRAS = 4f;
		final float ESPACO_FILEIRA_PRINCIPAL = 5f;

		//Cria iterador
		it = mesas_.iterator();

		/*Primeira Fileira*/
		gl.glTranslatef(-10f,0f,0f);	//Mesa Inicial
		for(i=0; it.hasNext() && i<2; i++){
			gl.glTranslatef(0f, 0f, ESPACO_ENTRE_MESAS);
				mesa = (Mesa) it.next();
				mesa.desenha(drawable);
		}
			gl.glTranslatef(10f,0f,-7.0f);

			/*
			fileira++;
		gl.glTranslatef(10f, 0f, i*ESPACO_ENTRE_MESAS-15);
		gl.glTranslatef(-10f-ESPACO_ENTRE_FILEIRAS,0f,15f);

			for(i=0; it.hasNext() && i<4; i++){
				gl.glTranslatef(0f, 0f, -ESPACO_ENTRE_MESAS);
				mesa = (Mesa) it.next();
				mesa.desenha(drawable);
		}
		gl.glTranslatef(10f+ESPACO_ENTRE_FILEIRAS, 0f, i*ESPACO_ENTRE_MESAS-15);
			*/
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
        float[] luzEspecular = new float[]{0.4f, 0.4f, 0.4f, 1.0f};
        float[] posicaoLuz = new float[]{50.0f, 20.0f, 100.0f, 0.0f};
        float[] posicaoLuz2 = new float[]{-50.0f, 10.0f, -100.0f, 0.0f};

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, luzDifusa, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, luzEspecular, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posicaoLuz, 0);
        
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, luzDifusa, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, luzEspecular, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, posicaoLuz2, 0);

        //Define que os dois lados de um plano ser�o iluminados
        gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_TRUE);

        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_LIGHT1);
    }
    
    public static boolean conflitoModelo(float x_camera, float z_camera, float delta) {
        Mesa mesa;
        for(Iterator it=mesas_.iterator(); it.hasNext();){
            mesa = (Mesa) it.next();

            if(mesa.conflito(x_camera, z_camera))
                return mesa.conflito(x_camera, z_camera);
        }

        if(maquinaCartao.conflito(x_camera, z_camera))
            return maquinaCartao.conflito(x_camera, z_camera);

        else if(gradeMaquinaCartao.conflito(x_camera, z_camera))
            return gradeMaquinaCartao.conflito(x_camera, z_camera);

        else if(gradeMaquinaCartao2.conflito(x_camera, z_camera))
            return gradeMaquinaCartao2.conflito(x_camera, z_camera);

        else if(portaFechada.conflito(x_camera, z_camera))
            return portaFechada.conflito(x_camera, z_camera);

        else if(portaAberta.conflito(x_camera, z_camera))
            return portaAberta.conflito(x_camera, z_camera);

        else if(paredeEntrada1.conflito(x_camera, z_camera))
            return paredeEntrada1.conflito(x_camera, z_camera);

        else if(paredeEntrada2.conflito(x_camera, z_camera))
            return paredeEntrada2.conflito(x_camera, z_camera);

        else if(catraca.conflito(x_camera, z_camera)){
            if(delta <= 0 && angle <= 121)
                angle += 10;
            else if(delta >= 0 && angle >= -1)
                angle -= 10;

            System.out.printf("\n%f",angle);

            return false;
        }
        else
            return false;
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
    private Piso piso;
    private static Catraca catraca;
	private static Extintor extintor;
    private static MaquinaCartao maquinaCartao;
	private static ArrayList mesas_;
    private static GradeMaquinaCartao gradeMaquinaCartao;
    private static GradeMaquinaCartao2 gradeMaquinaCartao2;
    private static GradeEntrada gradeEntrada;
	private static Lixeira lixeira;
    private static PortaFechada portaFechada;
    private static PortaAberta portaAberta;
    private static ParedeEntrada1 paredeEntrada1;
    private static ParedeEntrada2 paredeEntrada2;
    private static float angle = 0;

    private float deltaX;
    private float deltaZ;
}

