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
    private boolean view_grid = true;
    private boolean view_axis = true;
    private boolean edit_panel = false;
   
	private static int DL_AXIS;
    private static int DL_GRID;
    private static int DL_AXIS2D_TOP;

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
    
    int RUIDO_VERT = ManejoShaders2.addVertexShader( new RuidoVert(1f,0.25f,0.001f));
    int ESFERIZAR_VERT = ManejoShaders2.addVertexShader(new EsferizacionVert(1,0.0f,0.0f,0.0f,0.5f));
    int DOBLAR_VERT = ManejoShaders2.addVertexShader(new DoblarVert((float)Math.PI/2,2.0f,1f,1f));
   
    int RUIDO_FRAG = ManejoShaders2.addFragmentShader(new RuidoFrag());
    int TEXTURA_FRAG = ManejoShaders2.addFragmentShader(new TexturaFrag());
    int LUCES_FRAG = ManejoShaders2.addFragmentShader(new LucesFrag());
    
    
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
    	animator = new FPSAnimator(canvas, 60);
    	animator.add(canvas);
    	animator.start();
    }
  
    private void update()
    {
//    	if(cantReinicioLoop > 100)
//    	{
//    		cantReinicioLoop = 0;
//    		if(!pause)
//    		{
//    			if(edadActual<edadMaxima)
//    			{
//    				edadActual += velocidadCrecimiento;
//    				arbol.crecer(velocidadCrecimiento);
//    			}
//    			else {
//    				arbol.verHojas();
//    			}
//    		}
//    	}
//    	else
//    		cantReinicioLoop++;
    }
    
    public void display(GLAutoDrawable gLDrawable) 
    {    
    	final GL2 gl = gLDrawable.getGL().getGL2();
    	ManejoShaders2 mS = new ManejoShaders2(gLDrawable);
    	
    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	 	
    	//gl.glBindTexture(GL.GL_TEXTURE_2D, texture);

    	
        //timer010 = 0.09; //for screenshot!
    	gl.glPushMatrix();
	    	/*mS.usarPrograma(RUIDO_VERT, TEXTURA_FRAG);
	    	mS.displayUniform();
	    	mS.displayVertexAttrib();
    		drawMenu(gl, this);*/
    	gl.glPopMatrix(); 
    	gl.glPushMatrix();
    	
	        gl.glPushMatrix();        	
	        	
	        	gl.glRotatef(rotacionCamaraX, 0, 1, 0);
	        	gl.glRotatef(rotacionCamaraY, -1, 0, 0);
	        	
	        	mS.usarPrograma(DOBLAR_VERT, RUIDO_FRAG);
        	
	        	//this.shader.usarPrograma();
	        	//retorcer
	        	/*int location = gl.glGetUniformLocation(shader.getProgramHandler(),shader.getRetorcer().NOMBRE_TIME);
	    		gl.glUniform1f(location,shader.getRetorcer().getTime());  
	    		shader.getRetorcer().setTime(shader.getRetorcer().getTime() + 1f);
	        	*/
	    		
	        	mS.displayUniform();
				/*
				int location = gl.glGetUniformLocation(shader.getProgramHandler(),shader.getRuido().NOMBRE_TIME);
				gl.glUniform1f(location,shader.getRuido().getTime());  
				shader.getRuido().setTime(shader.getRuido().getTime() + 1.0f);*/
	    	
	    		
	    		/*esferizacion: 
	    		 int location = gl.glGetUniformLocation(shader.getProgramHandler(),shader.getEsferizacion().NOMBRE_FACTOR);
				gl.glUniform1f(location,shader.getEsferizacion().getFactorVariable());  
				float centro[] = shader.getEsferizacion().getCentro();
	    		 */
	    		
	    	  	//gl.glBegin(GL.GL_TRIANGLES); //no cambia nada
	    	  	//retorcer
	    	  	//gl.glVertexAttrib1f(shader.getRetorcer().getMemAngulo(),shader.getRetorcer().getAngulo() );
	    	  	//gl.glVertexAttrib1f(shader.getRetorcer().getMemAltura(),shader.getRetorcer().getAltura() );
	    	  		
	    	  	//	ruido con animacion:
	    		/*  	gl.glVertexAttrib1f(shader.getRuido().getMemAmplitud(),shader.getRuido().getAmplitud() );
	  				gl.glVertexAttrib1f(shader.getRuido().getMemFase(),shader.getRuido().getFase() );
	  				gl.glVertexAttrib1f(shader.getRuido().getMemLongOnda(),shader.getRuido().getLongOnda() );*/
	        	
	        	mS.displayVertexAttrib();

	    		 
	    	  	/*esferizacion
	    	  	gl.glVertexAttrib1f(shader.getEsferizacion().getMemRadio(),shader.getEsferizacion().getRadio() );
	    	  	gl.glVertexAttrib1f(shader.getEsferizacion().getMemFactor(),shader.getEsferizacion().getFactor() );
	    	  	gl.glVertexAttrib3f(shader.getEsferizacion().getMemRadio(),centro[0],centro[1],centro[2] );
	    		 */
	    		  	//	glut.glutSolidCube(3);
	    		  		
	    		  		primitiva.dibujar(gl);
	    		  
	    		  	//this.shader.pararPrograma();	
	    	  // 	gl.glEnd(); //no cambia nada
	    	   	
	    	   	
        
	        
	        gl.glPopMatrix();
        gl.glPopMatrix();
    	
      
		
	  	///////////////////////////////////////////////////
	
	  	///////////////////////////////////////////////////
	  	// Panel 2D para la vista superior
	  	if (edit_panel)
	  	{
	  		SetPanelTopEnv(gl);
	  		
	  		gl.glMatrixMode(GL2.GL_MODELVIEW);
	  		gl.glLoadIdentity();
	  		glu.gluLookAt (0, 0, 0.5, 0, 0, 0, 0, 1, 0);
	  		gl.glCallList(DL_AXIS2D_TOP);
	  	}
	  	//
	  	///////////////////////////////////////////////////
    	
	
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
		   
		   
		//Fin agregado
		//this.shader = new ManejoShaders("Ruido.vert","Textura.frag");
    	//this.shader = new ManejoShaders("SinDeformacion.vert","Ruido.frag");
    	//this.shader = new ManejoShaders("SinDeformacion.vert","PhongShader.frag");
	  //	this.shader.bindBuffer(gLDrawable);
	  //	this.shader.compiladoLinkeado(gLDrawable);
	  	
	    //DemoLight(gl);
	  	
	  	///TEXTURA

	  	gl.glEnable(GL.GL_TEXTURE_2D);
        texture = genTexture(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
        TextureReader.Texture texture = null;
        try {
            texture = TextureReader.readTexture("ladrillos.png");
        } catch (IOException e) {
            e.printStackTrace();
			throw new RuntimeException(e);
        }
        
        makeRGBTexture(gl, glu, texture, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
	 
	  	
	  	DL_AXIS = gl.glGenLists(3);
	  	DL_GRID = DL_AXIS+1;
	  	DL_AXIS2D_TOP = DL_AXIS+2;
    	
	  
//	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_color , 0);
//	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light_ambient, 0);
//	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_specular, 0);
//		float colorBlue[] = { 0.0f, 0.0f, 1.0f, 1.0f };
//		//gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, colorBlue,0);
//	  
//	  	gl.glEnable(GL2.GL_LIGHT0);
//	  	gl.glEnable(GL2.GL_LIGHTING);
	
	  	// Generaci�n de las Display Lists
	  	gl.glNewList(DL_AXIS, GL2.GL_COMPILE);
	  	this.DrawAxis(gl);
	  	gl.glEndList();
	  	gl.glNewList(DL_GRID, GL2.GL_COMPILE);
	  	this.DrawXYGrid(gl);
	  	gl.glEndList();
	  	gl.glNewList(DL_AXIS2D_TOP, GL2.GL_COMPILE);
	  	this.DrawAxis2DTopView(gl);
	  	gl.glEndList();
    
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
			
			gl.glPushMatrix();	
			esfera.dibujar(gl);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslatef(1,0,0);
			esfera.dibujar(gl);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslatef(2,0,0);
			esfera.dibujar(gl);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslatef(3,0,0);
			esfera.dibujar(gl);
			gl.glPopMatrix();
		
		gl.glPopMatrix();
		
		gl.glPushMatrix();
			gl.glTranslatef(3,1.6f,0);
			
			gl.glPushMatrix();				
				cubo.dibujar(gl);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
				gl.glTranslatef(0,-1f,0.0f);				
				cubo.dibujar(gl);
			gl.glPopMatrix();
		
			gl.glPushMatrix();
				gl.glTranslatef(0,-2f,0.0f);
				cubo.dibujar(gl);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
				gl.glTranslatef(0,-3f,0.0f);
				cubo.dibujar(gl);
			gl.glPopMatrix();
		
		gl.glPopMatrix();
		
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
	  			System.out.println("d1");
	  		}
	  	}.setRenderer(r));
  
	  	actionSquareManager.add(new actionSquare(0.8f, 0.25f, 0.94f, 0.5f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			System.out.println("d2");
	  		}
	  	}.setRenderer(r));
	  	
	  	actionSquareManager.add(new actionSquare(0.8f, 0.5f, 0.94f, 0.7f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			System.out.println("d3");
	  		}
	  	}.setRenderer(r));
	  	
	  	actionSquareManager.add(new actionSquare(0.8f, 0.7f, 0.94f ,0.9f){
	  		private Renderer r;
			public actionSquare setRenderer(Renderer r){
				this.r = r;
				return this;
			}
	  		public void actuar(){
	  			System.out.println("d4");
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
	  
	void DrawAxis(GL2 gl)
	{
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glBegin(GL2.GL_LINES);
			// X
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(15.0f, 0.0f, 0.0f);
			// Y
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 15.0f, 0.0f);
			// Z
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 15.0f);
		gl.glEnd();
		gl.glEnable(GL2.GL_LIGHTING);
	}
	
	void DrawAxis2DTopView(GL2 gl)
	{
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glBegin(GL2.GL_LINE_LOOP);
			// X
		gl.glColor3f(0.0f, 0.5f, 1.0f);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 1.0f, 0.0f);
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glEnd();
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(0.1f, 0.1f, 0.1f);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 1.0f, 0.0f);
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glEnd();
	
		gl.glEnable(GL2.GL_LIGHTING);
	}
	
	void DrawXYGrid(GL2 gl)
	{
		int i;
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glColor3f(0.25f, 0.2f, 0.2f);
		gl.glBegin(GL2.GL_LINES);
		for(i=-20; i<21; i++)
		{
			gl.glVertex3f((float)i, -20.0f, 0.0f);
			gl.glVertex3f((float)i,  20.0f, 0.0f);
			gl.glVertex3f(-20.0f, (float)i, 0.0f);
			gl.glVertex3f( 20.0f, (float)i, 0.0f);
		}
		gl.glEnd();
		gl.glEnable(GL2.GL_LIGHTING);
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