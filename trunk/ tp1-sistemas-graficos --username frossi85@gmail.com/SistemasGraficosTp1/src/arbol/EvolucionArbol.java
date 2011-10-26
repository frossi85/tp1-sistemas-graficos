package arbol;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
 
import javax.media.opengl.GL3;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
 
public class EvolucionArbol 
{
	private static GLCanvas glcanvas;
	
    public static void main(String[] args) 
    {
    	// setup OpenGL Version 2
    	GLProfile profile = GLProfile.get(GLProfile.GL2);
    	GLCapabilities capabilities = new GLCapabilities(profile);
 
    	// The canvas is the widget that's drawn in the JFrame
    	glcanvas = new GLCanvas(capabilities);
    	
    	Renderer renderer = new Renderer(glcanvas); 	
    	
    	glcanvas.addGLEventListener(renderer);
    	glcanvas.addKeyListener(renderer);
    	glcanvas.addMouseListener(renderer);
    	glcanvas.addMouseMotionListener(renderer);
    	glcanvas.addMouseWheelListener(renderer);
    	
    	glcanvas.setSize( 1000, 600 );
 
        JFrame frame = new JFrame( "TP1 Sistemas Graficos" );
        frame.getContentPane().add( glcanvas);
 
        // shutdown the program on windows close event
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
 
        frame.setSize( frame.getContentPane().getPreferredSize() );
        frame.setVisible( true );
    }
}