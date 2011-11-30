package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class RuidoVert extends VertexShader{

	private String fileName = "Ruido.vert";
	private float fase;
	private float amplitud;
	private float longOnda;
	private int memFase;
	private int memAmplitud;
	private int memLongOnda;
	private int memTime;
	private float time;
	public static String NOMBRE_FASE = "fase";
	public static String NOMBRE_AMPLITUD = "amplitud";
	public static String NOMBRE_LONGONDA = "longOnda";
	public static String NOMBRE_TIME = "time";
	
	public RuidoVert(float fase,float amplitud,float longOnda){
			
			this.amplitud = amplitud;
			this.longOnda = longOnda;
			this.fase = fase;
			this.time = 1f;
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
	
	public void setMemAmplitud(int memAmplitud){
		this.memAmplitud = memAmplitud;
	}

	public void setMemLongOnda(int memLongOnda){
		this.memLongOnda = memLongOnda;
	}

	public void setMemFase(int memFase){
		this.memFase = memFase;
	}
	@Override
	public void displayVertexAttrib() {
		/*gl.glVertexAttrib1f(getMemAmplitud(),getAmplitud() );
		gl.glVertexAttrib1f(getMemFase(),getFase() );
		gl.glVertexAttrib1f(getMemLongOnda(),getLongOnda() );
		*/
	}
	
	@Override
	public void displayUniform() {
		int location = gl.glGetUniformLocation(this.pgmHandler, NOMBRE_TIME);
		gl.glUniform1f(location,getTime());  
		setTime(getTime() + 1.0f);
		
		setMemAmplitud(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_AMPLITUD));
		gl.glUniform1f(getMemAmplitud(),getAmplitud());
		
		setMemLongOnda(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_LONGONDA));
		gl.glUniform1f(getMemLongOnda(),getLongOnda());
    	
		setMemFase(gl.glGetUniformLocation(this.pgmHandler,NOMBRE_FASE));
		gl.glUniform1f(getMemFase(),getFase());
		
		
	}
	
	@Override
	public String getFileName() {
		return this.fileName;
	}
}
