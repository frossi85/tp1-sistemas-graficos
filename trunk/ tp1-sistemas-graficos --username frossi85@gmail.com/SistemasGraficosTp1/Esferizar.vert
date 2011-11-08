#version 110
#define PI    3.14159265
//probado con: Esferizacion(2,0.0f,0.0f,0.0f,0.1f);/cubo cub = new cubo(2,3);

uniform float radio;
uniform float factor;
uniform vec3 centro;
uniform float time;

varying vec3 normal, lightDir, eyeVec;



float getRadio(float x, float y, float z){
	return(sqrt(pow(x,2.0) + pow(y,2.0) + pow(z,2.0)));
}



float getRadioFinal(float radioFigura, float radioEsfera,float fact){
	float difRadio = radioEsfera-radioFigura;
	difRadio = difRadio*fact;
	difRadio = difRadio + radioFigura;
	return(difRadio);

}

void main(void)
{
	vec4 resultado = vec4(gl_Vertex);
	
	
	
	float radioActual = getRadio(gl_Vertex.x,gl_Vertex.y,gl_Vertex.z);
	resultado.x = resultado.x/radioActual;
	resultado.y = resultado.y/radioActual;
	resultado.z = resultado.z/radioActual;
	
	float radioFinal = getRadioFinal(radioActual,radio,factor);
	
	
	resultado.x = resultado.x*(radioActual+(radioFinal-radioActual)*time);
	resultado.y =resultado.y*(radioActual+(radioFinal-radioActual)*time);
	resultado.z = resultado.z*(radioActual+(radioFinal-radioActual)*time);
	
	gl_Position = gl_ModelViewProjectionMatrix * resultado;

	
	//Cosas para el fragment shader
	normal = gl_NormalMatrix * gl_Normal;
	
	vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);
	
	lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	eyeVec = -vVertex;
} 
