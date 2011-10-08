#version 110

attribute vec4 glVertex;

void main(void)
	{
		vec4 v = vec4(gl_Vertex);
		if (gl_Vertex.z > 2.0){
			v.z = sin(4.0*v.x )*0.25 + gl_Vertex.z;
			
		}
		gl_Position = gl_ModelViewProjectionMatrix * v;
	} 
