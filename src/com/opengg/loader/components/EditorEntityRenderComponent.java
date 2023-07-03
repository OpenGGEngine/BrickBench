package com.opengg.loader.components;

import com.opengg.core.math.Vector4f;
import com.opengg.core.render.Renderable;
import com.opengg.core.render.SceneRenderUnit;
import com.opengg.core.world.Camera;
import com.opengg.core.world.components.RenderComponent;
import com.opengg.loader.EditorEntity;
import com.opengg.loader.game.nu2.gizmo.Gizmo;

/**
 * A utility component used to simplify the management of {@link EditorEntity}s in the OpenGG engine.
 *
 * If this is used, the entity path will be set as the name of this component so that this component
 * can be easily retrieved with {@code WorldEngine.getAllByName(entity.path())}.
 */
public class EditorEntityRenderComponent extends RenderComponent {
    public EditorEntityRenderComponent(EditorEntity<?> object, SceneRenderUnit.UnitProperties unitProperties) {
        this(object.path(), unitProperties);
    }

    public EditorEntityRenderComponent(String path, SceneRenderUnit.UnitProperties unitProperties) {
        super(unitProperties);
        this.setName(path);
    }

    public EditorEntityRenderComponent(EditorEntity<?> object, Renderable renderable, SceneRenderUnit.UnitProperties unitProperties) {
        this(object.path(), renderable, unitProperties);
    }
    
    public EditorEntityRenderComponent(String path, Renderable renderable, SceneRenderUnit.UnitProperties unitProperties) {
        super(renderable, unitProperties);
        this.setName(path);
    }

    @Override
    public void render() {
        if(this.mapObject instanceof Gizmo.GizPickup pickup) {
            if (Camera.rightPlane.dot(new Vector4f(pickup.pos().multiply(-1,1,0),1)) < 0) {
               // return;
            }
            /*Vector4f testerp = new Vector4f(special.getBoundingBox().getMin(), 1);
            if (Camera.leftPlane.x >= 0) {
                testerp = testerp.setX(special.getBoundingBox().getMax().x);
            }
            if (Camera.leftPlane.y >= 0) {
                testerp = testerp.setY(special.getBoundingBox().getMax().y);
            }
            if (Camera.leftPlane.z >= 0) {
                testerp = testerp.setZ(special.getBoundingBox().getMax().z);
            }
            //System.out.println(Camera.leftPlane.dot(testerp)+" | "+testerp+" | " + Camera.leftPlane);
            if (Camera.leftPlane.dot(testerp) < 0) {
                return;
            }
            testerp = new Vector4f(special.getBoundingBox().getMin(), 1);
            if (Camera.rightPlane.x >= 0) {
                testerp = testerp.setX(special.getBoundingBox().getMax().x);
            }
            if (Camera.rightPlane.y >= 0) {
                testerp = testerp.setY(special.getBoundingBox().getMax().y);
            }
            if (Camera.rightPlane.z >= 0) {
                testerp = testerp.setZ(special.getBoundingBox().getMax().z);
            }
            //System.out.println(Camera.leftPlane.dot(testerp)+" | "+testerp+" | " + Camera.leftPlane);
            if (Camera.rightPlane.dot(testerp) < 0) {
                return;
            }*/
        }
        super.render();
    }
}
