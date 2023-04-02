package com.opengg.loader.components;

import com.opengg.core.math.Vector3f;
import com.opengg.core.render.Renderable;
import com.opengg.core.render.SceneRenderUnit;
import com.opengg.core.render.internal.opengl.OpenGLRenderer;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.render.objects.TextureRenderable;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.world.components.Component;
import com.opengg.core.world.components.RenderComponent;
import com.opengg.loader.BrickBench;
import com.sun.jna.platform.win32.OpenGL32;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SuperJumpComponent extends RenderComponent {
    public ArrowComponent start;
    public ArrowComponent end;
    public Renderable lines;

    public SuperJumpComponent() {
        super(new SceneRenderUnit.UnitProperties().shaderPipeline("xFixOnly"));
        this.setUpdateEnabled(true);
        this.start = new ArrowComponent(e->{
            this.start.setPositionOffset(e);
            System.out.println(e);
            generatePath();
        },e->{
            this.start.setPositionOffset(e);
        });
        this.end = new ArrowComponent(e->{
            System.out.println(e);
            this.end.setPositionOffset(e);
            generatePath();
        },e->{
            this.end.setPositionOffset(e);
        });
        this.end.setPositionOffset(new Vector3f(0,1,0));
        this.attach(start);
        this.attach(end);
        generatePath();
    }
    public void update(float delta){
        super.update(delta);
    }
    public void generatePath(){
        var startPos = start.getPosition();
        var endPos = end.getPosition();
        var delta = endPos.subtract(startPos);
        List<Vector3f> positions = new ArrayList<>();
        for (float i = 0; i <= 1; i+=0.05) {
            positions.add(startPos.add(delta.multiply(i)).add(new Vector3f((float) 0,(float)(0.5f*2*0.75*Math.sin(Math.PI * i)),0)));
        }
        lines = new TextureRenderable(ObjectCreator.createLineList(positions), Texture.ofColor(Color.magenta));
        this.setRenderable(lines);
    }

    @Override
    public void render(){
        super.render();
    }
}
