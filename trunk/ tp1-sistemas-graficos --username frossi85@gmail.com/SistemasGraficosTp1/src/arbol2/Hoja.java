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
		//glu.gluSphere(glu.gluNewQuadric() , 0.1f, 10, 10);
//		float tamanio = 0.5f;
//		
//		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
//			gl.glVertex3f(0, -tamanio, 0);
//	  		gl.glVertex3f(0, 0, tamanio/2);
//	  		gl.glVertex3f(0, tamanio, 0);
//	  		gl.glVertex3f(0, 0, -tamanio/2);
//			gl.glVertex3f(0, -tamanio, 0);
//		gl.glEnd();
	}
}
