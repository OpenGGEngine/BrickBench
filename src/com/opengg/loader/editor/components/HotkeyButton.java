package com.opengg.loader.editor.components;

import com.opengg.loader.editor.BrickbenchBindings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HotkeyButton extends JToggleButton implements KeyListener, MouseListener {
    static class HotkeyButtonModel extends JToggleButton.ToggleButtonModel {

        HotkeyButton button;

        public HotkeyButtonModel(HotkeyButton hotkeyButton){
            super();
            button = hotkeyButton;
        }

        @Override
        public void setSelected(boolean b)
        {
            super.setSelected(b);
            button.selectEvent(b);
        }
    }

    public BrickbenchBindings.Binding binding;
    public Component listenerParent;

    public HotkeyButton(BrickbenchBindings.Binding inBinding, Component keyParent){
        binding = inBinding;
        listenerParent = keyParent;

        setModel(new HotkeyButtonModel(this));
        setText(getKeyText());
    }

    public void selectEvent(boolean b){
        if(b){
            this.addKeyListener(this);
            this.addMouseListener(this);
        }else{
            this.removeKeyListener(this);
            this.removeMouseListener(this);
        }
    }

    public void setBinding(KeyStroke stroke){
        binding.keystroke = stroke;
        BrickbenchBindings.keyMap.put(binding.actionName, binding);
        if (binding.actionName.startsWith("cam_")) {
            BrickbenchBindings.reapplyCameraBindings();
        }
        setText(getKeyText());
        setSelected(false);
    }

    public String getKeyText(){
        String text = binding.keystroke.toString();
        text = text.replaceAll("typed ", "");
        text = text.replaceAll("pressed ","");

        text = switch (text) {
            case " " -> "SPACE";
            case "\t" -> "TAB";
            default -> text;
        };

        return text.toUpperCase();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.isActionKey()){
            KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
            setBinding(keyStroke);
        }
        if(!(e.getKeyCode() == KeyEvent.VK_CONTROL) && !(e.getKeyCode() == KeyEvent.VK_ALT) && !(e.getKeyCode() == KeyEvent.VK_SHIFT)){
            KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
            setBinding(keyStroke);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
