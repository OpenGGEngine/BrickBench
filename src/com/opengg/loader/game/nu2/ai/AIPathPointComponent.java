package com.opengg.loader.game.nu2.ai;

import com.opengg.core.math.Matrix4f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.SceneRenderUnit;
import com.opengg.core.render.objects.MatrixRenderable;
import com.opengg.core.render.objects.RenderableGroup;
import com.opengg.core.render.objects.TextureRenderable;
import com.opengg.core.render.texture.Texture;
import com.opengg.loader.components.EditorEntityRenderComponent;
import com.opengg.loader.components.NativeCache;

import java.awt.*;
import java.util.List;


public class AIPathPointComponent extends EditorEntityRenderComponent {
    public AIPathPointComponent(AIPath.AIPathPoint point){
        //super(point,
          //      new RenderableGroup(List.of(new TextureRenderable(NativeCache.CYLINDER, Texture.ofColor(Color.GREEN)), new MatrixRenderable(new TextureRenderable(NativeCache.CUBE, Texture.ofColor(Color.GREEN)),new Matrix4f().scale(new Vector3f(2*point.xzSize(),point.maxY()-point.minY(),2*point.xzSize())).translate(point.pos())))),new SceneRenderUnit.UnitProperties().shaderPipeline("xFixOnly"));
        super(point,new TextureRenderable(NativeCache.CYLINDER, Texture.ofColor(point.exitNodeRoutIDBitfield() != 0 ? Color.RED : Color.GREEN)),new SceneRenderUnit.UnitProperties().shaderPipeline("xFixOnly"));
        this.setPositionOffset(point.pos().add(new Vector3f(0f,(point.maxY()-point.minY())/2.0f,0f)));
        this.setScaleOffset(new Vector3f(point.xzSize(),point.maxY()-point.minY(),point.xzSize()));
    }
}
