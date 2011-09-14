package arbol2;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
 
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.*;
 
public class HelloWorld2 
{
	private static GLCanvas glcanvas;
	
    public static void main(String[] args) 
    {
    	// setup OpenGL Version 2
    	GLProfile profile = GLProfile.get(GLProfile.GL2);
    	GLCapabilities capabilities = new GLCapabilities(profile);
 
    	// The canvas is the widget that's drawn in the JFrame
    	glcanvas = new GLCanvas(capabilities);
    	
    	Renderer2 renderer = new Renderer2(glcanvas); 	
    	
    	glcanvas.addGLEventListener(renderer);
    	glcanvas.addKeyListener(renderer);
    	glcanvas.addMouseListener(renderer);
    	glcanvas.addMouseMotionListener(renderer);
    	glcanvas.addMouseWheelListener(renderer);
    	
    	glcanvas.setSize( 300, 300 );
 
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