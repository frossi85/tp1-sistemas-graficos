package arbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.media.opengl.GL2;

public class Arbol {
	private int niveles;
	private float edad;
	private boolean verHojas = false;
	private float escala;
	public float anguloFiMaximo = 35;
	public float anguloFiMinimo = 20;
	public float anguloThetaMaximo = 180;
	public float anguloThetaMinimo = 50;
	private ArrayList<Float> angulosFiPorNivel = new ArrayList<Float>();
	private ArrayList<Float> angulosThetaPorNivel = new ArrayList<Float>();
	private Iterator<Float> iteradorAngulosFi;
	private Iterator<Float> iteradorAngulosTheta;
	
	public float ditanciaEnRamaMinima = 0.3f;
	public float ditanciaEnRamaMaxima = 0.7f;
	private ArrayList<Float> distanciasPorNivel = new ArrayList<Float>();
	private Iterator<Float> iteradorDistancias;
	private Hoja hojaModelo;
	private Rama ramaModelo;
	
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
			this.niveles += (int) this.escala;
			this.edad += (int) this.escala;
			this.escala = this.escala - (int) this.escala;
			
			int cantParametrosACalcular = (int) (Math.pow(2, (int) this.niveles+1));
			
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
	   	iteradorAngulosFi = angulosFiPorNivel.iterator();
	   	iteradorAngulosTheta = angulosThetaPorNivel.listIterator();
	   	iteradorDistancias = distanciasPorNivel.listIterator();
	   	
	   	escalaPorEdad = (float) Math.log10(this.edad);
	   	
	   	gl.glScalef(escalaPorEdad,escalaPorEdad,escalaPorEdad);
	   	
	   	hojaModelo = new Hoja(gl);
	   	ramaModelo = new Rama(gl, 3f, 0.25f);
		
	   	dibujarRecursivo(gl, true, ramaModelo, this.niveles, false);	   		
	}
	
	private void dibujarRecursivo(GL2 gl, boolean tronco, Rama ramaPrincipal,  int nivel, boolean esRamaIzquierda)
	{	   
	    float escala = 0.8f;
	    
		if(nivel > 0)
		{		    		   			
			ramaPrincipal.dibujar();
			
			gl.glPushMatrix();
				gl.glTranslatef(0,0, (Float) iteradorDistancias.next() * ramaPrincipal.largo);
			    gl.glRotatef((Float) iteradorAngulosFi.next(),0.5f,1f,0.0f);
			    gl.glRotatef((Float) iteradorAngulosTheta.next(), 0f, 0f, 1f);
			    gl.glScalef(escala,escala,escala);		
				dibujarRecursivo(gl, false, ramaPrincipal, nivel - 1, false);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslatef(0,0, (Float) iteradorDistancias.next() * ramaPrincipal.largo);
			    gl.glRotatef((Float) iteradorAngulosFi.next(),0.5f,1f,0.0f);
			    gl.glRotatef((Float) iteradorAngulosTheta.next(), 0f, 0f, 1f);
			    gl.glScalef(escala,escala,escala); 
				dibujarRecursivo(gl, false, ramaPrincipal, nivel - 1, true);				
			gl.glPopMatrix();
		}
	
		if(this.verHojas && nivel == 0)
		{
			float esc = (float) Math.pow(1/escala, this.niveles + 1);
			float orientacionHoja = 45;

			gl.glScalef(esc,esc,esc);
			if(esRamaIzquierda)
				orientacionHoja *= -1;
			gl.glRotatef(orientacionHoja,0.5f,1f,0.0f);
			hojaModelo.dibujar();
		}
	}
	
	private float getAleatorio(float min, float max)
	{
		return (new Random().nextFloat() * (max - min) ) + min;
	}
	
	public void verHojas(){
		this.verHojas = true;
	}
	
	public void esconderHoja(){
		this.verHojas = false;
	}
}

