package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class DoblarVert extends VertexShader{
	private String fileName = "Doblar.vert";
	private float angulo;
	private float distancia;
	private float alturaMax;
	private float diametro;
	private float time = 0.0f;
	private boolean parado = false;
	
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
	
	private int lado = 1;
	
	public DoblarVert(float angulo, float distancia,float diametro,float alturaMax){
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
		if (time > 0.5){
			lado = -1;
		}
		else if (time < 0) lado = 1;
			
			time += lado*0.01f;
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
	
	public void displayVertexAttrib(){/*
		gl.glVertexAttrib1f(getMemAngulo(),getAngulo() );
		gl.glVertexAttrib1f(getMemDistancia(),getDistancia() );*/
	  		
	}
	public void displayUniform(){
		setMemTime(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_TIME));
		if(!parado){
			update();
			gl.glUniform1f(getMemTime(),getTime());
		} else {
			gl.glUniform1f(getMemTime(),0.3f);
		}
		setMemAngulo(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_ANGULO));
		gl.glUniform1f(getMemAngulo(),getAngulo());
		setMemDistancia(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_DISTANCIA));
		gl.glUniform1f(getMemDistancia(),getDistancia());
		setMemAlturaMax(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_ALTURA_MAX));
		gl.glUniform1f(getMemAlturaMax(),getAlturaMax());
		
	}	
	public String getFileName(){
		return this.fileName;
	}


	public void pararanimacion() {
		parado = true;		
	}
	
	public void reiniciaranimacion(){
		parado = false;
	}

}
