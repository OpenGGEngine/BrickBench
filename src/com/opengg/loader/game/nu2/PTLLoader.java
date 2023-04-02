package com.opengg.loader.game.nu2;

import com.opengg.core.console.GGConsole;
import com.opengg.loader.Util;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PTLLoader {
    public static void load(ByteBuffer fileData, NU2MapData mapData){
        fileData.order(ByteOrder.LITTLE_ENDIAN);
        int version = fileData.getInt();
        int param2 = 0;
        if(version > 4 && version < 0x2b){
            int numTypes = fileData.getInt();
            System.out.println("Loading " + numTypes + " effect types");
            for (int i = 0; i < numTypes; i++) {
                loadSingleEffectType(fileData, version);
            }
        }
        /*int numOBJ2 = fileData.getInt();
        for (int i = 0; i < numOBJ2; i++) {
            fileData.getFloat();
            fileData.getFloat();
            fileData.getFloat();
        }*/
    }
    public static void loadSingleEffectType(ByteBuffer fileData, int version){
        String typeName = StringUtils.trimToNull(Util.getStringFromBuffer(fileData,0x10));
        System.out.println("Loading effect " + typeName + " " + Integer.toHexString(fileData.position()-0x10));
        fileData.getShort();
        fileData.getShort();
        if(version < 0x1c){
            fileData.getShort(); // /60.0
            fileData.getShort(); // /60.0
            fileData.getShort(); // /60.0
            fileData.getShort(); // /60.0
            fileData.getShort(); // /60.0
        }else{
            //particle duration params maybe?
            float f1 = fileData.getFloat();
            float f2 = fileData.getFloat();
            float f3 = fileData.getFloat();
            float f4 = fileData.getFloat();
            float f5 = fileData.getFloat();
            System.out.println(f1+","+f2+","+f3+","+f4+","+f5);
        }
        fileData.get();
        fileData.get();
        if(version < 0x23){
            //set 0
        }else{
            fileData.get();
        }
        fileData.get();
        if(version < 0x27){
            //Padding
            fileData.get();
        }
        if(version <  0x28){

        }else{
            fileData.get();
        }
        fileData.getFloat();

        if(version < 6){
        }else{
            fileData.getFloat();
            fileData.getFloat();
        }

        if(version < 10){

        }else{
            fileData.getFloat();
        }

        if(version < 0x17){

        }else{
            fileData.getFloat();
        }

        if(version < 0x18){

        }else{
            fileData.getFloat();
        }

        if(version < 7){
            //Both Padding
            fileData.getInt();
            fileData.getInt();
        }

        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();

        if(version < 0x12){
            //All Padding
            fileData.getFloat();
            fileData.getFloat();
            fileData.getFloat();
        }

        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();

        if(version < 0x12){
            //All Padding
            fileData.getFloat();
            fileData.getFloat();
            fileData.getFloat();
        }

        if(version < 0x1d){

        }else{
            fileData.getFloat();
            fileData.getFloat();
            fileData.getFloat();
        }
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getShort();
        fileData.get();
        fileData.get();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();

        if(version < 0x21){
            for (int i = 0; i < 8; i++) {
                fileData.getFloat();
                fileData.getFloat();
                fileData.getFloat();
                fileData.getFloat();
            }
        }else{
            for (int i = 0; i < 8; i++) {
                fileData.getFloat();
                //Unsigned chars
                fileData.get();
                fileData.get();
                fileData.get();
                fileData.get();
            }
        }

        for (int i = 0; i < 8; i++) {
            fileData.getFloat();
            fileData.getFloat();
        }

        if(version < 0x15){
            //set the next two reads to 0.125
        }else{
            fileData.getFloat();
            fileData.getFloat();
        }

        fileData.getFloat();
        fileData.getFloat();

        for (int i = 0; i < 8; i++) {
            fileData.getFloat();
            fileData.getFloat();
        }

        for (int i = 0; i < 8; i++) {
            fileData.getFloat();
            fileData.getFloat();
        }

        fileData.getFloat();
        fileData.getFloat();

        for (int i = 0; i < 8; i++) {
            fileData.getFloat();
            fileData.getFloat();
        }
        for (int i = 0; i < 8; i++) {
            fileData.getFloat();
            fileData.getFloat();
        }
        for (int i = 0; i < 8; i++) {
            fileData.getFloat();
            fileData.getFloat();
        }

        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();
        fileData.getFloat();

        if(version < 3){

        }else{
            for (int i = 0; i < 8; i++) {
                fileData.getFloat();
                fileData.getFloat();
            }
            fileData.get();
        }

        if(version < 0x11){

        }else{
            fileData.get();
        }

        if(version < 0x1f){

        }else{
            fileData.get();
        }

        if(version < 0x20){

        }else{
            fileData.get();
            fileData.getFloat();
            fileData.getFloat();
            fileData.getFloat();
        }

        if(version <0x1e){

        }else{
            if(version >= 0x24){
                fileData.getFloat();
            }
            //section a
            fileData.getFloat();
            fileData.getFloat();
            fileData.getFloat();

            for (int i = 0; i < 8; i++) {
                fileData.getFloat();
                fileData.getFloat();
            }

            for (int i = 0; i < 8; i++) {
                fileData.getFloat();
                fileData.getFloat();
            }

            for (int i = 0; i < 8; i++) {
                fileData.getFloat();
                fileData.getFloat();
            }
        }

        if(version < 0x17){
            for (int i = 0; i < 4; i++) {
                if(version < 0xb){

                }else{
                    fileData.getInt();
                    fileData.getInt();
                    fileData.getInt();
                }
            }
        }else{
            int numSFX = fileData.getInt();
            for (int i = 0; i < numSFX; i++) {
                String sfxName = Util.getStringFromBuffer(fileData,0x10);
                System.out.println("Loading SFX "+ sfxName);
                fileData.getInt();
                fileData.getInt();
            }
        }

        if (version < 0x10){

        }else{
            if(version < 0x29){
                fileData.getInt();
            }else{
                fileData.get();
            }
            fileData.getFloat();
        }

        if(version < 0x19){

        }else{
            if(version < 0x29){
                fileData.getInt();
            }else{
                fileData.get();
            }
            fileData.getFloat();
        }

        if(0x19 < version){
            fileData.getFloat();
        }
    }
}
