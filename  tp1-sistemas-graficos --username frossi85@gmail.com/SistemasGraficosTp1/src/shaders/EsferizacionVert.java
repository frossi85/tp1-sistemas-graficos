package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class EsferizacionVert extends VertexShader{
	private String filename = "Esferizar.vert";
	private float radio;
	private float centro[] = new float[3];
	private float factor;
	private float factorVariable = 0.0f;
	private float time = 0.0f;
	private boolean parado = false;
	int t = 1;
	
	private int memRadio = 1;
	private int memCentro = 3;
	private int memFactor = 2;
	private int memTime;
		
	public static String NOMBRE_FACTOR = "factor";
	public static String NOMBRE_CENTRO = "centro";
	public static String NOMBRE_RADIO = "radio";
	public static String NOMBRE_TIME = "time";
	
	public EsferizacionVert(float radio, float x, float y,float z, float factor){
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
	
	public void setMemCentro(int mem){
		this.memCentro = mem;
	}
	
	public void setMemRadio(int mem){
		this.memRadio = mem;
	}
	
	public void setMemFactor(int mem){
		this.memFactor = mem;
	}
	
	public void setMemTime(int mem){
		this.memTime = mem;
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
		if (time > 2){
			t = -1;
		}
		if(time < 0.0) {
			t = 1;
		}
			time += t * 0.05f;
			
	}
	
	public float getTime(){
		return this.time;
				
	}
	
	public int getMemTime(){
		return this.memTime;
	}
	
	public void setTime(float time){
		this.time = time;
	}

	
	public float getFactorVariable(){
		return factorVariable;
	}


	@Override
	public void displayVertexAttrib() {
		gl.glVertexAttrib1f(getMemRadio(),getRadio() );
	  	gl.glVertexAttrib1f(getMemFactor(),getFactor() );
	  	gl.glVertexAttrib3f(getMemRadio(),centro[0],centro[1],centro[2] );
	}

	@Override
	public void displayUniform() {
		int location = gl.glGetUniformLocation(this.pgmHandler,NOMBRE_FACTOR);
		gl.glUniform1f(location,getFactorVariable());  
		float centro[] = getCentro();
		setMemTime(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_TIME));
		if(!parado){	
			update();
			gl.glUniform1f(getMemTime(),getTime()); 
		}
		else {
			gl.glUniform1f(getMemTime(),0.6f); 
		}
		 
		setMemFactor(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_FACTOR));
		gl.glUniform1f(getMemFactor(),getFactor()); 
		setMemRadio(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_RADIO));
		gl.glUniform1f(getMemRadio(),getRadio()); 
		setMemCentro(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_CENTRO));
		gl.glUniform3f(getMemCentro(),centro[0],centro[1],centro[2]); 
	}

	@Override
	public String getFileName() {
		return this.filename;
	}
	
	
	public void pararanimacion() {
		parado = true;		
	}
	
	public void reiniciaranimacion(){
		parado = false;
	}
	
	
	
	

}
