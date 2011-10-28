#version 110
#define PI    3.14159265

uniform float angulo;
float distancia;

varying vec3 normal, lightDir, eyeVec;

void main(void)
{
	distancia = 1.0;
	vec4 v2 = vec4(gl_Vertex);
	
	v2.z = (distancia - gl_Vertex.x) * sin(PI/3.0);
	v2.x = (distancia - gl_Vertex.x) * cos(PI/3.0);
	v2.y = gl_Vertex.y;
	v2.w = 1.0;
	
		
	gl_Position = gl_ModelViewProjectionMatrix * v2;
	
	//Cosas para el fragment shader
	normal = gl_NormalMatrix * gl_Normal;

	vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);

	lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	eyeVec = -vVertex;	
 }