package arbol2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.media.opengl.GL2;

public class Arbol {
	private int niveles;
	private float edad;
	private float escala;
	public float anguloFiMaximo = 35;//45;
	public float anguloFiMinimo = 20;
	public float anguloThetaMaximo = 180;
	public float anguloThetaMinimo = 50 ;
	private ArrayList<Float> angulosFiPorNivel = new ArrayList<Float>();
	private ArrayList<Float> angulosThetaPorNivel = new ArrayList<Float>();
	private Iterator iteradorAngulosFi;
	private Iterator iteradorAngulosTheta;

	public Arbol(float edad)
	{
		this.niveles = (int) edad;
		this.edad = edad;
		this.escala = this.edad - this.niveles; //Ver como la uso por q si multiplico se achica y puede quedar cero
		
		int cantParametrosACalcular = (int) (Math.pow(2, niveles) - 1);
		float signo = -1;
		
	 	for(int i = 0; i< cantParametrosACalcular; i++)
	 	{
	 		signo *= -1;
	 		angulosFiPorNivel.add(signo*getAleatorio(anguloFiMinimo, anguloFiMaximo));
	 		angulosThetaPorNivel.add(getAleatorio(anguloThetaMinimo ,anguloThetaMaximo));
	 	}
	}
	
	public void crecer(float pasoDeTiempo)
	{
		this.niveles += (int) pasoDeTiempo;
		this.edad += pasoDeTiempo;
		
		//escala bien como calculo po r q si pasoDeTiempo 1 entonces la escala queda en uno y no deberia escalarse ya q crece un nivel
		this.escala = this.edad - this.niveles; //Ver como la uso por q si multiplico se achica y puede quedar cero
		  
		//Ver como optmizar el calculo para no calcular potencias tan grandes
		int cantParametrosACalcular = (int) (Math.pow(2, (int) this.niveles)) - angulosFiPorNivel.size() + 1;
		
		float signo = -1;
		
	 	for(int i = 0; i< cantParametrosACalcular; i++)
	 	{
	 		signo *= -1;
	 		angulosFiPorNivel.add(signo*getAleatorio(anguloFiMinimo, anguloFiMaximo));
	 		angulosThetaPorNivel.add(getAleatorio(anguloThetaMinimo ,anguloThetaMaximo));
	 	}
	}
	
	public void dibujar(GL2 gl)
	{
	   	gl.glTranslatef(0,0,-1);
	   	gl.glRotatef(-angulosFiPorNivel.get(0),0.5f,1.0f,0.0f); //Fijarme si la rotacion no es con Fi
	   	
	   	iteradorAngulosFi = angulosFiPorNivel.iterator();
	   	iteradorAngulosTheta = angulosThetaPorNivel.iterator();
	   	
	   	//El atributo escala solo se usa por primera vbez para pasar el tamaño del tronco principal
	   	
	   	//PARA EL ESCALADO DEL CRECIMIENTO APLICAR aca un escalado de OPEN GL de todo el arbol
	   	
		dibujarRecursivo(gl, true, new Rama(3f, 0.25f, 20f), niveles);
	}
	
	private void dibujarRecursivo(GL2 gl, boolean tronco, Rama ramaPrincipal,  int nivel)
	{
		if(nivel > 0)
		{
			float anguloFi = 0;
		    float anguloTheta = 0;
		    
		    //if(iteradorAngulosFi.hasNext())
		    	anguloFi = (Float) iteradorAngulosFi.next();
		    //if(iteradorAngulosFi.hasNext())
		    	anguloTheta= (Float) iteradorAngulosTheta.next() ;
			
		    Rama r1;
			
			if(tronco)
			{
				r1 = ramaPrincipal;
				gl.glPushMatrix();
			}
			else
			{
				r1= ramaPrincipal.getRamaEscalada(0.8f);
				gl.glPushMatrix();
			   	gl.glTranslatef(0,0,ramaPrincipal.largo / 1.5f);
			}
		
			r1.dibujar(gl, anguloFi, anguloTheta, ramaPrincipal.largo / 2);
		
			dibujarRecursivo(gl, false, r1, nivel - 1);
			dibujarRecursivo(gl, false, r1, nivel - 1);
			
			if(nivel==1)
			{
				gl.glPushMatrix();
					gl.glTranslatef(0,0,r1.largo);
					new Hoja().dibujar(gl); //aca no va seguro q va despues de la traslacion de la rama
				gl.glPopMatrix();
			}
				
			gl.glPopMatrix();
		}
	}
	
	private float getAleatorio(float min, float max)
	{
		//Se inicializa con System.currentTimeMillis()
		return (new Random().nextFloat() * (max - min) ) + min;
	}
}

