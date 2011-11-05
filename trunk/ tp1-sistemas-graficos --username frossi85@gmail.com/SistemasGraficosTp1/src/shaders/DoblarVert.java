package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class DoblarVert extends VertexShader{
	private String fileName = "Doblar.vert";
	private float angulo;
	private int memAngulo = 1;
	private int memDistancia = 2;
	private float distancia;
	public static String NOMBRE_ANGULO = "angulo"; 
	public static String NOMBRE_DISTANCIA ="distancia";
	
	public DoblarVert(float angulo, float distancia){
		this.angulo = angulo;
		this.distancia = distancia;
	}
	
	public float getAngulo(){
		return this.angulo;
	}
	
	public int getMemAngulo(){
		return this.memAngulo;
	}
	
	public float getDistancia(){
		return this.distancia;
	}
	
	public int getMemDistancia(){
		return this.memDistancia;
	}
	
	public void bind(GLAutoDrawable gLDrawable,int shaderprogram){
		
		GL2 gl_shader = gLDrawable.getGL().getGL2();
	 	gl_shader.glBindAttribLocation(shaderprogram,this.memAngulo,NOMBRE_ANGULO);	 	
	 	gl_shader.glBindAttribLocation(shaderprogram,this.memDistancia,NOMBRE_DISTANCIA);	 
	}
	
	public void displayVertexAttrib(){
		gl.glVertexAttrib1f(getMemAngulo(),getAngulo() );
		gl.glVertexAttrib1f(getMemDistancia(),getDistancia() );
	  		
	}
	public void displayUniform(){
		
	}	
	public String getFileName(){
		return this.fileName;
	}

}
