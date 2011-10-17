package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class Retorcer {
	
	private float angulo;
	public static String NOMBRE_ANGULO = "angulo";
	private int memAngulo;
	
public Retorcer(float angulo){
		this.angulo = angulo;
	}

public void bind(GLAutoDrawable gLDrawable,int shaderprogram){
	GL2 gl_shader = gLDrawable.getGL().getGL2();
 	gl_shader.glBindAttribLocation(shaderprogram,this.memAngulo,NOMBRE_ANGULO);
}

public float getAngulo(){
	return this.angulo;
}

public int getMemAngulo(){
	return this.memAngulo;
}

}
