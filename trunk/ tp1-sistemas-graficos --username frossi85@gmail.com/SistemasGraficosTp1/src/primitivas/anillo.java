package primitivas;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.media.opengl.GL2;

public class anillo implements primitiva{
	private float _radio;
	private float _miniRadio;
	private int _lateralVertice;
	private int _radialVertice;
	
	public anillo(float radio, float miniRadio, int lateralVertice, int radialVertice){
		this._radio = radio;
		this._miniRadio = miniRadio;
		if(lateralVertice > 2)
			this._lateralVertice = lateralVertice;
		else this._lateralVertice = 2;
		if(radialVertice > 2)
			this._radialVertice = radialVertice;
		else this._radialVertice = 2;
		
	
	}
	
	public void dibujar(GL2 gl){	
		double angulo = 0;
		double pasoAngular = (2*Math.PI)/this._lateralVertice;
		
		ArrayList<Point2D.Float> offset = this.getOffset();
		int cantidadDeOffset = offset.size();
		
		float x, y, x2, y2;
		float[] no;
		float texX, texY;
		float texXPaso = 6.0f / this._lateralVertice;
		float texYPaso = 3.0f / this._radialVertice;
		texX = 0;
		for(int i = 0; i < this._lateralVertice; i++){
			x = this._radio*(float)Math.cos(angulo);
			y = this._radio*(float)Math.sin(angulo);
			x2 = this._radio*(float)Math.cos(angulo+pasoAngular);
			y2 = this._radio*(float)Math.sin(angulo+pasoAngular);
			texY = 0;
			for(int j = 0; j < cantidadDeOffset; j++){
				gl.glBegin(GL2.GL_POLYGON);
				
				no = getNormal(
						x + offset.get((j+1)%cantidadDeOffset).x * (float)Math.cos(angulo),
						y + offset.get((j+1)%cantidadDeOffset).x * (float)Math.sin(angulo),
						offset.get((j+1)%cantidadDeOffset).y,
						x2 + offset.get((j+1)%cantidadDeOffset).x * (float)Math.cos(angulo+pasoAngular),
						y2 + offset.get((j+1)%cantidadDeOffset).x * (float)Math.sin(angulo+pasoAngular),
						offset.get((j+1)%cantidadDeOffset).y,
						x2 + offset.get(j).x * (float)Math.cos(angulo+pasoAngular),
						y2 + offset.get(j).x * (float)Math.sin(angulo+pasoAngular),
						offset.get(j).y);
				
				gl.glNormal3f(no[0], no[1], no[2]);
				
				gl.glTexCoord2f(texX, texY + texYPaso);
				gl.glVertex3f(x + offset.get((j+1)%cantidadDeOffset).x * (float)Math.cos(angulo),
							y + offset.get((j+1)%cantidadDeOffset).x * (float)Math.sin(angulo),
							offset.get((j+1)%cantidadDeOffset).y);
				gl.glTexCoord2f(texX + texXPaso, texY + texYPaso);
				gl.glVertex3f(x2 + offset.get((j+1)%cantidadDeOffset).x * (float)Math.cos(angulo+pasoAngular),
							y2 + offset.get((j+1)%cantidadDeOffset).x * (float)Math.sin(angulo+pasoAngular),
							offset.get((j+1)%cantidadDeOffset).y);
				gl.glTexCoord2f(texX + texXPaso, texY);
				gl.glVertex3f(x2 + offset.get(j).x * (float)Math.cos(angulo+pasoAngular),
							y2 + offset.get(j).x * (float)Math.sin(angulo+pasoAngular),
							offset.get(j).y);
				gl.glTexCoord2f(texX, texY);
				gl.glVertex3f(x + offset.get(j).x * (float)Math.cos(angulo),
							y + offset.get(j).x * (float)Math.sin(angulo),
							offset.get(j).y);
				gl.glEnd();	
				texY += texYPaso;
			}
			texX += texXPaso;
			angulo += pasoAngular;
		}
		gl.glEnd();
	}
	
	private ArrayList<Point2D.Float> getOffset(){
		ArrayList<Point2D.Float> res = new ArrayList<Point2D.Float>();
		double angulo = 0;
		double pasoAngular = (2*Math.PI)/this._radialVertice;
		
		for(int i = 0; i < this._radialVertice; i++){
			
			Point2D.Float punto = new Point2D.Float(this._miniRadio*(float)Math.cos(angulo), this._miniRadio*(float)Math.sin(angulo));
			res.add(punto);
			
			angulo += pasoAngular;
		}
		
		return res;
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
