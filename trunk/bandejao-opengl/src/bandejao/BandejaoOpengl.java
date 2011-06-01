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
		//OpenGl Parâmetros: Mudaremos
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
		
        //Devemos colocar lighting em init e em display pois senão a luz iria rotacionar junto com a câmera
        lighting(drawable);
    }

    public void display(GLAutoDrawable drawable) {
         GL gl = drawable.getGL();

        //Cor de Fundo
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        //O método lighting também é colocado aqui pois senão a luz iria girar junto com a câmera
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
	 * Cria as mesas no ArrayList.
	 * Cria as mesas no exato lugar onde elas ficarão para que o conflito funcione, ou seja, caso for mudar as mesas de lugar, mude aqui.
	 * Obs: As mesas são criadas no sentido de perto dos armários até perto da máquina de suco.
	 * @param drawable
	 */
	private void criarMesas(GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		mesas_ = new ArrayList();
		Mesa mesa;
		int i=0; //contara as mesas
		int fileira=0; //fileira atual
		float x=0, z=0;

		/*Medidas referentes às posições das mesas - Se for mudar, mude somente aqui*/
		final float ESPACO_ENTRE_MESAS = 3.5f;
		final float ESPACO_ENTRE_FILEIRAS = 4f;
		final float ESPACO_FILEIRA_PRINCIPAL = 5f;
		final float Z_INICIO = 10f;
		final float X_INICIO = -10f;
		final int X_FATOR = -1;
		final int Z_FATOR = -1;
		/*-------*/
		
		x = X_INICIO;

		//Primeira Fileira
		for(i=0;i<4;i++){
			//z = Z_FATOR*(i*ESPACO_ENTRE_MESAS + Z_INICIO);
			z = Z_FATOR*(i*ESPACO_ENTRE_MESAS) + Z_INICIO;
			gl.glTranslatef(x,0,z);
			mesa = new Mesa(drawable,x,z);
			mesas_.add(mesa);
			gl.glTranslatef(-x,0,-z);
		}

		fileira++;	//atualiza o numero da fileira atual
		x = X_FATOR*(fileira*ESPACO_ENTRE_FILEIRAS) + X_INICIO;
		
		//Segunda Fileira
		for(i=0;i<6;i++){
			z = Z_FATOR*(i*ESPACO_ENTRE_MESAS) + Z_INICIO;
			gl.glTranslatef(x,0,z);
			mesa = new Mesa(drawable,x,z);
			mesas_.add(mesa);
			gl.glTranslatef(-x,0,-z);
		}

		//Terceira Fileira até a quinta
		do{
			fileira++;	//atualiza o numero da fileira atual
			x = X_FATOR*(fileira*ESPACO_ENTRE_FILEIRAS) + X_INICIO;

			for(i=0;i<8;i++){
				z = Z_FATOR*(i*ESPACO_ENTRE_MESAS) + Z_INICIO;
				gl.glTranslatef(x,0,z);
				mesa = new Mesa(drawable,x,z);
				mesas_.add(mesa);
				gl.glTranslatef(-x,0,-z);
			}
		}while(fileira<5);

		//NAO TERMINEI AINDA, FICOU ABSURDO LERDO, VAMOS TER QUE FAZER ALGUMA COISA PRA NÃO DESENHAR MESAS DISTANTES
	}
	/**
	 * Percorre o ArrayList e desenha as mesas.
	 * Para mudar as mesas de lugar altere a função criarMesas.
	 * @param drawable
	 */
	private void desenharMesas(GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		Iterator it;	//Iterador
		Mesa mesa;		//Váriavel temporária que armazenará as mesas


		for(it = mesas_.iterator(); it.hasNext();){
			mesa = (Mesa) it.next();
			gl.glTranslatef(mesa.getDelta_x(),0,mesa.getDelta_z());
			mesa.desenha(drawable);
			gl.glTranslatef(-mesa.getDelta_x(),0,-mesa.getDelta_z());
		}

	}

    private void lighting(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        //Não foi preciso colocar a luz ambiente pois nossos modelos já possuem cor,
        //a luz difusa define qual a cor e a intensidade que será refletida pelos modelos, variando de 0 a 1,
        //a luz especular define a intensidade e a cor com que a fonte de luz será refletida nos objetos, e essa reflexão será
        //    observada de acordo com a posição da luz,
        //e na posição da luz, como queremos projetar os raios de luz do sol na cena, o quarto parâmetro será zero; e os 3 primeiros parâmetros definem
        //    onde a luz será posicionada, sendo que a direção e o sentido da luz sempre será para o ponto da origem (0,0,0).

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

        //Define que os dois lados de um plano serão iluminados
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

        //Cria o painel de projeção
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
 * Váriavei de Classe -> Terminação com o caractere "_"
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

