#version 110

attribute float fase;
attribute float amplitud;
attribute float longOnda;



void main(void)
	{
		
			
			vec4 v = vec4(gl_Vertex);
			
			v.z =  gl_Vertex.z + amplitud*sin(longOnda*gl_Vertex.x + fase );
			v.x =  gl_Vertex.x + amplitud*sin(longOnda*gl_Vertex.y + fase );
			v.y = gl_Vertex.y + amplitud*sin(longOnda*gl_Vertex.z + fase );
			v.w = 1.0;
			
		gl_Position = gl_ModelViewProjectionMatrix * v;
		
	} 
