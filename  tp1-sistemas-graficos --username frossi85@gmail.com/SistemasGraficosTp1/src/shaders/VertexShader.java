package shaders;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public abstract class VertexShader {
	protected int pgmHandler;
	protected GL2 gl;
	abstract public void bind(GLAutoDrawable gLDrawable,int shaderprogram);
	abstract public void displayVertexAttrib();
	abstract public void displayUniform();	
	abstract public String getFileName();

	public void setInfos(int pgmHandler, GL2 gl){
		this.pgmHandler = pgmHandler;
		this.gl = gl;
	}
	
}
