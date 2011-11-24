uniform samplerCube cubeMap;
varying vec3 vTexCoord;

void main(void)
{

	// Access the cube map texture
	gl_FragColor = vec4(textureCube(cubeMap, vTexCoord));
}
