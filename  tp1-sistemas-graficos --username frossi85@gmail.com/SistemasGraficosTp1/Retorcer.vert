#version 110
#define PI    3.14159265

attribute float angulo;
uniform float time;

varying vec3 normal, lightDir, eyeVec;

/*
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
*/
vec4 DoTwist( vec4 pos, float t )
{
	float st = sin(t);
	float ct = cos(t);
	vec4 new_pos;
	
	new_pos.x = pos.x*ct - pos.z*st;
	new_pos.z = pos.x*st + pos.z*ct;
	
	new_pos.y = pos.y;
	new_pos.w = pos.w;

	return new_pos ;
}


void main(void)
{
	/*
	vec4 v = vec4(gl_Vertex);
	float radio = getRadio(gl_Vertex.x, gl_Vertex.y);			
	float tetha = getTetha(gl_Vertex.x, gl_Vertex.y);
	
	tetha = tetha + angulo;
	if(tetha >= 2.0*PI)
		tetha = tetha - 2.0*PI;
	
	v.z =  gl_Vertex.z;
	
	v.x =  gl_Vertex.x*cos(angulo) - gl_Vertex.y*sin(angulo);
	v.y =  gl_Vertex.x*sin(angulo) + gl_Vertex.y*cos(angulo);
	
	v.x =  getX(tetha, radio);
	v.y =  getY(tetha,radio);
	
	v.w = 1.0;
	*/


	/*
	float angle_deg = angulo*sin(time);
	float angle_rad = angle_deg * PI / 180.0;
	*/
        float ang = (gl_Vertex.z*0.5 + gl_Vertex.y)/gl_Vertex.z;
	vec4 pos = vec4(gl_Vertex);
	float t  = ang;
	
	float st = sin(t);
	float ct = cos(t);
	vec4 new_pos;
	
	new_pos.x = pos.x*ct - pos.z*st;
	new_pos.z = pos.x*st + pos.z*ct;
	
	new_pos.y = pos.y;
	new_pos.w = pos.w;
	vec4 twistedPosition = vec4(new_pos);


	
	//vec4 twistedPosition = DoTwist(aux, ang);
	//vec4 twistedNormal = DoTwist(vec4(gl_Normal), ang), 
	
	

	//vec4 twistedPosition = gl_Vertex;
	gl_Position = gl_ModelViewProjectionMatrix * twistedPosition;
	
	//Cosas para el fragment shader
	normal = gl_NormalMatrix * gl_Normal;

	vec3 vVertex = vec3(gl_ModelViewMatrix * gl_Vertex);

	lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	eyeVec = -vVertex;	
} 