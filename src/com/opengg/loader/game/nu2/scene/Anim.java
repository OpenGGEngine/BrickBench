package com.opengg.loader.game.nu2.scene;

import com.opengg.core.math.Vector3f;
import com.opengg.core.world.WorldEngine;
import com.opengg.loader.EditorEntity;
import com.opengg.loader.MapEntity;
import com.opengg.loader.Animator;
import com.opengg.loader.game.nu2.scene.SpecialObjectComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Anim implements MapEntity<Anim> {
    String name;
    String specialName;
    public int numFrames;
    public int numCurves;

    public int curveType = 0;
    public List<KeyFrame>[] frames;
    public Anim(String name, int numFrames, int numCurves){
        this.name = name;
        this.numFrames = numFrames;
        this.numCurves = numCurves;
        frames = new ArrayList[numCurves];
        for (int i = 0; i < numCurves; i++) {
            frames[i] = new ArrayList<>();
        }
    }
    public void setSpecialName(String name){
        specialName = name;
    }
    @Override
    public String name() {
        return name;
    }

    @Override
    public String path() {
        return "Render/Anim/"+name();
    }

    @Override
    public List<Property> properties() {
        List<Property> keyFramePosProps = new ArrayList<>();
        List<Property> keyFrameRotProps = new ArrayList<>();
        List<Property> keyFrameScaleProps = new ArrayList<>();
        System.out.println("SDL:"+frames[0].size()+","+numFrames);
        for (int i = 0; i <= numFrames; i++) {
            keyFramePosProps.add(new VectorProperty("Position",new Vector3f(frames[0].get(i).val,frames[1].get(i).val,frames[2].get(i).val),true,false));
            if(numCurves >=6){
                keyFrameRotProps.add(new VectorProperty("Rotate",new Vector3f(frames[3].get(i).val,frames[4].get(i).val,frames[5].get(i).val),true,false));
            }
            if(numCurves == 9)
            keyFrameScaleProps.add(new VectorProperty("Scale",new Vector3f(frames[6].get(i).val,frames[7].get(i).val,frames[8].get(i).val),true,false));
        }
        JButton play = new JButton("Play for " + specialName);
        play.addActionListener(e->{
            Animator.playAnim(this,(SpecialObjectComponent) WorldEngine.getCurrent().findByName("special_"+specialName).get(0));
        });
        return List.of(new IntegerProperty("Num Frames",numFrames,false),
                new IntegerProperty("Num Curves",numCurves,false),
                new ListProperty("KeyFrames Pos",keyFramePosProps,false),
                new ListProperty("KeyFrames Rot",keyFrameRotProps,false),
                new ListProperty("KeyFrames Scale",keyFrameScaleProps,false),
                new CustomUIProperty("Play",play,true));
    }
    public void addFrame(int curve,float val){
        frames[curve].add(new KeyFrame(val));
    }
    public static class KeyFrame{
        public float val;
        public KeyFrame(float val){
            this.val = val;
        }
    }
}
