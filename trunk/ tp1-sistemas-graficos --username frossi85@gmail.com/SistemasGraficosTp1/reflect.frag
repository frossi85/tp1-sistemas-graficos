uniform samplerCube cubeMap;
//uniform sampler2D colorMap;
varying vec3 vTexCoord;

void main(void)
{

	// Access the cube map texture
	
	gl_FragColor = vec4(textureCube(cubeMap, vTexCoord));
	
	//gl_FragColor = texture2D( colorMap, vTexCoord.st);
}
