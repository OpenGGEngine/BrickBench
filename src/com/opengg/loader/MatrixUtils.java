package com.opengg.loader;

import com.opengg.core.math.FastMath;
import com.opengg.core.math.Matrix4f;

public class MatrixUtils {
    public static Matrix4f rotateY(Matrix4f mat,float y){
        float cos = FastMath.cos(y);
        float sin = FastMath.sin(y);
        return new Matrix4f(mat.m00*cos + sin*mat.m02, mat.m01,mat.m02*cos- mat.m00*sin , mat.m03,
                            mat.m10*cos+sin*mat.m12,mat.m11,mat.m12*cos-sin*mat.m10, mat.m13,
                                mat.m20*cos+sin*mat.m22,mat.m21,mat.m22*cos-sin*mat.m20, mat.m23,
                                mat.m30*cos+sin*mat.m32,mat.m31,mat.m32*cos-mat.m30*sin, mat.m33);
    }
    public static Matrix4f preRotateX(Matrix4f mat,float x){
        float cos = FastMath.cos(x);
        float sin = FastMath.sin(x);
        return new Matrix4f(mat.m00, mat.m01, mat.m02, mat.m03,
                cos*mat.m10+sin*mat.m20,cos* mat.m11+sin*mat.m21, cos*mat.m12+sin*mat.m22, mat.m13,
                cos*mat.m20- mat.m10*sin, cos*mat.m21- mat.m11*sin, cos*mat.m22- mat.m12*sin, mat.m23,
                mat.m30, mat.m31, mat.m32, mat.m33);
    }
    public static Matrix4f preRotateY(Matrix4f mat,float y){
        float cos = FastMath.cos(y);
        float sin = FastMath.sin(y);
        return new Matrix4f(mat.m00*cos-sin* mat.m20, cos*mat.m01-sin* mat.m21, cos*mat.m02-sin* mat.m22, mat.m03,
                mat.m10, mat.m11, mat.m12, mat.m13,
                mat.m20*cos+ mat.m00*sin, mat.m21*cos+ mat.m01*sin, mat.m22*cos+ mat.m02*sin, mat.m23,
                mat.m30, mat.m31, mat.m32, mat.m33);
    }
}
