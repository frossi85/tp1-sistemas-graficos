package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class Doblar {

		private float angulo;
		private int memAngulo = 1;
		public static String NOMBRE_ANGULO = "angulo"; 
		
		public Doblar(float angulo){
			this.angulo = angulo;
		}
		
		public float getAngulo(){
			return this.angulo;
		}
		
		public int getMemAngulo(){
			return this.memAngulo;
		}
		
		public void bind(GLAutoDrawable gLDrawable,int shaderprogram){
			
			GL2 gl_shader = gLDrawable.getGL().getGL2();
		 	gl_shader.glBindAttribLocation(shaderprogram,this.memAngulo,NOMBRE_ANGULO);	 		   
		}
}
