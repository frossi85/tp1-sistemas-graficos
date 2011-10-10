package primitivas;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.media.opengl.GL2;

public class cilindro {
	
	private float _mediaAltura;
	private float _radio;
	private int _caras;
	private int _anillos;
	private ArrayList<Point2D.Float> _puntos;

	private boolean _showColors = false;
	private float _noColor[] = {1.0f, 1.0f, 1.0f};
	private float _colors[][] = {
			{1.0f, 0.0f, 0.0f},
			{0.0f, 1.0f, 0.0f},
			{0.0f, 0.0f, 1.0f}
	};

	public cilindro(float mediaAltura, float radio, int caras, int anillos){
		this._mediaAltura = mediaAltura;
		this._radio = radio;
		this._caras = caras;
		this._anillos = anillos;
		this._puntos = this.getVerticesCirculo(caras, radio);
	}
	
	public void dibujar(GL2 gl){
		gl.glPushMatrix();
			if(this._showColors)
				gl.glColor3fv(this._colors[0],0);
			else gl.glColor3fv(this._noColor,0);
			
			gl.glTranslatef(0.0f, 0.0f,this._mediaAltura);
			this.dibujarCirculo(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();		
			if(this._showColors)
				gl.glColor3fv(this._colors[1],0);
			else gl.glColor3fv(this._noColor,0);
			
			gl.glTranslatef(0.0f, 0.0f,-this._mediaAltura);			
			this.dibujarCirculo(gl);
		gl.glPopMatrix();
		
		float alturaAnillo = this._mediaAltura * 2 / this._anillos;
		float zM;
		int cantidadDePuntos = this._puntos.size();
		
		if(this._showColors)
			gl.glColor3fv(this._colors[2],0);
		else gl.glColor3fv(this._noColor,0);
		
		for(float z = -this._mediaAltura; z < this._mediaAltura; z += alturaAnillo){
			zM = (z + alturaAnillo < this._mediaAltura)? z+alturaAnillo : this._mediaAltura;
			
			for(int i = 0; i < cantidadDePuntos - 1; i++){
				gl.glBegin(GL2.GL_POLYGON);
					gl.glVertex3f(this._puntos.get(i).x, this._puntos.get(i).y, zM);
					gl.glVertex3f(this._puntos.get(i+1).x, this._puntos.get(i+1).y, zM);
					gl.glVertex3f(this._puntos.get(i+1).x, this._puntos.get(i+1).y, z);
					gl.glVertex3f(this._puntos.get(i).x, this._puntos.get(i).y, z);
	
				gl.glEnd();
			}
			
			gl.glBegin(GL2.GL_POLYGON);
				gl.glVertex3f(this._puntos.get(cantidadDePuntos-1).x, this._puntos.get(cantidadDePuntos-1).y, zM);
				gl.glVertex3f(this._puntos.get(0).x, this._puntos.get(0).y, zM);
				gl.glVertex3f(this._puntos.get(0).x, this._puntos.get(0).y, z);
				gl.glVertex3f(this._puntos.get(cantidadDePuntos-1).x, this._puntos.get(cantidadDePuntos-1).y, z);
			gl.glEnd();
		}
	}
	
	private void dibujarCirculo(GL2 gl){	
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			for(Point2D.Float p : this._puntos){
				gl.glVertex3f(p.x, p.y, 0.0f);
			}
			gl.glVertex3f(this._puntos.get(0).x, this._puntos.get(0).y, 0.0f);
		gl.glEnd();
	}
	
	
	private ArrayList<Point2D.Float> getVerticesCirculo(int cantVertices, float radio)
	{
		double angulo = 0;
		double pasoAngular = (2*Math.PI)/cantVertices;
		ArrayList<Point2D.Float> puntos = new ArrayList<Point2D.Float>(); 

		for(int i = 0; i < cantVertices; i++, angulo+=pasoAngular)
		{
			Point2D.Float punto = new Point2D.Float(radio*(float)Math.cos(angulo), radio*(float)Math.sin(angulo));
			puntos.add(punto);
		}	
		return puntos; 
	}
	
	public void showColors(){
		this._showColors = true;
	}
	
	public void hideColors(){
		this._showColors = false;
	}
}
