package shaders;

import java.io.BufferedReader;
import java.io.File;
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

private static int 	CAPACIDAD_MATRICES = 16;
private FloatBuffer modelViewBuffer = FloatBuffer.allocate (CAPACIDAD_MATRICES);
private FloatBuffer projectionBuffer = FloatBuffer.allocate (CAPACIDAD_MATRICES);
private static String NOMBRE_MODELVIEW_MATRIX = "gl_ModelViewMatrix";
private static String NOMBRE_PROJECTION_MATRIX = "gl_ProjectionMatrix";
private float gl_ModelViewMatrix[] = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
private float gl_ProjectionMatrix[]= {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
private int memoriaModelView;
private int memoriaProyeccion;

private static int 	CAPACIDAD_ATRIBUTOS = 4;
private static String NOMBRE_ATRIBUTO = "gl_Vertex";
private FloatBuffer atribBuffer = FloatBuffer.allocate (CAPACIDAD_ATRIBUTOS);
private int memoriaAtributo;

private String archivoVertex;
private String archivoFragment;
private int programHandler;

private static int POSITION_BUFFER_SIZE = 9;
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

public  ManejoShaders(String archivoVertex, String archivoFragment){
	this.archivoVertex = archivoVertex;
	this.archivoFragment = archivoFragment;
	
}

public int getProgramHandler(){
	return this.programHandler;		
}

public void bindBuffer(GLAutoDrawable gLDrawable){
	
	GL2 gl_aux = (GL2)gLDrawable.getGL().getGL();	// para usar glShadeModel
	GL4 gl_shader = (GL4)gLDrawable.getGL().getGL();
	gl_shader.glClearColor (0.02f, 0.02f, 0.04f, 0.0f);
  	gl_aux.glShadeModel (GL2.GL_SMOOTH);
  	gl_shader.glEnable(GL2.GL_DEPTH_TEST);
  	
  	/*
  	Buffer buff_aux = null;
  	int vboHandles[] = new int [2];
  	//
  	//gl_shader.glGenBuffers(2, vboHandles,0);
  	//positionBufferHandle = vboHandles[0];
  	//colorBufferHandle = vboHandles[1];
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
    //gl_shader.glDeleteBuffers(0, vboHandles,2);
   */
}

public void compiladoLinkeado(GLAutoDrawable gLDrawable){
		
	GL4 gl_shader = (GL4)gLDrawable.getGL().getGL();
	//GL2ES2 gles = gLDrawable.getGL().getGL2ES2();
    int creador = gl_shader.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
    int f = gl_shader.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
    BufferedReader brv = null;
	try {
		File archivo = new File (this.archivoVertex);
		FileReader fr = new FileReader (archivo);
		brv = new BufferedReader(fr);
	} catch (FileNotFoundException e) {
		System.out.println("No se pudo abrir archivo de Shaders");
		e.printStackTrace();
	}
    String vsrc = "";
    String line = "";
    try {
		while ((line=brv.readLine()) != null) {
		  vsrc += line + "\n";
		}
	} catch (IOException e) {
		System.out.println("Problemas al leer archivo de Shaders");
		e.printStackTrace();
	}
   
    String [] vectorVsrc = new String [1];
    vectorVsrc[0] = vsrc;
    
    gl_shader.glShaderSource(creador, 1, vectorVsrc, (int[])null, 0);	
    gl_shader.glCompileShader(creador);
    
    BufferedReader brf = null;
	try {
		brf = new BufferedReader(new FileReader(this.archivoFragment));
	} catch (FileNotFoundException e1) {
		System.out.println("No se pudo abrir archivo de Fragment");
		e1.printStackTrace();
	}
    String fsrc = "";
    line ="";
    try {
		while ((line=brf.readLine()) != null) {
		  fsrc += line + "\n";
		}
	} catch (IOException e) {
		System.out.println("Problemas al leer archivo de Fragment");
		e.printStackTrace();
	}
    String [] vectorFsrc = new String [1];
    vectorFsrc[0] = fsrc;
    gl_shader.glShaderSource(f, 1, vectorFsrc, (int[])null,0);
    gl_shader.glCompileShader(f);
    
    int shaderprogram = gl_shader.glCreateProgram();
    gl_shader.glAttachShader(shaderprogram, creador);
    gl_shader.glAttachShader(shaderprogram, f);
    gl_shader.glLinkProgram(shaderprogram);
    gl_shader.glValidateProgram(shaderprogram);

    gl_shader.glUseProgram(shaderprogram);
    this.programHandler = shaderprogram;  
    this.iniciarMatrices(gLDrawable);
    this.iniciarAtributos(gLDrawable);
        
}

public void setModelViewMatrix(GLAutoDrawable gLDrawable, float arreglo[]){
	GL2 gl = gLDrawable.getGL().getGL2();
	this.modelViewBuffer.clear();
	this.modelViewBuffer.put(arreglo);
	gl.glUniformMatrix2fv(this.memoriaModelView, 1, true, this.modelViewBuffer);
}

public float[] getModelViewMatrix(){
	return this.gl_ModelViewMatrix;
	
}

public void setProjectionMatrix(GLAutoDrawable gLDrawable, float[] arreglo){
	GL2 gl = gLDrawable.getGL().getGL2();
	this.projectionBuffer.clear();
	this.projectionBuffer.put(arreglo);
	gl.glUniformMatrix2fv(this.memoriaProyeccion, 1, true, this.projectionBuffer);
}

public float[] getProjectionMatrix(){
	return this.gl_ProjectionMatrix;
}

private void iniciarMatrices(GLAutoDrawable gLDrawable){
	GL2 gl = gLDrawable.getGL().getGL2();
	this.modelViewBuffer.clear();
	this.projectionBuffer.clear();
	// Armo matriz de ModelView
	this.modelViewBuffer.put(this.gl_ModelViewMatrix);
	int location = gl.glGetUniformLocation(this.programHandler,NOMBRE_MODELVIEW_MATRIX);
	this.memoriaModelView = location;
	gl.glUniformMatrix2fv(location, 1, true, this.modelViewBuffer);
	// Armo matriz de Proyeccion
	this.projectionBuffer.put(this.gl_ProjectionMatrix);
	int location2 = gl.glGetUniformLocation(this.programHandler,NOMBRE_PROJECTION_MATRIX);
	this.memoriaProyeccion = location2;
	gl.glUniformMatrix2fv(location2, 1, true, this.projectionBuffer);
}

private void iniciarAtributos(GLAutoDrawable gLDrawable){
	GL2 gl = gLDrawable.getGL().getGL2();
	this.atribBuffer.clear();
	this.atribBuffer.put(1);this.atribBuffer.put(1);this.atribBuffer.put(1);this.atribBuffer.put(1);
	int location = gl.glGetAttribLocation(this.programHandler,this.NOMBRE_ATRIBUTO);
	this.memoriaAtributo = location;
	gl.glVertexAttrib1fv(location, this.atribBuffer);
		
}

public void setPosVertex(GLAutoDrawable gLDrawable, float x,float y,float z){
	GL2 gl = gLDrawable.getGL().getGL2();
	this.atribBuffer.clear();
	this.atribBuffer.put(x);this.atribBuffer.put(y);this.atribBuffer.put(z);this.atribBuffer.put(1);
	gl.glVertexAttrib1fv(this.memoriaAtributo, this.atribBuffer);
}



public static void main(String[] args) {

	}

}