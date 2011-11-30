#version 110

uniform  float fase;
uniform float amplitud;
uniform float longOnda;
uniform float time;

uniform bool esTextura2D;

//Variables para cube Map
varying vec3 vTexCoord;
uniform bool esCubeMap;

uniform bool esMaterialBrillante;

//Variables generales de luces
varying vec3 normal, lightDir, eyeVec;

void main(void)
{
	vec4 v2 = vec4(gl_Vertex);
	//v2.z = sin(longOnda*v2.x+ time+fase )*amplitud;
	v2.z =   gl_Vertex.z+ amplitud*sin(longOnda*gl_Vertex.x + time + fase );
	v2.x =   gl_Vertex.x + amplitud*sin(longOnda*gl_Vertex.z + time +fase );
	v2.y =   gl_Vertex.y+amplitud*sin(longOnda*gl_Vertex.y + time*0.1 + fase );
	v2.w = 1.0;
		
	gl_Position = gl_ModelViewProjectionMatrix * v2;
	
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
	}
	else
	{
		if(esTextura2D)
		{
			//gl_TexCoord[0] = gl_MultiTexCoord0;
		}
	}
	
	gl_TexCoord[0] = gl_MultiTexCoord0;
} 
