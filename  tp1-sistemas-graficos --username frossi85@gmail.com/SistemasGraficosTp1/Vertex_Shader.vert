#version 110

attribute vec4 glVertex;
varying vec3 normal, lightDir, eyeVec;

void main(void)
{
	vec4 v = vec4(gl_Vertex);
	if (gl_Vertex.z > 2.0){
		v.z = sin(4.0*v.x )*0.25 + gl_Vertex.z;
		
	}
	gl_Position = gl_ModelViewProjectionMatrix * v;
	
	//Cosas para el fragment shader
	normal = gl_NormalMatrix * gl_Normal;

	vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);

	lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	eyeVec = -vVertex;
} 
