package com.opengg.loader.game.ntt;

import com.opengg.core.engine.OpenGG;
import com.opengg.core.math.Matrix4f;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.SceneRenderUnit;
import com.opengg.core.render.objects.DrawnObject;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.render.objects.TextureRenderable;
import com.opengg.core.render.shader.VertexArrayBinding;
import com.opengg.core.render.shader.VertexArrayFormat;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.system.Allocator;
import com.opengg.core.world.WorldEngine;
import com.opengg.core.world.components.RenderComponent;
import com.opengg.loader.Util;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DNOLoader {
    public static void main(String[] args) {
        /*
        ByteBuffer fileData = null;
        try {
            fileData = ByteBuffer.wrap(Files.readAllBytes(Path.of("C:\\Users\\warre\\Downloads\\nstool-v1.6.3-win_x64\\1\\out\\ASSETS\\MODELS\\LEVELS\\STORY\\EPISODE_08\\LEVEL_04\\ESCAPETHESUPREMACY.DNO")));
            fileData = ByteBuffer.wrap(Files.readAllBytes(Path.of("C:\\Users\\warre\\Downloads\\nstool-v1.6.3-win_x64\\1\\out\\ASSETS\\MODELS\\LEVELS\\STORY\\EPISODE_03\\LEVEL_01\\IH_JANITOR_ROOM_NEW_COL.DNO")));
            fileData = ByteBuffer.wrap(Files.readAllBytes(Path.of("C:\\Users\\warre\\Downloads\\nstool-v1.6.3-win_x64\\1\\out\\ASSETS\\MODELS\\LEVELS\\STORY\\EPISODE_06\\LEVEL_02\\JABBABARGE_EXTERIOR\\MODELS\\JABBABARGE.DNO")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("load");
        load(fileData, null);
    }
    public static class TerrainController{
        public void load(ByteBuffer file,int versionMaybe){
            if(versionMaybe != 0){
                file.get();
                file.get();
                file.get();
                file.get();
                file.getShort();
                file.getShort();
                file.get();
            }
        }
    }
    public static class TerrainMesh{
        public void load(ByteBuffer file,int versionMaybe){
            file.getInt();
            int this0x4c = file.getInt();
            int this0x54 = file.getInt();
            int this0x58 = file.getInt();
            int strLen = file.getInt();
            Util.getStringFromBuffer(file,strLen);
            file.get(new byte[this0x4c * 0xc]);
            for (int i = 0; i < this0x58; i++) {
                file.getInt();
            }
            //Polygons
            for (int i = 0; i < this0x54; i++) {
                file.getInt();
                file.getInt();
                if(versionMaybe < 3){
                    file.getShort();
                    file.get();
                    file.get();
                    file.get();
                    file.get();
                }else{
                    file.get();
                }
            }
        }
    }
    public static class TerrainGeometry{
        public void load(ByteBuffer file,int versionMaybe){
            if(versionMaybe != 0){
                file.getInt();
                file.getInt();
                file.getInt();
                file.getInt();
                file.getShort();
                file.getShort();
                file.getShort();
                if(versionMaybe < 4){
                    file.getShort();
                    file.get();
                    file.get();
                    file.get();
                    file.get();
                    file.get();
                    file.get();
                    if(versionMaybe > 1){
                        file.getInt();
                        if(versionMaybe != 2){
                            file.getInt();
                        }
                    }
                }
            }
        }
    }
    public static class TerrainSpline{
        public void load(ByteBuffer file,int versionMaybe){
            if(versionMaybe != 0){
                int this0x18 = Short.toUnsignedInt(file.getShort());
                file.get();
                file.get();
                file.get();
                for (int i = 0; i < this0x18; i++) {
                    new Vector3f(file.getFloat(),file.getFloat(),file.getFloat());
                }
            }
        }
    }
    public static class TerrainTileMap{
        public void load(ByteBuffer file,int versionMaybe){
            if(versionMaybe != 0){
                int this0x18 = file.getInt(); //puvar1
                int this0x1c = file.getInt(); //puvar1
                int this0x20 = Short.toUnsignedInt(file.getShort()); //puvar2
                int this0x22 = Short.toUnsignedInt(file.getShort()); //puvar3
                file.get();
                for (int i = 0; i < this0x20*this0x22; i++){
                    file.getFloat();
                    file.getFloat();
                    file.getFloat();
                    file.getFloat();
                }
            }
        }
    }
    public static class TerrainEntity{
        public void load(ByteBuffer file,int versionMaybe){
            file.getInt();
            file.getInt();
            file.getInt();
            file.getShort();
            file.getShort();
            file.get();
            file.get();
            file.get();
            file.get();
            file.get();
            file.get();
            file.get();
            file.get();
            file.getInt();
            file.getInt();
            file.getInt();
            file.get();
            //Loop not done
            file.get();//0x39
            file.get();//0x3a;
            if(versionMaybe>1){
                file.get();
                file.getFloat();
                file.getFloat();
                file.getFloat();
                if(versionMaybe != 2){
                    file.getInt();
                    if(versionMaybe > 3){
                        file.getInt();
                        file.getInt();
                        if(versionMaybe != 4){
                            if(versionMaybe > 5){
                                file.getShort();
                                if(versionMaybe != 6){
                                    file.get();
                                    if(versionMaybe > 7){
                                        if(versionMaybe > 0xf){
                                            file.get();
                                            file.get();
                                            file.get();
                                            file.get();
                                            file.getInt();
                                            return;
                                        }else{
                                            file.get();
                                            if(versionMaybe > 8){
                                                file.get();
                                                if(versionMaybe != 9){
                                                    if(versionMaybe > 10){
                                                        file.get();
                                                        if(versionMaybe == 0xb){

                                                        }else if (versionMaybe - 10 > 2){
                                                            if(versionMaybe < 0xe){
                                                                return;
                                                            }
                                                            file.get();
                                                            if(versionMaybe == 0xe){
                                                                return;
                                                            }
                                                            file.getInt();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static void load(ByteBuffer file, DX11MapData data) {
        file.order(ByteOrder.BIG_ENDIAN);
        int resourceHeaderLength = file.getInt();
        file.position(file.position()+resourceHeaderLength);
        int DNOSize = file.getInt();
        if(!Util.getStringFromBuffer(file,0xb).equals("SERIALISED")){
            System.err.println("SERIALIZED BLOCK NOT FOUND: " + Integer.toHexString(file.position()));
            return;
        }
        int DNO0x68 = file.getInt();
        System.out.println("DNO Version: " + Integer.toHexString(DNO0x68));
        //Strings
        int unkShort1 = Short.toUnsignedInt(file.getShort());
        int unkInt1 = file.getInt(); //Maybe subblock version
        for (int i = 0; i < unkShort1; i++) {
            if(unkInt1 > 1){ //This might be wrong
                int strLen = file.getInt();
                String pinnerString = Util.getStringFromBuffer(file,strLen);
            }
        }
        //Matrices
        int unkShort2 = Short.toUnsignedInt(file.getShort());
        int unkInt2 = file.getInt(); //Maybe subblock version
        for (int i = 0; i < unkShort2; i++) {
            Matrix4f mat = new Matrix4f(file.getFloat(),file.getFloat(),file.getFloat(),file.getFloat(),
                    file.getFloat(),file.getFloat(),file.getFloat(),file.getFloat(),
                    file.getFloat(),file.getFloat(),file.getFloat(),file.getFloat(),
                    file.getFloat(),file.getFloat(),file.getFloat(),file.getFloat());
        }
        //Entities
        int unkShort3 = Short.toUnsignedInt(file.getShort());
        System.out.println("Loading: " + unkShort3 + " entities");
        int unkInt3 = file.getInt(); //Maybe subblock version
        for (int i = 0; i < unkShort3; i++) {
            var terEntity = new TerrainEntity();
            terEntity.load(file,unkInt3);
        }
        //Mesh
        int unkShort4 = Short.toUnsignedInt(file.getShort());
        int unkInt4 = file.getInt(); //Maybe subblock version
        System.out.println("Loading: " + unkShort4 + " meshes");
        for (int i = 0; i < unkShort4; i++) {
            var terMesh = new TerrainMesh();
            terMesh.load(file,unkInt4);
        }
        //Joint
        int unkShort5 = Short.toUnsignedInt(file.getShort());
        int unkInt5 = file.getInt(); //Maybe subblock version
        //Geom
        int unkShort6 = Short.toUnsignedInt(file.getShort());
        int unkInt6 = file.getInt(); //Maybe subblock version
        System.out.println("Loading: " + unkShort6 + " geoms");
        for (int i = 0; i < unkShort6; i++) {
            var terGeom = new TerrainGeometry();
            terGeom.load(file,unkInt6);
        }
        System.out.println("Num Geom: " + unkShort6);
        //Spline
        int unkShort7 = Short.toUnsignedInt(file.getShort());
        int unkInt7 = file.getInt(); //Maybe subblock version
        System.out.println("Loading: " + unkShort7 + " splines");
        for (int i = 0; i < unkShort7; i++) {
            var terSpline = new TerrainSpline();
            terSpline.load(file,unkInt7);
        }
        //Controller
        int unkShort8 = Short.toUnsignedInt(file.getShort());
        int unkInt8 = file.getInt(); //Maybe subblock version
        for (int i = 0; i < unkShort8; i++) {
            var terController = new TerrainController();
            terController.load(file,unkInt8);
        }
        //Tile Map
        int unkShort9 = Short.toUnsignedInt(file.getShort());
        int unkInt9 = file.getInt(); //Maybe subblock version
        for (int i = 0; i < unkShort9; i++) {
            var terTileMap = new TerrainTileMap();
            terTileMap.load(file,unkInt9);
        }
        //Convex Mesh
        int unkShort10 = Short.toUnsignedInt(file.getShort());
        System.out.println("Num convex mesh: " + unkShort10);
        int unkInt10 = file.getInt(); //Maybe subblock version
        //KD Terrain
        int unkShort11 = Short.toUnsignedInt(file.getShort());
        int unkInt11 = file.getInt(); //Maybe subblock version
        for (int i = 0; i < unkShort11; i++) {
            int unkTer0x10PuVar1 = file.getInt();
            int unkTer0x18PuVar3 = file.getInt();
            int unkTer0x1cPuVar4 = file.getInt();
            int unkTer0x20PuVar5 = file.getInt();
            int unkTer0x24PuVar6 = file.getInt();
            System.out.println(unkTer0x10PuVar1 + ","+unkTer0x18PuVar3+","+unkTer0x1cPuVar4+","+unkTer0x20PuVar5+","+unkTer0x24PuVar6);
            List<Vector3f> points = new ArrayList<>();
            for (int j = 0; j < unkTer0x10PuVar1; j++) {
                var point = new Vector3f(file.getFloat(),file.getFloat(),file.getFloat());
                points.add(point);
            }
            System.out.println("Position Check: " + Integer.toHexString(file.position()));
            List<Integer> indices = new ArrayList<>();
            for (int j = 0; j < unkTer0x1cPuVar4; j++) {
                int index1 = Short.toUnsignedInt(file.getShort());
                int index2 = Short.toUnsignedInt(file.getShort());
                int index3 = Short.toUnsignedInt(file.getShort());
                indices.add(index1);
                indices.add(index2);
                indices.add(index3);
                file.getShort();
                file.getShort();
            }
            OpenGG.asyncExec(()->{
                FloatBuffer buffer = Allocator.allocFloat(points.size() * 9);
                for (int i2 = 0; i2 < points.size(); i2++) {
                    buffer.put(points.get(i2).x).put(points.get(i2).y).put(points.get(i2).z);
                    buffer.put(1).put(1).put(1);
                    buffer.put(1).put(1).put(1);
                }
                buffer.flip();
                IntBuffer indexBuffer = Allocator.allocInt(indices.size());
                for (int i2 = 0; i2 < indices.size(); i2++) {
                    indexBuffer.put(indices.get(i2));
                }
                indexBuffer.flip();
                VertexArrayFormat format = new VertexArrayFormat(java.util.List.of(new VertexArrayBinding(0, 36, 0, List.of(
                        new VertexArrayBinding.VertexArrayAttribute("position", 12, VertexArrayBinding.VertexArrayAttribute.Type.FLOAT3, 0),
                        new VertexArrayBinding.VertexArrayAttribute("normal", 3 * 4, VertexArrayBinding.VertexArrayAttribute.Type.FLOAT3, 3 * 4),
                        new VertexArrayBinding.VertexArrayAttribute("color", 3 * 4, VertexArrayBinding.VertexArrayAttribute.Type.FLOAT3, 6 * 4)))));
                DrawnObject drawn;
                if (indices.size() == 0) {
                    drawn = DrawnObject.create(format, buffer);
                } else {
                    drawn = DrawnObject.create(format, indexBuffer, buffer);
                }
                WorldEngine.getCurrent().attach(new RenderComponent(new TextureRenderable(drawn, Texture.ofColor(Color.RED)), new SceneRenderUnit.UnitProperties().transparency(false).shaderPipeline("collision")));
            });
            //if(true)
            //  return;
            System.out.println("After Mesh Position Check: " + Integer.toHexString(file.position()));
            for (int j = 0; j < unkTer0x20PuVar5; j++) {
                file.getFloat(); //Some Floats here probably as well
                file.getFloat();
                file.getFloat();
                file.getInt();
                file.getFloat();
                file.getFloat();
                file.getFloat();
                file.getInt();
            }
            System.out.println("Before puvar4 Position Check: " + Integer.toHexString(file.position()));
            for (int j = 0; j < unkTer0x1cPuVar4; j++) {
                file.getShort();
                file.getShort();
            }
            for (int j = 0; j < unkTer0x1cPuVar4; j++) {
                file.get();
            }
            for (int j = 0; j < unkTer0x18PuVar3; j++) {
                file.getInt();
                file.getInt();
                file.getShort();
                file.get();
            }
            System.out.println("Position Check: " + Integer.toHexString(file.position()));
            for (int j = 0; j < unkTer0x24PuVar6; j++) {
                if(unkInt11 < 4) {
                    for (int k = 0; k < unkTer0x24PuVar6; k++) {
                        file.get();
                        file.get();
                        file.getShort();
                        file.get();
                    }
                }else{
                    for (int k = 0; k < unkTer0x24PuVar6; k++) {
                        file.get();
                    }
                }
            }
            System.out.println("Position Check: " + Integer.toHexString(file.position()));
            if(unkInt11 < 2){
                //removeduplicatedvertices
                //calculateweldinfo
            }else{
                int newPuVar4 = file.getInt();
                for (int j = 0; j < newPuVar4; j++) {
                    file.getFloat();
                    file.getFloat();
                    file.getFloat();
                }
                if(unkInt11 - 3 < 2){
                    //String?
                    file.get(new byte[0x10]);
                }
                if(unkInt11 < 5){
                    //removeduplicatedvertices
                    //calculateweldinfo
                }else{
                    int strLeng = file.getInt();
                    Util.getStringFromBuffer(file,strLeng);
                    //removeegeneratetris
                }
            }

        }
        //Mesh
        int unkShort12 = Short.toUnsignedInt(file.getShort());
        int unkInt12 = file.getInt(); //Maybe subblock version
        for (int i = 0; i < unkShort12; i++) {
            var terMesh = new TerrainMesh();
            terMesh.load(file,unkInt12);
        }
        System.out.println("Num mesh 2: " + unkShort12);
        System.out.println(Integer.toHexString(file.position()));
    }
    */}
}
