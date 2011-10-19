#version 110
#define PI    3.14159265
//probado con: Esferizacion(2,0.0f,0.0f,0.0f,0.1f);/cubo cub = new cubo(2,3);

attribute float radio;
attribute float factor;
attribute vec3 centro;

varying vec3 normal, lightDir, eyeVec;

float getTetha( float x,  float y, float z){
	
	if (z > 0.0) {
		float raiz = pow(x,2.0);
		raiz = raiz + pow(y,2.0);
		raiz = sqrt(raiz);
		float retorno = atan(raiz,z);		
		return retorno;	
	
	}
	
	else if (gl_Vertex.z == 0.0){
		return (PI/2.0);		
	}


	else{
		float raiz = sqrt(pow(x,2.0) + pow(y,2.0));
		return (PI + atan(raiz,z));		
	}

}

float getFi(float x, float y, float z){
	
	if((x > 0.0) && (y > 0.0)){
		return (atan(y,x));
	}

	else if((x > 0.0) && (y < 0.0)){
		return(2.0*PI + atan(y,x));
	}

	else if(x == 0.0){
		return((PI/2.0)*sign(y));
	}

	else if(x < 0.0){
		return(PI + atan(y,x));
	}

}

float getRadio(float x, float y, float z){
	return(sqrt(pow(x,2.0) + pow(y,2.0) + pow(z,2.0)));
}


float getX(float radio,float tetha, float fi){
	return(radio*sin(tetha)*cos(fi));
}

float getY(float radio, float tetha, float fi){
	return(radio*sin(tetha)*sin(fi));
}

float getZ(float radio,float tetha){
	return(radio*cos(tetha));
}


void main(void)
{
	vec4 resultado = vec4(gl_Vertex);
	if(factor == 2.0) gl_Position = gl_Vertex;
	else{
	float radioFigura = getRadio(gl_Vertex.x,gl_Vertex.y,gl_Vertex.z);
	float tethaFigura = getTetha(gl_Vertex.x,gl_Vertex.y,gl_Vertex.z);
	float fiFigura = getFi(gl_Vertex.x,gl_Vertex.y,gl_Vertex.z);

	float radioEsfera = radio;
	float tethaEsfera = tethaFigura;
	float fiEsfera = fiFigura;	
	float distancia;	
	//if(radioEsfera > radioFigura){		
	//	 distancia = mod(radioEsfera,radioFigura);
	//}
	//else 
		 distancia = mod(radioFigura,radioEsfera);
	
	float nuevoRadio;
	if(factor == 0.0)
		resultado = vec4(gl_Vertex);
	 else{
		nuevoRadio = radioEsfera + distancia*(1.0 -factor);
		//float nuevoRadio = radioEsfera - distancia*factor;				
	
		resultado.y = getY(nuevoRadio,tethaFigura,fiEsfera);

		if(gl_Vertex.x <= 0.0)
			resultado.x = -getX(nuevoRadio,tethaFigura,fiEsfera);
		else
			resultado.x = getX(nuevoRadio,tethaFigura,fiEsfera);
	
		if(gl_Vertex.z <= 0.0)
			resultado.z = -getZ(nuevoRadio,tethaFigura);
		else 
			resultado.z = getZ(nuevoRadio,tethaFigura);				
	}	
	gl_Position = gl_ModelViewProjectionMatrix * resultado;

	}
	
	//Cosas para el fragment shader
	normal = gl_NormalMatrix * gl_Normal;
	
	vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);
	
	lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	eyeVec = -vVertex;
} 
