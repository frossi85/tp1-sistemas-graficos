package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class Ruido extends Transformaciones {
	
	private float fase;
	private float amplitud;
	private float longOnda;
	private int memFase = 1;
	private int memAmplitud = 2;
	private int memLongOnda = 3;
	private int memTime = 4;
	private float time;
	public static String NOMBRE_FASE = "fase";
	public static String NOMBRE_AMPLITUD = "amplitud";
	public static String NOMBRE_LONGONDA = "longOnda";
	public static String NOMBRE_TIME = "time";
	
public Ruido(float fase,float amplitud,float longOnda){
		
		this.amplitud = amplitud;
		this.longOnda = longOnda;
		this.fase = fase;
		this.time = 0.0f;
		
}

public int getMemAmplitud(){
	return this.memAmplitud;
}

public int getMemLongOnda(){
	return this.memLongOnda;
}

public int getMemFase(){
	return this.memFase;
}

public float getFase(){
	return this.fase;
}

public float getLongOnda(){
	return this.longOnda;
}

public float getAmplitud(){
	return this.amplitud;
}

public void bind(GLAutoDrawable gLDrawable,int shaderprogram){
	
		GL2 gl_shader = gLDrawable.getGL().getGL2();
	 	gl_shader.glBindAttribLocation(shaderprogram,this.memFase,NOMBRE_FASE);
	 	gl_shader.glBindAttribLocation(shaderprogram,this.memAmplitud,NOMBRE_AMPLITUD);
	 	gl_shader.glBindAttribLocation(shaderprogram,this.memLongOnda,NOMBRE_LONGONDA);
	 		   
}

public void setTime(float time){
	this.time = time;
}

public int getMemTime(){
	return this.memTime;
}

public float getTime(){
	return this.time;
}

}
