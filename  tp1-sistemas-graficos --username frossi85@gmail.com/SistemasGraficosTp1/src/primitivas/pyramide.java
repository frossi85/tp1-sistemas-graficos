package primitivas;

import javax.media.opengl.GL2;

public class pyramide {

	public pyramide(){
	
	}
	
	public void dibujar(GL2 gl){
		
		/// cumbre en y
		gl.glBegin(GL2.GL_TRIANGLES);
    	gl.glNormal3f(0, 0, 1);
    	gl.glVertex3f(-1,0,1);
    	gl.glVertex3f(0, 2, 0);
    	gl.glVertex3f(1, 0, 1);
    	gl.glEnd();
    	
    	gl.glBegin(GL2.GL_TRIANGLES);
    	gl.glNormal3f(1, 0, 0);
    	gl.glVertex3f(1, 0, 1);
    	gl.glVertex3f(0, 2, 0);
    	gl.glVertex3f(1, 0, -1);
    	gl.glEnd();
    	
    	gl.glBegin(GL2.GL_TRIANGLES);
    	gl.glNormal3f(0, 0, -1);
    	gl.glVertex3f(1, 0, -1);
    	gl.glVertex3f(0, 2, 0);
    	gl.glVertex3f(-1, 0, -1);
    	gl.glEnd();
    	
    	gl.glBegin(GL2.GL_TRIANGLES);
    	gl.glNormal3f(-1, 0, 0);
    	gl.glVertex3f(-1, 0, -1);
    	gl.glVertex3f(0, 2, 0);
    	gl.glVertex3f(-1, 0, 1);
    	gl.glEnd();
    	
    	//cumbre en z
    	/*
    	gl.glBegin(GL2.GL_TRIANGLES);
    	gl.glNormal3f(0, 1, 0);
    	gl.glVertex3f(-1, 1, 0);
    	gl.glVertex3f(0, 0, 2);
    	gl.glVertex3f(1, 1, 0);
    	gl.glEnd();
    	
    	gl.glBegin(GL2.GL_TRIANGLES);
    	gl.glNormal3f(1, 0, 0);
    	gl.glVertex3f(1, 1, 0);
    	gl.glVertex3f(0, 0, 2);
    	gl.glVertex3f(1, -1, 0);
    	gl.glEnd();
    	
    	gl.glBegin(GL2.GL_TRIANGLES);
    	gl.glNormal3f(0, -1, 0);
    	gl.glVertex3f(1, -1, 0);
    	gl.glVertex3f(0, 0, 2);
    	gl.glVertex3f(-1, -1, 0);
    	gl.glEnd();
    	
    	gl.glBegin(GL2.GL_TRIANGLES);
    	gl.glNormal3f(-1, 0, 0);
    	gl.glVertex3f(-1, -1, 0);
    	gl.glVertex3f(0, 0, 2);
    	gl.glVertex3f(-1, 1, 0);
    	gl.glEnd();
    	*/
	}
}
