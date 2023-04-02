package com.opengg.loader.game.nu2.scene.commands;

import java.util.List;

public class DynamicCommandResource implements DisplayCommandResource<DynamicCommandResource>{
    private int address;
    public DynamicCommandResource(int address){
        this.address = address;
    }
    @Override
    public String name() {
        return "Dynamic " + Integer.toHexString(address);
    }

    @Override
    public String path() {
        return null;
    }

    @Override
    public List<Property> properties() {
        return null;
    }

    @Override
    public void run() {

    }

    @Override
    public int getAddress() {
        return this.address;
    }

    @Override
    public DisplayCommand.CommandType getType() {
        return DisplayCommand.CommandType.DYNAMIC_GEOMETRY;
    }
}
