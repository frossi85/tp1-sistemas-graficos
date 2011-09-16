package pruebas.camara;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.awt.GLCanvas;

public class UserEventMediator implements KeyListener, MouseListener, MouseMotionListener 
{
	private GLCanvas canvas;
	
  public UserEventMediator(//JOGLbase base, 
    //InputDevice inputDevice,
    //View view, 
    //Light light, 
    GLCanvas canvas ) 
    {
//      this.base  = base;
//      this.inputDevice  = inputDevice;
//      this.view   = view;
//      this.light  = light;
      this.canvas = canvas;
    }

  	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
//		rotacionCamara += e.getX() - posicionAnteriorMouse.getX();
//		
//		posicionAnteriorMouse.setLocation(e.getX(), e.getY());
//		canvas.display();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent key) {	
		//convertir todas las letras a mayusculas o minusculas
		switch (key.getKeyChar()) {
	    	case KeyEvent.VK_ESCAPE:
	    		System.exit(0);
	    		break;
	    	case 'd':    
	    		//if(keys['w'])                         // Move forwards
			      // {
	    		//cameraZPosition -= 0.1;
			    //cameraLZPosition -= 0.1;
			 
			    //camera.moveForward(0.1);
			    //pacman.moveForward(0.1);
			    //camera.look(10);
	    		
	    		//rotacionCamara += 10;
	    		canvas.display();
			    //   }
	    		break; 
  		
	    	case 'r':
//	    		R Reiniciar la animación de crecimiento
	    		break;
	    	case 'p':
//	    		P Pausar/reanudar animación
	    		break;
	    	case 'q':
//	    		Q incrementar velocidad de crecimiento
	    		break;
	    	case 'a':
//	    		A decrementar velocidad de crecimiento
	    		break;

	      default:
	        break;
	    }	
	    
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	  // Redisplay must be called if no animator is used.
	  // Hence when the display is not updated regularly,
	  // but an update should be triggered on some event.
	  private void redisplay()
	  {
	      canvas.display();
	  }
}