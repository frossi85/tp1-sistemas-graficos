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

import primitivas.cilindro;
import primitivas.cubo;
import primitivas.esfera;
import primitivas.pyramide;

import actionSquare.*;

import shaders.ManejoShaders;

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
 

//Implementar el Renderer como Singleton
class Renderer implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener 
{
    private GLU glu = new GLU();
    public static GLUT glut = new GLUT();  
	private Point2D.Float posicionAnteriorMouse = new Point2D.Float(0,0); 
	private float rotacionCamara = 0;
	
	private actionSquareManager actionSquareManager = new actionSquareManager();
	 
	private ManejoShaders shader;
	// Variables que controlan la ubicaci�n de la c�mara en la Escena 3D
    private float eye[] =  {15.0f, 15.0f, 5.0f};
    private float at[]  = { 0.0f,  0.0f, 0.0f};
    private float up[]  = { 0.0f,  0.0f, 1.0f};

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
    	
    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	 	
	   	//gl.glRotatef(rotacionCamara,0f,0f,1f);
    	
        //timer010 = 0.09; //for screenshot!
    	gl.glPushMatrix();
    	
	        gl.glPushMatrix();
	        	gl.glRotatef(20,0f,1f,0f);
	        	gl.glRotatef(17,-1f,0f,0f);
	        	gl.glScalef(0.5f, 0.5f, 0.5f);
	        	this.shader.usarPrograma();
	        	
	        	//cubo cil = new cubo(1,3);
	        	//gl.glBegin(GL.GL_TRIANGLES);
	        	//cil.dibujar(gl);
	        	//gl.glEnd();*/
	        	
	        	pyramide p = new pyramide();
	        	p.dibujar(gl);
	        
	        	

	        	//glut.glutSolidCube(3);
	        	
	        	//glutSwapBuffers();
	        gl.glPopMatrix();
        gl.glPopMatrix();
    	
		
//	  	gl.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
//	  	
//	  	
//	  	///FACUNDO PARA FRAGMENT SHADER///
//	  	gl.glDisable(GL2.GL_LIGHT0);
//	  	gl.glDisable(GL2.GL_LIGHTING);
//	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_color , 0);
//	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light_ambient, 0);
//	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_specular, 0);
//		float color[] = { 0.0f, 1.0f, 0.0f, 1f };
//		
//		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, color,0);
//    	gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
//    	gl.glEnable(GL2.GL_COLOR_MATERIAL);	
//
//	  
//	  	gl.glEnable(GL2.GL_LIGHT0);
//	  	gl.glEnable(GL2.GL_LIGHTING);
//	  	
//	  	///////FIN PARA FRAGMENT SHADER////////////////
//	  	
//	  	
//	  	///////////////////////////////////////////////////
//	  	// Escena 3D
//	  	
//	  	Set3DEnv(gl); //funcion de los profes
//	  	
//	  	gl.glMatrixMode(GL2.GL_MODELVIEW);
//	  	gl.glLoadIdentity(); 	
//	  	gl.glBindVertexArray( vaoHandle.get(0) );	// No se si va
//	   	glu.gluLookAt(eye[0], eye[1] , eye[2], 
//	   				  at[0], at[1], at[2],
//	   				  up[0], up[1], up[2]);
//
//	 	
//	   	gl.glRotatef(rotacionCamara,0f,0f,1f);
//	  	
//	   	if (view_axis)
//	  		 gl.glCallList(DL_AXIS);
//	  	
//	  	if (view_grid)
//	  		 gl.glCallList(DL_GRID);
//	
////	    ///////////////////////////////////////////////////
////	  	//
////	  	// Draw here
////	  	//
////	  	//gl.glColor3f(1, 0, 1);
////	  	
////	  	gl.glRotatef(-40,1f,1f,0f);
////	  	
////	  
//	  	gl.glTranslatef(0, 0, 3);
////	  	shader.setColor(gLDrawable, 1, 0, 0);
////	  	
////		//cilindro cil = new cilindro(5,1,4,10);
////	  	//esfera cil = new esfera(4,50, 50);
////	  	cubo cil = new cubo(3.0f,20);
////		gl.glColor3f(1f,0.2f,0.3f);
////		
////		
////
////	  	gl.glBegin(GL.GL_TRIANGLES);
////
////	  	gl.glVertexAttrib1f(shader.getRuido().getMemAmplitud(),shader.getRuido().getAmplitud() );
////	  	gl.glVertexAttrib1f(shader.getRuido().getMemFase(),shader.getRuido().getFase() );
////	  	gl.glVertexAttrib1f(shader.getRuido().getMemLongOnda(),shader.getRuido().getLongOnda() );
////	  	cil.dibujar(gl);
////
////	   	gl.glEnd();
//	  	
//	  	gl.glPushMatrix();
//	  		gl.glRotatef(90,1f,0f,0f);
//	  		glut.glutSolidTeapot(4);
//	  	gl.glPopMatrix();
//	 
//	   	this.shader.usarPrograma();
//	   	
//    	//UPDATE del modelo
//    	this.update();
//    	
//    	
//  
//    	
//	  	//arbol.dibujar(gl);
		
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
 
