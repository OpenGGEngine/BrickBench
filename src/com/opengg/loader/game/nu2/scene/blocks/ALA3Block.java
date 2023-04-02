package com.opengg.loader.game.nu2.scene.blocks;

import com.opengg.core.engine.OpenGG;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.SceneRenderUnit;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.render.objects.TextureRenderable;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.world.WorldEngine;
import com.opengg.core.world.components.RenderComponent;
import com.opengg.loader.game.nu2.NU2MapData;
import com.opengg.loader.game.nu2.scene.Anim;
import com.opengg.loader.game.nu2.scene.blocks.DefaultFileBlock;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ALA3Block extends DefaultFileBlock {
    @Override
    /*public void readFromFile(ByteBuffer fileBuffer, long blockLength, int blockID, int blockOffset, MapData mapData) throws IOException {
        int numAnim = fileBuffer.getInt();
        System.out.println("Loading " + numAnim + " animations");
        PrintWriter pw = new PrintWriter(new File("C:/res/analysis.txt"));
        for (int i = 0; i < numAnim; i++) {
            int position = fileBuffer.position();
            int subHead = fileBuffer.getInt();
            int realOffset = position + subHead;
            if(subHead != 0) {
                while (true) {
                    fileBuffer.position(realOffset);
                    System.out.println(Integer.toHexString(fileBuffer.getInt()));
                    System.out.println(fileBuffer.getShort()+","+fileBuffer.getShort()+","+fileBuffer.getShort()+","+fileBuffer.getShort()+","+fileBuffer.getShort()+","+fileBuffer.getShort()+","+"\n");
                    //System.out.println(fileBuffer.getShort());
                    //System.out.println(fileBuffer.getShort());
                    //System.out.println(fileBuffer.getShort());
                    //System.out.println(fileBuffer.getShort());
                    //System.out.println(fileBuffer.getShort());
                    //System.out.println(fileBuffer.getShort());
                    fileBuffer.position(realOffset+0x14);
                    int offset = Short.toUnsignedInt(fileBuffer.getShort());
                    int offset2 = Short.toUnsignedInt(fileBuffer.getShort());
                    System.out.println("ANI: " + offset + "," + offset2);
                    fileBuffer.position(realOffset+0x34);
                    System.out.println("Block Size: " + Integer.toHexString(fileBuffer.getInt()));
                    if(offset == 0) break;
                    realOffset+= offset;
                }
            }
            fileBuffer.position(position+4);
        }
        pw.close();
    }*/
    public void readFromFile(ByteBuffer fileBuffer, long blockLength, int blockID, int blockOffset, NU2MapData mapData) throws IOException {
        //if(1==1)return;
        fileBuffer.order(ByteOrder.LITTLE_ENDIAN);
        int numBlocks = fileBuffer.getInt();
        fileBuffer.getInt();
        ArrayList<Integer> blockOffsets = new ArrayList<>();
        System.out.println("Reading ANim " + numBlocks + " blocks\n");
        for (int i = 0; i < numBlocks; i++) {
            //System.out.println("Offset: " + Integer.toHexString(fileBuffer.getInt()));
            blockOffsets.add(fileBuffer.getInt());
        }
        for (int i2 = 0; i2 < numBlocks; i2++) {
            var offset = blockOffsets.get(i2);
            fileBuffer.position(offset + 0x6);
            int frameCount = Short.toUnsignedInt(fileBuffer.getShort());
            fileBuffer.position(offset + 0xc);
            var numCurves = Short.toUnsignedInt(fileBuffer.getShort());
            fileBuffer.position(offset + 10);
            var topLimit = Short.toUnsignedInt(fileBuffer.getShort());
            fileBuffer.position(offset + 0xe);
            var mystery2 = Short.toUnsignedInt(fileBuffer.getShort());
            frameCount = topLimit+mystery2;
            ArrayList<Vector3f> testPoints = new ArrayList<>();
            if(i2 == 269)
            System.out.println(Integer.toHexString(fileBuffer.position()));
            var anim = new Anim("Anim: " + i2, frameCount, numCurves);
            for (float currFrame = 0; currFrame <= frameCount; currFrame += 1f) {
                int frameBoundary = (int) currFrame;
                int fourFramePackOffset = (int) currFrame % 4;
                float delta = currFrame - frameBoundary;
                var testPoint = new Vector3f();
                fileBuffer.position(offset);
                fileBuffer.position(offset + 0x6);
                //if (currFrame > frameCount) break;
                //System.out.println("Frame count: " + frameCount);
                fileBuffer.position(offset + 0x8);
                int frameSize = Short.toUnsignedInt(fileBuffer.getShort());
                //System.out.println("Frame size: " + Integer.toHexString(frameSize));
                fileBuffer.position(offset + 0xc);
                fileBuffer.position(offset + 0x11);
                int bVar3 = fileBuffer.get();
                fileBuffer.position(offset + 0x1c);
                float oneC = fileBuffer.getFloat();
                fileBuffer.position(offset + 0x20);
                float twoZero = fileBuffer.getFloat();
                fileBuffer.position(offset + 0x24);
                int floatScaleOffsetAddress = fileBuffer.getInt();
                fileBuffer.position(offset + 0x28);
                int model10Param = fileBuffer.getInt();
                fileBuffer.position(offset + 0x2c);
                int floatFormatAddress = fileBuffer.getInt();
                fileBuffer.position(offset + 0x30);
                int dataAddress = fileBuffer.getInt();
                dataAddress += (frameBoundary / 4) * frameSize;
                fileBuffer.position(offset + 0x34);
                int curveTypesAddress = fileBuffer.getInt();
                fileBuffer.position(curveTypesAddress);
                anim.curveType = fileBuffer.get();
                int oldData = dataAddress;
                for (int i = 0; i < numCurves; i++) {
                    float output = 0.0f;
                    int format = readUnsignedShortAtLocation(floatFormatAddress + i * 2, fileBuffer);
                    //System.out.println("Read format: " + format);
                    fileBuffer.position(dataAddress);
                    long data1, data2, data3, data4, data5, data6, data7;
                    float fscale, foffset;
                    if(i2==269){
                        System.out.println(i + "," + format);
                    }
                    switch (format) {
                        case 6:
                            data1 = readUnsignedIntAtLocation(dataAddress, fileBuffer);
                            data2 = readUnsignedIntAtLocation(dataAddress + frameSize, fileBuffer);
                            data3 = data1 >>> 8;
                            int framePackSixOff = fourFramePackOffset * 6;
                            float part1 = (data3 >>> framePackSixOff & 0x3f) * 0.01587302f;
                            float part2 = (data3 >>> (framePackSixOff + 6 & 0x1f) & 0x3f) * 0.01587302f;
                            float part3 = (float) (data1 & 0xff);
                            float part4 = (float) (data2 & 0xff);
                            fscale = readFloatAtLocation(floatScaleOffsetAddress, fileBuffer);
                            foffset = readFloatAtLocation(floatScaleOffsetAddress + 4, fileBuffer);
                            if (fourFramePackOffset < 3) {
                                output = ((part1 + delta * (part2 - part1)) * (part4 - part3) + part3) * fscale + foffset;
                            }else{
                                float intermediate = part3 + part1 * (part4-part3);
                                int pbvar2 = readByteAtLocation(dataAddress + frameSize*2,fileBuffer);
                                float intermediate2 = (pbvar2-part4)*(data2 >>> 8 & 0x3f) * 0.01587302f + part4;
                                output = ((intermediate + delta * (intermediate2-intermediate))) * fscale + foffset;
                            }
                            floatScaleOffsetAddress += 8;
                            dataAddress += 4;
                            break;
                        case 7:
                            fscale = readFloatAtLocation(floatScaleOffsetAddress, fileBuffer);
                            foffset = readFloatAtLocation(floatScaleOffsetAddress + 4, fileBuffer);
                            switch (fourFramePackOffset) {
                                case 0:
                                    data1 = readUnsignedShortAtLocation(dataAddress, fileBuffer);
                                    data2 = readUnsignedShortAtLocation(dataAddress + 2, fileBuffer);
                                    data3 = readUnsignedShortAtLocation(dataAddress + 4, fileBuffer);
                                    data4 = readUnsignedShortAtLocation(dataAddress + frameSize, fileBuffer);
                                    output = (((((float) ((data3 & 0xfff) - (data2 & 0xfff)) * delta + (float) (data2 & 0xfff))) / 4095.0f) * ((float) (data4 - data1)) + (float) data1) * fscale + foffset;
                                    break;
                                case 1:
                                    data1 = readUnsignedShortAtLocation(dataAddress, fileBuffer);
                                    data2 = readUnsignedShortAtLocation(dataAddress + 4, fileBuffer);
                                    data3 = readUnsignedShortAtLocation(dataAddress + 6, fileBuffer);
                                    data4 = readUnsignedShortAtLocation(dataAddress + frameSize, fileBuffer);
                                    output = ((((float) ((data3 & 0xfff) - (data2 & 0xfff)) * delta + (float) (data2 & 0xfff)) / 4095.0f) * (float) (data4 - data1) + (float) data1) * fscale + foffset;
                                    break;
                                case 2:
                                    data1 = readUnsignedShortAtLocation(dataAddress, fileBuffer);
                                    data2 = readUnsignedShortAtLocation(dataAddress + 2, fileBuffer);
                                    data3 = readUnsignedByteAtLocation(dataAddress + 5, fileBuffer);
                                    data4 = readUnsignedShortAtLocation(dataAddress + 6, fileBuffer);
                                    data5 = readUnsignedShortAtLocation(dataAddress + frameSize, fileBuffer);
                                    output = ((((float) ((data3 & 0xf0 | (data2 >> 0xc) | data4 >> 4 & 0xf00) - (data4 & 0xfff)) * delta + (float) (data4 & 0xfff)) / 4095.0f) * (float) (data5 - data1) + (float) data1) * fscale + foffset;
                                    break;
                                case 3:
                                    data1 = readUnsignedShortAtLocation(dataAddress, fileBuffer);
                                    data2 = readUnsignedByteAtLocation(dataAddress + 3, fileBuffer);
                                    data3 = readUnsignedByteAtLocation(dataAddress + 5, fileBuffer);
                                    data4 = readUnsignedShortAtLocation(dataAddress + 6, fileBuffer);
                                    data5 = readUnsignedShortAtLocation(dataAddress + frameSize, fileBuffer);
                                    data6 = readUnsignedShortAtLocation(dataAddress + frameSize + 2, fileBuffer);
                                    data7 = readUnsignedShortAtLocation(dataAddress + 2 * frameSize, fileBuffer);
                                    float added = (float) data1 + (((data4 & 0xf00f | data2) >> 4 | (data3 & 0xf0)) * (data5 - data1)) / 4095.0f;
                                    output = ((added) + delta * (((((float) (data6 & 0xfff)) * (float) (data7 - data5)) / 4095.0f + (float) data5) - added)) * fscale + foffset;
                                    break;
                            }
                            dataAddress += 8;
                            floatScaleOffsetAddress += 8;
                            break;
                        case 8:
                            data1 = readByteAtLocation(dataAddress + fourFramePackOffset, fileBuffer);
                            output = (float) readShortAtLocation((int) (model10Param + data1 * 2), fileBuffer);
                            dataAddress += 4;
                            break;
                        default:
                            output = (float) readUnsignedShortAtLocation(model10Param - 0x20 + bVar3 * 2 + format * 2, fileBuffer) * twoZero + oneC;
                            break;
                        case 10:
                            output = (float) (readShortAtLocation(model10Param + 2 + readByteAtLocation(dataAddress + fourFramePackOffset, fileBuffer) * 2, fileBuffer) | readShortAtLocation(model10Param + readByteAtLocation(dataAddress + fourFramePackOffset, fileBuffer) * 2, fileBuffer) << 0x10);
                            dataAddress += 4;
                            break;
                    }
                    if (i == 0) {
                        testPoint = testPoint.setX(output);
                        anim.addFrame(0,output);
                    } else if (i == 1) {
                        //if(i2==95) {
                        //    System.out.println(output + " | " + fourFramePackOffset);
                        //}
                        testPoint = testPoint.setY(output);
                        anim.addFrame(1,output);
                    } else if (i == 2) {
                        testPoint = testPoint.setZ(output);
                        anim.addFrame(2,output);
                    }else{
                        anim.addFrame(i,output);
                    }

                    testPoints.add(testPoint);
                    //System.out.println("Diff " + Integer.toHexString(dataAddress - oldData));
                }
            }

            mapData.scene().animations().add(anim);
        }
        /*
        ArrayList<Vector3f> testPoints = new ArrayList<>();
        for (float currFrame = 1; currFrame < 142; currFrame+=0.5f) {
            int frameBoundary = (int)currFrame;
            int fourFramePackOffset = (int)currFrame % 4;
            float delta = currFrame - frameBoundary;
            var testPoint = new Vector3f();
            for (var offset : blockOffsets) {
                //offset = blockOffsets.get(29);
                offset = blockOffsets.get(19);
                //System.out.println(Integer.toHexString(offset));
                fileBuffer.position(offset);
                fileBuffer.position(offset + 0x6);
                int frameCount = Short.toUnsignedInt(fileBuffer.getShort());
                if(currFrame >= frameCount) break;
                //System.out.println("Frame count: " + frameCount);
                fileBuffer.position(offset + 0x8);
                int frameSize = Short.toUnsignedInt(fileBuffer.getShort());
                //System.out.println("Frame size: " + Integer.toHexString(frameSize));
                fileBuffer.position(offset + 0xc);
                int numCurves = Short.toUnsignedInt(fileBuffer.getShort());
                fileBuffer.position(offset + 0x11);
                int bVar3 = fileBuffer.get();
                fileBuffer.position(offset + 0x1c);
                float oneC = fileBuffer.getFloat();
                fileBuffer.position(offset + 0x20);
                float twoZero = fileBuffer.getFloat();
                fileBuffer.position(offset + 0x24);
                int floatScaleOffsetAddress = fileBuffer.getInt();
                fileBuffer.position(offset + 0x28);
                int model10Param = fileBuffer.getInt();
                fileBuffer.position(offset + 0x2c);
                int floatFormatAddress = fileBuffer.getInt();
                fileBuffer.position(offset + 0x30);
                int dataAddress = fileBuffer.getInt();
                dataAddress += (frameBoundary / 4) * frameSize;
                fileBuffer.position(offset + 0x34);
                int curveTypesAddress = fileBuffer.getInt();
                int oldData = dataAddress;
                for (int i = 0; i < numCurves; i++) {
                    float output = 0.0f;
                    int format = readUnsignedShortAtLocation(floatFormatAddress + i * 2, fileBuffer);
                    //System.out.println("Read format: " + format);
                    fileBuffer.position(dataAddress);
                    long data1, data2, data3, data4, data5, data6, data7;
                    float fscale, foffset;
                    switch (format) {
                        case 6:
                            data1 = readUnsignedIntAtLocation(dataAddress,fileBuffer);
                            data2 = readUnsignedIntAtLocation(dataAddress + frameSize,fileBuffer);
                            data3 = data1 >> 8;
                            int framePackSixOff = fourFramePackOffset * 6;
                            float part1 = (data3 >> framePackSixOff & 0x3f) * 0.01587302f;
                            float part2 = (data3>>(framePackSixOff + 6 & 0x1f) & 0x3f) * 0.01587302f;
                            float part3 = (float)(data1 & 0xff);
                            float part4 = (float)(data2 & 0xff);
                            fscale = readFloatAtLocation(floatScaleOffsetAddress,fileBuffer);
                            foffset = readFloatAtLocation(floatScaleOffsetAddress+4,fileBuffer);
                            if(fourFramePackOffset < 3){
                                output = ((part1 + delta * (part2 - part1)) * (part4 - part3) + part3) * fscale + foffset;
                            }
                            floatScaleOffsetAddress += 8;
                            dataAddress += 4;
                            break;
                        case 7:
                            fscale = readFloatAtLocation(floatScaleOffsetAddress,fileBuffer);
                            foffset = readFloatAtLocation(floatScaleOffsetAddress+4,fileBuffer);
                            switch(fourFramePackOffset){
                                case 0:
                                    data1 = readUnsignedShortAtLocation(dataAddress,fileBuffer);
                                    data2 = readUnsignedShortAtLocation(dataAddress+2,fileBuffer);
                                    data3 = readUnsignedShortAtLocation(dataAddress+4,fileBuffer);
                                    data4 = readUnsignedShortAtLocation(dataAddress + frameSize,fileBuffer);
                                    output = (((((float)( (data3 & 0xfff) - (data2 & 0xfff) ) + (float)(data2 & 0xfff)) * delta) / 4095.0f) * ((float)(data4 - data1) + (float)data1)) * fscale + foffset;
                                    break;
                                case 1:
                                    data1 = readUnsignedShortAtLocation(dataAddress,fileBuffer);
                                    data2 = readUnsignedShortAtLocation(dataAddress+4,fileBuffer);
                                    data3 = readUnsignedShortAtLocation(dataAddress+6,fileBuffer);
                                    data4 = readUnsignedShortAtLocation(dataAddress + frameSize,fileBuffer);
                                    output = ((((float)((data3 & 0xfff) - (data2 & 0xfff)) * delta + (float)(data2 & 0xfff)) / 4095.0f) * (float)(data4 - data1) + (float)data1) * fscale + foffset;
                                    break;
                                case 2:
                                    data1 = readUnsignedShortAtLocation(dataAddress,fileBuffer);
                                    data2 = readUnsignedShortAtLocation(dataAddress+2,fileBuffer);
                                    data3 = readUnsignedByteAtLocation(dataAddress+5,fileBuffer);
                                    data4 = readUnsignedShortAtLocation(dataAddress+6,fileBuffer);
                                    data5 = readUnsignedShortAtLocation(dataAddress + frameSize,fileBuffer);
                                    output = ((((float)((data3 & 0xf0 | (data2 >> 0xc) | data4 >> 4 & 0xf00) - (data4 & 0xfff)) * delta + (float)(data4 & 0xfff)) / 4095.0f) * (float)(data5 - data1) + (float)data1) * fscale + foffset;
                                    break;
                                case 3:
                                    data1 = readUnsignedShortAtLocation(dataAddress,fileBuffer);
                                    data2 = readUnsignedByteAtLocation(dataAddress+3,fileBuffer);
                                    data3 = readUnsignedByteAtLocation(dataAddress+5,fileBuffer);
                                    data4 = readUnsignedShortAtLocation(dataAddress+6,fileBuffer);
                                    data5 = readUnsignedShortAtLocation(dataAddress + frameSize,fileBuffer);
                                    data6 = readUnsignedShortAtLocation(dataAddress + frameSize + 2,fileBuffer);
                                    data7 = readUnsignedShortAtLocation(dataAddress + 2*frameSize,fileBuffer);
                                    float added = (float)data1 + (((data4 & 0xf00f | data2) >> 4 | (data3 & 0xf0)) * (data5-data1))/4095.0f;
                                    output = ((added) + delta * ((( ((float)(data6 & 0xfff)) * (float)(data7-data5)) /4095.0f + (float)data5) - added)) * fscale + foffset;
                                    break;
                            }
                            dataAddress += 8;
                            floatScaleOffsetAddress += 8;
                            break;
                        case 8:
                            data1 = readByteAtLocation(dataAddress+fourFramePackOffset,fileBuffer);
                            output = (float) readShortAtLocation((int) (model10Param + data1 * 2),fileBuffer);
                            dataAddress += 4;
                            break;
                        default:
                            output = (float)readUnsignedShortAtLocation(model10Param - 0x20 + bVar3 * 2 + format * 2,fileBuffer)  * twoZero + oneC;
                            break;
                        case 10:
                            output = (float)( readShortAtLocation(model10Param + 2 + readByteAtLocation(dataAddress+fourFramePackOffset,fileBuffer) * 2,fileBuffer)| readShortAtLocation(model10Param + readByteAtLocation(dataAddress+fourFramePackOffset,fileBuffer)*2,fileBuffer)<< 0x10);
                            dataAddress += 4;
                            break;
                    }
                    if(i == 0){
                        testPoint = testPoint.setX(output);
                    }else if(i == 1){
                        testPoint = testPoint.setY(output);
                    }else if(i == 2){
                        testPoint = testPoint.setZ(output);
                    }
                }
                testPoints.add(testPoint);
                //System.out.println("Diff " + Integer.toHexString(dataAddress - oldData));
            }
        }*/
        /*
        OpenGG.asyncExec(()->{
            // if(!WorldEngine.findEverywhereByName("map").isEmpty())
            if(!mapData.name().toLowerCase().contains("things"))
            WorldEngine.findEverywhereByName("map").get(0).attach(new RenderComponent(new TextureRenderable(ObjectCreator.createPointList(testPoints), Texture.ofColor(Color.GREEN)),new SceneRenderUnit.UnitProperties().shaderPipeline("xFixOnly")));
        });*/
    }

    public int readByteAtLocation(int position, ByteBuffer fb) {
        int old = fb.position();
        fb.position(position);
        int ret = fb.get();
        fb.position(old);
        return ret;
    }

    public int readUnsignedByteAtLocation(int position, ByteBuffer fb) {
        int old = fb.position();
        fb.position(position);
        int ret = Byte.toUnsignedInt(fb.get());
        fb.position(old);
        return ret;
    }

    public int readUnsignedShortAtLocation(int position, ByteBuffer fb) {
        int old = fb.position();
        fb.position(position);
        int ret = Short.toUnsignedInt(fb.getShort());
        fb.position(old);
        return ret;
    }

    public int readShortAtLocation(int position, ByteBuffer fb) {
        int old = fb.position();
        fb.position(position);
        int ret = fb.getShort();
        fb.position(old);
        return ret;
    }

    public long readUnsignedIntAtLocation(int position, ByteBuffer fb) {
        int old = fb.position();
        fb.position(position);
        long ret = fb.getInt();
        fb.position(old);
        return ret;
    }

    public float readFloatAtLocation(int position, ByteBuffer fb) {
        int old = fb.position();
        fb.position(position);
        float ret = fb.getFloat();
        fb.position(old);
        return ret;
    }

    public int getOffsetPointer(ByteBuffer fb) {
        return fb.position() + fb.getInt();
    }

    public void parseAnimData() {
        int switcher = 0;
        switch (switcher) {
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 10:
                break;
            default:
                break;
        }
    }
}
