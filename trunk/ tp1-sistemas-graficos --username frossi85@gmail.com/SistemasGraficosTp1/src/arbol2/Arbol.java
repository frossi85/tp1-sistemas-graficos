package arbol2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;

import javax.media.opengl.GL2;

public class Arbol {
	private int niveles;
	private float edad;
	private float escala;
	public float anguloFiMaximo = 35;//45;
	public float anguloFiMinimo = 20;
	public float anguloThetaMaximo = 180;
	public float anguloThetaMinimo = 50;
	private ArrayList<Float> angulosFiPorNivel = new ArrayList<Float>();
	private ArrayList<Float> angulosThetaPorNivel = new ArrayList<Float>();
	private Iterator iteradorAngulosFi;
	private Iterator iteradorAngulosTheta;
	
	public float ditanciaEnRamaMinima = 0.4f;
	public float ditanciaEnRamaMaxima = 0.7f;
	private ArrayList<Float> distanciasPorNivel = new ArrayList<Float>();
	private Iterator iteradorDistancias;
	
	private float escalaPorEdad;
	
	public Arbol(float edad)
	{
		this.niveles = (int) edad;
		this.edad = edad;
		this.escala = this.edad - this.niveles; //Ver como la uso por q si multiplico se achica y puede quedar cero
		
		int cantParametrosACalcular = (int) (Math.pow(2, niveles+1));
		float signo = -1;
		
	 	for(int i = 0; i< cantParametrosACalcular; i++)
	 	{
	 		signo *= -1;
	 		angulosFiPorNivel.add(signo*getAleatorio(anguloFiMinimo, anguloFiMaximo));
	 		angulosThetaPorNivel.add(getAleatorio(anguloThetaMinimo ,anguloThetaMaximo));
	 		distanciasPorNivel.add(getAleatorio(ditanciaEnRamaMinima ,ditanciaEnRamaMaxima));
	 	}
	}
	
	public void crecer(float velocidadCrecimiento)
	{
		this.escala += velocidadCrecimiento;
		
		if(this.escala >= 1){
			//System.out.println("nuevo nivel : " + this.niveles);
			this.niveles += (int) this.escala;
			this.edad += (int) this.escala;
			this.escala = this.escala - (int) this.escala;
			  
			//Ver como optmizar el calculo para no calcular potencias tan grandes
			int cantParametrosACalcular = (int) (Math.pow(2, (int) this.niveles+1));// - angulosFiPorNivel.size() + 1;
			
			float signo = -1;
			
		 	for(int i = 0; i< cantParametrosACalcular; i++)
		 	{
		 		signo *= -1;
		 		angulosFiPorNivel.add(signo*getAleatorio(anguloFiMinimo, anguloFiMaximo));
		 		angulosThetaPorNivel.add(getAleatorio(anguloThetaMinimo ,anguloThetaMaximo));
		 		distanciasPorNivel.add(getAleatorio(ditanciaEnRamaMinima ,ditanciaEnRamaMaxima));
		 	}
		}
	}
	
	public void dibujar(GL2 gl)
	{	
	   //	gl.glTranslatef(-1,1,-1);
	   	
	   	iteradorAngulosFi = angulosFiPorNivel.iterator();
	   	iteradorAngulosTheta = angulosThetaPorNivel.listIterator();
	   	iteradorDistancias = distanciasPorNivel.listIterator();
	   	
	   	escalaPorEdad = (float) Math.log10(this.edad);
	   	
	   	gl.glScalef(escalaPorEdad,escalaPorEdad,escalaPorEdad);
	   			
	   	dibujarRecursivo(gl, true, new Rama(3f, 0.25f, 20f), this.niveles);	   		
	}
	
	private void dibujarRecursivo(GL2 gl, boolean tronco, Rama ramaPrincipal,  int nivel)
	{
		if(nivel > 0)
		{
			float anguloFi = 0;
		    float anguloTheta = 0;
		    float distancia1 = 0;
		    float distancia2 = 0;
		    float escala = 0.8f;
		    
		   
		    	anguloFi = (Float) iteradorAngulosFi.next();
		    	anguloTheta = (Float) iteradorAngulosTheta.next();
		    	distancia1 = (Float) iteradorDistancias.next();
				distancia2 = (Float) iteradorDistancias.next();
		    
		    Rama r1;
			
			r1 = ramaPrincipal.getRamaEscalada(0.8f);
			r1.dibujar(gl);
			
			gl.glPushMatrix();
				gl.glTranslatef(0,0, (Float) iteradorDistancias.next() * ramaPrincipal.largo);
			    gl.glRotatef((Float) iteradorAngulosFi.next(),0.5f,1f,0.0f);
			    gl.glRotatef((Float) iteradorAngulosTheta.next(), 0f, 0f, 1f);
				//gl.glScalef(escala,escala,escala);			    
				dibujarRecursivo(gl, false, r1, nivel - 1);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(0,0, (Float) iteradorDistancias.next() * ramaPrincipal.largo);
			    gl.glRotatef(-(Float) iteradorAngulosFi.next(),0.5f,1f,0.0f);
			    gl.glRotatef((Float) iteradorAngulosTheta.next(), 0f, 0f, 1f);
			    //gl.glScalef(escala,escala,escala);
				dibujarRecursivo(gl, false, r1, nivel - 1);				
			gl.glPopMatrix();
		}
	
	if(nivel==	0)
	{
		gl.glPushMatrix();
		//gl.glTranslatef(0,0,r1.largo);
		new Hoja().dibujar(gl); //aca no va seguro q va despues de la traslacion de la rama
		gl.glPopMatrix();
	}
	
	}
	
	private float getAleatorio(float min, float max)
	{
		//Se inicializa con System.currentTimeMillis()
		return (new Random().nextFloat() * (max - min) ) + min;
	}
}

