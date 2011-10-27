package primitivas;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.media.opengl.GL2;


public class esfera implements primitiva{

	private float _radio;
	private int _horizontalVertice;
	private int _verticalVertice;
	private ArrayList<Point2D.Float> _offset;
	private ArrayList<Point2D.Float> _coords;
	public esfera(float radio, int horizontalVertice, int verticalVertice){
		this._radio = radio;
		if(verticalVertice > 2)
			this._verticalVertice = verticalVertice;
		else this._verticalVertice = 2;
		if(horizontalVertice > 2)
			this._horizontalVertice = horizontalVertice;
		else this._horizontalVertice = 2;
		
		this._offset = this.getOffset();
		this._coords = this.getCoords();
	}
	
	public void dibujar(GL2 gl){
		
		
		int i2, j2;
		float[] no;
		float texX, texY;
		float texXPaso = 3.0f / this._horizontalVertice;
		float texYPaso = 3.0f / this._verticalVertice;
		texX = 0;
		for(int i = 0; i < this._horizontalVertice; i++){		
			i2 = (i+1)%this._horizontalVertice;
			texY = texYPaso;
			for(int j = 1; j < this._verticalVertice; j++){
				j2 = (j+1)%this._verticalVertice;
				gl.glBegin(GL2.GL_TRIANGLES);
				no = getNormal(
						this._coords.get(i2).x * this._offset.get(j2).x,
						this._coords.get(i2).y * this._offset.get(j2).x,
						this._offset.get(j2).y,
						this._coords.get(i).x * this._offset.get(j2).x,
						this._coords.get(i).y * this._offset.get(j2).x,
						this._offset.get(j2).y,
						this._coords.get(i2).x * this._offset.get(j).x,
						this._coords.get(i2).y * this._offset.get(j).x,
						this._offset.get(j).y);
				gl.glNormal3f(no[0], no[1], no[2]);
				gl.glTexCoord2f(texX, texY + texYPaso);
				gl.glVertex3f(this._coords.get(i).x * this._offset.get(j2).x,
							this._coords.get(i).y * this._offset.get(j2).x,
							this._offset.get(j2).y);
				gl.glTexCoord2f(texX+ texXPaso, texY + texYPaso);
				gl.glVertex3f(this._coords.get(i2).x * this._offset.get(j2).x,
							this._coords.get(i2).y * this._offset.get(j2).x,
							this._offset.get(j2).y);
				gl.glTexCoord2f(texX, texY);
				gl.glVertex3f(this._coords.get(i).x * this._offset.get(j).x,
							this._coords.get(i).y * this._offset.get(j).x,
							this._offset.get(j).y);
				
				gl.glTexCoord2f(texX, texY);
				gl.glVertex3f(this._coords.get(i).x * this._offset.get(j).x,
							this._coords.get(i).y * this._offset.get(j).x,
							this._offset.get(j).y);
				gl.glTexCoord2f(texX+ texXPaso, texY + texYPaso);
				gl.glVertex3f(this._coords.get(i2).x * this._offset.get(j2).x,
							this._coords.get(i2).y * this._offset.get(j2).x,
							this._offset.get(j2).y);
				gl.glTexCoord2f(texX + texXPaso, texY);
				gl.glVertex3f(this._coords.get(i2).x * this._offset.get(j).x,
							this._coords.get(i2).y * this._offset.get(j).x,
							this._offset.get(j).y);
				
				gl.glEnd();	
				texY += texYPaso;
			}
			texX += texXPaso;
		}
		
	
		gl.glBegin(GL2.GL_TRIANGLES);
		Point2D.Float minOffset = this._offset.get(this._offset.size()-1);
		Point2D.Float maxOffset = this._offset.get(1);
		
		texX = 0;
		for(int i = 0; i < this._horizontalVertice; i++){
			i2 = (i+1)% this._horizontalVertice;
			no = getNormal(0.0f, 0.0f, this._radio,
					this._coords.get(i2).x*minOffset.x, this._coords.get(i2).y*minOffset.x, -minOffset.y,
					this._coords.get(i).x*minOffset.x, this._coords.get(i).y*minOffset.x, -minOffset.y);
			gl.glNormal3f(no[0], no[1], no[2]);
			gl.glTexCoord2f(texX, 1);
			gl.glVertex3f(0.0f, 0.0f, this._radio);
			gl.glTexCoord2f(texX, 1-texYPaso);
			gl.glVertex3f(this._coords.get(i2).x*minOffset.x, this._coords.get(i2).y*minOffset.x, -minOffset.y);
			gl.glTexCoord2f(texX + texXPaso, 1-texYPaso);
			gl.glVertex3f(this._coords.get(i).x*minOffset.x, this._coords.get(i).y*minOffset.x, -minOffset.y);
		}
		texX = 0;
		for(int i = 0; i < this._horizontalVertice; i++){
			i2 = (i+1)% this._horizontalVertice;	
			no = getNormal(0.0f, 0.0f, -this._radio,
					this._coords.get(i).x*maxOffset.x, this._coords.get(i).y*maxOffset.x, -maxOffset.y,
					this._coords.get(i2).x*maxOffset.x, this._coords.get(i2).y*maxOffset.x, -maxOffset.y
					);
			gl.glNormal3f(no[0], no[1], no[2]);
			gl.glTexCoord2f(texX, 0);
			gl.glVertex3f(0.0f, 0.0f, -this._radio);
			gl.glTexCoord2f(texX, texYPaso);
			gl.glVertex3f(this._coords.get(i2).x*maxOffset.x, this._coords.get(i2).y*maxOffset.x, -maxOffset.y);			
			gl.glTexCoord2f(texX + texXPaso, texYPaso);			
			gl.glVertex3f(this._coords.get(i).x*maxOffset.x, this._coords.get(i).y*maxOffset.x, -maxOffset.y);
			texX += texXPaso;
		}
		gl.glEnd();
	}
	
	private ArrayList<Point2D.Float> getOffset(){
		ArrayList<Point2D.Float> res = new ArrayList<Point2D.Float>();
		double angulo = 0;
		double pasoAngular = Math.PI/this._verticalVertice;
		
		for(int i = 0; i < this._verticalVertice; i++){
			//angulo = (angulo > Math.PI)? 0.0f : angulo;
			Point2D.Float p = new Point2D.Float((float)Math.sin(angulo),this._radio*(float)Math.cos(angulo));
			res.add(p);
			angulo += pasoAngular;
		}
		return res;
	}
	
	private ArrayList<Point2D.Float> getCoords(){
	
		ArrayList<Point2D.Float> res = new ArrayList<Point2D.Float>();
		double angulo = 0;
		double pasoAngular = (2*Math.PI)/this._horizontalVertice;
		
		for(int i = 0; i < this._horizontalVertice; i++){
			Point2D.Float p = new Point2D.Float(this._radio*(float)Math.cos(angulo), this._radio*(float)Math.sin(angulo));
			res.add(p);
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
