uniform sampler2D tex;

uniform bool esTextura2D;

//Variables para cube Map
uniform samplerCube cubeMap;
varying vec3 vTexCoord;
uniform bool esCubeMap;

uniform bool esMaterialBrillante;

//Variables generales de luces
varying vec3 normal, lightDir, eyeVec;

void main()
{
	//ESTE COLOR ES POR SI NO ENTRA A NINGUN IF
	gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
	
	
	if(esCubeMap)
	{
		// Accedo a la textura del cubeMap
		vec4 texColor = vec4(textureCube(cubeMap, vTexCoord));
		
		vec4 final_color = 
				(gl_FrontLightModelProduct.sceneColor * gl_FrontMaterial.ambient) + 
				(gl_LightSource[0].ambient * gl_FrontMaterial.ambient);
									
			vec3 N = normalize(normal);
			vec3 L = normalize(lightDir);
			
			float lambertTerm = dot(N,L);
			
			if(lambertTerm > 0.0)
			{
				final_color += gl_LightSource[0].diffuse * 
							   lambertTerm * vec4(texColor.rgb,1.0);	
				
				vec3 E = normalize(eyeVec);
				vec3 R = reflect(-L, N);
				float specular = pow( max(dot(R, E), 0.0), 
				                 gl_FrontMaterial.shininess );
				final_color += gl_LightSource[0].specular * 
							   specular * texColor.a;	
			}	
		
			gl_FragColor = final_color;	
	}
	else
	{
		if(esTextura2D)
		{
			vec4 texColor = texture2D(tex,gl_TexCoord[0].st);
			vec4 final_color = 
				(gl_FrontLightModelProduct.sceneColor * gl_FrontMaterial.ambient) + 
				(gl_LightSource[0].ambient * gl_FrontMaterial.ambient);
									
			vec3 N = normalize(normal);
			vec3 L = normalize(lightDir);
			
			float lambertTerm = dot(N,L);
			
			if(lambertTerm > 0.0)
			{
				final_color += gl_LightSource[0].diffuse * 
				               gl_FrontMaterial.diffuse * 
							   lambertTerm * vec4(texColor.rgb,1.0);	
				
				vec3 E = normalize(eyeVec);
				vec3 R = reflect(-L, N);
				float specular = pow( max(dot(R, E), 0.0), 
				                 gl_FrontMaterial.shininess );
				final_color += gl_LightSource[0].specular * 
				               gl_FrontMaterial.specular * 
							   specular * texColor.a;	
			}	
		
			gl_FragColor = final_color;	
		}
		else
		{
			vec4 final_color = 
				(gl_FrontLightModelProduct.sceneColor * gl_FrontMaterial.ambient) + 
				(gl_LightSource[0].ambient * gl_FrontMaterial.ambient);
									
			vec3 N = normalize(normal);
			vec3 L = normalize(lightDir);
			
			float lambertTerm = dot(N,L);
			
			if(lambertTerm > 0.0)
			{
				final_color += gl_LightSource[0].diffuse * 
				               gl_FrontMaterial.diffuse * 
							   lambertTerm;		
			}
		
			if(esMaterialBrillante)
			{			
				if(lambertTerm > 0.0)
				{					
					vec3 E = normalize(eyeVec);
					vec3 R = reflect(-L, N);
					
					float specular = pow( max(dot(R, E), 0.0), 
					                 gl_FrontMaterial.shininess );
					                 
					final_color += gl_LightSource[0].specular * 
					               gl_FrontMaterial.specular * 
								   specular;	
				}
			}
			else
			{

			}
			gl_FragColor = final_color;	
		}
	}
}