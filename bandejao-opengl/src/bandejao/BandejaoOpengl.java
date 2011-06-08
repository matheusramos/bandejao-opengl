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
		//OpenGl Parâmetros
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_DEPTH_TEST);

        //Compila todos os modelos da cena
        catraca = new Catraca(drawable);
        maquinaCartao = new MaquinaCartao(drawable);
        gradeEntrada = new GradeEntrada(drawable);
        gradeSaida1 = new GradeSaida1(drawable);
        gradeSaida2 = new GradeSaida2(drawable);
        gradeMaquinaCartao = new GradeMaquinaCartao(drawable);
        gradeMaquinaCartao2 = new GradeMaquinaCartao2(drawable);
        portaFechada = new PortaFechada(drawable);
        portaAberta = new PortaAberta(drawable);
        paredeEntrada1 = new ParedeEntrada1(drawable);
        paredeEntrada2 = new ParedeEntrada2(drawable);
        pia1 = new Pia1(drawable);
        pia2 = new Pia2(drawable);
        lixeiraPia1 = new LixeiraPia1(drawable);
        lixeiraPia2 = new LixeiraPia2(drawable);
        estante1 = new Estante1(drawable);
        estante2 = new Estante2(drawable);

        //Objetos que não terão conflito

        disco1 = new Disco1(drawable);
        disco2 = new Disco2(drawable);

        //Vetor de mesas
		mesas_ = new ArrayList();
        criarMesas(drawable);

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
        
        gradeMaquinaCartao.desenha(drawable);
        gradeMaquinaCartao2.desenha(drawable);
        gradeSaida1.desenha(drawable);
        gradeSaida2.desenha(drawable);
        lixeiraPia1.desenha(drawable);
        lixeiraPia2.desenha(drawable);
        estante1.desenha(drawable);
        estante2.desenha(drawable);

		//Vetor de mesas
        desenharMesas(drawable);
		
        //Objetos sem conflito
        disco1.desenha(drawable);
        disco2.desenha(drawable);

        gl.glTranslatef(0.0f, 1.627f, 0.0f);

        maquinaCartao.desenha(drawable);
        gradeEntrada.desenha(drawable);
        portaFechada.desenha(drawable);
        portaAberta.desenha(drawable);
        paredeEntrada1.desenha(drawable);
        paredeEntrada2.desenha(drawable);
        pia1.desenha(drawable);
        pia2.desenha(drawable);

        gl.glRotatef(angle, -1.0f, -1.0f, 0.0f);
        catraca.desenha(drawable);

        gl.glRotatef(120, -1.0f, -1.0f, 0.0f);
        catraca.desenha(drawable);

        gl.glRotatef(-240, -1.0f, -1.0f, 0.0f);
        catraca.desenha(drawable);

        gl.glTranslatef(0.0f, -1.627f, 0.0f);
        gl.glRotatef(120-angle, -1.0f, -1.0f, 0.0f);

        

        gl.glFlush(); //execute all commands

    }
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {   
    }
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

	/*
	 *
	 *	MÉTODOS PARA CRIAR OS OBJETOS
	 *
	 */


	/**
	 * Cria as mesas no ArrayList.
	 * Cria as mesas no exato lugar onde elas ficarão para que o conflito funcione, ou seja, caso for mudar as mesas de lugar, mude aqui.
	 * Obs: As mesas são criadas no sentido de perto dos armários até perto da máquina de suco.
	 * @param drawable
	 */
	private void criarMesas(GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		Mesa mesa;
		int i=0; //contara as mesas
		int fileira=0; //fileira atual
		float x=0, z=0;

		/*Medidas referentes às posições das mesas - Se for mudar, mude somente aqui*/
		final float ESPACO_ENTRE_MESAS = 3.5f;
		float ESPACO_ENTRE_FILEIRAS = 3.6f;
		final float ESPACO_FILEIRA_PRINCIPAL = ESPACO_ENTRE_FILEIRAS + 1.5f;
		float Z_INICIO = 0f;
		final float X_INICIO = -13.2f;
		final int X_FATOR = -1;
		final int Z_FATOR = -1;
		/*-------*/

		x = X_INICIO;

		//Primeira Fileira
		criarFileiraMesa(drawable, x, Z_INICIO, 4);

		//Segunda Fileira
		fileira++;	//atualiza o numero da fileira atual
		x += X_FATOR*(fileira*ESPACO_ENTRE_FILEIRAS);
		criarFileiraMesa(drawable, x, Z_INICIO, 6);

		//Terceira Fileira até a quinta
		do{
			fileira++;	//atualiza o numero da fileira atual
			x += X_FATOR*(ESPACO_ENTRE_FILEIRAS);
			criarFileiraMesa(drawable, x, Z_INICIO, 8);
		}while(fileira<5);

		//CORREDOR PRINCIPAL AQUI!!!!

		//sexta fileira
		fileira++;	//atualiza o numero da fileira atual
		x += X_FATOR*(ESPACO_FILEIRA_PRINCIPAL);
		criarFileiraMesa(drawable, x, Z_INICIO, 8);

		//PILAR!!!

		//Sétima fileira
		ESPACO_ENTRE_FILEIRAS = 3.5f;	//Muda o espaço entre fileiras
		fileira++;	//atualiza o numero da fileira atual
		x += X_FATOR*(ESPACO_ENTRE_FILEIRAS);
		criarFileiraMesa(drawable, x, Z_INICIO, 8);

		//oitava a nona fileira
		ESPACO_ENTRE_FILEIRAS = 4.5f;	//Muda o espaço entre fileiras
		do{
			fileira++;	//atualiza o numero da fileira atual
			x += X_FATOR*(ESPACO_ENTRE_FILEIRAS);
			criarFileiraMesa(drawable, x, Z_INICIO, 8);
		}while(fileira<9);


		//A PARTIR DAQUI AS FILEIRAS TEM 9 MESAS - MUDAR O Z
		Z_INICIO += 3.5f;
		//Fileira 10
		fileira++;	//atualiza o numero da fileira atual
		x += X_FATOR*(ESPACO_ENTRE_FILEIRAS);
		criarFileiraMesa(drawable, x, Z_INICIO, 9);

		//PILAR!!!

		//décima primeira fileira
		ESPACO_ENTRE_FILEIRAS = 3.5f;	//Muda o espaço entre fileiras
		fileira++;	//atualiza o numero da fileira atual
		x += X_FATOR*(ESPACO_ENTRE_FILEIRAS);
		criarFileiraMesa(drawable, x, Z_INICIO, 9);
		
		//fileira 12 a 14
		ESPACO_ENTRE_FILEIRAS = 4f;	//Muda o espaço entre fileiras
		do{
			fileira++;	//atualiza o numero da fileira atual
			x += X_FATOR*(ESPACO_ENTRE_FILEIRAS);
			criarFileiraMesa(drawable, x, Z_INICIO, 9);
		}while(fileira<14);

		//PILAR!!

		//fileira 15
		ESPACO_ENTRE_FILEIRAS = 3.5f;	//Muda o espaço entre fileiras
		fileira++;	//atualiza o numero da fileira atual
		x += X_FATOR*(ESPACO_ENTRE_FILEIRAS);
		criarFileiraMesa(drawable, x, Z_INICIO, 9);

		//JANELA
	}
	/**
	 * Função para desenhar uma fileira de mesas na direção da saída para a cozinha.
	 * @param drawable
	 * Drawable.
	 * @param x
	 * Eixo x onde ficará a fileira.
	 * @param z_inicio
	 * Coordenada z inicial.
	 * @param qtde
	 * Quantidade de mesas na fileira.
	 */
	private void criarFileiraMesa(GLAutoDrawable drawable, float x, float z_inicio, int qtde){
		GL gl = drawable.getGL();
		Mesa mesa;
		float z=0;

		/*Medidas referentes às posições das mesas - Se for mudar, mude somente aqui*/
		final float ESPACO_ENTRE_MESAS = 3.5f;
		final int Z_FATOR = -1;


		for(int i=0; i<qtde; i++){
			z = Z_FATOR*(i*ESPACO_ENTRE_MESAS) + z_inicio;
			gl.glTranslatef(x,0,z);
			mesa = new Mesa(drawable,x,z);
			mesas_.add(mesa);
			gl.glTranslatef(-x,0,-z);
		}
	}

	private void criarMesas90(GLAutoDrawable drawable){
		Mesa90 mesa90;

	}


	/*
	 *
	 *	MÉTODOS PARA DESENHAR OS OBJETOS
	 *
	 */

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
			if(isPerto(mesa.getDelta_x(),mesa.getDelta_z())){
				gl.glTranslatef(mesa.getDelta_x(),0,mesa.getDelta_z());
				mesa.desenha(drawable);
				gl.glTranslatef(-mesa.getDelta_x(),0,-mesa.getDelta_z());
			}
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

        else if(gradeSaida1.conflito(x_camera, z_camera))
            return gradeSaida1.conflito(x_camera, z_camera);

        else if(gradeSaida2.conflito(x_camera, z_camera))
            return gradeSaida2.conflito(x_camera, z_camera);

        else if(portaFechada.conflito(x_camera, z_camera))
            return portaFechada.conflito(x_camera, z_camera);

        else if(portaAberta.conflito(x_camera, z_camera))
            return portaAberta.conflito(x_camera, z_camera);

        else if(paredeEntrada1.conflito(x_camera, z_camera))
            return paredeEntrada1.conflito(x_camera, z_camera);

        else if(paredeEntrada2.conflito(x_camera, z_camera))
            return paredeEntrada2.conflito(x_camera, z_camera);

        else if(pia1.conflito(x_camera, z_camera))
            return pia1.conflito(x_camera, z_camera);

        else if(pia2.conflito(x_camera, z_camera))
            return pia2.conflito(x_camera, z_camera);

        else if(lixeiraPia1.conflito(x_camera, z_camera))
            return lixeiraPia1.conflito(x_camera, z_camera);

        else if(lixeiraPia2.conflito(x_camera, z_camera))
            return lixeiraPia2.conflito(x_camera, z_camera);

        else if(estante1.conflito(x_camera, z_camera))
            return estante1.conflito(x_camera, z_camera);

        else if(estante2.conflito(x_camera, z_camera))
            return estante2.conflito(x_camera, z_camera);

        else if(catraca.conflito(x_camera, z_camera)){
            if(delta <= 0 && angle <= 121)
                angle += 10;
            else if(delta >= 0 && angle >= -1)
                angle -= 10;

            return false;
        }
        else
            return false;
	}

	public boolean isPerto(float x,float z){
		float x_cam = this.getX_camera();
		float z_cam = this.getZ_camera();

		if(Math.sqrt(Math.pow(x-x_cam,2)+Math.pow(z-z_cam,2)) <= DISTANCIA_MAXIMA_)
			return true;
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
	private static Estante1 estante1;
	private static Estante2 estante2;
	private static Extintor extintor;
	private static GradeEntrada gradeEntrada;
	private static GradeMaquinaCartao gradeMaquinaCartao;
    private static GradeMaquinaCartao2 gradeMaquinaCartao2;
	private static GradeSaida1 gradeSaida1;
    private static GradeSaida2 gradeSaida2;
	private static LixeiraPia1 lixeiraPia1;
    private static LixeiraPia2 lixeiraPia2;
    private static MaquinaCartao maquinaCartao;
    private static PortaFechada portaFechada;
    private static PortaAberta portaAberta;
    private static ParedeEntrada1 paredeEntrada1;
    private static ParedeEntrada2 paredeEntrada2;
	private static Pia1 pia1;
    private static Pia2 pia2;

	//Objeto sem conflito
	private static Disco1 disco1;
	private static Disco2 disco2;

	
    private static float angle = 0;

	private static ArrayList mesas_;

	private static int DISTANCIA_MAXIMA_ = 18;

    private float deltaX;
    private float deltaZ;
}

