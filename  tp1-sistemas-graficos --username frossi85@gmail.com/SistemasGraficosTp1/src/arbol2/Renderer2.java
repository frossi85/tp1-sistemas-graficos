package arbol2;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import com.jogamp.opengl.util.gl2.GLUT;
 

//Implementar el Renderer como Singleton
//HAcer geters del canvas, gl y demmas cosas q necesite para hacer las actualizaciones de la camara
class Renderer2 implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener 
{
    private GLU glu = new GLU();
	public static GLUT glut = new GLUT();  
	private Camera camera = new Camera(15.0, 15.0, 5.0, 0.0, 0.0, 0.0);
	
	private Point2D.Float posicionAnteriorMouse = new Point2D.Float(0,0); 
	
	private float rotacionCamara = 0;
	
    
 // Variables que controlan la ubicación de la cámara en la Escena 3D
    //float eye[] =  {15.0f, 15.0f, 5.0f};
    //float at[]  = { 0.0f,  0.0f, 0.0f};
    float up[]  = { 0.0f,  0.0f, 1.0f};

    // Variables asociadas a única fuente de luz de la escena
    float light_color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    float light_position[] = {10.0f, 10.0f, 8.0f};
    float light_ambient[] = {0.05f, 0.05f, 0.05f, 1.0f};

    // Variables de control
    boolean view_grid = true;
    boolean view_axis = true;
    boolean edit_panel = false;
   
	private static int DL_AXIS;
    private static int DL_GRID;
    private static int DL_AXIS2D_TOP;

    // Tamaño de la ventana
    static float window_size[] = new float[2];
    private static float W_WIDTH = window_size[0];
    private static float W_HEIGHT = window_size[1];

    private static final int TOP_VIEW_POSX = ((int)((float)W_WIDTH*0.60f));
    private static final int TOP_VIEW_W = ((int)((float)W_WIDTH*0.40f));
    private static final int TOP_VIEW_POSY = ((int)((float)W_HEIGHT*0.60f));
    private static final int TOP_VIEW_H = ((int)((float)W_HEIGHT*0.40f));
    
    
    ///////////////////CAMARA////////////////////////////
  //angle of rotation
    float xpos = 0, ypos = 0, zpos = 0, xrot = 0, yrot = 0, angle=0.0f;
    float lastx, lasty;

  //positions of the cubes
  float positionz[] = new float[10];
  float positionx[] = new float[10];
  
  public GLCanvas canvas;
  
  //ATRIBUTOS DE LA ANIMACION
  private float velocidadCrecimiento = 1; 
  private int cantReinicioLoop = 0;
  private boolean pause = false;
  private float edadMaxima=12;
  private float edadActual = 1;
	private Arbol arbol = new Arbol(edadActual);
  
  private void update()
  {
	  if(cantReinicioLoop > 100)
	  {
		  cantReinicioLoop = 0;
		  if(!pause)
		  {
			  if(edadActual<edadMaxima)
			  {
				  System.out.println("UPDATED");
				  edadActual += velocidadCrecimiento;
				  arbol.crecer(velocidadCrecimiento);
			  }
		  }
	  }
	  else
		  cantReinicioLoop++;
  }
    public void display(GLAutoDrawable gLDrawable) 
    {    
    	
    	System.out.println("display called");
    	final GL2 gl = gLDrawable.getGL().getGL2();
		
	  	gl.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
	  	///////////////////////////////////////////////////
	  	// Escena 3D
	  	
	  	Set3DEnv(gl); //funcion de los profes
	  	
	  	gl.glMatrixMode(GL2.GL_MODELVIEW);
	  	gl.glLoadIdentity(); 	
	  	
	  	glu.gluLookAt(camera.getXPos(), camera.getYPos() , camera.getZPos(), 
                camera.getXLPos(), camera.getYLPos(), camera.getZLPos(),
                up[0], up[1], up[2]);

	     
	  	if (view_axis)
	  		 gl.glCallList(DL_AXIS);
	  	
	  	if (view_grid)
	  		 gl.glCallList(DL_GRID);
	  	//
	  	///////////////////////////////////////////////////
	
	    ///////////////////////////////////////////////////
	  	//
	  	// Draw here
	  	//
	  	
	  	gl.glRotatef(rotacionCamara,0f,0f,1f);
	  	
  	
    	//UPDATE del modelo
    	this.update();
    	
	  	//El ALGORITMO ESTA BIEN< AHORA FALTA ESCALAR y POSICION ALEATORIA EN LA RAMA PRINCIPAL
	  	

	  		arbol.dibujar(gl);
	  	
	  	
	  	
//	  	Rama rama = new Rama(5, 8, 0);
//	  	gl.glTranslatef(0,0,2);
//	    gl.glRotatef(70,0f,1f,0f);
//	  	rama.dibujar(gl, 0, 0, 0);
	  	
	  	//rama.dibujarCirculo(gl, 4.0, 0);
	    
	    Hoja hoja = new Hoja();
	    
	    //hoja.dibujar(gl);
		
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
	  	
	
	  	//glutSwapBuffers();
	  	//Si lo de abajo no funciona borrar y hacer: Setting double buffered in the capabilities object is the only thing that needs to be done.
	  	//Supuestamente en ves de glutSwap.. va gl.glFlush();
	  	gl.glFlush();
    }
 
    public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) 
    {
    	System.out.println("displayChanged called");
    }
 
    public void init(GLAutoDrawable gLDrawable) 
    {
    	System.out.println("init() called");

    	GL2 gl = gLDrawable.getGL().getGL2();
	    DL_AXIS = gl.glGenLists(3);
	  	DL_GRID = DL_AXIS+1;
	  	DL_AXIS2D_TOP = DL_AXIS+2;
    	
	
	  	gl.glClearColor (0.02f, 0.02f, 0.04f, 0.0f);
	  	gl.glShadeModel (GL2.GL_SMOOTH);
	  	gl.glEnable(GL2.GL_DEPTH_TEST);
	  
	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_color , 0);
	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light_ambient, 0);
	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);
	  
	  	gl.glEnable(GL2.GL_LIGHT0);
	  	gl.glEnable(GL2.GL_LIGHTING);
	
	  	// Generación de las Display Lists
	  	gl.glNewList(DL_AXIS, GL2.GL_COMPILE);
	  	this.DrawAxis(gl);
	  	gl.glEndList();
	  	gl.glNewList(DL_GRID, GL2.GL_COMPILE);
	  	this.DrawXYGrid(gl);
	  	gl.glEndList();
	  	gl.glNewList(DL_AXIS2D_TOP, GL2.GL_COMPILE);
	  	this.DrawAxis2DTopView(gl);
	  	gl.glEndList();
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
		// TODO Auto-generated method stub
		rotacionCamara += e.getX() - posicionAnteriorMouse.getX();
		
		posicionAnteriorMouse.setLocation(e.getX(), e.getY());
		
		//canvas.display();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
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
	    		//if(keys['w'])                         // Move forwards
			      // {
	    		//cameraZPosition -= 0.1;
			    //cameraLZPosition -= 0.1;
			 
			    //camera.moveForward(0.1);
			    //pacman.moveForward(0.1);
			    //camera.look(10);
	    		
	    		//rotacionCamara += 10;
	    		//canvas.display();
			    //   }
	    		break; 
    		
	    	case 'r':
//	    		R Reiniciar la animación de crecimiento
	    		break;
	    	case 'p':
//	    		P Pausar/reanudar animación
	    		break;
	    	case 'q':

//	    		Q incrementar velocidad de crecimiento
	    		break;
	    	case 'a':
//	    		A decrementar velocidad de crecimiento
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