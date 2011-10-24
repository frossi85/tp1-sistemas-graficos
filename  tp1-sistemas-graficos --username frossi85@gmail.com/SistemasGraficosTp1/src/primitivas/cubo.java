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
					gl.glBegin(GL2.GL_TRIANGLES);
						if(this._showColors)
							gl.glColor3fv(this._colors[f],0);
						else gl.glColor3fv(this._noColor,0);
						
						jM = (j+paso > this._medioAncho)? this._medioAncho : j+paso;
						iM = (i+paso > this._medioAncho)? this._medioAncho : i+paso;
						
						gl.glNormal3f(0,0,2*p);
						
						gl.glTexCoord2f(1-i, 1-jM); gl.glVertex3f(i, jM, p);
						gl.glTexCoord2f(1-iM, 1-jM); gl.glVertex3f(iM, jM, p);
						gl.glTexCoord2f(1-i, 1-j); gl.glVertex3f(i, j, p);
						
						gl.glTexCoord2f(1-i, 1-j); gl.glVertex3f(i, j, p);
						gl.glTexCoord2f(1-iM, 1-jM); gl.glVertex3f(iM, jM, p);
						gl.glTexCoord2f(1-iM, 1-jM); gl.glVertex3f(iM, j, p);
						
					gl.glEnd();
				}
			}
		}
		
		for(int f = 0; f < 2; f++){
			p = (f%2 == 0)? -1 * this._medioAncho : this._medioAncho;
			for(float j = -this._medioAncho; j < this._medioAncho; j+= paso){
				for(float i = -this._medioAncho; i < this._medioAncho; i+= paso){
					gl.glBegin(GL2.GL_TRIANGLES);
						if(this._showColors)
							gl.glColor3fv(this._colors[f+2],0);
						else gl.glColor3fv(this._noColor,0);
						
						jM = (j+paso > this._medioAncho)? this._medioAncho : j+paso;
						iM = (i+paso > this._medioAncho)? this._medioAncho : i+paso;
						
						gl.glNormal3f(2*p,0,0);
						
						gl.glTexCoord2f(1-i, 1-jM); gl.glVertex3f(p, i, jM);
						gl.glTexCoord2f(1-iM, 1-jM); gl.glVertex3f(p, iM, jM);
						gl.glTexCoord2f(1-i, 1-j); gl.glVertex3f(p, i, j);
						
						gl.glTexCoord2f(1-i, 1-j); gl.glVertex3f(p, i, j);						
						gl.glTexCoord2f(1-iM, 1-jM); gl.glVertex3f(p, iM, jM);
						gl.glTexCoord2f(1-iM, 1-j); gl.glVertex3f(p, iM, j);
						
					gl.glEnd();
				}
			}
		}
		
		for(int f = 0; f < 2; f++){
			p = (f%2 == 0)? -1 * this._medioAncho : this._medioAncho;			
			for(float j = -this._medioAncho; j < this._medioAncho; j+= paso){
				for(float i = -this._medioAncho; i < this._medioAncho; i+= paso){
					gl.glBegin(GL2.GL_TRIANGLES);
						if(this._showColors)
							gl.glColor3fv(this._colors[f+4],0);
						else gl.glColor3fv(this._noColor,0);
						
						jM = (j+paso > this._medioAncho)? this._medioAncho : j+paso;
						iM = (i+paso > this._medioAncho)? this._medioAncho : i+paso;
					
						
						gl.glNormal3f(0,2*p,0);
						
						gl.glTexCoord2f(1-i, 1-jM); gl.glVertex3f(i, p,jM);
						gl.glTexCoord2f(1-iM, 1-jM); gl.glVertex3f(iM, p, jM);
						gl.glTexCoord2f(1-i, 1-j); gl.glVertex3f(i, p, j);
						
						gl.glTexCoord2f(1-i, 1-j); gl.glVertex3f(i, p, j);
						gl.glTexCoord2f(1-iM, 1-jM); gl.glVertex3f(iM, p, jM);
						gl.glTexCoord2f(1-iM, 1-j); gl.glVertex3f(iM, p, j);						
						
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
	
}