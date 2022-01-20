attribute vec4 vPosition; // attribute 变量用于顶点着色器中，表示普通的变量。 
attribute vec2 vCoord; // 纹理坐标
varying vec2 fCoord;   // 输出片元着色器坐标

void main(){
    gl_Position = vPosition; // 设置顶点的位置
    fCoord = vCoord;
}