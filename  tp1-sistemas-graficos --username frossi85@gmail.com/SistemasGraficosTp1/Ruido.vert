#version 110

attribute float fase;
attribute float amplitud;
attribute float longOnda;
uniform float time;

//varying vec3 normal, lightDir, eyeVec;

//varying float intensity;

varying vec3 N;
varying vec3 v;

void main(void)
{
	vec4 v2 = vec4(gl_Vertex);
	
	v2.z =  gl_Vertex.z + amplitud*sin(longOnda*gl_Vertex.x + time*0.01 + fase );
	v2.x =  gl_Vertex.x + amplitud*sin(longOnda*gl_Vertex.y + time*0.01 +fase );
	v2.y = gl_Vertex.y + amplitud*sin(longOnda*gl_Vertex.z + time*0.01 + fase );
	v2.w = 1.0;
		
	gl_Position = gl_ModelViewProjectionMatrix * v2;
	
	//////
	//vec3 ld;
		
	//intensity = dot(lightDir,gl_Normal);
	
	//v = vec3(gl_ModelViewMatrix * gl_Vertex);       
   	//N = normalize(gl_NormalMatrix * gl_Normal);
		
		
	//Cosas para el fragment shader
	//normal = gl_NormalMatrix * gl_Normal;

	//vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);

	//lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	//eyeVec = -vVertex;
} 