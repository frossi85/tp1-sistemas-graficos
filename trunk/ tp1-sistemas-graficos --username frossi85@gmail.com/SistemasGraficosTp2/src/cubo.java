
import javax.media.opengl.GL2;

public class cubo {

	private int _interVertice;
	private float _medioAncho;
	private boolean _showColors = false;
	
	public cubo(float medioAncho, int interVertice){
		this._interVertice = interVertice;
		this._medioAncho = medioAncho;
	}
	
	public void dibujar(GL2 gl){
		int x, y, z;
		float paso = 1 / (float)this._interVertice;
	
		float noColor[] = {1.0f, 1.0f, 1.0f};
		float colors[][] = {
				{1.0f, 0.0f, 0.0f},
				{0.0f, 1.0f, 0.0f},
				{0.0f, 0.0f, 1.0f},
				{0.5f, 0.5f, 0.0f},
				{0.5f, 0.0f, 0.5f},
				{0.0f, 0.5f, 0.5f},
		};
		
		float p;
		for(int f = 0; f < 2; f++){
			p = (f%2 == 0)? -1 * this._medioAncho : this._medioAncho;
			for(float j = -this._medioAncho; j < this._medioAncho; j+= paso){
				for(float i = -this._medioAncho; i < this._medioAncho; i+= paso){
					gl.glBegin(GL2.GL_POLYGON);
						if(this._showColors)
							gl.glColor3fv(colors[f],0);
						else gl.glColor3fv(noColor,0);
						gl.glVertex3f(i, j+paso, p);
						gl.glVertex3f(i+paso, j+paso, p);
						gl.glVertex3f(i+paso, j, p);
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
							gl.glColor3fv(colors[f],0);
						else gl.glColor3fv(noColor,0);
						gl.glVertex3f(p, i, j+paso);
						gl.glVertex3f(p, i+paso, j+paso);
						gl.glVertex3f(p, i+paso, j);
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
							gl.glColor3fv(colors[f],0);
						else gl.glColor3fv(noColor,0);
						gl.glVertex3f(i, p,j+paso);
						gl.glVertex3f(i+paso, p, j+paso);
						gl.glVertex3f(i+paso, p, j);
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
}
