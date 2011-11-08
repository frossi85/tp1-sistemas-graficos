#version 110
#define PI    3.14159265

uniform float angulo;
uniform float distancia;
uniform float alturaMax;
uniform float time;


float getRadio(float x, float y,float z){
	return(sqrt(pow(x,2.0)  + pow(y,2.0) + pow(z,2.0)));
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
	
	float ang = gl_Vertex.z*(angulo*time)/alturaMax;
	
	float radio = distancia - gl_Vertex.x;
	
	resultado.x = distancia - radio*cos(ang);
	
	
	resultado.z = radio*sin(ang);	

		
	gl_Position = gl_ModelViewProjectionMatrix * resultado;
	
	
} 
