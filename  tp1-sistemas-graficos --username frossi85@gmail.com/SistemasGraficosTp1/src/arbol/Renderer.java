package arbol;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL3;
import javax.media.opengl.GL3bc;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import primitivas.*;

import actionSquare.*;

import shaders.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import common.TextureReader;


//Implementar el Renderer como Singleton
class Renderer implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    private GLU glu = new GLU();
    public static GLUT glut = new GLUT();
	private Point2D.Float posicionAnteriorMouse = new Point2D.Float(0,0);
	private float rotacionCamaraX = 0, rotacionCamaraY = 0;

	private actionSquareManager actionSquareManager = new actionSquareManager();

	private ManejoShaders shader;
	//private ManejoShaders sinTexturaShader = new ManejoShaders("SinDeformacion.vert","SinTextura.frag");

	// Variables que controlan la ubicaci�n de la c�mara en la Escena 3D
    private float eye[] =  {0.0f, 0.0f, 4.0f};
    private float at[]  = { 0.0f,  0.0f, -1.0f};
    private float up[]  = { 0.0f,  1.0f, 0.0f};

    // Variables asociadas a �nica fuente de luz de la escena
    //private float light_color[] = {0.5f, 0.3f, 0.7f, 1.0f};
    private float light_color[] = {0.5f, 0.5f, 0.5f, 1.0f};

    //private float light_position[] = {10.0f, 10.0f, 8.0f};
    private float light_position[] = {-1f,1f,1f,0f};

	private float light_ambient[] = {0.05f, 0.05f, 0.05f, 1.0f};
    //private float light_ambient[] = {1f, 1f, 1f, 0.3f};

    private float light_specular[] = {0f,0f,0f,0f};
	//private float light_specular[] = {0.3f,0.3f,0.3f,1.0f};

    // Variables de control
    // Tama�o de la ventana
    private static float window_size[] = new float[2];
    private static float W_WIDTH = window_size[0];
    private static float W_HEIGHT = window_size[1];

    private static final int TOP_VIEW_POSX = ((int)((float)W_WIDTH*0.60f));
    private static final int TOP_VIEW_W = ((int)((float)W_WIDTH*0.40f));
    private static final int TOP_VIEW_POSY = ((int)((float)W_HEIGHT*0.60f));
    private static final int TOP_VIEW_H = ((int)((float)W_HEIGHT*0.40f));

    //Valores del buffer de color y posicion

    static int POSITION_BUFFER_SIZE = 9;
    private FloatBuffer positionData = FloatBuffer.allocate (POSITION_BUFFER_SIZE);
    private FloatBuffer colorData = FloatBuffer.allocate (POSITION_BUFFER_SIZE);
    private IntBuffer vaoHandle = IntBuffer.allocate(1);
   // float positionData[] =

    private int texture;

    private primitiva primitiva =  new cubo(0.5f,10);//new cilindro(0.6f,0.8f,100,50);//new esfera(1, 300, 300); //new anillo(1, 0.5f, 200, 200);// new cilindro(0.6f,0.8f,100,50);//////new cubo(0.8f,4);//

    //SHADERS

    int RUIDO_VERT = ManejoShaders2.addVertexShader( new RuidoVert(3f,0.25f,1f);
    int ESFERIZAR_VERT = ManejoShaders2.addVertexShader(new EsferizacionVert(1,0.0f,0.0f,0.0f,0.5f));
    int DOBLAR_VERT = ManejoShaders2.addVertexShader(new DoblarVert((float)Math.PI/2,2.0f,1f,1f));
    int RETORCER_VERT = ManejoShaders2.addVertexShader(new RetorcerVert((float)Math.PI,2f));



    int SIN_DEFORMACION_VERT = ManejoShaders2.addVertexShader(new SinDeformacionVert());

    int RUIDO_FRAG = ManejoShaders2.addFragmentShader(new RuidoFrag());
    int TEXTURA_FRAG = ManejoShaders2.addFragmentShader(new TexturaFrag());
    int LUCES_FRAG = ManejoShaders2.addFragmentShader(new LucesFrag());

    int GENERIC_FRAG;
    FragmentGeneral fragment;



    efectoFragment currentFrag;
    int currentVert;

    ManejoShaders2 mS;

    public enum efectoFragment {SEMI_MATE,BRILLANTE, TEXTURA2D, REFLEJAR_ENTORNO};


float [] positionDataOrig =
{
    -0.8f, -0.8f, 0.0f,
     0.8f, -0.8f, 0.0f,
     0.0f,  0.8f, 0.0f
};



   int positionBufferHandle;

   float colorDataOrig[] =
    {
         1.0f,  0.0f, 0.0f,
         0.0f,  1.0f, 0.0f,
         0.0f,  0.0f, 1.0f
    };
   int colorBufferHandle;

   public GLCanvas canvas;

    //ATRIBUTOS DE LA ANIMACION
    private float velocidadCrecimiento = 0.25f;
    private int cantReinicioLoop = 0;
    private boolean pause = false;
    private float edadMaxima = 12;
    private float edadActual = 1;
    private Arbol arbol = new Arbol(edadActual);
    private FPSAnimator animator;

    public Renderer(GLCanvas glCanvas)
    {
    	this.canvas = glCanvas;
    	//animator = new FPSAnimator(canvas, 60);
    	animator = new FPSAnimator(canvas, 50);
    	animator.add(canvas);
    	animator.start();
    }

    private void update(GL2 gl)
    {
    	if(cantReinicioLoop > 1)
    	{

    	}
    	else
    		cantReinicioLoop++;
    }

    public void setEfectoLuz( efectoFragment efecto)
    {
    	switch(efecto) {
    		case BRILLANTE:
    				fragment.setEfectoBrillante();
    			break;
    		case SEMI_MATE:
    				fragment.setEfectoSemiMate();
    			break;
    		case TEXTURA2D:
    			fragment.setEfectoTextura2D();
    			break;
    		case REFLEJAR_ENTORNO:
    			fragment.setEfectoReflejarEntorno();
    			break;
    		default:
    			break;
    	}


    }

    public void display(GLAutoDrawable gLDrawable)
    {
    	final GL2 gl = gLDrawable.getGL().getGL2();

    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    	//gl.glBindTexture(GL.GL_TEXTURE_2D, texture);

  		update(gl);
    	gl.glPushMatrix();
    		drawMenu(gl, this);
    	gl.glPopMatrix();
    	gl.glPushMatrix();

	        gl.glPushMatrix();

	        	gl.glRotatef(rotacionCamaraX, 0, 1, 0);
	        	gl.glRotatef(rotacionCamaraY, -1, 0, 0);

	        	//mS.usarPrograma(SIN_DEFORMACION_VERT, GENERIC_FRAG);
	        	//mS.usarPrograma(ESFERIZAR_VERT, GENERIC_FRAG);
	        	mS.usarPrograma(currentVert, GENERIC_FRAG);


	        	setEfectoLuz(currentFrag);

	        	//retorcer
	        	/*int location = gl.glGetUniformLocation(shader.getProgramHandler(),shader.getRetorcer().NOMBRE_TIME);
	    		gl.glUniform1f(location,shader.getRetorcer().getTime());
	    		shader.getRetorcer().setTime(shader.getRetorcer().getTime() + 1f);
	        	*/
	    	  	//retorcer
	    	  	//gl.glVertexAttrib1f(shader.getRetorcer().getMemAngulo(),shader.getRetorcer().getAngulo() );
	    	  	//gl.glVertexAttrib1f(shader.getRetorcer().getMemAltura(),shader.getRetorcer().getAltura() );

	        	mS.displayUniform();
	        	mS.displayVertexAttrib();
	    		primitiva.dibujar(gl);

	        gl.glPopMatrix();
        gl.glPopMatrix();

	  	//En ves de glutSwapBuffers();.. va gl.glFlush();
	    gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged)
    {
    	System.out.println("displayChanged called");
    }

    private void makeRGBTexture(GL gl, GLU glu, TextureReader.Texture img,
            int target, boolean mipmapped) {

        if (mipmapped) {
            glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(),
                    img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
        } else {
            gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(),
                    img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
        }
    }

    private int genTexture(GL gl) {
        final int[] tmp = new int[1];
        gl.glGenTextures(1, tmp, 0);
        return tmp[0];
    }

    public void init(GLAutoDrawable gLDrawable)
    {
    	//GL4 gl2 = gLDrawable.getGL().getGL4();

    	//System.out.println("GL_VERSION: "+ gl2.glGetString(GL3.GL_VERSION)); ;
    	//System.out.println("GL_SHADING_LANGUAGE_VERSION: " + gl2.glGetString(GL4.GL_SHADING_LANGUAGE_VERSION));


    	GL2 gl = gLDrawable.getGL().getGL2();

    	gl.glClearColor(0.5f, 0.5f, 1.0f, 0.0f);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		//setear la camera
		Set3DEnv(gl);

		mS = new ManejoShaders2(gLDrawable);

		fragment = new FragmentGeneral(gl, glu, mS);
		GENERIC_FRAG = ManejoShaders2.addFragmentShader(fragment);

		currentVert = SIN_DEFORMACION_VERT;
		currentFrag = efectoFragment.BRILLANTE;

		DemoLight(gl);

    }

    public static FloatBuffer makeFloatBuffer(float[] arr) {
	    ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
	    bb.order(ByteOrder.nativeOrder());
	    FloatBuffer fb = bb.asFloatBuffer();
	    fb.put(arr);
	    fb.position(0);
	    return fb;
    }

    private void DemoLight(GL2 gl)
    {
      gl.glEnable(GL2.GL_LIGHTING);
      gl.glEnable(GL2.GL_LIGHT0);
      gl.glEnable(GL2.GL_NORMALIZE);

      // Light model parameters:
      // -------------------------------------------

      float lmKa[] = {0.1f, 0.1f, 0.1f, 1.0f };

      gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, makeFloatBuffer(lmKa));

      gl.glLightModelf(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, 1.0f);
      gl.glLightModelf(GL2.GL_LIGHT_MODEL_TWO_SIDE, 0.0f);

      // -------------------------------------------
      // Spotlight Attenuation

      //float spot_direction[] = {1.0f, -1.0f, -1.0f }; //ORIGINAL
      float spot_direction[] = {1.0f, -1.0f, -1.0f };


      int spot_exponent = 30;
      int spot_cutoff = 180;

      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, makeFloatBuffer(spot_direction));
      gl.glLighti(GL2.GL_LIGHT0, GL2.GL_SPOT_EXPONENT, spot_exponent);
      gl.glLighti(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, spot_cutoff);

      float Kc = 1.0f;
      float Kl = 0.0f;
      float Kq = 0.0f;

      gl.glLightf(GL2.GL_LIGHT0, GL2.GL_CONSTANT_ATTENUATION,Kc);
      gl.glLightf(GL2.GL_LIGHT0, GL2.GL_LINEAR_ATTENUATION, Kl);
      gl.glLightf(GL2.GL_LIGHT0, GL2.GL_QUADRATIC_ATTENUATION, Kq);


      // -------------------------------------------
      // Lighting parameters:

      //float light_pos[] = {0.0f, 5.0f, 5.0f, 1.0f}; //ORIGINAL
      float light_pos[] = {1.0f, 3.0f, 10.0f, 1.0f}; //DIRECTIONAL LITGH O POINT??
      //float light_Ka[]  = {1.0f, 0.5f, 0.5f, 1.0f}; //ORIGINAL
      //float light_Ka[] = {0.5f, 0.5f, 0.5f, 1.0f};
      float light_Ka[] = {0.0f, 0.0f, 0.0f, 1.0f }; //Del segundo tuto
      //float light_Kd[]  = {1.0f, 0.1f, 0.1f, 1.0f}; //ORIGINAL
      float light_Kd[]  = {1.0f, 1.0f, 1.0f, 1.0f};
      float light_Ks[]  = {1.0f, 1.0f, 1.0f, 1.0f}; //Brillante


      //float light_Ks[]  = {0.0f, 0.0f, 0.0f, 0.0f}; //SemiMate

      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, makeFloatBuffer(light_pos));
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, makeFloatBuffer(light_Ka));
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, makeFloatBuffer(light_Kd));
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, makeFloatBuffer(light_Ks));

      // -------------------------------------------
      // Material parameters:

      //float material_Ka[] = {0.0f, 0.9f, 0.7f, 1.0f}; //Color Material del brillante
      float material_Ka[] = {0.3f, 0.3f, 0.3f, 1.0f }; //Del Segundo Tuto
      //float material_Kd[] = {0.4f, 0.4f, 0.4f, 1.0f};
      float material_Kd[] = {0.9f, 0.5f, 0.5f, 1.0f }; //del segundo TUTO
      //float material_Ks[] = {0.8f, 0.8f, 0.8f, 1.0f};
      float material_Ks[] = {0.6f, 0.6f, 0.6f, 1.0f }; //del segundo tuto
      float material_Ke[] = {0.1f, 0.0f, 0.0f, 0.0f};
      //float material_Se = 15.0f;
      float material_Se = 60.0f; //del segundo tuto

      gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, makeFloatBuffer(material_Ka));
      gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, makeFloatBuffer(material_Kd));
      gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, makeFloatBuffer(material_Ks));
      gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, makeFloatBuffer(material_Ke));
      gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, material_Se);

    }



    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height)
    {
    	System.out.println("reshape() called: x = "+x+", y = "+y+", width = "+width+", height = "+height);

        W_WIDTH  = (float)width;
    	W_HEIGHT = (float)height;

    	GL2 gl = gLDrawable.getGL().getGL2();
    	//setear la camera
		Set3DEnv(gl);
    }

	public void dispose(GLAutoDrawable arg0)
	{
		System.out.println("dispose() called");
	}

	private void Set3DEnv(GL2 gl)
	{
	  	gl.glViewport (0, 0, (int) W_WIDTH, (int) W_HEIGHT);
	    gl.glMatrixMode (GL2.GL_PROJECTION);
	    gl.glLoadIdentity ();
	    glu.gluPerspective(60.0, W_WIDTH/W_HEIGHT, 0.10, 100.0);

	    gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(eye[0], eye[1] , eye[2],
				  at[0], at[1], at[2],
 				  up[0], up[1], up[2]);

		//Agregago
		//int h = (int)W_HEIGHT, w = (int)W_WIDTH;
		/*System.out.println(W_HEIGHT + " aaaaa " + W_WIDTH);
		int h = 600, w = 600;
	      if(h == 0) h = 1;
		   float ratio = 1.0f * (float)w / (float)h;*/

	      /*
		   gl.glMatrixMode(GL2.GL_PROJECTION);
		   gl.glLoadIdentity();

		   gl.glViewport(0, 0, w, h);

	      glu.gluPerspective(45,ratio,1,100);*/
		  /* gl.glMatrixMode(GL2.GL_MODELVIEW);
		   gl.glLoadIdentity();
		 // gl.glBindVertexArray( vaoHandle.get(0) );	// No se si va
		   glu.gluLookAt(0.0f,0.0f,4.0f,
			          0.0,0.0,-1.0,
				       0.0f,1.0f,0.0f);
//		   	glu.gluLookAt(eye[0], eye[1] , eye[2],
//		   				  at[0], at[1], at[2],
//		   				  up[0], up[1], up[2]);*/
	}

	private void drawMenu(GL2 gl, Renderer r){

		cubo cubo = new cubo(0.25f,1);
		esfera esfera = new esfera(0.3f,30,30);
		anillo anillo = new anillo(0.3f,0.1f, 30,30);
		cilindro cilindro = new cilindro(0.3f, 0.3f, 30,1);

		mS.usarPrograma(SIN_DEFORMACION_VERT, GENERIC_FRAG);

    	mS.displayUniform();
    	mS.displayVertexAttrib();
		gl.glPushMatrix();
			gl.glTranslatef(-3,1.6f,0);

			gl.glPushMatrix();
				esfera.dibujar(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
				gl.glTranslatef(0,-1f,0.0f);
				cubo.dibujar(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
				gl.glTranslatef(0,-2f,0.0f);
				anillo.dibujar(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
				gl.glTranslatef(0,-3f,0.0f);
				cilindro.dibujar(gl);
			gl.glPopMatrix();

		gl.glPopMatrix();

		gl.glPushMatrix();

			gl.glTranslatef(-1.5f, 1.6f, 0);

			//mS.usarPrograma(SIN_DEFORMACION_VERT, GENERIC_FRAG);
	    	mS.displayUniform();
	    	mS.displayVertexAttrib();

			gl.glPushMatrix();
			fragment.setEfectoSemiMate();
			esfera.dibujar(gl);
			gl.glPopMatrix();

	    	mS.displayUniform();
	    	mS.displayVertexAttrib();

			gl.glPushMatrix();
			fragment.setEfectoBrillante();
			gl.glTranslatef(1,0,0);
			esfera.dibujar(gl);
			gl.glPopMatrix();

	    	mS.displayUniform();
	    	mS.displayVertexAttrib();

			gl.glPushMatrix();
			gl.glTranslatef(2,0,0);
			fragment.setEfectoTextura2D();
			esfera.dibujar(gl);
			gl.glPopMatrix();

			//mS.usarPrograma(SIN_DEFORMACION_VERT, GENERIC_FRAG);
	    	mS.displayUniform();
	    	mS.displayVertexAttrib();

			gl.glPushMatrix();
			fragment.setEfectoReflejarEntorno();
			gl.glTranslatef(3,0,0);
			gl.glPushMatrix();
			gl.glRotatef(45f, 1f, 0.8f, 0.15f);
			esfera.dibujar(gl);
			gl.glPopMatrix();
			gl.glPopMatrix();

		gl.glPopMatrix();


		gl.glPushMatrix();
			gl.glTranslatef(3,1.6f,0);

//			mS.usarPrograma(RETORCER_VERT, GENERIC_FRAG);
//	    	mS.displayUniform();
//	    	mS.displayVertexAttrib();

			gl.glPushMatrix();
				cubo.dibujar(gl);
			gl.glPopMatrix();

//			mS.usarPrograma(DOBLAR_VERT, GENERIC_FRAG);
//	    	mS.displayUniform();
//	    	mS.displayVertexAttrib();

			gl.glPushMatrix();
				gl.glTranslatef(0,-1f,0.0f);
				cubo.dibujar(gl);
			gl.glPopMatrix();

//			mS.usarPrograma(RUIDO_VERT, GENERIC_FRAG);
//	    	mS.displayUniform();
//	    	mS.displayVertexAttrib();

			gl.glPushMatrix();
				gl.glTranslatef(0,-2f,0.0f);
				cubo.dibujar(gl);
			gl.glPopMatrix();

//			mS.usarPrograma(ESFERIZAR_VERT, GENERIC_FRAG);
//	    	mS.displayUniform();
//	    	mS.displayVertexAttrib();

			gl.glPushMatrix();
				gl.glTranslatef(0,-3f,0.0f);
				cubo.dibujar(gl);
			gl.glPopMatrix();

		gl.glPopMatrix();


		//arriba
		actionSquareManager.add(new actionSquare(0.64f, 0.1f, 0.73f, 0.25f){
			private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			r.currentFrag  = efectoFragment.REFLEJAR_ENTORNO;
	  		}
	  	}.setRenderer(r));

	  	actionSquareManager.add(new actionSquare(0.518f, 0.1f, 0.605f, 0.25f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			r.currentFrag  = efectoFragment.TEXTURA2D;
	  		}
	  	}.setRenderer(r));

	  	actionSquareManager.add(new actionSquare(0.396f, 0.1f, 0.478f, 0.25f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			r.currentFrag  = efectoFragment.BRILLANTE;
	  		}
	  	}.setRenderer(r));

	  	actionSquareManager.add(new actionSquare(0.274f, 0.1f, 0.356f ,0.25f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			r.currentFrag  = efectoFragment.SEMI_MATE;
	  		}
	  	}.setRenderer(r));


		//izquierda
		actionSquareManager.add(new actionSquare(0.09f, 0.1f, 0.20f, 0.25f){
			private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			r.setPrimitiva( new esfera(1, 50, 50) );
	  		}
	  	}.setRenderer(r));

	  	actionSquareManager.add(new actionSquare(0.09f, 0.25f, 0.20f, 0.5f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			r.setPrimitiva( new cubo(0.6f,10) );
	  		}
	  	}.setRenderer(r));

	  	actionSquareManager.add(new actionSquare(0.09f, 0.5f, 0.20f, 0.7f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			r.setPrimitiva( new anillo(0.7f,0.3f, 50, 30) );
	  		}
	  	}.setRenderer(r));

	  	actionSquareManager.add(new actionSquare(0.09f, 0.7f, 0.20f ,0.9f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			r.setPrimitiva( new cilindro(0.6f,0.8f,50,10) );
	  		}
	  	}.setRenderer(r));


	  	//derecha
	  	actionSquareManager.add(new actionSquare(0.8f, 0.1f, 0.94f, 0.25f){
			private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			currentVert = RETORCER_VERT;
	  		}
	  	}.setRenderer(r));

	  	actionSquareManager.add(new actionSquare(0.8f, 0.25f, 0.94f, 0.5f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			currentVert = RUIDO_VERT;
	  		}
	  	}.setRenderer(r));

	  	actionSquareManager.add(new actionSquare(0.8f, 0.5f, 0.94f, 0.7f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			currentVert = DOBLAR_VERT;
	  		}
	  	}.setRenderer(r));

	  	actionSquareManager.add(new actionSquare(0.8f, 0.7f, 0.94f ,0.9f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			currentVert = ESFERIZAR_VERT;
	  		}
	  	}.setRenderer(r));

	}

	public void setPrimitiva(primitiva p){
		this.primitiva = p;
	}

	void SetPanelTopEnv(GL2 gl)
	{
		gl.glViewport (TOP_VIEW_POSX, TOP_VIEW_POSY, TOP_VIEW_W, TOP_VIEW_H);
		gl.glMatrixMode (GL2.GL_PROJECTION);
		gl.glLoadIdentity ();
		glu.gluOrtho2D(-0.10, 1.05, -0.10, 1.05);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseMoved(MouseEvent e) {
		rotacionCamaraX += e.getX() - posicionAnteriorMouse.getX();
		rotacionCamaraY += e.getY() - posicionAnteriorMouse.getY();
		posicionAnteriorMouse.setLocation(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent m) {
		this.actionSquareManager.trata(m.getX()/W_WIDTH, m.getY()/W_HEIGHT);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent key) {
		//convertir todas las letras a mayusculas o minusculas
		switch (key.getKeyChar()) {
	    	case KeyEvent.VK_ESCAPE:
	    		System.exit(0);
	    		break;
	    	case 'd':
	    		break;
	    	case 'r':
//	    		R Reiniciar la animaci�n de crecimiento
	    		System.out.println("Reiniciar animaci�n");
	    		this.edadActual = 1;
	    		this.arbol = new Arbol(this.edadActual);
	    		break;
	    	case 'p':
//	    		P Pausar/reanudar animaci�n
	    		String texto = (this.pause)? "Reanudar": "Pausar";
	    		System.out.println(texto);
	    		this.pause = !this.pause;
	    		break;
	    	case 'q':
//	    		Q incrementar velocidad de crecimiento
	    		if(this.velocidadCrecimiento < 2){
	    			this.velocidadCrecimiento *= 2;
	    			System.out.println("Nueva velocidad : " + this.velocidadCrecimiento);
	    		}
	    		break;
	    	case 'a':
//	    		A decrementar velocidad de crecimiento
	    		if(this.velocidadCrecimiento > 0.1){
	    			this.velocidadCrecimiento /= 2;
	    			System.out.println("Nueva velocidad : " + this.velocidadCrecimiento);
	    		}
	    		break;
	    	case 'x':
	    	    light_specular[0] += 0.25f;
	    		break;
	    	case 'z':
	    	    light_specular[0] -= 0.25f;
	    		break;
	    	default:
	    		break;
	    }

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

}
