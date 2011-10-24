varying float intensity;

varying vec3 N;
varying vec3 v;    

void main()
{
//	vec4 color;
	
//	if (intensity > 0.95)
//		color = vec4(1.0,0.5,0.5,1.0);
//	else if (intensity > 0.5)
//		color = vec4(0.6,0.3,0.3,1.0);
//	else if (intensity > 0.25)
//		color = vec4(0.4,0.2,0.2,1.0);
//	else
//		color = vec4(0.2,0.1,0.1,1.0);

//	gl_FragColor = color;


   vec3 L = normalize(gl_LightSource[0].position.xyz - v);   
   vec3 E = normalize(-v); // we are in Eye Coordinates, so EyePos is (0,0,0)  
   vec3 R = normalize(-reflect(L,N));  
 
   //calculate Ambient Term:  
   vec4 Iamb = gl_FrontLightProduct[0].ambient;    

   //calculate Diffuse Term:  
   vec4 Idiff = gl_FrontLightProduct[0].diffuse * max(dot(N,L), 0.0);    
   
   // calculate Specular Term:
   vec4 Ispec = gl_FrontLightProduct[0].specular 
                * pow(max(dot(R,E),0.0),0.3*gl_FrontMaterial.shininess);

   // write Total Color:  
   gl_FragColor = gl_FrontLightModelProduct.sceneColor + Iamb + Idiff + Ispec; 
}