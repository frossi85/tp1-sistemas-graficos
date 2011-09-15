package arbol2;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

public class Hoja {
	public float largo;
	public float ancho;
	public float angulo;
	public float escala;
	
	//El ALGORITMO ESTA BIEN< AHORA FALTA ESCALAR, GRADOS ALEATORIOS y POSICION ALEATORIA EN LA RAMA PRINCIPAL
	public void dibujar(GL2 gl)
	{
		GLU glu = new GLU();
		float tamanio = 0.2f;	

		
		gl.glBegin(GL2.GL_TRIANGLE_STRIP);	
			gl.glColor3f(0.4f,0.7f,0.3f);
			gl.glVertex3f(0, -tamanio/2, tamanio);
	  		gl.glVertex3f(0, 0, 2*tamanio);
	  		gl.glVertex3f(0, tamanio/2, tamanio);
	  		gl.glVertex3f(0, 0, 0);
			gl.glVertex3f(0, -tamanio/2, tamanio);
		gl.glEnd();
	}
}
