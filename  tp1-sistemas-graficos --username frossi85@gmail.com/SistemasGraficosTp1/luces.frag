varying vec4 diffuse,ambientGlobal, ambient;
varying vec3 normal,lightDir,halfVector, eyeVec;
varying float dist;
uniform sampler2D tex;

void main()
{
	vec3 n,halfV,viewV,ldir;
	float NdotL,NdotHV;
	vec4 color = ambientGlobal;
	float att;
	
	/* a fragment shader can't write a varying variable, hence we need
	a new variable to store the normalized interpolated normal */
	n = normalize(normal);
	
	vec3 L = normalize(lightDir);

	
	/* compute the dot product between normal and normalized lightdir */
	NdotL = max(dot(n,normalize(lightDir)),0.0);

	if (NdotL > 0.0) {
	
		att = 1.0 / (gl_LightSource[0].constantAttenuation +
				gl_LightSource[0].linearAttenuation * dist +
				gl_LightSource[0].quadraticAttenuation * dist * dist);
		color += att * (diffuse * NdotL + ambient);
	
		
		halfV = normalize(halfVector);
		NdotHV = max(dot(n,halfV),0.0);
		
		//MATERIAL BRILLANTE-PLASTICO, el primero q piden
		float specular = pow(NdotHV,gl_FrontMaterial.shininess); 
	
		//MATERIAL SEMI-MATE
		//float specular = 0.0f;
		
		color += att * gl_FrontMaterial.specular * gl_LightSource[0].specular * 
						specular;
	}

	gl_FragColor = color;
}