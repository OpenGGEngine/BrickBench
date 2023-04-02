package com.opengg.loader.game.nu2.scene.blocks;

import com.opengg.loader.game.nu2.NU2MapData;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TAS0Block extends DefaultFileBlock{
    public static String toBinary(int x, int len)
    {
        if (len > 0)
        {
            return String.format("%" + len + "s",
                    Integer.toBinaryString(x)).replaceAll(" ", "0");
        }

        return null;
    }
    @Override
    public void readFromFile(ByteBuffer fileBuffer, long blockLength, int blockID, int blockOffset, NU2MapData mapData) throws IOException {
        super.readFromFile(fileBuffer,blockLength,blockID,blockOffset,mapData);
        int what = fileBuffer.getInt();
        fileBuffer.getInt();
        for (int i = 0; i < what; i++) {
            System.out.println("Position: " + Integer.toHexString(fileBuffer.position()));
            //ptr section?
            var int1 = fileBuffer.getInt();
            //ptr section?
            var int2 = fileBuffer.getInt();
            System.out.println(Integer.toHexString(int1));
            System.out.println(Integer.toHexString(int2));
            //Offset into texture list
            var int3 = fileBuffer.getInt();
            //Number of frames
            var short1 = fileBuffer.getShort();
            var short2 = fileBuffer.getShort();
            //mat id
            var int4 = fileBuffer.getInt();
            //ptr section?
            var int5 = fileBuffer.getInt();
            System.out.println(short1+","+short2+","+int3+","+int4+","+Integer.toHexString(int5));
            var nameTableOffset1 = this.readPointer();
            System.out.println(NameTableFileBlock.CURRENT.getByOffsetFromStart(nameTableOffset1));
            var nameTableOffset2 = this.readPointer();
            System.out.println(NameTableFileBlock.CURRENT.getByOffsetFromStart(nameTableOffset2));
        }
        //Texture List
        int numTexIds = fileBuffer.getInt();
        for (int i = 0; i < numTexIds; i++) {
            int id = Short.toUnsignedInt(fileBuffer.getShort());
        }
    }
}

