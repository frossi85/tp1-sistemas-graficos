package shaders;

import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import common.TextureReader;

public class FragmentGeneral extends FragmentShader {
	private String fileName = "fragmentGenerico.frag";
	private GL2 gl;
	//private ManejoShaders shader;
	private ManejoShaders2 shader;
	private GLU glu;
	int texturaLadrillo;
	TexturaCubeMap texturaCubica;
	
//	public FragmentGeneral(GL2 gl, GLU glu, ManejoShaders shader)
//	{
//		this.gl = gl;
//		this.shader = shader;
//		this.glu = glu;
//		
//		//Inicializacion de cube map
//	  	texturaCubica = new TexturaCubeMap(gl, glu, 512);
//	  	
//	  	texturaCubica.cargarXPositivo("posx.jpeg");
//	  	texturaCubica.cargarYPositivo("posy.jpeg");
//	  	texturaCubica.cargarZPositivo("posz.jpeg");
//	  	texturaCubica.cargarXNegativo("negx.jpeg");
//	  	texturaCubica.cargarYNegativo("negy.jpeg");
//	  	texturaCubica.cargarZNegativo("negz.jpeg");
//	}
	
	public FragmentGeneral(GL2 gl, GLU glu, ManejoShaders2 shader)
	{
		this.gl = gl;
		this.shader = shader;
		this.glu = glu;
		
		//Inicializacion de cube map
	  	texturaCubica = new TexturaCubeMap(gl, glu, 512);
	  	
	  	texturaCubica.cargarXPositivo("posx.jpeg");
	  	texturaCubica.cargarYPositivo("posy.jpeg");
	  	texturaCubica.cargarZPositivo("posz.jpeg");
	  	texturaCubica.cargarXNegativo("negx.jpeg");
	  	texturaCubica.cargarYNegativo("negy.jpeg");
	  	texturaCubica.cargarZNegativo("negz.jpeg");
	}
	
	@Override
	public String getFileName() {
		return this.fileName;
	}
	
	public void setEfectoSemiMate()
	{
		int uniloc = -1;
	    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esTextura2D");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 0); //0=false, 1=true
	    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esCubeMap");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 0); //0=false, 1=true
	    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esMaterialBrillante");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 0); //0=false, 1=true 
	}
	
	public void setEfectoBrillante()
	{
		int uniloc = -1;
	    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esTextura2D");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 0); //0=false, 1=truei
	    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esCubeMap");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 0); //0=false, 1=true
	    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esMaterialBrillante");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 1); //0=false, 1=true 		
	}
	
	public void setEfectoTextura2D()
	{
		///TEXTURA SIMPLE
	  	gl.glEnable(GL.GL_TEXTURE_2D);
	   	//gl.glActiveTexture(GL2.GL_TEXTURE0);
        texturaLadrillo = genTexture(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texturaLadrillo);
        TextureReader.Texture texture = null;
        try {
            texture = TextureReader.readTexture("ladrillos.jpeg");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        makeRGBTexture(gl, glu, texture, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        
		
		int uniloc = -1;
		   
    	//LOS 2 UNIFORMS PARA LA TEXTURA 2D
    	uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "tex");
    	if( uniloc >= 0 )
    		gl.glUniform1i(uniloc, texturaLadrillo);
    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esTextura2D");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 1); //0=false, 1=true
	    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esCubeMap");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 0); //0=false, 1=true
	    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esMaterialBrillante");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 0); //0=false, 1=true
	}
	
	public void setEfectoReflejarEntorno()
	{	  	
	  	texturaCubica.habilitar();
		
		int uniloc = -1;
    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esTextura2D");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 0); //0=false, 1=true
	    
	    //SETEO LA TEXTURA DEL UNIFORM cubeMap A LA UNIDAD DE TEXTURA 0
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "cubeMap");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, texturaCubica.cubemap);
	    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esCubeMap");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 1); //0=false, 1=true
	    
	    uniloc = gl.glGetUniformLocation(shader.getProgramHandler(), "esMaterialBrillante");
	    if( uniloc >= 0 )
	    	gl.glUniform1i(uniloc, 0); //0=false, 1=true 	
	}
	
	
    private void makeRGBTexture(GL gl, GLU glu, TextureReader.Texture img, 
            int target, boolean mipmapped) {
        
        if (mipmapped) {
            glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(), 
                    img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
        } else {
            gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(), 
                    img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
        }
    }

    private int genTexture(GL gl) {
        final int[] tmp = new int[1];
        gl.glGenTextures(1, tmp, 0);
        return tmp[0];
    }
}
