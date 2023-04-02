package com.opengg.loader.game.nu2.ai;

import com.opengg.core.render.SceneRenderUnit;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.render.objects.TextureRenderable;
import com.opengg.core.render.texture.Texture;
import com.opengg.loader.components.EditorEntityRenderComponent;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class AIPathConnectionsComponent extends EditorEntityRenderComponent {
    public AIPathConnectionsComponent(AIPath path){
        super(path,new TextureRenderable(ObjectCreator.createLines(path.connections().stream().flatMap(e-> Stream.of(path.pathPoints().get(e.aNode()).pos(), path.pathPoints().get(e.bNode()).pos())).collect(Collectors.toList())), Texture.ofColor(Color.GREEN)),new SceneRenderUnit.UnitProperties().shaderPipeline("xFixOnly"));
    }
}
