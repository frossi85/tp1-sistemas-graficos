package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class Retorcer {
	
	private float angulo;
	private float altura;
	private float time;
	public static String NOMBRE_ANGULO = "angulo";
	public static String NOMBRE_ALTURA = "altura";
	public static String NOMBRE_TIME = "time";
	private int memAngulo;
	private int memAltura;
	private int memTime;
	
public Retorcer(float angulo,float altura){
		this.angulo = angulo;
		this.time = 0.0f;
		this.altura = altura;
	}

public void bind(GLAutoDrawable gLDrawable,int shaderprogram){
	GL2 gl_shader = gLDrawable.getGL().getGL2();
 	gl_shader.glBindAttribLocation(shaderprogram,this.memAngulo,NOMBRE_ANGULO);
}

public float getAngulo(){
	return this.angulo;
}

public float getAltura(){
	return this.altura;
}


public void setMemAltura(int mem){
	this.memAltura = mem;
}

public void setMemAngulo(int mem){
	this.memAngulo = mem;
}

public float getTime(){
	return this.time;
			
}

public int getMemTime(){
	return this.memTime;
}

public void setMemTime(int mem){
	this.memTime = mem;
}

public void setTime(float time){
	this.time = time;
}

public int getMemAngulo(){
	return this.memAngulo;
}


public int getMemAltura(){
	return this.memAltura;
}

public void update(){
	time += 0.05f;
		
	
	
}
}
