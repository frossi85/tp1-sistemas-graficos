package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.lang.Math;

public class Doblar {
	
	private float angulo;
	private float distancia;
	private float alturaMax;
	private float diametro;
	private float time = 0.0f;
	
	public static String NOMBRE_ANGULO = "angulo";
	public static String NOMBRE_DISTANCIA = "distancia";
	public static String NOMBRE_ALTURA_MAX = "alturaMax";
	public static String NOMBRE_DIAMETRO = "diametro";
	public static String NOMBRE_TIME = "time";
	
	private int memAngulo;
	private int memDistancia;
	private int memAlturaMax;
	private int memDiametro;
	private int memTime;
	
	public Doblar(float angulo, float distancia,float diametro,float alturaMax){
			this.angulo = angulo;
			this.distancia = distancia;
			this.diametro = diametro;
			this.alturaMax = alturaMax;
			
	}
	
	public void bind(GLAutoDrawable gLDrawable,int shaderprogram){
		GL2 gl_shader = gLDrawable.getGL().getGL2();
	 	gl_shader.glBindAttribLocation(shaderprogram,this.memAngulo,NOMBRE_ANGULO);
	 	gl_shader.glBindAttribLocation(shaderprogram,this.memDistancia,NOMBRE_DISTANCIA);
	}

	public float getAngulo(){
		return this.angulo;
	}

	public float getDiametro(){
		return this.diametro;
	}
	
	public float getDistancia(){
		return this.distancia;
	}
	
	public float getAlturaMax(){
		return this.alturaMax;
	}
	
	public int getMemAngulo(){
		return this.memAngulo;
	}
	
	public int getMemDiametro(){
		return this.memDiametro;
	}
	
	public int getMemDistancia(){
		return this.memDistancia;
	}
	
	public int getMemTime(){
		return this.memTime;
	}
	
	public int getMemAlturaMax(){
		return this.memAlturaMax;
	}
	
	public float alturaMaxEsfera(float rad){
		return (rad*(float)Math.sin(Math.PI/2));
	}
	
	public float alturaMaxAnillo(float radExt){
		return (radExt*(float)Math.sin(Math.PI/2));
	}
	
	public void setAlturamax(float altura){
		this.alturaMax = altura;
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

	public void setMemAlturaMax(int memAltura){
		this.memAlturaMax = memAltura;
	}
	
	public void setMemAngulo(int memAngulo){
		this.memAngulo = memAngulo;
	}
	
	public void setMemDistancia(int memDistancia){
		this.memDistancia = memDistancia;
	}
	
	public void setMemTime(int memTime){
		this.memTime = memTime;
	}
}
