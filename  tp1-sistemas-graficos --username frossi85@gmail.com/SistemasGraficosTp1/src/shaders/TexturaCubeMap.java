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
	
    private static int imageSize = 4;
    private static ByteBuffer imageBuf1 //
    = Buffers.newDirectByteBuffer(imageSize * imageSize * 4);
    private static ByteBuffer imageBuf2 //
    = Buffers.newDirectByteBuffer(imageSize * imageSize * 4);
    private static ByteBuffer imageBuf3 //
    = Buffers.newDirectByteBuffer(imageSize * imageSize * 4);
    private static ByteBuffer imageBuf4 //
    = Buffers.newDirectByteBuffer(imageSize * imageSize * 4);
    private static ByteBuffer imageBuf5 //
    = Buffers.newDirectByteBuffer(imageSize * imageSize * 4);
    private static ByteBuffer imageBuf6 //
    = Buffers.newDirectByteBuffer(imageSize * imageSize * 4);
    float diffuse[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
	
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

		
		File file =  new File(url);
	  		
		TextureData data = null;
		try {
			data = TextureIO.newTextureData(gl.getGLProfile(), file, false, TextureIO.JPG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	    //return data;
	    
	    return texture;
	}
	
	private void makeImages()
    {
      int c = 0;
      for (int i = 0; i < imageSize; i++)
      {
        for (int j = 0; j < imageSize; j++)
        {
          // c = ((((i&0x1)==0)^((j&0x1))==0))*255;
          c = (byte) ((((byte) ((i & 0x1) == 0 ? 0x00 : 0xff)//
          ^ (byte) ((j & 0x1) == 0 ? 0x00 : 0xff))));
          imageBuf1.put((byte) c);
          imageBuf1.put((byte) c);
          imageBuf1.put((byte) c);
          imageBuf1.put((byte) 255);

          imageBuf2.put((byte) c);
          imageBuf2.put((byte) c);
          imageBuf2.put((byte) 0);
          imageBuf2.put((byte) 255);

          imageBuf3.put((byte) c);
          imageBuf3.put((byte) 0);
          imageBuf3.put((byte) c);
          imageBuf3.put((byte) 255);

          imageBuf4.put((byte) 0);
          imageBuf4.put((byte) c);
          imageBuf4.put((byte) c);
          imageBuf4.put((byte) 255);

          imageBuf5.put((byte) 255);
          imageBuf5.put((byte) c);
          imageBuf5.put((byte) c);
          imageBuf5.put((byte) 255);

          imageBuf6.put((byte) c);
          imageBuf6.put((byte) c);
          imageBuf6.put((byte) 255);
          imageBuf6.put((byte) 255);
        }
      }
      imageBuf1.rewind();
      imageBuf2.rewind();
      imageBuf3.rewind();
      imageBuf4.rewind();
      imageBuf5.rewind();
      imageBuf6.rewind();
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
	
	public void habilitar2()
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
	
	public void habilitar() {
		
		gl.glActiveTexture(GL2.GL_TEXTURE0);
		int texID = genTexture(gl);
		gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texID);

  		
//	    gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
//	    
//	    gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
//	    gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
//	    gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_R, GL2.GL_REPEAT);
//	    gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
//	    gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);

//
//	    gl.glTexImage2D(GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL2.GL_RGBA,
//    		tamanioLadoCubo, tamanioLadoCubo, 0, GL.GL_RGBA, GL2.GL_UNSIGNED_BYTE, xPositivo.getPixels());
//	    gl.glTexImage2D(GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL2.GL_RGBA,
//    		tamanioLadoCubo, tamanioLadoCubo, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, xNegativo.getPixels());
//	    gl.glTexImage2D(GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL2.GL_RGBA,
//    		tamanioLadoCubo, tamanioLadoCubo, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, yPositivo.getPixels());
//	    gl.glTexImage2D(GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL2.GL_RGBA,
//    		tamanioLadoCubo, tamanioLadoCubo, 0, GL.GL_RGBA, GL2.GL_UNSIGNED_BYTE, yNegativo.getPixels());
//	    gl.glTexImage2D(GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL2.GL_RGBA,
//    		tamanioLadoCubo, tamanioLadoCubo, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, zPositivo.getPixels());
//	    gl.glTexImage2D(GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL2.GL_RGBA,
//    		tamanioLadoCubo, tamanioLadoCubo, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, zNegativo.getPixels());
//    
	      
	    
//	    gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_NORMAL_MAP);
//	    gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_NORMAL_MAP);
//	    gl.glTexGeni(GL2.GL_R, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_NORMAL_MAP);
//	    gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_REFLECTION_MAP);
//	    gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_REFLECTION_MAP);
//	    gl.glTexGeni(GL2.GL_R, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_REFLECTION_MAP);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_S);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_T);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_R);

	    gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

	    gl.glEnable(GL.GL_TEXTURE_CUBE_MAP);
	    
	  
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
