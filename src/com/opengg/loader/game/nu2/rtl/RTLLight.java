package com.opengg.loader.game.nu2.rtl;

import com.opengg.core.math.Vector3f;
import com.opengg.core.physics.collision.colliders.BoundingBox;
import com.opengg.loader.MapEntity;
import com.opengg.loader.Util;
import com.opengg.loader.components.Selectable;
import com.opengg.loader.loading.MapWriter;

import java.util.List;

public record RTLLight (
        Vector3f pos, 
        Vector3f rot, 
        Vector3f color,
        Vector3f high_color, 
        Vector3f flickerColor, 
        LightType type, 
        LightOption options, 
        float distance, 
        float falloff, 
        float multiplier,
        float d1,
        float d2,
        float d3,
        float d4,
        float timer, 
        int address, 
        int idx)  implements MapEntity<RTLLight>, Selectable {
    @Override
    public String name() {
        return "Light_" + idx;
    }

    @Override
    public String path() {
        return "Render/Lights/" + name();
    }

    @Override
    public List<Property> properties() {
        return List.of(
                new VectorProperty("Position", pos, true, true),
                new VectorProperty("Angle", rot, false, true),
                new ColorProperty("Color", color),
                new ColorProperty("High Color", high_color),
                new ColorProperty("Flicker Color", flickerColor),
                new EnumProperty("Light Type", type, true),
                new EnumProperty("Light Options", options, true),
                new FloatProperty("Distance", distance, true),
                new FloatProperty("Falloff", falloff, true),
                new FloatProperty("Multiplier", multiplier, true),
                new FloatProperty("Flicker High Time", d1, true),
                new FloatProperty("Flicker Low Time", d2, true),
                new FloatProperty("Flicker Random High Time", d3, true),
                new FloatProperty("Flicker Random Low Time", d4, true),
                new FloatProperty("Flicker Timer", timer, true)
        );
    }

    @Override
    public void applyPropertyEdit(String propName, Property newValue) {
        switch (newValue) {
            case VectorProperty vp when propName.equals("Position") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address, vp.value().toLittleEndianByteBuffer());
            case VectorProperty vp when propName.equals("Angle") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 12, vp.value().toLittleEndianByteBuffer());
            case ColorProperty cp when propName.equals("Color") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x18, cp.value().toLittleEndianByteBuffer());
            case ColorProperty cp when propName.equals("High Color") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x24, cp.value().toLittleEndianByteBuffer());
            case ColorProperty cp when propName.equals("Flicker Color") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x30, cp.value().toLittleEndianByteBuffer());
            case EnumProperty ep when propName.equals("Light Type") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x58, new byte[]{(byte) ((LightType)ep.value()).BYTE_VALUE});
            case EnumProperty ep when propName.equals("Light Options") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x59, new byte[]{(byte) ((LightOption)ep.value()).OPT_VALUE});
            case FloatProperty fp when propName.equals("Distance") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x3C, Util.littleEndian(fp.value()));
            case FloatProperty fp when propName.equals("Falloff") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x40, Util.littleEndian(fp.value()));
            case FloatProperty fp when propName.equals("Multiplier") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x6C, Util.littleEndian(fp.value()));
            case FloatProperty fp when propName.equals("Flicker High Time") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x44, Util.littleEndian(fp.value()));
            case FloatProperty fp when propName.equals("Flicker Low Time") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x48, Util.littleEndian(fp.value()));
            case FloatProperty fp when propName.equals("Flicker Random High Time") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x4C, Util.littleEndian(fp.value()));
            case FloatProperty fp when propName.equals("Flicker Random Low Time") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x50, Util.littleEndian(fp.value()));
            case FloatProperty fp when propName.equals("Flicker Timer") -> MapWriter.applyPatch(MapWriter.WritableObject.LIGHTS, address + 0x54, Util.littleEndian(fp.value()));
            default -> {}
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(pos.subtract(0.07f), pos.add(0.07f));
    }

    public enum LightType {
        INVALID((byte)0),
        AMBIENT((byte)1),
        POINT((byte)2),
        POINTFLICKER((byte)3),
        DIRECTIONAL((byte)4),
        CAMDIR((byte)5),
        POINTBLEND((byte)6),
        ANTILIGHT((byte)7),
        JONFLICKER((byte)8),
        CNT((byte)9);

        public final int BYTE_VALUE;

        LightType(byte val) {
            BYTE_VALUE = val;
        }

        public static LightType getLightTypeFromId(byte id) {
            return switch (id) {
                case 0 -> INVALID;
                case 1 -> AMBIENT;
                case 2 -> POINT;
                case 3 -> POINTFLICKER;
                case 4 -> DIRECTIONAL;
                case 5 -> CAMDIR;
                case 6 -> POINTBLEND;
                case 7 -> ANTILIGHT;
                case 8 -> JONFLICKER;
                case 9 -> CNT;
                default -> throw new IllegalArgumentException("Unknown.LIGHTS light " + id);
            };
        }
    }
    
    public enum LightOption {
        DISABLE_FLAG((byte)0),
        CAST_SHADOW((byte)1),
        HAS_SPECULAR((byte)2),
        UNKNOWN((byte)3), //left in the case we find out unknown light options
        UNK_CLOUDCITY((byte)32);

        public final int OPT_VALUE;

        LightOption(byte option) {
            OPT_VALUE = option;
        }

        public static LightOption getLightOptionFromId(byte id_option) {
            LightOption lightOption = switch (id_option) {
                case 0 -> DISABLE_FLAG;
                case 1 -> CAST_SHADOW;
                case 2 -> HAS_SPECULAR;
                //default -> throw new IllegalArgumentException("Unknown.LIGHTS option " + id_option);
                default -> UNKNOWN;
            };

            if (lightOption == UNKNOWN) {
                System.out.println("LIN DEBUG - Found Undocumented Light Option with id: " + id_option);
            }

            return lightOption;
        }
    }
}
