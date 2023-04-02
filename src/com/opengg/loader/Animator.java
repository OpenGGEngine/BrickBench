package com.opengg.loader;

import com.opengg.core.math.FastMath;
import com.opengg.core.math.Matrix4f;
import com.opengg.core.math.Quaternionf;
import com.opengg.core.math.Vector3f;
import com.opengg.loader.game.nu2.scene.Anim;
import com.opengg.loader.game.nu2.scene.SpecialObject;
import com.opengg.loader.game.nu2.scene.SpecialObjectComponent;

public class Animator {
    static volatile Anim currAnim;
    static SpecialObjectComponent object;
    static int currFrame = 0;
    static float elapsed = 0;
    static float trueVal = 0;
    public static void playAnim(Anim currAnim2, SpecialObjectComponent object2){
        if(object != null){
            object.offset = new Vector3f(0);
            object.rotateOff2=new Quaternionf();
            object.scaleOffset=new Vector3f(1);
        }
        currAnim = currAnim2;
        object = object2;
        currFrame = 0;
        trueVal = 0;
        elapsed = 0;
    }
    public static void update(float delta){
        if (currAnim == null || object == null) {
            return;
        }
        /*elapsed+=delta;
        if(elapsed < 1/30.0){
            return;
        }
        elapsed-=1/30.0;*/
        synchronized (currAnim) {
            //System.out.println("Animating: " + currAnim.name() + ","+object.getName());
            //object.offset = new Vector3f(currAnim.frames[0].get(currFrame).val, currAnim.frames[1].get(currFrame).val, currAnim.frames[2].get(currFrame).val);
            int currFrame =  Math.min((int)(trueVal*30.0),currAnim.frames[0].size()-1);
            //System.out.println(trueVal+","+currFrame);
            int nextFrame = Math.min(currFrame+1,currAnim.frames[0].size()-1);
            var currPos = new Vector3f(currAnim.frames[0].get(currFrame).val, currAnim.frames[1].get(currFrame).val, currAnim.frames[2].get(currFrame).val);
            var nextPos = new Vector3f(currAnim.frames[0].get(nextFrame).val, currAnim.frames[1].get(nextFrame).val, currAnim.frames[2].get(nextFrame).val);
            object.offset = Vector3f.lerp(currPos,nextPos, (float) (trueVal*30.0-currFrame));
            if (currAnim.numCurves >= 6) {
                //object.rotateOffset = new Vector3f(currAnim.frames[3].get(currFrame).val, currAnim.frames[4].get(currFrame).val, currAnim.frames[5].get(currFrame).val);
                var currRot = new Vector3f(currAnim.frames[3].get(currFrame).val, currAnim.frames[4].get(currFrame).val, currAnim.frames[5].get(currFrame).val);
                var nextRot = new Vector3f(currAnim.frames[3].get(nextFrame).val, currAnim.frames[4].get(nextFrame).val, currAnim.frames[5].get(nextFrame).val);
                object.rotateOffset = Vector3f.lerp(currRot,nextRot, (float) (trueVal*30.0-currFrame));
                //System.out.println(object.rotateOffset);
                //System.out.println(object.rotateOffset.length());
                //object.rotateOff2 = new Quaternionf(object.rotateOffset.length(), object.rotateOffset.normalize().multiply(-1, -1, 1));
                //object.rotateOff2 = new Quaternionf(object.rotateOffset.length(), object.rotateOffset.normalize());
            }

            if (currAnim.numCurves >= 9) {
                //object.scaleOffset = new Vector3f(currAnim.frames[6].get(currFrame).val, currAnim.frames[7].get(currFrame).val, currAnim.frames[8].get(currFrame).val);
                var currScale = new Vector3f(currAnim.frames[6].get(currFrame).val, currAnim.frames[7].get(currFrame).val, currAnim.frames[8].get(currFrame).val);
                var nextScale = new Vector3f(currAnim.frames[6].get(currFrame).val, currAnim.frames[7].get(currFrame).val, currAnim.frames[8].get(currFrame).val);
                object.scaleOffset = Vector3f.lerp(currScale,nextScale, (float) (trueVal*30.0-currFrame));
            }
            //object.updatePos();
            //Matrix4f mat = specialCorrection(preScale(rotateXYZ(object.rotateOffset).multiply(object.special.iablObj().transform()),object.scaleOffset).translate(object.offset));
            //System.out.println(mat.toString());
            //Matrix4f matrix4f = specialCorrection(Matrix4f.IDENTITY.translate(object.offset.add(object.getPositionOffset())).rotate(object.rotateOff2));
            //Matrix4f matrix4f = object.rotateOff2.toMatrix();
            //System.out.println(matrix4f.toString());
            object.setOverrideMatrix(specialCorrection(preScale(rotateXYZ(object.rotateOffset),object.scaleOffset).translate(object.offset)).translate(object.getPositionOffset()));
            //System.out.println(object);
            currFrame++;
            if (currFrame >= currAnim.numFrames+1) {
                currFrame = 0;
            }
        }
        trueVal += delta;
        if(trueVal>=(currAnim.frames[0].size())/30.0){
            trueVal = 0;
        }
    }

    public static Matrix4f preScale(Matrix4f matrix4f,Vector3f scaling){
        return new Matrix4f(matrix4f.m00 * scaling.x,matrix4f.m01 * scaling.x,matrix4f.m02 * scaling.x, matrix4f.m03,
                            matrix4f.m10 * scaling.y,matrix4f.m11 * scaling.y,matrix4f.m12 * scaling.y, matrix4f.m13,
                            matrix4f.m20 * scaling.z,matrix4f.m21 * scaling.z,matrix4f.m22 * scaling.z, matrix4f.m23,
                                matrix4f.m30,matrix4f.m31,matrix4f.m32,matrix4f.m33);
    }
    public static Matrix4f specialCorrection(Matrix4f matrix4f){
        if(false){
            return matrix4f;
        }
        return new Matrix4f(matrix4f.m00,matrix4f.m01,-matrix4f.m02,matrix4f.m03,
                            matrix4f.m10,matrix4f.m11,-matrix4f.m12,matrix4f.m13,
                            -matrix4f.m20,-matrix4f.m21,matrix4f.m22,-matrix4f.m23,
                            matrix4f.m30,matrix4f.m31,-matrix4f.m32,matrix4f.m33);
    }
    public static Matrix4f rotateXYZ(Vector3f rot){
        double sinx = FastMath.sin(rot.x);
        double siny = FastMath.sin(rot.y);
        double sinz = FastMath.sin(rot.z);
        double cosx = FastMath.cos(rot.x);
        double cosy = FastMath.cos(rot.y);
        double cosz = FastMath.cos(rot.z);
        return new Matrix4f((float) (cosy*cosz), (float) (sinz*cosy), (float) -siny, (float) 0,
                (float) ((sinx*siny*cosz)-(sinz*cosx)), (float) ((sinz*sinx*siny)+(cosz*cosx)), (float) (cosy*sinx), (float) 0,
                (float) ((cosz*siny*cosx)+sinz*sinx), (float) ((siny*cosx*sinz)-(cosz*sinx)), (float) (cosy*cosx), (float) 0,
                            0, (float) 0,0,1);
    }
}
