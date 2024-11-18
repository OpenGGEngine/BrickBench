package com.opengg.loader.game.nu2.rtl;

import com.opengg.core.math.Vector3f;
import com.opengg.loader.game.nu2.NU2MapData;
import com.opengg.loader.game.nu2.rtl.RTLLight;
import com.opengg.loader.game.nu2.scene.GSCMesh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RTLLoader {
    public static void load(ByteBuffer data, NU2MapData mapData) {
        GSCMesh.lightCache.clear();

        data.order(ByteOrder.LITTLE_ENDIAN);
        int version = data.getInt();

        int lightOneCount = switch (version){
            case 0 -> 0x80;
            case 1 -> 0;
            case 2 -> 0x40;
            case 3 -> 0x40;
            default -> 0x80;
        };
        int firstPosition = data.position();
        for (int i = 0; i < lightOneCount; i++) {
            data.position(firstPosition + 0x8c * i);

            int address = data.position();
            Vector3f pos = new Vector3f(data.getFloat(),data.getFloat(),data.getFloat());
            Vector3f rot = new Vector3f(data.getFloat(),data.getFloat(),data.getFloat());
            Vector3f color = new Vector3f(data.getFloat(),data.getFloat(),data.getFloat());
            Vector3f high_color = new Vector3f(data.getFloat(),data.getFloat(),data.getFloat());
            Vector3f flickerColor = new Vector3f(data.getFloat(),data.getFloat(),data.getFloat());
   
            //MapViewer.pointsToV(u1);
            //MapViewer.textPoints.add(Tuple.of(u1.toString(),u1));
            //Large
            float radius = data.getFloat();
            float falloff = data.getFloat();

            float d1 = data.getFloat();
            float d2 = data.getFloat();
            float d3 = data.getFloat();
            float d4 = data.getFloat();

            var timer = data.getFloat();//data.getInt();

            //var type = RTLLight.LightType.getLightTypeFromId((byte) data.getShort());
            
            //LIN - Added light option :D - 18/11/2024
            short shortData = data.getShort(); // Obtiene el short
            var type = RTLLight.LightType.getLightTypeFromId((byte)shortData);
            System.out.println("Type: " + type);
            byte highByte_TOUSE = (byte) ((shortData >> 8) & 0xFF); // Byte más significativo
            System.out.println("high byte: " + highByte_TOUSE);
            var options = RTLLight.LightOption.getLightOptionFromId(highByte_TOUSE);
            System.out.println("Option: " + options);

            var d5 = data.getShort();
            var d6 = data.getShort();
            var d7 = data.getShort();

            var d8 = data.getInt();

            var d9 = data.getShort();
            var d10 = data.getShort();

            var i1 = data.getInt();
            var multiplier = data.getFloat();
            var i2 = data.getInt();

            if (type != RTLLight.LightType.INVALID) {
                System.out.println(i + " " + address + " " + type);
                mapData.rtl().lights().add(new RTLLight(pos, rot, color, high_color, flickerColor, type, options, radius, falloff, multiplier, d1, d2, d3, d4, timer, address, i));
            }
        }
        //System.out.println("---------------------------------------------");
        int num1 = switch (version){
            case 0 -> 0x20;
            case 1 -> 0;
            case 2 -> 1;
            case 3 -> 0x20;
            default -> 0x20;
        };
        int secondStart = data.position();
        for (int i = 0; i < num1; i++) {
            data.getInt();
            data.getInt();
            data.getInt();
            data.getInt();
            data.getInt();
            data.getInt();
            data.getInt();
            Vector3f u1 = new Vector3f(data.getFloat(),data.getFloat(),data.getFloat());
            //System.out.println(u1);
            data.getInt();
            data.getInt();
            data.getInt();
            data.getInt();
            data.position(secondStart+i*0x4c);
        }
       // System.out.println("Unread: " + (data.capacity() - data.position()));
    }
}
