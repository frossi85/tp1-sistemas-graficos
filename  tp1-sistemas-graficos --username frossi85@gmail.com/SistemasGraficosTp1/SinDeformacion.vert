#version 110


varying vec3 normal, lightDir, eyeVec;
	
varying float intensity;


varying vec3 N;
varying vec3 v;

void main(void)
{
	//Cosas para el fragment shader
	normal = gl_NormalMatrix * gl_Normal;

	vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);

	lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	eyeVec = -vVertex;	
	
	
	
	//////
	vec3 ld;
		
	intensity = dot(lightDir,gl_Normal);
	
	//gl_Position = ftransform();
	
	   v = vec3(gl_ModelViewMatrix * gl_Vertex);       
   N = normalize(gl_NormalMatrix * gl_Normal);

   gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
} 


	