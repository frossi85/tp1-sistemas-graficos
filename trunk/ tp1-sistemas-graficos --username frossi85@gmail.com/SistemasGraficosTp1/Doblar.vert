#version 110
#define PI    3.14159265

uniform float angulo;
uniform float distancia;
uniform float alturaMax;
uniform float time;

varying vec3 normal, lightDir, eyeVec;
//varying float intensity;


uniform bool esTextura2D;

//Variables para cube Map
varying vec3 vTexCoord;
uniform bool esCubeMap;

uniform bool esMaterialBrillante;


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
	
	float radio = distancia - gl_Vertex.x;//forma la normal con 2 puntos: uno es resultado 
	
	resultado.x = distancia - radio*cos(ang);// y el otro es: (distancia,gl_Vertex.y,0.0)
	
	
	resultado.z = radio*sin(ang);	

		
	gl_Position = gl_ModelViewProjectionMatrix * resultado;
	
	//Entonces es brillante o semimate por lo q el calculo para ambas es el mismo
	normal = normalize(gl_NormalMatrix * gl_Normal);
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
