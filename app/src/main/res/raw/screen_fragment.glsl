// 后面我们完成其他的功能的时候应该就会用到这些内置函数了。比如说相机的各种滤镜、各种静态、动态贴纸，其实现少不了特定片元着色器的支持

// SurfaceTexture比较特殊
// float数据是什么精度的
precision mediump float; // 相机预览时的片元指令

// 采样点的坐标
varying vec2 fCoord; // varying 变量用于顶点着色器和片元着色器之间的传递信息，通常用于传递颜色信息和坐标信息（比如纹理坐标）
// 采样器
uniform sampler2D vTexture; // uniform 变量用于顶点着色器和片元着色器中，用于表示不变的变量，比如，在顶点着色器中，表示点大小的变量，可以使用uniform，如果所有的顶点大小一致

void main(){
    gl_FragColor = texture2D(vTexture,fCoord); // 设置像素的颜色
}

// 以 黑白滤镜 的片元着色器为例：
/* precision highp float; */
/* varying vec2 textureCoordinate; */
/* uniform sampler2D inputImageTexture; */
/* const highp vec3 W = vec3(0.2125, 0.7154, 0.0721); */

/* void main() { */
/*   lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate); */
/*   float luminance = dot(textureColor.rgb, W); */
/*   gl_FragColor = vec4(vec3(luminance), textureColor.a); */
/* } */
