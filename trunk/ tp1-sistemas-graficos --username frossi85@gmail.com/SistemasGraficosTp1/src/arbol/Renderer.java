package arbol;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL4;
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
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
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
	 
	// Variables que controlan la ubicación de la cámara en la Escena 3D
    private float eye[] =  {15.0f, 15.0f, 5.0f};
    private float at[]  = { 0.0f,  0.0f, 0.0f};
    private float up[]  = { 0.0f,  0.0f, 1.0f};

    // Variables asociadas a única fuente de luz de la escena
    private float light_color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private float light_position[] = {10.0f, 10.0f, 8.0f};
    private float light_ambient[] = {0.05f, 0.05f, 0.05f, 1.0f};

    // Variables de control
    private boolean view_grid = true;
    private boolean view_axis = true;
    private boolean edit_panel = false;
   
	private static int DL_AXIS;
    private static int DL_GRID;
    private static int DL_AXIS2D_TOP;

    // Tamaño de la ventana
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
    	if(cantReinicioLoop > 100)
    	{
    		cantReinicioLoop = 0;
    		if(!pause)
    		{
    			if(edadActual<edadMaxima)
    			{
    				edadActual += velocidadCrecimiento;
    				arbol.crecer(velocidadCrecimiento);
    			}
    			else {
    				arbol.verHojas();
    			}
    		}
    	}
    	else
    		cantReinicioLoop++;
    }
    
    public void display(GLAutoDrawable gLDrawable) 
    {    
    	final GL2 gl = gLDrawable.getGL().getGL2();
		
	  	gl.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
	  	
	  	///////////////////////////////////////////////////
	  	// Escena 3D
	  	
	  	Set3DEnv(gl); //funcion de los profes
	  	
	  	gl.glMatrixMode(GL2.GL_MODELVIEW);
	  	gl.glLoadIdentity(); 	
	  	gl.glBindVertexArray( vaoHandle.get(0) );	// No se si va
	   	glu.gluLookAt(eye[0], eye[1] , eye[2], 
	   				  at[0], at[1], at[2],
	   				  up[0], up[1], up[2]);

	 	
	   	gl.glRotatef(rotacionCamara,0f,0f,1f);
	  	
	   	if (view_axis)
	  		 gl.glCallList(DL_AXIS);
	  	
	  	if (view_grid)
	  		 gl.glCallList(DL_GRID);
	
	    ///////////////////////////////////////////////////
	  	//
	  	// Draw here
	  	//
  	
    	//UPDATE del modelo
    	this.update();
	  	
    	gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
    	gl.glEnable(GL2.GL_COLOR_MATERIAL);	
    	
	  	arbol.dibujar(gl);
		
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
    	System.out.println("init() called");
    	   	
    	GL2 gl = gLDrawable.getGL().getGL2();
	    
    	gl.glClearColor (0.02f, 0.02f, 0.04f, 0.0f);
	  	gl.glShadeModel (GL2.GL_SMOOTH);
	  	gl.glEnable(GL2.GL_DEPTH_TEST);
	  	
	  	GL4 gl_shader = gLDrawable.getGL().getGL4();
	  	Buffer buff_aux = null;
	  	int vboHandles[] = new int [2];
	  	gl_shader.glGenBuffers(2, vboHandles,0);
	  	positionBufferHandle = vboHandles[0];
	  	colorBufferHandle = vboHandles[1];
	  	long long_aux = 9*4;  // 9* sizeof(float)
	  	positionData.put(positionDataOrig);
	  	colorData.put(colorDataOrig);
	  	gl_shader.glBindBuffer( GL.GL_ARRAY_BUFFER, positionBufferHandle );
	  	gl_shader.glBufferData( GL.GL_ARRAY_BUFFER, long_aux, positionData, GL.GL_STATIC_DRAW );
	  	gl_shader.glBindBuffer( GL.GL_ARRAY_BUFFER, colorBufferHandle );
	    gl.glBufferData(GL. GL_ARRAY_BUFFER, long_aux, colorData, GL.GL_STATIC_DRAW );
	    // Create and set-up the vertex array objet
	    gl_shader.glGenVertexArrays( 1, vaoHandle );
	    gl_shader.glBindVertexArray( vaoHandle.get(0) );
	    // Enable the vertex attributes array
	    gl_shader.glEnableVertexAttribArray(0);
	    gl_shader.glEnableVertexAttribArray(1);
	    // Map index 0 to the position buffer
	    gl_shader.glBindBuffer( GL.GL_ARRAY_BUFFER, positionBufferHandle);
	    gl_shader.glVertexAttribPointer( 0, 3, GL.GL_FLOAT, false, 0,buff_aux);
	    // Map index 1 to the color buffer
	    gl_shader.glBindBuffer( GL.GL_ARRAY_BUFFER, colorBufferHandle);
	    gl_shader.glVertexAttribPointer( 1, 3, GL.GL_FLOAT, false, 0, buff_aux);
	    
	    
	    /* ARRANCA COMPILACION */
	    GL2ES2 gles = gLDrawable.getGL().getGL2ES2();
	    int creador = gl_shader.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
	    BufferedReader brv = null;
		try {
			brv = new BufferedReader(new FileReader("Vertex_Shader.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String vsrc = "";
	    String line;
	    try {
			while ((line=brv.readLine()) != null) {
			  vsrc += line + "\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    gl_shader.glShaderSource(creador, 1, vsrc, (int[])null);	
	    gl.glCompileShader(creador);
	   
	    
	    DL_AXIS = gl.glGenLists(3);
	  	DL_GRID = DL_AXIS+1;
	  	DL_AXIS2D_TOP = DL_AXIS+2;
    	
	  
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
		rotacionCamara += e.getX() - posicionAnteriorMouse.getX();			
		posicionAnteriorMouse.setLocation(e.getX(), e.getY());
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
	    		break; 
	    	case 'r':
//	    		R Reiniciar la animación de crecimiento
	    		System.out.println("Reiniciar animación");
	    		this.edadActual = 1;
	    		this.arbol = new Arbol(this.edadActual);
	    		break;
	    	case 'p':
//	    		P Pausar/reanudar animación
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