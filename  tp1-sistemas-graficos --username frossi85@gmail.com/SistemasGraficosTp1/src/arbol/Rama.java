package arbol;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.media.opengl.GL2;

public class Rama {
	public float largo;
	public float ancho;
	public float escala;
	static public GL2 gl;
	private int DL_RAMA;
	
	public Rama(GL2 gl, float largo, float ancho)
	{
		this.largo = largo;
		this.ancho = ancho;
		Rama.gl = gl;
		
		DL_RAMA = gl.glGenLists(1);

		gl.glNewList(DL_RAMA, GL2.GL_COMPILE);
			gl.glColor3f(0.6f,0.3f,0.1f);
			dibujarCilindro(Rama.gl, this.ancho, this.largo);
	  	gl.glEndList();
	}
	
	public void dibujar()
	{
	    gl.glCallList(DL_RAMA);
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
