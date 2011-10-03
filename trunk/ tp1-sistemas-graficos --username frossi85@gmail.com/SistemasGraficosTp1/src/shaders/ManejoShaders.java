package shaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;

public class ManejoShaders {

private String archivo;
static int POSITION_BUFFER_SIZE = 9;
private FloatBuffer positionData = FloatBuffer.allocate (POSITION_BUFFER_SIZE);
private FloatBuffer colorData = FloatBuffer.allocate (POSITION_BUFFER_SIZE);
private IntBuffer vaoHandle = IntBuffer.allocate(1);

float [] positionDataOrig = 
{
    -0.8f, -0.8f, 0.0f,
     0.8f, -0.8f, 0.0f,
     0.0f,  0.8f, 0.0f
};

int positionBufferHandle;
    
float colorDataOrig[] = 
  {
     1.0f,  0.0f, 0.0f,
     0.0f,  1.0f, 0.0f,
     0.0f,  0.0f, 1.0f
  };

int colorBufferHandle;

public void ManejoShader(String nombreArchivo){
	this.archivo = nombreArchivo;
	
}

public void bindBuffer(GLAutoDrawable gLDrawable){
	
	GL2 gl_aux = gLDrawable.getGL().getGL2();	// para usar glShadeModel
	GL4 gl_shader = gLDrawable.getGL().getGL4();
	gl_shader.glClearColor (0.02f, 0.02f, 0.04f, 0.0f);
  	gl_aux.glShadeModel (GL2.GL_SMOOTH);
  	gl_shader.glEnable(GL2.GL_DEPTH_TEST);
  	
  	
  	Buffer buff_aux = null;
  	int vboHandles[] = new int [2];
  	gl_shader.glGenBuffers(2, vboHandles,0);
  	positionBufferHandle = vboHandles[0];
  	colorBufferHandle = vboHandles[1];
  	long long_aux = 9*4;  // 9* sizeof(float)
  	positionData.put(positionDataOrig);
  	colorData.put(colorDataOrig);
  	gl_shader.glBindBuffer( GL.GL_ARRAY_BUFFER, positionBufferHandle );
  	gl_shader.glBufferData( GL.GL_ARRAY_BUFFER, long_aux, positionData, GL.GL_STATIC_DRAW );
  	gl_shader.glBindBuffer( GL.GL_ARRAY_BUFFER, colorBufferHandle );
    gl_shader.glBufferData(GL. GL_ARRAY_BUFFER, long_aux, colorData, GL.GL_STATIC_DRAW );
    // Create and set-up the vertex array objet
    gl_shader.glGenVertexArrays( 1, vaoHandle );
    gl_shader.glBindVertexArray( vaoHandle.get(0) );
    // Enable the vertex attributes array
    gl_shader.glEnableVertexAttribArray(0);
    gl_shader.glEnableVertexAttribArray(1);
    // Map index 0 to the position buffer
    gl_shader.glBindBuffer( GL.GL_ARRAY_BUFFER, positionBufferHandle);
    gl_shader.glVertexAttribPointer( 0, 3, GL.GL_FLOAT, false, 0,buff_aux);
    // Map index 1 to the color buffer
    gl_shader.glBindBuffer( GL.GL_ARRAY_BUFFER, colorBufferHandle);
    gl_shader.glVertexAttribPointer( 1, 3, GL.GL_FLOAT, false, 0, buff_aux);
    
}

public void compiladoLinkeado(GLAutoDrawable gLDrawable){
	
	GL4 gl_shader = gLDrawable.getGL().getGL4();
	GL2ES2 gles = gLDrawable.getGL().getGL2ES2();
    int creador = gl_shader.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
    BufferedReader brv = null;
	try {
		brv = new BufferedReader(new FileReader(this.archivo));
	} catch (FileNotFoundException e) {
		System.out.println("No se pudo abrir archivo de Shaders");
		e.printStackTrace();
	}
    String vsrc = "";
    String line;
    try {
		while ((line=brv.readLine()) != null) {
		  vsrc += line + "\n";
		}
	} catch (IOException e) {
		System.out.println("Problemas al leer archivo de Shaders");
		e.printStackTrace();
	}
    //Agregado facundo
    String [] vectorVsrc = new String [1];
    vectorVsrc[0] = vsrc;
    
    gl_shader.glShaderSource(creador, 1, vectorVsrc, (int[])null, 0);	
    gl_shader.glCompileShader(creador);
    
    /* 	INCOMPLETO  */
}

public static void main(String[] args) {
	// TODO Auto-generated method stub
	}

}
