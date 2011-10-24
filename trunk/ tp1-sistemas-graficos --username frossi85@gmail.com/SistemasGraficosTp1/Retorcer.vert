#version 110
#define PI    3.14159265

attribute float angulo;

varying vec3 normal, lightDir, eyeVec;


float getRadio(float x, float y){
	return(sqrt(pow(x,2.0) + pow(y,2.0)));
}

float getTetha(float x, float y){
	if((x > 0.0) && (y >= 0.0))
		return (atan(y,x));
	else if((x > 0.0) && (y < 0.0))
		return(atan(y,x) + 2.0*PI);
	else if(x < 0.0)
		return(atan(y,x) + PI);
	else if((x == 0.0) && (y > 0.0))
		return(PI/2.0);
	else
		return(3.0*PI/2.0);
}

float getX(float tetha, float radio){
	return(radio*cos(tetha));
}

float getY(float radio, float tetha){
	return(radio*sin(tetha));
}

void main(void)
{
	
	vec4 v = vec4(gl_Vertex);
	float radio = getRadio(gl_Vertex.x, gl_Vertex.y);			
	float tetha = getTetha(gl_Vertex.x, gl_Vertex.y);
	
	tetha = tetha + angulo;
	if(tetha >= 2.0*PI)
		tetha = tetha - 2.0*PI;
	
	v.z =  gl_Vertex.z;
	
	v.x =  gl_Vertex.x*cos(angulo) - gl_Vertex.y*sin(angulo);
	v.y =  gl_Vertex.x*sin(angulo) + gl_Vertex.y*cos(angulo);
	/*
	v.x =  getX(tetha, radio);
	v.y =  getY(tetha,radio);
	*/
	v.w = 1.0;
		
	gl_Position = gl_ModelViewProjectionMatrix * v;
	
	//Cosas para el fragment shader
	normal = gl_NormalMatrix * gl_Normal;

	vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);

	lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	eyeVec = -vVertex;	
} 