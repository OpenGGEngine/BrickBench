package com.opengg.loader.game.nu2.scene.blocks;

import com.opengg.core.math.Vector2f;
import com.opengg.core.math.Vector4f;
import com.opengg.core.render.texture.Texture;
import com.opengg.loader.Util;
import com.opengg.loader.game.nu2.NU2MapData;
import com.opengg.loader.game.nu2.scene.FileMaterial;
import com.opengg.loader.game.nu2.scene.FileMaterial.UVAnimType;
import com.opengg.loader.game.nu2.scene.FileMaterial.UVAnimationProperties;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MaterialBlock extends DefaultFileBlock {
    @Override
    public void readFromFile(ByteBuffer fileBuffer, long blockLength, int blockID, int blockOffset, NU2MapData mapData) throws IOException {
        super.readFromFile(fileBuffer, blockLength, blockID, blockOffset, mapData);
        int materialCount = fileBuffer.getInt();
        fileBuffer.getInt();

        for (int i = 0; i < materialCount; i++) {
            var ptr = fileBuffer.position();
            //System.out.println("material " + i + " | " + Integer.toHexString(ptr));
            var material = new FileMaterial(ptr);
            mapData.scene().materials().put(ptr, material);

            fileBuffer.position(ptr);
            var data = new byte[0x2C4];
            fileBuffer.get(data);

            fileBuffer.position(ptr + 0x38);
            int materialID = fileBuffer.getInt();
            material.mysteryPointer = readPointer();
            material.setID(materialID);
            int alphaBlend = fileBuffer.getInt();

            fileBuffer.position(ptr + 0x54);
            material.setColor(new Vector4f(
                    fileBuffer.getFloat(),
                    fileBuffer.getFloat(),
                    fileBuffer.getFloat(),
                    fileBuffer.getFloat()));
            
            fileBuffer.position(ptr + 0x74);
            material.setDiffuseFileTexture(mapData.scene().texturesByRealIndex().get((int) fileBuffer.getShort()));

            fileBuffer.position(ptr + 0xB4);
            material.setTextureFlags(fileBuffer.getInt());
            material.setDiffuseFileTexture(mapData.scene().texturesByRealIndex().get(fileBuffer.getInt()));

            fileBuffer.position(ptr + 0x40);
            int wrapInt = fileBuffer.getInt();
            material.uWrap = switch(wrapInt >> 10 & 3){
                case 1:
                    yield Texture.WrapType.CLAMP_EDGE;
                case 2:
                    yield Texture.WrapType.REPEAT_MIRRORED;
                case 3:
                    yield Texture.WrapType.CLAMP_BORDER;
                default:
                    yield Texture.WrapType.REPEAT;
            };
            material.vWrap = switch(wrapInt >> 8 & 3){
                case 1:
                    yield Texture.WrapType.CLAMP_EDGE;
                case 2:
                    yield Texture.WrapType.REPEAT_MIRRORED;
                case 3:
                    yield Texture.WrapType.CLAMP_BORDER;
                default:
                    yield Texture.WrapType.REPEAT;
            };
            if((wrapInt >> 10 & 3) != 0 || ((wrapInt >> 8 & 3) != 0)){
                System.exit(0);
            }
            System.out.println(i+","+(wrapInt >> 10 & 3)+","+(wrapInt >> 8 & 3));


            var layer1TexID = fileBuffer.getInt();
            if(layer1TexID != -1) {
                mapData.scene().texturesByRealIndex().get(layer1TexID);
                material.setLayer1DiffuseTexture(mapData.scene().texturesByRealIndex().get(layer1TexID));
            }
            fileBuffer.position(ptr + 0xB4 + 0x78);
            float reflPower = fileBuffer.getFloat();
            float exp = fileBuffer.getFloat();
            
            fileBuffer.position(ptr + 0xB4 + 0x90);
            float fresnelMul = fileBuffer.getFloat();
            float fresnelCoeff = fileBuffer.getFloat();
            material.setReflectivityColor(Util.packedIntToVector4f(0x7f7f7f7f));
            material.setSpecular(new Vector4f(exp, reflPower, fresnelMul, fresnelCoeff));
            
            fileBuffer.position(ptr + 0xB4 + 0x44);
            fileBuffer.get();
            fileBuffer.get();
            fileBuffer.get();
            fileBuffer.get();
            material.setSpecularFileTexture(mapData.scene().texturesByRealIndex().get(fileBuffer.getInt()));
            int normalIndex = fileBuffer.getInt();
            material.setNormalIndex(mapData.scene().texturesByRealIndex().get(normalIndex));
            if(i==57){
                System.out.println("ringgggg," + normalIndex);
            }
            
            fileBuffer.position(ptr + 0xB4 + 0x13C);
            int vertexFormatBits = fileBuffer.getInt();
            int formatBits2 = fileBuffer.getInt();

            fileBuffer.position(ptr + 0xB4 + 0xA8);
            byte lightmapIdx = fileBuffer.get();
            byte surfaceIdx = fileBuffer.get();
            byte specularIdx = fileBuffer.get();
            byte normalIdx = fileBuffer.get();

            fileBuffer.position(ptr + 0xB4 + 0x1B4);
            int inputDefines = fileBuffer.getInt();
            int shaderDefines = fileBuffer.getInt();
            int uvsetCoords = fileBuffer.getInt();

            material.setAlphaType(alphaBlend);
            material.setFormatBits(vertexFormatBits);
            material.setInputDefinesBits(inputDefines);
            material.setShaderDefinesBits(shaderDefines);
            material.setUVSetCoords(uvsetCoords);
            material.setLightmapSetIndex(lightmapIdx);
            material.setSpecularIndex(specularIdx);
            material.setSurfaceUVIndex(surfaceIdx);
            material.generateShaderSettings();

            System.out.println("Opacities:");
            fileBuffer.position(ptr + 0xB4 + 0x24);
            float layer0 = fileBuffer.getFloat();
            float layer1 = fileBuffer.getFloat();
            float layer2 = fileBuffer.getFloat();
            float layer3 = fileBuffer.getFloat();
            material.setLayerOpacities(new Vector4f(layer0,layer1,layer2,layer3));
            //material.setLayerOpacities(new Vector4f(1,1,1,1));
            System.out.println(i+","+layer0+","+layer1+","+layer2+","+layer3+","+layer1TexID);

            fileBuffer.position(ptr + 0xB4+0x44);
            var combineop1 = fileBuffer.get();
            var combineop2 = fileBuffer.get();
            var combineop3 = fileBuffer.get();
            System.out.println(i+" combine op: " + combineop1 + " " + combineop2 + " " + combineop3 + " " + Integer.toHexString(fileBuffer.position()));
            material.setCombineOp1(combineop1);


            fileBuffer.position(ptr + 0xB4+0x14);
            material.layerDiffuse[0] = (Util.packedIntToVector4f(fileBuffer.getInt()));
            material.layerDiffuse[1] = (Util.packedIntToVector4f(fileBuffer.getInt()));
            material.layerDiffuse[2] = (Util.packedIntToVector4f(fileBuffer.getInt()));
            material.layerDiffuse[3] = (Util.packedIntToVector4f(fileBuffer.getInt()));
            material.setColor(material.layerDiffuse[0]);

            fileBuffer.position(ptr + 0xB8);
            int t1id = fileBuffer.getInt();
            int t2id = fileBuffer.getInt();
            int t3id = fileBuffer.getInt();
            int t4id = fileBuffer.getInt();
            System.out.println("Texture IDs: "+ t1id + "," + t2id + "," + t3id + "," + t4id);

            fileBuffer.position(0x1f4);
            System.out.println("Shader?"+","+ fileBuffer.getShort());

            for (int j = 0; j < 4; j++) {
                fileBuffer.position(ptr + 0x1c0 + j * 4);
                var animEnabled = fileBuffer.getInt();
                
                fileBuffer.position(ptr + 0x1F8 + j * 20);
                var animTypeX = switch (fileBuffer.get()) {
                    case 2 -> UVAnimType.LINEAR;
                    case 3 -> UVAnimType.SINE;
                    case 4 -> UVAnimType.COSINE;
                    default -> UVAnimType.OFF;
                };
                var animTypeY = switch (fileBuffer.get()) {
                    case 2 -> UVAnimType.LINEAR;
                    case 3 -> UVAnimType.SINE;
                    case 4 -> UVAnimType.COSINE;
                    default -> UVAnimType.OFF;
                };
                fileBuffer.get();
                fileBuffer.get();

                var xTrigScale = fileBuffer.getFloat();
                var yTrigScale = fileBuffer.getFloat();
                var xScrollSpeed = fileBuffer.getFloat();
                var yScrollSpeed = fileBuffer.getFloat();
                
                material.setUVAnimationProperties(j, 
                    new UVAnimationProperties(
                        j,
                        animEnabled != -1,
                        animTypeX,
                        animTypeY,
                        xScrollSpeed,
                        xTrigScale,
                        yScrollSpeed,
                        yTrigScale)
                );
            }

            if(isLayerEnabled(1,shaderDefines)){
                fileBuffer.position(ptr + 0x74);
                var t1 = fileBuffer.getShort();
                var t2 = fileBuffer.getShort();
                var t3 = fileBuffer.getShort();
            }else{
                fileBuffer.position(ptr + 0x74);
                var t1 = fileBuffer.getShort();
                var t2 = fileBuffer.getShort();
                var t3 = fileBuffer.getShort();
               // System.out.println("not layer 1:" + t1+","+layer1TexID + "," + combineop1);
            }

            fileBuffer.position(ptr + 0x2C4);

        }
    }
    private boolean isLayerEnabled(int layer,int shaderDefinesBits){
        return switch (layer){
            case 0 -> true;
            case 1 -> (shaderDefinesBits & 0x40) != 0;
            case 2 -> (shaderDefinesBits & 0x80) != 0;
            case 3 -> (shaderDefinesBits & 0x100) != 0;

            default -> throw new IllegalStateException("Unexpected layer: " + layer);
        };
    }
}
