#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;

out vec2 TexCoord;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform vec3 pos;

void main()
{
    vec4 worldPos = model * vec4(aPos, 1.0);
    vec4 viewPos = view * worldPos;
    vec4 clipPos = projection * viewPos;
    
    gl_Position = clipPos;
    TexCoord = aTexCoord;
}
