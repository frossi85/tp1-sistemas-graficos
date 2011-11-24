#version 110

attribute float fase;
attribute float amplitud;
attribute float longOnda;
uniform float time;

varying vec3 normal, lightDir, eyeVec;
//varying float intensity;


uniform bool esTextura2D;

//Variables para cube Map
varying vec3 vTexCoord;
uniform bool esCubeMap;

uniform bool esMaterialBrillante;

void main(void)
{
	//gl_TexCoord[0] = gl_MultiTexCoord0;
	vec4 v2 = vec4(gl_Vertex);
	v2.z =  gl_Vertex.z + amplitud*sin(longOnda*gl_Vertex.x + time*0.01 + fase );
	v2.x =  gl_Vertex.x + amplitud*sin(longOnda*gl_Vertex.y + time*0.01 +fase );
	v2.y = gl_Vertex.y + amplitud*sin(longOnda*gl_Vertex.z + time*0.01 + fase );
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