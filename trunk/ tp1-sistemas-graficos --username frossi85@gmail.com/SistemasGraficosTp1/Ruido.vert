#version 110

attribute float fase;
attribute float amplitud;
attribute float longOnda;

varying vec3 normal, lightDir, eyeVec;

void main(void)
{
	vec4 v = vec4(gl_Vertex);
	
	v.z =  gl_Vertex.z + amplitud*sin(longOnda*gl_Vertex.x + fase );
	v.x =  gl_Vertex.x + amplitud*sin(longOnda*gl_Vertex.y + fase );
	v.y = gl_Vertex.y + amplitud*sin(longOnda*gl_Vertex.z + fase );
	v.w = 1.0;
		
	gl_Position = gl_ModelViewProjectionMatrix * v;
		
	
	//Cosas para el fragment shader
	normal = gl_NormalMatrix * gl_Normal;

	vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);

	lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	eyeVec = -vVertex;
} 
