package com.opengg.loader.game.nu2.scene;

import com.opengg.core.math.Matrix4f;
import com.opengg.core.math.Quaternionf;
import com.opengg.core.math.Vector3f;
import com.opengg.core.math.Vector4f;
import com.opengg.core.render.RenderEngine;
import com.opengg.core.render.SceneRenderUnit;
import com.opengg.core.world.Camera;
import com.opengg.loader.components.EditorEntityRenderComponent;
import com.opengg.loader.components.TextBillboardComponent;
import com.opengg.loader.game.nu2.NU2MapData;

public class SpecialObjectComponent extends EditorEntityRenderComponent {
    public Vector3f offset = new Vector3f(0);
    public Vector3f rotateOffset = new Vector3f(0);
    public Vector3f scaleOffset = new Vector3f(1);
    public Quaternionf rotateOff2 = new Quaternionf();
    public SpecialObject special;
    public SpecialObjectComponent(SpecialObject specialObject, NU2MapData mapData) {
        super(specialObject, specialObject, new SceneRenderUnit.UnitProperties().shaderPipeline("ttNormal").manualPriority(-1));
        var bounds = mapData.scene().boundingBoxes().get(specialObject.boundingBoxIndex());
        this.setPositionOffset(specialObject.pos());
        this.setOverrideMatrix(specialObject.iablObj().transform());
        this.setUpdateEnabled(false);
        special = specialObject;
        //this.setName("special_"+specialObject.name());
        this.setName(specialObject.path());
        this.attach(new TextBillboardComponent(specialObject.name(), new Vector3f(specialObject.pos().multiply(new Vector3f(2,0,0)))));
        this.attach(new IABLComponent(bounds));
    }

    @Override
    public void render() {
        var box = special.getBoundingBox();
        Vector4f testerp = new Vector4f(box.getMin(),1);
        if(Camera.leftPlane.x >= 0){
            testerp = testerp.setX(box.getMax().x);
        }
        if(Camera.leftPlane.y >= 0){
            testerp = testerp.setY(box.getMax().y);
        }
        if(Camera.leftPlane.z >= 0){
            testerp = testerp.setZ(box.getMax().z);
        }
        //System.out.println(Camera.leftPlane.dot(testerp)+" | "+testerp+" | " + Camera.leftPlane);
        //System.out.println(Camera.leftPlane + " | " + Camera.rightPlane + " | " + RenderEngine.getCurrentView().getPosition());
        if(Camera.leftPlane.dot(testerp) < 0){
            return;
        }
        /*
        testerp = new Vector4f(special.getBoundingBox().getMin(),1);
        if(Camera.rightPlane.x >= 0){
            testerp = testerp.setX(special.getBoundingBox().getMax().x);
        }
        if(Camera.rightPlane.y >= 0){
            testerp = testerp.setY(special.getBoundingBox().getMax().y);
        }
        if(Camera.rightPlane.z >= 0){
            testerp = testerp.setZ(special.getBoundingBox().getMax().z);
        }
        //System.out.println(Camera.leftPlane.dot(testerp)+" | "+testerp+" | " + Camera.leftPlane);
        if(Camera.rightPlane.dot(testerp) < 0){
            return;
        }*/
        super.render();
    }

    public void updatePos(){
        //this.setPositionOffset(offset);
        //var mat = new Matrix4f().scale(scaleOffset).rotate(rotateOff2).translate(offset);
       // this.setOverrideMatrix(new Matrix4f().translate(special.iablObj().transform().transform(new Vector3f(0,0,0))).multiply(mat.animMove()));
    }
}
