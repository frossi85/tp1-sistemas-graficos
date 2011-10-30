package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class Esferizacion {
	
	private float radio;
	private float centro[] = new float[3];
	private float factor;
	private float factorVariable = 0.0f;
	private float time = 0.0f;
	
	private int memRadio = 1;
	private int memCentro = 3;
	private int memFactor = 2;
		
	public static String NOMBRE_FACTOR = "factor";
	public static String NOMBRE_CENTRO = "centro";
	public static String NOMBRE_RADIO = "radio";
	public static String NOMBRE_TIME = "time";
	
	public Esferizacion(float radio, float x, float y,float z, float factor){
		this.factor = factor;
		this.radio = radio;
		this.centro[0] = x;
		this.centro[1] = y;
		this.centro[2] = z;
		this.factor = factor;
		
	}
	
	public void bind(GLAutoDrawable gLDrawable,int shaderprogram){
		
		GL2 gl_shader = gLDrawable.getGL().getGL2();
	 	gl_shader.glBindAttribLocation(shaderprogram,this.memFactor,NOMBRE_FACTOR);
	 	gl_shader.glBindAttribLocation(shaderprogram,this.memCentro,NOMBRE_CENTRO);
	 	gl_shader.glBindAttribLocation(shaderprogram,this.memRadio,NOMBRE_RADIO);
	   
}

	public int getMemCentro(){
		return this.memCentro;
	}
	
	public int getMemFactor(){
		return this.memFactor;
	}
	
	public int getMemRadio(){
		return this.memRadio;
	}
	
	public float getRadio(){
		return this.radio;
	}
	
	public float getFactor(){
		return this.factor;
	}
	
	public float[] getCentro(){
		return this.centro;
	}
	
	public void update(){
		if (time < 1.0){
			time += 0.005f;
			
		}
	}
	
	public float getTime(){
		return this.time;
				
	}

	public void setTime(float time){
		this.time = time;
	}

	
	public float getFactorVariable(){
		return factorVariable;
	}
	
}
