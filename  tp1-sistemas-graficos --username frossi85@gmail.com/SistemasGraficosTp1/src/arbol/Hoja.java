package arbol;

import javax.media.opengl.GL2;

public class Hoja {	   
	private static int DL_HOJA;
	private static GL2 gl;
	
	public Hoja(GL2 gl){
		Hoja.gl = gl;
		DL_HOJA = gl.glGenLists(1);
		
		gl.glNewList(DL_HOJA, GL2.GL_COMPILE);
			float tamanio = 0.2f;	
	
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);	
				gl.glColor3f(0.4f,0.7f,0.3f);
				gl.glVertex3f(0, -tamanio/2, tamanio);
		  		gl.glVertex3f(0, 0, 2*tamanio);
		  		gl.glVertex3f(0, tamanio/2, tamanio);
		  		gl.glVertex3f(0, 0, 0);
				gl.glVertex3f(0, -tamanio/2, tamanio);
			gl.glEnd();
	  	gl.glEndList();
	}
	
	public void dibujar()
	{
		gl.glCallList(DL_HOJA);
	}
}
