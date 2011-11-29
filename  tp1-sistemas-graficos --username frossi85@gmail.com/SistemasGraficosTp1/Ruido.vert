#version 110

uniform  float fase;
uniform float amplitud;
uniform float longOnda;
uniform float time;



void main(void)
{
	vec4 v2 = vec4(gl_Vertex);
	//v2.z = sin(longOnda*v2.x+ time+fase )*amplitud;
	v2.z =   gl_Vertex.z+ amplitud*sin(longOnda*gl_Vertex.x + time + fase );
	v2.x =   gl_Vertex.x + amplitud*sin(longOnda*gl_Vertex.z + time +fase );
	v2.y =   gl_Vertex.y+amplitud*sin(longOnda*gl_Vertex.y + time*0.1 + fase );
	v2.w = 1.0;
		
	gl_Position = gl_ModelViewProjectionMatrix * v2;
	
	/*vec3 ld;
		
	intensity = dot(lightDir,gl_Normal);
	
	v = vec3(gl_ModelViewMatrix * gl_Vertex);       
   	N = normalize(gl_NormalMatrix * gl_Normal);
		
		
	//Cosas para el fragment shader
	normal = gl_NormalMatrix * gl_Normal;

	vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);

	lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	eyeVec = -vVertex;*/
} 
