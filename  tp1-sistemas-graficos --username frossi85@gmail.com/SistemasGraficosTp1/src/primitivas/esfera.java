package primitivas;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.media.opengl.GL2;


public class esfera {

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
		for(int i = 0; i < this._horizontalVertice; i++){		
			i2 = (i+1)%this._horizontalVertice;
			for(int j = 0; j < this._verticalVertice-1; j++){
				j2 = (j+1)%this._verticalVertice;
				gl.glBegin(GL2.GL_POLYGON);
				gl.glVertex3f(this._coords.get(i).x * this._offset.get(j2).x,
							this._coords.get(i).y * this._offset.get(j2).x,
							this._offset.get(j2).y);
				gl.glVertex3f(this._coords.get(i2).x * this._offset.get(j2).x,
							this._coords.get(i2).y * this._offset.get(j2).x,
							this._offset.get(j2).y);
				gl.glVertex3f(this._coords.get(i2).x * this._offset.get(j).x,
							this._coords.get(i2).y * this._offset.get(j).x,
							this._offset.get(j).y);
				gl.glVertex3f(this._coords.get(i).x * this._offset.get(j).x,
							this._coords.get(i).y * this._offset.get(j).x,
							this._offset.get(j).y);
				gl.glEnd();	
			}
		}
		
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
			gl.glVertex3f(0.0f, 0.0f, this._radio);
			Point2D.Float maxOffset = this._offset.get(0);
			for(Point2D.Float p : this._coords){
				gl.glVertex3f(p.x*maxOffset.x, p.y*maxOffset.x, maxOffset.y);
			}
			gl.glVertex3f(this._coords.get(0).x*maxOffset.x, this._coords.get(0).y*maxOffset.x, maxOffset.y);
		gl.glEnd();
		
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glVertex3f(0.0f, 0.0f, -this._radio);
		Point2D.Float minOffset = this._offset.get(this._offset.size()-1);
		for(Point2D.Float p : this._coords){
			gl.glVertex3f(p.x*minOffset.x, p.y*minOffset.x, minOffset.y);
		}
		gl.glVertex3f(this._coords.get(0).x*minOffset.x, this._coords.get(0).y*minOffset.x, minOffset.y);
	gl.glEnd();
	
	}
	
	private ArrayList<Point2D.Float> getOffset(){
		ArrayList<Point2D.Float> res = new ArrayList<Point2D.Float>();
		double angulo = 0;
		double pasoAngular = Math.PI/this._verticalVertice;
		
		for(int i = 0; i < this._verticalVertice; i++){
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
		
		for(int i = 0; i < this._verticalVertice; i++){
			Point2D.Float p = new Point2D.Float(this._radio*(float)Math.cos(angulo), this._radio*(float)Math.sin(angulo));
			res.add(p);
			angulo += pasoAngular;
		}
		return res;
	}
}
