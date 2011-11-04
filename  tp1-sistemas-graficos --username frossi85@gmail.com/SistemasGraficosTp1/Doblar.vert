#version 110
#define PI    3.14159265

attribute float angulo;
attribute float distancia;
attribute float alturaMax;
attribute float diametro;



float getRadio(float x, float y,float z){
	return(sqrt(pow(x,2.0)  + pow(y,2.0) + pow(z,2.0)));
}

float getTetha(float x, float y){
	if((x > 0.0) && (y >= 0.0))
		return (atan(y,x));
		
	else if((x > 0.0) && (y < 0.0))
		return(atan(y,x) + 2.0*PI);
		

	else if(x < 0.0)
		return(atan(y,x) + PI);
		
	else if((x == 0.0) && (y > 0.0))
		return(PI/2.0);
	else if((x == 0.0) && (y < 0.0))
		return (3.0*PI/2.0);
	
}

float getX(float radio, float tetha){
	return(radio*cos(tetha));
}

float getY(float radio, float tetha){
	return(radio*sin(tetha));
}

float getAngulo(float z,float anguloMax,float alturaFigura){
	return(z*anguloMax/alturaFigura);
}

void main(void)
{
	vec4 resultado = vec4(gl_Vertex);
	/*
	vec4 original = vec4(gl_Vertex);
	float radioOrig = getRadio(distancia-original.x,original.y,original.z);
	float radioInterno = distancia - diametro/2.0;
	float radioExterno = distancia + diametro/2.0;
		

	resultado = vec4(distancia-gl_Vertex.x,gl_Vertex.y,gl_Vertex.z-0.0,1.0);
	float radioActual = getRadio(resultado.x,resultado.y,resultado.z);
	resultado.x = resultado.x/radioActual;
	resultado.y = resultado.y/radioActual;
	resultado.z = resultado.z/radioActual;

	if((radioOrig  >= radioInterno) && (radioOrig < radioExterno)){
		resultado.x = resultado.x*(radioInterno+gl_Vertex.x);
		resultado.y = resultado.y*(radioInterno+gl_Vertex.x);
		resultado.z = resultado.z*(radioInterno+gl_Vertex.x);
	
	}
	else if (radioOrig >= radioExterno){
		resultado.x = resultado.x*(radioExterno+gl_Vertex.x);
		resultado.y = resultado.y*(radioInterno+gl_Vertex.x);
		resultado.z = resultado.z*(radioInterno+gl_Vertex.x);

	}
	else{
		resultado.x = 0.0;
		resultado.y = 0.0;
		resultado.z = 0.0;
	}
	resultado.x = resultado.x+distancia;
	resultado.y = resultado.y+0.0;
	resultado.z = resultado.z+0.0;
	resultado.w = 1.0;
	*/
	
	float ang = getAngulo(gl_Vertex.z,angulo,alturaMax);
	resultado.x = getX(distancia-gl_Vertex.x,ang);
	resultado.z = getY(distancia-gl_Vertex.x,ang);
	
	
	
	
	gl_Position = gl_ModelViewProjectionMatrix * resultado;
	
	
} 
