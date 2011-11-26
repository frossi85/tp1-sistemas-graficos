package shaders;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import common.TextureReader;

//import common.TextureReader;

public class TexturaCubeMap {
	
	private GL2 gl;
	private GLU glu;
	private int tamanioLadoCubo;
	TextureReader.Texture xPositivo;
	TextureReader.Texture yPositivo;
	TextureReader.Texture zPositivo;
	TextureReader.Texture xNegativo;
	TextureReader.Texture yNegativo;
	TextureReader.Texture zNegativo;
	public int cubemap;
	
//	TextureData xPositivo;
//	TextureData yPositivo;
//	TextureData zPositivo;
//	TextureData xNegativo;
//	TextureData yNegativo;
//	TextureData zNegativo;
	
	public TexturaCubeMap(GL2 gl, GLU glu, int tamanioLadoCubo)
	{
		this.gl = gl;
		this.glu = glu;
		this.tamanioLadoCubo = tamanioLadoCubo;
	}
	
	
	public void cargarXPositivo(String url) {
		xPositivo = generarTexturaDesdeURL(url);
	}
	
	public void cargarXNegativo(String url) {
		xNegativo = generarTexturaDesdeURL(url);
	}
	
	public void cargarYPositivo(String url) {
		yPositivo = generarTexturaDesdeURL(url);
	}
	
	public void cargarYNegativo(String url) {
		yNegativo = generarTexturaDesdeURL(url);
	}
	
	public void cargarZPositivo(String url) {
		zPositivo = generarTexturaDesdeURL(url);
	}
	
	public void cargarZNegativo(String url) {
		zNegativo = generarTexturaDesdeURL(url);
	}
	
	private int genTexture(GL gl) {
        final int[] tmp = new int[1];
        gl.glGenTextures(1, tmp, 0);
        return tmp[0];
    }
	
	//private TextureData generarTexturaDesdeURL(String url){
	//private com.jogamp.opengl.util.texture.Texture generarTexturaDesdeURL(String url){
	private TextureReader.Texture generarTexturaDesdeURL(String url) {
        TextureReader.Texture texture = null;
        try {
            texture = TextureReader.readTexture(url);
        } catch (IOException e) {
            e.printStackTrace();
			throw new RuntimeException(e);
        }

		
//		File file =  new File(url);
//	  		
//		TextureData data = null;
//		try {
//			data = TextureIO.newTextureData(gl.getGLProfile(), file, false, TextureIO.JPG);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			
	    //return data;
	    
	    return texture;
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
	
	public void habilitar()
	{
	    gl.glEnable(GL2.GL_TEXTURE_CUBE_MAP);

		cubemap = genTexture(gl);
		
		//gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, cubemap);
                    
        makeRGBTexture(gl, glu, xPositivo, GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_X, false);
        makeRGBTexture(gl, glu, xNegativo, GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, false);
        makeRGBTexture(gl, glu, yPositivo, GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, false);
        makeRGBTexture(gl, glu, yNegativo, GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, false);
        makeRGBTexture(gl, glu, zPositivo, GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, false);
        makeRGBTexture(gl, glu, zNegativo, GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, false);
       
		
		 // Typical cube map settings
	    gl.glTexParameterf(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
	    gl.glTexParameterf(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
	    gl.glTexParameterf(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
	    gl.glTexParameterf(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
	    gl.glTexParameterf(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_R, GL2.GL_CLAMP_TO_EDGE);
    
	    
	    gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_NORMAL_MAP);
	    gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_NORMAL_MAP);
	    gl.glTexGeni(GL2.GL_R, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_NORMAL_MAP);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_S);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_T);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_R);

	    gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

	    gl.glEnable(GL2.GL_LIGHTING);
	    gl.glEnable(GL2.GL_LIGHT0);
	    gl.glEnable(GL2.GL_AUTO_NORMAL);
	    gl.glEnable(GL2.GL_NORMALIZE);
	}
	
	public void desabilitar()
	{
		//gl.glDisable(GL2.GL_TEXTURE_GEN_S);
		//gl.glDisable(GL2.GL_TEXTURE_GEN_T);
		//gl.glDisable(GL2.GL_TEXTURE_2D);
	}
}
