import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
 
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
 
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

public class launch implements GLEventListener, KeyListener {
	
	private static Frame frame = new Frame("OpenGl Exemple");
	
	private static GLCanvas canvas = new GLCanvas();
	
	private static  Animator animator = new Animator(canvas);
	
	private static GLU glu = new GLU();
	
	private static GLUT glut = new GLUT();
	
	// Tamaño de la ventana
    private static float window_size[] = new float[2];
    private static float W_WIDTH = window_size[0];
    private static float W_HEIGHT = window_size[1];
	
	private static int DL_AXIS;
    private static int DL_GRID;
    
    private boolean view_axis = false;
    private boolean view_grid = false;
    private boolean auto_rotate = false;
    
    private float angulo = 0.0f;
    
    private float eye[] =  {3.0f, 3.0f, 3.0f};
    private float at[]  = { 0.0f,  0.0f, 0.0f};
    private float up[]  = { 0.0f,  0.0f, 1.0f};
  /*  
    private float light_color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private float light_position[] = {0.0f, 0.0f, 10.0f};
    private float light_ambient[] = {0.05f, 0.05f, 0.05f, 1.0f}; */
    
    public void draw(GL2 gl){
    	   	
    	new cubo(0.5f, 20).dibujar(gl);
    }

	public static void main(String[] args) {
		
		launch launch = new launch();
		canvas.addGLEventListener(launch);
		canvas.addKeyListener(launch);
		frame.add(canvas);
		
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
		
		frame.setSize(640, 480);
		frame.setVisible(true);
		
		animator.start();
	}

	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		
		gl.glEnable(GL2.GL_DEPTH_TEST);

		/**
		 * Cette fonction permet de désactiver
		 * la syncronisation verticale, indépendement
		 * de la plateforme utilisée
		 */
		gl.setSwapInterval(1);
		
		//luz
	/*	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_color , 0);
	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light_ambient, 0);
	  	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0); */

	  	gl.glEnable(GL2.GL_LIGHT0);
	  	gl.glEnable(GL2.GL_LIGHTING);
		
		// Generación de las Display Lists
		DL_AXIS = gl.glGenLists(2);
	  	DL_GRID = DL_AXIS+1;
	  	
	  	gl.glNewList(DL_AXIS, GL2.GL_COMPILE);
	  	this.DrawAxis(gl);
	  	gl.glEndList();
	  	gl.glNewList(DL_GRID, GL2.GL_COMPILE);
	  	this.DrawXYGrid(gl);
	  	gl.glEndList();

	}

	/**
	 * reshape() sera appelée si la fenêtre d'affichage est redimensionnée
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		
		W_WIDTH  = (float)width;
    	W_HEIGHT = (float)height;

	}
	/**
	 * display() sera appelée en boucle tout au long de l'application
	 * par la classe Animator. C'est dans cette fonction qu'on fera
	 * tout ce qui doit être affiché
	 */
	public void display(GLAutoDrawable drawable) {

		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);

        set3DEnv(gl);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
	  	gl.glLoadIdentity();
        glu.gluLookAt(eye[0], eye[1] , eye[2],
 				  at[0], at[1], at[2],
 				  up[0], up[1], up[2]);
        
	  	if (view_grid)
	  		 gl.glCallList(DL_GRID);
		if (view_axis)
	  		 gl.glCallList(DL_AXIS);
		
    	gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
    	gl.glEnable(GL2.GL_COLOR_MATERIAL);
    	
		if(auto_rotate)  		
    		angulo += 0.2f;
		
    	gl.glRotatef(angulo, 1, 1, 0);
    	
		draw(gl);
	}
	
	public void set3DEnv(GL2 gl){
		gl.glMatrixMode (GL2.GL_PROJECTION);
	    gl.glLoadIdentity ();
	    glu.gluPerspective(60.0, W_WIDTH/W_HEIGHT, 0.10, 100.0);
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	/**
	 * displayChanged() est appelée si le mode d'affichage par exemple
	 * est modifié. Cependant nous n'implémenterons pas cette méthode.
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void exit() {
        animator.stop();
        frame.dispose();
        System.exit(0);
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
	public void keyPressed(KeyEvent key) {
		switch(key.getKeyChar()){
			case KeyEvent.VK_ESCAPE:
				exit();
				break;
			case 'a':
				view_axis = !view_axis;
				break;
			case 'g':
				view_grid = !view_grid;
				break;
			case 'r':
				auto_rotate = !auto_rotate;
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