#version 110

attribute float fase;
attribute float amplitud;
attribute float longOnda;
uniform float time;

varying vec3 normal, lightDir, eyeVec;
//varying float intensity;

vec3 computeNormal( vec3 pos, vec3 tangent, vec3 binormal, float amplitud, float longOnda, float fase, float time )
{
	mat3 J;
	
	// A matrix is an array of column vectors so J[2] is 
	// the third column of J.
	
	J[0][0] = 1.0 + amplitud * longOnda * cos(longOnda * pos.x + time * 0.01 + fase);
	J[0][1] = 0.0;
	J[0][2] = 0.0;
	
	J[1][0] = 0.0;
	J[1][1] = 1.0 + amplitud * longOnda * cos(longOnda * pos.y + time * 0.01 + fase);
	J[1][2] = 0.0;

	J[2][0] = 0.0;
	J[2][1] = 0.0;
	J[2][2] = 1.0 + amplitud * longOnda * cos(longOnda * pos.z + time * 0.01 + fase);
	
	vec3 u = J * tangent;
	vec3 v = J * binormal;
	
	vec3 n = cross(v, u);
	return normalize(n);
}

void main(void)
{
	vec4 v2 = vec4(gl_Vertex);
	v2.z =  gl_Vertex.z + amplitud*sin(longOnda*gl_Vertex.x + time*0.01 + fase );
	v2.x =  gl_Vertex.x + amplitud*sin(longOnda*gl_Vertex.y + time*0.01 +fase );
	v2.y = gl_Vertex.y + amplitud*sin(longOnda*gl_Vertex.z + time*0.01 + fase );
	v2.w = 1.0;
		
	gl_Position = gl_ModelViewProjectionMatrix * v2;
	
	// 2 - Compute the displaced normal
	//
	
	// if the engine does not provide the tangent vector you 
	// can compute it with the following piece of of code:
	//
	vec3 tangent; 
	vec3 binormal; 
	
	vec3 c1 = cross(gl_Normal, vec3(0.0, 0.0, 1.0)); 
	vec3 c2 = cross(gl_Normal, vec3(0.0, 1.0, 0.0)); 
	
	if(length(c1)>length(c2))
	{
		tangent = c1;	
	}
	else
	{
		tangent = c2;	
	}
	
	tangent = normalize(tangent);
	
	binormal = cross(gl_Normal, tangent); 
	binormal = normalize(binormal);

	vec3 displacedNormal;

	displacedNormal = gl_Normal;
	//displacedNormal = computeNormal( v2.xyz, tangent.xyz, binormal, amplitud, longOnda, fase, time);
   	
   	normal = normalize(gl_NormalMatrix * displacedNormal);
		
	//intensity = dot(lightDir,gl_Normal);

	vec3 vVertex = vec3(gl_ModelViewMatrix * v2);

	lightDir = vec3(gl_LightSource[0].position.xyz - vVertex);
	eyeVec = -vVertex;
} 