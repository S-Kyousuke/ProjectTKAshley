
uniform sampler2D u_texture;

varying vec2 v_texCoord;

uniform vec2 resolution;

void main(){
	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5,0.5);
	position.x *= resolution.x/resolution.y;
	float len = length(position);
	
	vec4 color = texture2D(u_texture, v_texCoord);
	
	//gl_FragColor = vec4(color.rgb * vec3(0.3,0.3,0.7), color.a);
	gl_FragColor = vec4(mix(color.rgb * vec3(0.3,0.3,0.7), color.rgb*smoothstep(0.2,0.1,len),0.9),color.a);
	 
}