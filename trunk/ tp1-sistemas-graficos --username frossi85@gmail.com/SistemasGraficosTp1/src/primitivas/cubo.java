package primitivas;

import javax.media.opengl.GL2;

public class cubo {

	private int _interVertice;
	private float _medioAncho;
	private boolean _showColors = false;
	private float _noColor[] = {1.0f, 1.0f, 1.0f};
	private float _colors[][] = {
			{1.0f, 0.0f, 0.0f},
			{0.0f, 1.0f, 0.0f},
			{0.0f, 0.0f, 1.0f},
			{0.5f, 0.5f, 0.0f},
			{0.5f, 0.0f, 0.5f},
			{0.0f, 0.5f, 0.5f},
	};
	
	public cubo(float medioAncho, int interVertice){
		if(interVertice <= 0)
			this._interVertice = 1;
		else this._interVertice = interVertice;
		this._medioAncho = medioAncho;
	}
	
	public void dibujar(GL2 gl){
		float iM, jM;
		float paso = 1 / (float)this._interVertice;
	
		float p;
		for(int f = 0; f < 2; f++){
			p = (f%2 == 0)? -1 * this._medioAncho : this._medioAncho;
			for(float j = -this._medioAncho; j < this._medioAncho; j+= paso){
				for(float i = -this._medioAncho; i < this._medioAncho; i+= paso){
					gl.glBegin(GL2.GL_POLYGON);
						if(this._showColors)
							gl.glColor3fv(this._colors[f],0);
						else gl.glColor3fv(this._noColor,0);
						
						jM = (j+paso > this._medioAncho)? this._medioAncho : j+paso;
						iM = (i+paso > this._medioAncho)? this._medioAncho : i+paso;
						
						float[] vert1 = {i,jM,p}, vert2 = {iM,jM,p}, vert3 = {iM,jM,p};
						setNormal(gl, vert1, vert2, vert3);
						gl.glVertex3f(i, jM, p);
						gl.glVertex3f(iM, jM, p);
						gl.glVertex3f(iM, j, p);
						gl.glVertex3f(i, j, p);
					gl.glEnd();
				}
			}
		}
		
		for(int f = 0; f < 2; f++){
			p = (f%2 == 0)? -1 * this._medioAncho : this._medioAncho;
			for(float j = -this._medioAncho; j < this._medioAncho; j+= paso){
				for(float i = -this._medioAncho; i < this._medioAncho; i+= paso){
					gl.glBegin(GL2.GL_POLYGON);
						if(this._showColors)
							gl.glColor3fv(this._colors[f+2],0);
						else gl.glColor3fv(this._noColor,0);
						
						jM = (j+paso > this._medioAncho)? this._medioAncho : j+paso;
						iM = (i+paso > this._medioAncho)? this._medioAncho : i+paso;
						
						float[] vert1 = {p,i,jM}, vert2 = {p,iM,jM}, vert3 = {p,iM,j};
						setNormal(gl, vert1, vert2, vert3);
						
						gl.glVertex3f(p, i, jM);
						gl.glVertex3f(p, iM, jM);
						gl.glVertex3f(p, iM, j);
						gl.glVertex3f(p, i, j);
					gl.glEnd();
				}
			}
		}
		
		for(int f = 0; f < 2; f++){
			p = (f%2 == 0)? -1 * this._medioAncho : this._medioAncho;			
			for(float j = -this._medioAncho; j < this._medioAncho; j+= paso){
				for(float i = -this._medioAncho; i < this._medioAncho; i+= paso){
					gl.glBegin(GL2.GL_POLYGON);
						if(this._showColors)
							gl.glColor3fv(this._colors[f+4],0);
						else gl.glColor3fv(this._noColor,0);
						
						jM = (j+paso > this._medioAncho)? this._medioAncho : j+paso;
						iM = (i+paso > this._medioAncho)? this._medioAncho : i+paso;
						
						float[] vert1 = {i,p,jM}, vert2 = {iM,p,jM}, vert3 = {iM,p,j};
						setNormal(gl, vert1, vert2, vert3);
						
						gl.glVertex3f(i, p,jM);
						gl.glVertex3f(iM, p, jM);
						gl.glVertex3f(iM, p, j);
						gl.glVertex3f(i, p, j);
					gl.glEnd();
				}
			}
		}
	}
	
	public void showColors(){
		this._showColors = true;
	}
	
	public void hideColors(){
		this._showColors = false;
	}
	
	private void setNormal(GL2 gl, float[] fVert1, float[] fVert2, float[] fVert3){
		float Qx,Qy,Qz,Px,Py,Pz, Nx, Ny, Nz;
		Qx = fVert2[0]-fVert1[0];
		Qy = fVert2[1]-fVert1[1];
		Qz = fVert2[2]-fVert1[2];
		Px = fVert3[0]-fVert1[0];
		Py = fVert3[1]-fVert1[1];
		Pz = fVert3[2]-fVert1[2];
		
		Nx = Py*Qz - Pz*Qy;
		Ny = Pz*Qx - Px*Qz;
		Nz = Px*Qy - Py*Qx;
		
		//float v = (float) Math.sqrt(Math.pow(Nx,2) + Math.pow(Ny, 2) + Math.pow(Nz,2));
		//System.out.println(Nx/v + " " + Ny/v + " " + Nz/v);
		
		//System.out.println(Nx + " " + Ny + " " + Nz);
		gl.glNormal3f(-Nx,-Ny,-Nz);		
	}
	
	
}
