#version 110
#define PI    3.14159265
//probado con: Esferizacion(2,0.0f,0.0f,0.0f,0.1f);/cubo cub = new cubo(2,3);

uniform float radio;
uniform float factor;
uniform vec3 centro;
uniform float time;

varying vec3 normal, lightDir, eyeVec;
//varying float intensity;


uniform bool esTextura2D;

//Variables para cube Map
varying vec3 vTexCoord;
uniform bool esCubeMap;

uniform bool esMaterialBrillante;


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
	
	
	
	float radioActual = getRadio(gl_Vertex.x,gl_Vertex.y,gl_Vertex.z); //dado las coordenadas cartesianas, calculo el radio
	resultado.x = resultado.x/radioActual; //normalizo
	resultado.y = resultado.y/radioActual;
	resultado.z = resultado.z/radioActual;
	
	float radioFinal = getRadioFinal(radioActual,radio,factor); 
	
	vec4 normaux = resultado;

	resultado.x = resultado.x*(radioActual+(radioFinal-radioActual)*time); 
	resultado.y =resultado.y*(radioActual+(radioFinal-radioActual)*time);
	resultado.z = resultado.z*(radioActual+(radioFinal-radioActual)*time);
	
	gl_Position = gl_ModelViewProjectionMatrix * resultado; //la normal tendria la misma direccion que resultado.

	
	//Entonces es brillante o semimate por lo q el calculo para ambas es el mismo
	normal = normalize(gl_NormalMatrix * gl_Normal);
	normal.x = normaux.x;
	normal.y = normaux.y;
	normal.z = normaux.z;
	vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);
	lightDir = normalize(vec3(gl_LightSource[0].position) - vVertex);
	eyeVec = -vVertex;
	
	if(esCubeMap)
	{		
		//CALCULOS PARA EL CUBE MAP
		// Normal in Eye Space
	    vec3 vEyeNormal = gl_NormalMatrix * gl_Normal;
	    // Vertex position in Eye Space
	    vec4 vVert4 = gl_ModelViewMatrix * gl_Vertex;
	    vec3 vEyeVertex = normalize(vVert4.xyz / vVert4.w);
	    vec4 vCoords = vec4(reflect(vEyeVertex, vEyeNormal), 0.0);
	    // Rotate by flipped camera
	    vCoords = gl_ModelViewMatrixInverse * vCoords;
	    vTexCoord.xyz = normalize(vCoords.xyz);
	    // Don't forget to transform the geometry!  
	}
	else
	{
		if(esTextura2D)
		{
			gl_TexCoord[0] = gl_MultiTexCoord0;
		}
	}
} 
