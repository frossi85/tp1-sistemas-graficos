package primitivas;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.media.opengl.GL2;

public class anillo {
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
		for(int i = 0; i < this._lateralVertice; i++){
			x = this._radio*(float)Math.cos(angulo);
			y = this._radio*(float)Math.sin(angulo);
			x2 = this._radio*(float)Math.cos(angulo+pasoAngular);
			y2 = this._radio*(float)Math.sin(angulo+pasoAngular);
			
			for(int j = 0; j < cantidadDeOffset; j++){
				gl.glBegin(GL2.GL_POLYGON);
				gl.glVertex3f(x + offset.get((j+1)%cantidadDeOffset).x * (float)Math.cos(angulo), y + offset.get((j+1)%cantidadDeOffset).x * (float)Math.sin(angulo), offset.get((j+1)%cantidadDeOffset).y);
				gl.glVertex3f(x2 + offset.get((j+1)%cantidadDeOffset).x * (float)Math.cos(angulo+pasoAngular), y2 + offset.get((j+1)%cantidadDeOffset).x * (float)Math.sin(angulo+pasoAngular), offset.get((j+1)%cantidadDeOffset).y);
				gl.glVertex3f(x2 + offset.get(j).x * (float)Math.cos(angulo+pasoAngular), y2 + offset.get(j).x * (float)Math.sin(angulo+pasoAngular), offset.get(j).y);
				gl.glVertex3f(x + offset.get(j).x * (float)Math.cos(angulo), y + offset.get(j).x * (float)Math.sin(angulo), offset.get(j).y);
				gl.glEnd();	
			}
			
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
	

}
