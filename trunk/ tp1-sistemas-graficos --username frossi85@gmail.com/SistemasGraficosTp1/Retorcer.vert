#version 110
#define PI    3.14159265

uniform float angulo;
uniform float altura;
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

float getAng(float alt,float posY,float ang){
		return (2.0*ang*posY/alt - ang);
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
	/*
	v.x =  gl_Vertex.x*cos(angulo) - gl_Vertex.y*sin(angulo);
	v.y =  gl_Vertex.x*sin(angulo) + gl_Vertex.y*cos(angulo);
	v.x =  getX(tetha, radio);
	v.y =  getY(tetha,radio);
	
	v.x =  gl_Vertex.z*sin(angulo) + gl_Vertex.y*cos(angulo);
	v.z =  gl_Vertex.z*cos(angulo) - gl_Vertex.x*sin(angulo);
	
	v.x =  getX(tetha, radio);
	v.z =  getY(tetha,radio);
	
	v.w = 1.0;
	*/


	
	float angle_deg = 20.0*angulo*sin(time);
	float angle_rad = angle_deg *  3.14159 / 180.0;
	
        float ang = (altura*0.5 + gl_Vertex.y)/altura*gl_Vertex.z*angle_rad;
	vec4 pos = vec4(gl_Vertex);
	float t;
	//t = ang;
	t = getAng(altura,gl_Vertex.y,angulo)*sin(time*0.5);
	
	float st = sin(t);
	float ct = cos(t);
	
	float stn = sin(-t);
	float ctn = cos(-t);
	
	vec4 new_pos;
	
		new_pos.x = pos.x*ct - pos.z*st;
		new_pos.z = pos.x*st + pos.z*ct;
	
		new_pos.y = pos.y;
		new_pos.w = pos.w;
	
	/*
	
	new_pos.x = pos.z*st + pos.x*ct;
	new_pos.z = pos.z*ct - pos.x*st;
	
	new_pos.y = pos.y;
	new_pos.w = pos.w;
	*/
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
