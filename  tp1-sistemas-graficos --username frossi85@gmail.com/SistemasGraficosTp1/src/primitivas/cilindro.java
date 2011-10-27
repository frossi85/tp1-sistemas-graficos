package primitivas;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.media.opengl.GL2;

public class cilindro implements primitiva{
	
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
		
		if(caras > 2)
			this._caras = caras;
		else this._caras = 2;
		
		if(anillos > 0)
			this._anillos = anillos;
		else this._anillos = 1;
		
		this._puntos = this.getVerticesCirculo(this._caras, this._radio);
	}
	
	public void dibujar(GL2 gl){
		int cantidadDePuntos = this._puntos.size();
		int i2;
		gl.glBegin(GL2.GL_TRIANGLES);
			
			
			for(int i = 0; i < cantidadDePuntos; i++){
				i2 = (i+1)%cantidadDePuntos;
				
				if(this._showColors)
					gl.glColor3fv(this._colors[0],0);
				else gl.glColor3fv(this._noColor,0);
				
				gl.glNormal3f(0.0f,0.0f,1);
				gl.glTexCoord2f(0.5f, 0.5f); 
				gl.glVertex3f(0.0f, 0.0f, this._mediaAltura);
				gl.glTexCoord2f(this._puntos.get(i2).x/this._radio, this._puntos.get(i2).y/this._radio); 
				gl.glVertex3f(this._puntos.get(i2).x, this._puntos.get(i2).y, this._mediaAltura);
				gl.glTexCoord2f(this._puntos.get(i).x/this._radio, this._puntos.get(i).y/this._radio); 
				gl.glVertex3f(this._puntos.get(i).x, this._puntos.get(i).y, this._mediaAltura);
				
				
				if(this._showColors)
					gl.glColor3fv(this._colors[1],0);
				else gl.glColor3fv(this._noColor,0);
				
				gl.glNormal3f(0.0f,0.0f,-1);
				gl.glTexCoord2f(0.5f, 0.5f); 
				gl.glVertex3f(0.0f, 0.0f, -this._mediaAltura);
				gl.glTexCoord2f(this._puntos.get(i2).x/this._radio, this._puntos.get(i2).y/this._radio); 
				gl.glVertex3f(this._puntos.get(i2).x, this._puntos.get(i2).y, -this._mediaAltura);
				gl.glTexCoord2f(this._puntos.get(i).x/this._radio, this._puntos.get(i).y/this._radio); 
				gl.glVertex3f(this._puntos.get(i).x, this._puntos.get(i).y, -this._mediaAltura);
			}
		gl.glEnd();
		
		
		float alturaAnillo = this._mediaAltura * 2 / this._anillos;
		float zM;
		float[] no;
		float texX, texY, texXPaso, texYPaso;
		texXPaso = 3.0f / this._caras;
		texYPaso = 1.0f / this._anillos;
		
		
		
		if(this._showColors)
			gl.glColor3fv(this._colors[2],0);
		else gl.glColor3fv(this._noColor,0);
		
		
		texY = 0;
		for(float z = -this._mediaAltura; z < this._mediaAltura; z += alturaAnillo){
			zM = (z + alturaAnillo < this._mediaAltura)? z+alturaAnillo : this._mediaAltura;
			texX = 0;			
			for(int i = 0; i < cantidadDePuntos; i++){
				i2 = (i+1)%cantidadDePuntos;
				gl.glBegin(GL2.GL_TRIANGLES);
					no = getNormal(this._puntos.get(i).x, this._puntos.get(i).y, zM,
							this._puntos.get(i2).x, this._puntos.get(i2).y, zM,
							this._puntos.get(i2).x, this._puntos.get(i2).y, z);
					gl.glNormal3f(no[0], no[1], no[2]);
					
					gl.glTexCoord2f(texX, texY+texYPaso); gl.glVertex3f(this._puntos.get(i).x, this._puntos.get(i).y, zM);
					gl.glTexCoord2f(texX+texXPaso, texY+texYPaso); gl.glVertex3f(this._puntos.get(i2).x, this._puntos.get(i2).y, zM);
					gl.glTexCoord2f(texX, texY); gl.glVertex3f(this._puntos.get(i).x, this._puntos.get(i).y, z);
					
					gl.glTexCoord2f(texX, texY); gl.glVertex3f(this._puntos.get(i).x, this._puntos.get(i).y, z);
					gl.glTexCoord2f(texX+texXPaso, texY+texYPaso); gl.glVertex3f(this._puntos.get(i2).x, this._puntos.get(i2).y, zM);
					gl.glTexCoord2f(texX+texXPaso, texY); gl.glVertex3f(this._puntos.get(i2).x, this._puntos.get(i2).y, z);
					
	
				gl.glEnd();
				texX += texXPaso;
			}
			texY += texYPaso;
			
			/*gl.glBegin(GL2.GL_POLYGON);
				no = getNormal(this._puntos.get(cantidadDePuntos-1).x, this._puntos.get(cantidadDePuntos-1).y, zM,
						this._puntos.get(0).x, this._puntos.get(0).y, zM,
						this._puntos.get(0).x, this._puntos.get(0).y, z);
				gl.glNormal3f(no[0], no[1], no[2]);
				gl.glVertex3f(this._puntos.get(cantidadDePuntos-1).x, this._puntos.get(cantidadDePuntos-1).y, zM);
				gl.glVertex3f(this._puntos.get(0).x, this._puntos.get(0).y, zM);
				gl.glVertex3f(this._puntos.get(0).x, this._puntos.get(0).y, z);
				gl.glVertex3f(this._puntos.get(cantidadDePuntos-1).x, this._puntos.get(cantidadDePuntos-1).y, z);
			gl.glEnd();*/
		}
	}
	
	private void dibujarCirculo(GL2 gl){	
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
			gl.glTexCoord2f(0.5f, 0.5f); gl.glVertex3f(0.0f, 0.0f, 0.0f);
			for(Point2D.Float p : this._puntos){
				gl.glTexCoord2f(p.x/this._radio, p.y/this._radio); gl.glVertex3f(p.x, p.y, 0.0f);
			}
			gl.glTexCoord2f(this._puntos.get(0).x/this._radio, this._puntos.get(0).y/this._radio); gl.glVertex3f(this._puntos.get(0).x, this._puntos.get(0).y, 0.0f);
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
	
	private float[] getNormal(float x, float y, float z, float x2, float y2, float z2, float x3, float y3, float z3){
		float Qx, Qy, Qz, Px, Py, Pz, Nx, Ny, Nz;
		/* Qx = fVert2[0]-fVert1[0];
		   Qy = fVert2[1]-fVert1[1];
		   Qz = fVert2[2]-fVert1[2];
		   Px = fVert3[0]-fVert1[0];
		   Py = fVert3[1]-fVert1[1];
		   Pz = fVert3[2]-fVert1[2];*/
		Qx = x2 - x;
		Qy = y2 - y;
		Qz = z2 - z;
		
		Px = x3 - x;
		Py = y3 - y;
		Pz = z3 - z;
		
		/*
		   *fNormalX = Py*Qz - Pz*Qy;
		   *fNormalY = Pz*Qx - Px*Qz;
		   *fNormalZ = Px*Qy - Py*Qx;*/
		
		Nx = Py*Qz - Pz*Qy;
		Ny = Pz*Qx - Px*Qz;
		Nz = Px*Qy - Py*Qx;
		
		float[] res = {Nx, Ny, Nz};
		return res;
		
	}
}
