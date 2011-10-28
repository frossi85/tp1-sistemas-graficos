package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class Retorcer {
	
	private float angulo;
	private float time;
	public static String NOMBRE_ANGULO = "angulo";
	public static String NOMBRE_TIME = "time";
	private int memAngulo;
	
public Retorcer(float angulo){
		this.angulo = angulo;
		this.time = 0.0f;
	}

public void bind(GLAutoDrawable gLDrawable,int shaderprogram){
	GL2 gl_shader = gLDrawable.getGL().getGL2();
 	gl_shader.glBindAttribLocation(shaderprogram,this.memAngulo,NOMBRE_ANGULO);
}

public float getAngulo(){
	return this.angulo;
}

public float getTime(){
	return this.time;
			
}

public void setTime(float time){
	this.time = time;
}

public int getMemAngulo(){
	return this.memAngulo;
}

}
