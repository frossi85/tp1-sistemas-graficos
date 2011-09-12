package arbol2;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class Rama {
	public float largo;
	public float ancho;
	public float angulo;
	public float escala;
	
	public Rama(float largo, float ancho, float angulo)
	{
		this.largo = largo;
		this.ancho = ancho;
		this.angulo = angulo;	
	}
	
	//El ALGORITMO ESTA BIEN< AHORA FALTA ESCALAR, GRADOS ALEATORIOS y POSICION ALEATORIA EN LA RAMA PRINCIPAL
	public void dibujar(GL2 gl, float anguloFi, float anguloTheta,float translado)
	{
		GLU glu = new GLU();
	
		    gl.glRotatef(anguloFi,0.5f,1f,0.0f);
		    gl.glRotatef(anguloTheta, 0f, 0f, 1f);
		    //glu.gluCylinder(glu.gluNewQuadric(), ancho, ancho/2, largo, 8, 5); //Ver q son los ultimos parametros
		    dibujarCilindro(gl, ancho, largo);
	}
	
	public Rama getRamaEscalada(float escala)
	{
		this.escala = escala;
		return new Rama(escala*this.largo, escala*this.ancho, this.angulo);	
	}
	
	private void dibujarCilindro(GL2 gl, float radio, float largo)
	{
		ArrayList<Point2D.Double> circuloInferior = this.getVerticesCirculo(radio);
		ArrayList<Point2D.Double> circuloSuperior = this.getVerticesCirculo(radio/2);
		
		int cantidadDePuntos = circuloInferior.size();
		
		dibujarCirculo(gl, radio/2, largo);
		
		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
		for(int i = 0; i < cantidadDePuntos; i++)
		{
			gl.glTexCoord3f((float)circuloSuperior.get(i).getX(), (float)circuloSuperior.get(i).getY(), largo ); //vertex 1
			gl.glVertex3f((float)circuloSuperior.get(i).getX(), (float)circuloSuperior.get(i).getY(), largo ); //vertex 1
			
			gl.glTexCoord3f((float)circuloInferior.get(i).getX(), (float)circuloInferior.get(i).getY(), 0.0f ); //vertex 1
			gl.glVertex3f((float)circuloInferior.get(i).getX(), (float)circuloInferior.get(i).getY(), 0.0f ); //vertex 1			
		}
		
		gl.glTexCoord3f((float)circuloSuperior.get(0).getX(), (float)circuloSuperior.get(0).getY(), largo ); //vertex 1
		gl.glVertex3f((float)circuloSuperior.get(0).getX(), (float)circuloSuperior.get(0).getY(), largo ); //vertex 1
		
		gl.glTexCoord3f((float)circuloInferior.get(0).getX(), (float)circuloInferior.get(0).getY(), 0.0f ); //vertex 1
		gl.glVertex3f((float)circuloInferior.get(0).getX(), (float)circuloInferior.get(0).getY(), 0.0f ); //vertex 1
		
	gl.glEnd();
		
		dibujarCirculo(gl, radio, 0);	
	}
	
	public void dibujarCirculo(GL2 gl, double radio, double z)
	{
		ArrayList<Point2D.Double> circulo = this.getVerticesCirculo(radio);
		
		circulo.add(0, new Point2D.Double(0, 0));
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
			for( Point2D.Double punto : circulo )
			{
				gl.glVertex3f((float) punto.getX(), (float) punto.getY(), 0);
			}
			
			gl.glVertex3f((float) circulo.get(1).getX(), (float) circulo.get(1).getY(), (float) z);
			
		gl.glEnd();
	}
	
	private ArrayList<Point2D.Double> getVerticesCirculo(double radio)
	{
		int cantVertices = 20;
		double angulo = 0;
		double pasoAngular = (2*Math.PI)/cantVertices;
		ArrayList<Point2D.Double> puntos = new ArrayList<Point2D.Double>(); 
		
		//Convertir paso angular a radianes???
		for(int i = 0; i < cantVertices; i++, angulo+=pasoAngular)
		{
			Point2D.Double punto = new Point2D.Double(radio*Math.cos(angulo), radio*Math.sin(angulo));
			puntos.add(punto);
		}	
		return puntos; 
	}
}