    public void init(GLAutoDrawable gLDrawable) 
    {   	
    	//GL4 gl2 = gLDrawable.getGL().getGL4();
    	
    	//System.out.println("GL_VERSION: "+ gl2.glGetString(GL3.GL_VERSION)); ; 
    	//System.out.println("GL_SHADING_LANGUAGE_VERSION: " + gl2.glGetString(GL4.GL_SHADING_LANGUAGE_VERSION)); 

    	   	
    	GL2 gl = gLDrawable.getGL().getGL2();
		
    	gl.glClearColor(0.5f, 0.5f, 1.0f, 0.0f);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		
		
		//Agregago
		int h = 600, w = 600;
	      if(h == 0) h = 1;
		   float ratio = 1.0f * (float)w / (float)h;

	      
		   gl.glMatrixMode(GL2.GL_PROJECTION);
		   gl.glLoadIdentity();
		
		   gl.glViewport(0, 0, w, h);

	      glu.gluPerspective(45,ratio,1,100);
		   gl.glMatrixMode(GL2.GL_MODELVIEW);
		   gl.glLoadIdentity();
		 // gl.glBindVertexArray( vaoHandle.get(0) );	// No se si va
		   glu.gluLookAt(0.0f,0.0f,4.0f, 
			          0.0,0.0,-1.0,
				       0.0f,1.0f,0.0f);
//		   	glu.gluLookAt(eye[0], eye[1] , eye[2], 
//		   				  at[0], at[1], at[2],
//		   				  up[0], up[1], up[2]);
		   
		   
		   
		//Fin agregado
		this.shader = new ManejoShaders("Ruido.vert","Ruido.frag");
    	//this.shader = new ManejoShaders("SinDeformacion.vert","Ruido.frag");
    	//this.shader = new ManejoShaders("SinDeformacion.vert","PhongShader.frag");
	  	this.shader.bindBuffer(gLDrawable);
	  	this.shader.compiladoLinkeado(gLDrawable);
	  	
	    //DemoLight(gl);
	 
	  	
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
	  	actionSquareManager.add(new actionSquare(0.1f, 0.1f, 0.25f, 0.9f){
	  		public void actuar(){
	  			System.out.println("izquierda");
	  		}
	  	});
	  	
	  	actionSquareManager.add(new actionSquare(0.3f, 0.05f, 0.7f, 0.25f){
	  		public void actuar(){
	  			System.out.println("arriba");
	  		}
	  	});
	  	
	  	actionSquareManager.add(new actionSquare(0.75f, 0.1f, 0.9f, 0.9f){
	  		public void actuar(){
	  			System.out.println("derecha");
	  		}
	  	});
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
      
      float lmKa[] = {0.0f, 0.0f, 0.0f, 0.0f };
      
      gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, makeFloatBuffer(lmKa));
      
      gl.glLightModelf(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, 1.0f);
      gl.glLightModelf(GL2.GL_LIGHT_MODEL_TWO_SIDE, 0.0f);
      
      // -------------------------------------------
      // Spotlight Attenuation
      
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
      float light_pos[] = {1.0f, 3.0f, 10.0f, 1.0f};
      //float light_Ka[]  = {1.0f, 0.5f, 0.5f, 1.0f}; //ORIGINAL
      float light_Ka[]  = {0.5f, 0.5f, 0.5f, 1.0f}; 
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
      float material_Ka[] = {0.0f, 0.9f, 0.7f, 1.0f};
      float material_Kd[] = {0.4f, 0.4f, 0.5f, 1.0f};
      float material_Ks[] = {0.8f, 0.8f, 0.0f, 1.0f};
      float material_Ke[] = {0.1f, 0.0f, 0.0f, 0.0f};
      float material_Se = 20.0f;

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
    }
 
	public void dispose(GLAutoDrawable arg0) 
	{
		System.out.println("dispose() called");
	}
    
	void Set3DEnv(GL2 gl)
	{		
	  	gl.glViewport (0, 0, (int) W_WIDTH, (int) W_HEIGHT); 
	    gl.glMatrixMode (GL2.GL_PROJECTION);
	    gl.glLoadIdentity ();
	    glu.gluPerspective(60.0, W_WIDTH/W_HEIGHT, 0.10, 100.0);
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
		rotacionCamara += e.getX() - posicionAnteriorMouse.getX();			
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