package com.opengg.loader.editor;

import com.github.kwhat.jnativehook.NativeInputEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.opengg.core.console.GGConsole;
import com.opengg.core.io.input.keyboard.Key;
import com.opengg.core.io.input.keyboard.KeyboardController;
import com.opengg.core.math.util.Tuple;
import com.opengg.loader.editor.components.HotkeyButton;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.event.KeyEvent;

import static com.opengg.core.io.input.keyboard.Key.*;

public class BrickbenchBindings {

    public static class Binding {
        public KeyStroke keystroke;
        public String displayName;
        public String actionName;

        public Binding(String inActionName, String inDisplayName, KeyStroke inKeyStroke){
            keystroke = inKeyStroke;
            actionName = inActionName;
            displayName = inDisplayName;
        }
    }

    public static Map<String, Binding> keyMap = new LinkedHashMap<>();

    public static void load(String path){

        // Camera movement bindings (shown in Controls panel)
        keyMap.put("cam_forward", new Binding("cam_forward","Camera Forward", KeyStroke.getKeyStroke(KeyEvent.VK_W, 0)));
        keyMap.put("cam_backward", new Binding("cam_backward","Camera Backward", KeyStroke.getKeyStroke(KeyEvent.VK_S, 0)));
        keyMap.put("cam_left", new Binding("cam_left","Camera Left", KeyStroke.getKeyStroke(KeyEvent.VK_A, 0)));
        keyMap.put("cam_right", new Binding("cam_right","Camera Right", KeyStroke.getKeyStroke(KeyEvent.VK_D, 0)));
        keyMap.put("cam_up", new Binding("cam_up","Camera Up", KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0)));
        keyMap.put("cam_down", new Binding("cam_down","Camera Down", KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0)));

        // Hook bindings (shown in Key Bindings panel)
        keyMap.put("teleport1", new Binding("teleport1","Teleport Player 1", KeyStroke.getKeyStroke(KeyEvent.VK_T,0)));
        keyMap.put("teleport2", new Binding("teleport2","Teleport Player 2", KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK | KeyEvent.CTRL_DOWN_MASK)));
        keyMap.put("loadMap1", new Binding("loadMap1","Load Map", KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0)));
        keyMap.put("loadMap2", new Binding("loadMap2","Load Map Alternate Bind", KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SLASH, 0)));
        keyMap.put("speedHackOn", new Binding("speedHackOn","Toggle Speed Hack", KeyStroke.getKeyStroke(KeyEvent.VK_F9,0)));
        keyMap.put("p1SetData", new Binding("p1SetData", "Set Player 1 Properties", KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET,0)));
        keyMap.put("p2SetData", new Binding("p2SetData", "Set Player 2 Properties", KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET,0)));

        try(BufferedReader br = new BufferedReader( new FileReader( path ))) {
            String line;
            while(( line = br.readLine()) != null ) {
                int splitLine = line.indexOf("=");
                if(splitLine >= 0){
                    System.out.println(line);
                    String actionName = line.substring(0,splitLine).strip();
                    if(BrickbenchBindings.keyMap.containsKey(actionName)){
                        keyMap.get(actionName).keystroke = KeyStroke.getKeyStroke(line.substring(splitLine+1));
                        System.out.println(keyMap.get(actionName).keystroke.toString());
                    }
                }
            }
        } catch (FileNotFoundException e){
            GGConsole.log("No Binds File Found. Initializing Default Binds.");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static final java.util.Map<String, String> CAM_ACTION_MAP = java.util.Map.of(
            "cam_forward","forward", "cam_backward","backward",
            "cam_left","left", "cam_right","right",
            "cam_up","up", "cam_down","down");

    public static void reapplyCameraBindings() {
        for (var entry : CAM_ACTION_MAP.entrySet()) {
            var bind = getOpenGGKeycode(entry.getKey());
            if (bind != null) {
                com.opengg.core.engine.BindController.addBind(
                        new com.opengg.core.io.Bind(com.opengg.core.io.ControlType.KEYBOARD, entry.getValue(), bind.x()));
            }
        }
    }

    public static void save(String path){
        try(BufferedWriter writer = new BufferedWriter( new FileWriter( path ))) {
            for(Map.Entry<String, BrickbenchBindings.Binding> entry : BrickbenchBindings.keyMap.entrySet()){
                writer.write(entry.getValue().actionName + " = " + entry.getValue().keystroke.toString());
                writer.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Tuple<Integer,Integer> getOpenGGKeycode(String action){
        if(keyMap.containsKey(action)) {
            KeyStroke stroke = keyMap.get(action).keystroke;

            int keyCode = Key.KEY_A;
            // End Sun keyboards
            keyCode = switch (stroke.getKeyCode()) {
                case KeyEvent.VK_ESCAPE -> Key.KEY_ESCAPE;

                // Begin Function Keys
                case KeyEvent.VK_F1 -> Key.KEY_F1;
                case KeyEvent.VK_F2 -> Key.KEY_F2;
                case KeyEvent.VK_F3 -> Key.KEY_F3;
                case KeyEvent.VK_F4 -> Key.KEY_F4;
                case KeyEvent.VK_F5 -> Key.KEY_F5;
                case KeyEvent.VK_F6 -> Key.KEY_F6;
                case KeyEvent.VK_F7 -> Key.KEY_F7;
                case KeyEvent.VK_F8 -> Key.KEY_F8;
                case KeyEvent.VK_F9 -> Key.KEY_F9;
                case KeyEvent.VK_F10 -> Key.KEY_F10;
                case KeyEvent.VK_F11 -> Key.KEY_F11;
                case KeyEvent.VK_F12 -> Key.KEY_F12;
                case KeyEvent.VK_F13 -> Key.KEY_F13;
                case KeyEvent.VK_F14 -> Key.KEY_F14;
                case KeyEvent.VK_F15 -> Key.KEY_F15;
                case KeyEvent.VK_F16 -> Key.KEY_F16;
                case KeyEvent.VK_F17 -> Key.KEY_F17;
                case KeyEvent.VK_F18 -> Key.KEY_F18;
                case KeyEvent.VK_F19 -> Key.KEY_F19;
                case KeyEvent.VK_F20 -> Key.KEY_F20;
                case KeyEvent.VK_F21 -> Key.KEY_F21;
                case KeyEvent.VK_F22 -> Key.KEY_F22;
                case KeyEvent.VK_F23 -> Key.KEY_F23;
                case KeyEvent.VK_F24 -> Key.KEY_F24;
                // End Function Keys

                // Begin Alphanumeric Zone
                case KeyEvent.VK_BACK_QUOTE -> NativeKeyEvent.VC_BACKQUOTE; //Nope
                case KeyEvent.VK_1 -> Key.KEY_1;
                case KeyEvent.VK_2 -> Key.KEY_2;
                case KeyEvent.VK_3 -> Key.KEY_3;
                case KeyEvent.VK_4 -> Key.KEY_4;
                case KeyEvent.VK_5 -> Key.KEY_5;
                case KeyEvent.VK_6 -> Key.KEY_6;
                case KeyEvent.VK_7 -> Key.KEY_7;
                case KeyEvent.VK_8 -> Key.KEY_8;
                case KeyEvent.VK_9 -> Key.KEY_9;
                case KeyEvent.VK_0 -> Key.KEY_0;
                case KeyEvent.VK_MINUS -> Key.KEY_MINUS;
                case KeyEvent.VK_EQUALS -> Key.KEY_EQUAL;
                case KeyEvent.VK_BACK_SPACE -> Key.KEY_BACKSPACE;
                case KeyEvent.VK_TAB -> Key.KEY_TAB;
                case KeyEvent.VK_CAPS_LOCK -> Key.KEY_CAPS_LOCK;
                case KeyEvent.VK_A -> Key.KEY_A;
                case KeyEvent.VK_B -> Key.KEY_B;
                case KeyEvent.VK_C -> Key.KEY_C;
                case KeyEvent.VK_D -> Key.KEY_D;
                case KeyEvent.VK_E -> Key.KEY_E;
                case KeyEvent.VK_F -> Key.KEY_F;
                case KeyEvent.VK_G -> Key.KEY_G;
                case KeyEvent.VK_H -> Key.KEY_H;
                case KeyEvent.VK_I -> Key.KEY_I;
                case KeyEvent.VK_J -> Key.KEY_J;
                case KeyEvent.VK_K -> Key.KEY_K;
                case KeyEvent.VK_L -> Key.KEY_L;
                case KeyEvent.VK_M -> Key.KEY_M;
                case KeyEvent.VK_N -> Key.KEY_N;
                case KeyEvent.VK_O -> Key.KEY_O;
                case KeyEvent.VK_P -> Key.KEY_P;
                case KeyEvent.VK_Q -> Key.KEY_Q;
                case KeyEvent.VK_R -> Key.KEY_R;
                case KeyEvent.VK_S -> Key.KEY_S;
                case KeyEvent.VK_T -> Key.KEY_T;
                case KeyEvent.VK_U -> Key.KEY_U;
                case KeyEvent.VK_V -> Key.KEY_V;
                case KeyEvent.VK_W -> Key.KEY_W;
                case KeyEvent.VK_X -> Key.KEY_X;
                case KeyEvent.VK_Y -> Key.KEY_Y;
                case KeyEvent.VK_Z -> Key.KEY_Z;
                case KeyEvent.VK_OPEN_BRACKET -> Key.KEY_LEFT_BRACKET;
                case KeyEvent.VK_CLOSE_BRACKET -> Key.KEY_RIGHT_BRACKET;
                case KeyEvent.VK_BACK_SLASH -> Key.KEY_BACKSLASH;
                case KeyEvent.VK_SEMICOLON -> Key.KEY_SEMICOLON;
                case KeyEvent.VK_QUOTE -> Key.KEY_APOSTROPHE;
                case KeyEvent.VK_ENTER -> Key.KEY_ENTER;
                case KeyEvent.VK_COMMA -> Key.KEY_COMMA;
                case KeyEvent.VK_PERIOD -> Key.KEY_PERIOD;
                case KeyEvent.VK_SLASH -> Key.KEY_SLASH;
                case KeyEvent.VK_SPACE -> Key.KEY_SPACE;
                // End Alphanumeric Zone

                case KeyEvent.VK_PRINTSCREEN -> Key.KEY_PRINT_SCREEN;
                case KeyEvent.VK_SCROLL_LOCK -> Key.KEY_SCROLL_LOCK;
                case KeyEvent.VK_PAUSE -> Key.KEY_PAUSE;

                // Begin Edit Key Zone
                case KeyEvent.VK_INSERT -> Key.KEY_INSERT;
                case KeyEvent.VK_DELETE -> Key.KEY_DELETE;
                case KeyEvent.VK_HOME -> Key.KEY_HOME;
                case KeyEvent.VK_END -> Key.KEY_END;
                case KeyEvent.VK_PAGE_UP -> Key.KEY_PAGE_UP;
                case KeyEvent.VK_PAGE_DOWN -> Key.KEY_PAGE_DOWN;
                // End Edit Key Zone

                // Begin Cursor Key Zone
                case KeyEvent.VK_UP -> Key.KEY_UP;
                case KeyEvent.VK_LEFT -> Key.KEY_LEFT;
                case KeyEvent.VK_CLEAR -> NativeKeyEvent.VC_CLEAR; //gfgfgfg
                case KeyEvent.VK_RIGHT -> Key.KEY_RIGHT;
                case KeyEvent.VK_DOWN -> Key.KEY_DOWN;
                // End Cursor Key Zone

                // Begin Numeric Zone
                case KeyEvent.VK_NUM_LOCK -> Key.KEY_NUM_LOCK;
                case KeyEvent.VK_SEPARATOR -> NativeKeyEvent.VC_SEPARATOR;
                // End Numeric Zone

                // Begin Modifier and Control Keys
                case KeyEvent.VK_SHIFT -> NativeKeyEvent.VC_SHIFT;
                case KeyEvent.VK_CONTROL -> NativeKeyEvent.VC_CONTROL;
                case KeyEvent.VK_ALT -> NativeKeyEvent.VC_ALT;
                case KeyEvent.VK_META -> NativeKeyEvent.VC_META;
                case KeyEvent.VK_CONTEXT_MENU -> NativeKeyEvent.VC_CONTEXT_MENU;
                // End Modifier and Control Keys


			/* Begin Media Control Keys
			case NativeKeyEvent.VC_POWER:
			case NativeKeyEvent.VC_SLEEP:
			case NativeKeyEvent.VC_WAKE:

			case NativeKeyEvent.VC_MEDIA_PLAY:
			case NativeKeyEvent.VC_MEDIA_STOP:
			case NativeKeyEvent.VC_MEDIA_PREVIOUS:
			case NativeKeyEvent.VC_MEDIA_NEXT:
			case NativeKeyEvent.VC_MEDIA_SELECT:
			case NativeKeyEvent.VC_MEDIA_EJECT:

			case NativeKeyEvent.VC_VOLUME_MUTE:
			case NativeKeyEvent.VC_VOLUME_UP:
			case NativeKeyEvent.VC_VOLUME_DOWN:

			case NativeKeyEvent.VC_APP_MAIL:
			case NativeKeyEvent.VC_APP_CALCULATOR:
			case NativeKeyEvent.VC_APP_MUSIC:
			case NativeKeyEvent.VC_APP_PICTURES:

			case NativeKeyEvent.VC_BROWSER_SEARCH:
			case NativeKeyEvent.VC_BROWSER_HOME:
			case NativeKeyEvent.VC_BROWSER_BACK:
			case NativeKeyEvent.VC_BROWSER_FORWARD:
			case NativeKeyEvent.VC_BROWSER_STOP:
			case NativeKeyEvent.VC_BROWSER_REFRESH:
			case NativeKeyEvent.VC_BROWSER_FAVORITES:
			// End Media Control Keys */

                // Begin Japanese Language Keys
                case KeyEvent.VK_KATAKANA -> NativeKeyEvent.VC_KATAKANA;
                case KeyEvent.VK_UNDERSCORE -> NativeKeyEvent.VC_UNDERSCORE;

                //case VC_FURIGANA:

                case KeyEvent.VK_KANJI -> NativeKeyEvent.VC_KANJI;
                case KeyEvent.VK_HIRAGANA -> NativeKeyEvent.VC_HIRAGANA;

                //case VC_YEN:
                // End Japanese Language Keys

                // Begin Sun keyboards
                case KeyEvent.VK_HELP -> NativeKeyEvent.VC_SUN_HELP;
                case KeyEvent.VK_STOP -> NativeKeyEvent.VC_SUN_STOP;

                //case VC_SUN_FRONT:

                //case VC_SUN_OPEN:

                case KeyEvent.VK_PROPS -> NativeKeyEvent.VC_SUN_PROPS;
                case KeyEvent.VK_FIND -> NativeKeyEvent.VC_SUN_FIND;
                case KeyEvent.VK_AGAIN -> NativeKeyEvent.VC_SUN_AGAIN;

                //case NativeKeyEvent.VC_SUN_INSERT:

                case KeyEvent.VK_COPY -> NativeKeyEvent.VC_SUN_COPY;
                case KeyEvent.VK_CUT -> NativeKeyEvent.VC_SUN_CUT;
                default -> keyCode;
            };

            return Tuple.of(keyCode, stroke.getModifiers());
        }
        return null;
    }

    public static boolean isNativeBindPressed(NativeKeyEvent nativeEvent, String action){
        NativeKeyEvent event = getNativeKeyEvent(action);

        if(event != null){
            boolean modifiersGood = true;

            int bindModifiers = event.getModifiers();
            int modifier = nativeEvent.getModifiers();

            modifiersGood &= ((bindModifiers & NativeKeyEvent.CTRL_MASK) != 0) == ((modifier & NativeKeyEvent.CTRL_MASK) != 0);
            modifiersGood &= ((bindModifiers & NativeKeyEvent.ALT_MASK) != 0) == ((modifier & NativeKeyEvent.ALT_MASK) != 0);
            modifiersGood &= ((bindModifiers & NativeKeyEvent.SHIFT_MASK) != 0) == ((modifier & NativeKeyEvent.SHIFT_MASK) != 0);

            return (nativeEvent.getKeyCode() == event.getKeyCode()) && modifiersGood;
        }
        return false;
    }

    public static boolean isOpenGGBindPressed(int key, String action){
        Tuple<Integer, Integer> bind = getOpenGGKeycode(action);
        if(bind != null){
            int modifier = bind.y();

            boolean modifiersGood = true;

            modifiersGood &= (((modifier & KeyEvent.CTRL_DOWN_MASK) != 0 || (modifier & KeyEvent.CTRL_MASK) != 0)
                    == (KeyboardController.isKeyPressed(KEY_LEFT_CONTROL) || KeyboardController.isKeyPressed(KEY_RIGHT_CONTROL)));

            modifiersGood &= ((modifier & KeyEvent.ALT_MASK) != 0 || (modifier & KeyEvent.ALT_DOWN_MASK) != 0)
                    == (KeyboardController.isKeyPressed(KEY_LEFT_ALT) || KeyboardController.isKeyPressed(KEY_RIGHT_ALT));

            modifiersGood &= ((modifier & KeyEvent.SHIFT_MASK) != 0 || (modifier & KeyEvent.SHIFT_DOWN_MASK) != 0)
                    == (KeyboardController.isKeyPressed(KEY_LEFT_SHIFT) || KeyboardController.isKeyPressed(KEY_RIGHT_SHIFT));

            return (bind.x() == key) && modifiersGood;
        }
        return false;
    }

    public static NativeKeyEvent getNativeKeyEvent(String action){
        if(keyMap.containsKey(action)) {
            KeyStroke stroke = keyMap.get(action).keystroke;

            int keyCode = NativeKeyEvent.CHAR_UNDEFINED;
            // End Sun keyboards
            keyCode = switch (stroke.getKeyCode()) {
                case KeyEvent.VK_ESCAPE -> NativeKeyEvent.VC_ESCAPE;

                // Begin Function Keys
                case KeyEvent.VK_F1 -> NativeKeyEvent.VC_F1;
                case KeyEvent.VK_F2 -> NativeKeyEvent.VC_F2;
                case KeyEvent.VK_F3 -> NativeKeyEvent.VC_F3;
                case KeyEvent.VK_F4 -> NativeKeyEvent.VC_F4;
                case KeyEvent.VK_F5 -> NativeKeyEvent.VC_F5;
                case KeyEvent.VK_F6 -> NativeKeyEvent.VC_F6;
                case KeyEvent.VK_F7 -> NativeKeyEvent.VC_F7;
                case KeyEvent.VK_F8 -> NativeKeyEvent.VC_F8;
                case KeyEvent.VK_F9 -> NativeKeyEvent.VC_F9;
                case KeyEvent.VK_F10 -> NativeKeyEvent.VC_F10;
                case KeyEvent.VK_F11 -> NativeKeyEvent.VC_F11;
                case KeyEvent.VK_F12 -> NativeKeyEvent.VC_F12;
                case KeyEvent.VK_F13 -> NativeKeyEvent.VC_F13;
                case KeyEvent.VK_F14 -> NativeKeyEvent.VC_F14;
                case KeyEvent.VK_F15 -> NativeKeyEvent.VC_F15;
                case KeyEvent.VK_F16 -> NativeKeyEvent.VC_F16;
                case KeyEvent.VK_F17 -> NativeKeyEvent.VC_F17;
                case KeyEvent.VK_F18 -> NativeKeyEvent.VC_F18;
                case KeyEvent.VK_F19 -> NativeKeyEvent.VC_F19;
                case KeyEvent.VK_F20 -> NativeKeyEvent.VC_F20;
                case KeyEvent.VK_F21 -> NativeKeyEvent.VC_F21;
                case KeyEvent.VK_F22 -> NativeKeyEvent.VC_F22;
                case KeyEvent.VK_F23 -> NativeKeyEvent.VC_F23;
                case KeyEvent.VK_F24 -> NativeKeyEvent.VC_F24;
                // End Function Keys

                // Begin Alphanumeric Zone
                case KeyEvent.VK_BACK_QUOTE -> NativeKeyEvent.VC_BACKQUOTE;
                case KeyEvent.VK_1 -> NativeKeyEvent.VC_1;
                case KeyEvent.VK_2 -> NativeKeyEvent.VC_2;
                case KeyEvent.VK_3 -> NativeKeyEvent.VC_3;
                case KeyEvent.VK_4 -> NativeKeyEvent.VC_4;
                case KeyEvent.VK_5 -> NativeKeyEvent.VC_5;
                case KeyEvent.VK_6 -> NativeKeyEvent.VC_6;
                case KeyEvent.VK_7 -> NativeKeyEvent.VC_7;
                case KeyEvent.VK_8 -> NativeKeyEvent.VC_8;
                case KeyEvent.VK_9 -> NativeKeyEvent.VC_9;
                case KeyEvent.VK_0 -> NativeKeyEvent.VC_0;
                case KeyEvent.VK_MINUS -> NativeKeyEvent.VC_MINUS;
                case KeyEvent.VK_EQUALS -> NativeKeyEvent.VC_EQUALS;
                case KeyEvent.VK_BACK_SPACE -> NativeKeyEvent.VC_BACKSPACE;
                case KeyEvent.VK_TAB -> NativeKeyEvent.VC_TAB;
                case KeyEvent.VK_CAPS_LOCK -> NativeKeyEvent.VC_CAPS_LOCK;
                case KeyEvent.VK_A -> NativeKeyEvent.VC_A;
                case KeyEvent.VK_B -> NativeKeyEvent.VC_B;
                case KeyEvent.VK_C -> NativeKeyEvent.VC_C;
                case KeyEvent.VK_D -> NativeKeyEvent.VC_D;
                case KeyEvent.VK_E -> NativeKeyEvent.VC_E;
                case KeyEvent.VK_F -> NativeKeyEvent.VC_F;
                case KeyEvent.VK_G -> NativeKeyEvent.VC_G;
                case KeyEvent.VK_H -> NativeKeyEvent.VC_H;
                case KeyEvent.VK_I -> NativeKeyEvent.VC_I;
                case KeyEvent.VK_J -> NativeKeyEvent.VC_J;
                case KeyEvent.VK_K -> NativeKeyEvent.VC_K;
                case KeyEvent.VK_L -> NativeKeyEvent.VC_L;
                case KeyEvent.VK_M -> NativeKeyEvent.VC_M;
                case KeyEvent.VK_N -> NativeKeyEvent.VC_N;
                case KeyEvent.VK_O -> NativeKeyEvent.VC_O;
                case KeyEvent.VK_P -> NativeKeyEvent.VC_P;
                case KeyEvent.VK_Q -> NativeKeyEvent.VC_Q;
                case KeyEvent.VK_R -> NativeKeyEvent.VC_R;
                case KeyEvent.VK_S -> NativeKeyEvent.VC_S;
                case KeyEvent.VK_T -> NativeKeyEvent.VC_T;
                case KeyEvent.VK_U -> NativeKeyEvent.VC_U;
                case KeyEvent.VK_V -> NativeKeyEvent.VC_V;
                case KeyEvent.VK_W -> NativeKeyEvent.VC_W;
                case KeyEvent.VK_X -> NativeKeyEvent.VC_X;
                case KeyEvent.VK_Y -> NativeKeyEvent.VC_Y;
                case KeyEvent.VK_Z -> NativeKeyEvent.VC_Z;
                case KeyEvent.VK_OPEN_BRACKET -> NativeKeyEvent.VC_OPEN_BRACKET;
                case KeyEvent.VK_CLOSE_BRACKET -> NativeKeyEvent.VC_CLOSE_BRACKET;
                case KeyEvent.VK_BACK_SLASH -> NativeKeyEvent.VC_BACK_SLASH;
                case KeyEvent.VK_SEMICOLON -> NativeKeyEvent.VC_SEMICOLON;
                case KeyEvent.VK_QUOTE -> NativeKeyEvent.VC_QUOTE;
                case KeyEvent.VK_ENTER -> NativeKeyEvent.VC_ENTER;
                case KeyEvent.VK_COMMA -> NativeKeyEvent.VC_COMMA;
                case KeyEvent.VK_PERIOD -> NativeKeyEvent.VC_PERIOD;
                case KeyEvent.VK_SLASH -> NativeKeyEvent.VC_SLASH;
                case KeyEvent.VK_SPACE -> NativeKeyEvent.VC_SPACE;
                // End Alphanumeric Zone

                case KeyEvent.VK_PRINTSCREEN -> NativeKeyEvent.VC_PRINTSCREEN;
                case KeyEvent.VK_SCROLL_LOCK -> NativeKeyEvent.VC_SCROLL_LOCK;
                case KeyEvent.VK_PAUSE -> NativeKeyEvent.VC_PAUSE;

                // Begin Edit Key Zone
                case KeyEvent.VK_INSERT -> NativeKeyEvent.VC_INSERT;
                case KeyEvent.VK_DELETE -> NativeKeyEvent.VC_DELETE;
                case KeyEvent.VK_HOME -> NativeKeyEvent.VC_HOME;
                case KeyEvent.VK_END -> NativeKeyEvent.VC_END;
                case KeyEvent.VK_PAGE_UP -> NativeKeyEvent.VC_PAGE_UP;
                case KeyEvent.VK_PAGE_DOWN -> NativeKeyEvent.VC_PAGE_DOWN;
                // End Edit Key Zone

                // Begin Cursor Key Zone
                case KeyEvent.VK_UP -> NativeKeyEvent.VC_UP;
                case KeyEvent.VK_LEFT -> NativeKeyEvent.VC_LEFT;
                case KeyEvent.VK_CLEAR -> NativeKeyEvent.VC_CLEAR;
                case KeyEvent.VK_RIGHT -> NativeKeyEvent.VC_RIGHT;
                case KeyEvent.VK_DOWN -> NativeKeyEvent.VC_DOWN;
                // End Cursor Key Zone

                // Begin Numeric Zone
                case KeyEvent.VK_NUM_LOCK -> NativeKeyEvent.VC_NUM_LOCK;
                case KeyEvent.VK_SEPARATOR -> NativeKeyEvent.VC_SEPARATOR;
                // End Numeric Zone

                // Begin Modifier and Control Keys
                case KeyEvent.VK_SHIFT -> NativeKeyEvent.VC_SHIFT;
                case KeyEvent.VK_CONTROL -> NativeKeyEvent.VC_CONTROL;
                case KeyEvent.VK_ALT -> NativeKeyEvent.VC_ALT;
                case KeyEvent.VK_META -> NativeKeyEvent.VC_META;
                case KeyEvent.VK_CONTEXT_MENU -> NativeKeyEvent.VC_CONTEXT_MENU;
                // End Modifier and Control Keys


			/* Begin Media Control Keys
			case NativeKeyEvent.VC_POWER:
			case NativeKeyEvent.VC_SLEEP:
			case NativeKeyEvent.VC_WAKE:

			case NativeKeyEvent.VC_MEDIA_PLAY:
			case NativeKeyEvent.VC_MEDIA_STOP:
			case NativeKeyEvent.VC_MEDIA_PREVIOUS:
			case NativeKeyEvent.VC_MEDIA_NEXT:
			case NativeKeyEvent.VC_MEDIA_SELECT:
			case NativeKeyEvent.VC_MEDIA_EJECT:

			case NativeKeyEvent.VC_VOLUME_MUTE:
			case NativeKeyEvent.VC_VOLUME_UP:
			case NativeKeyEvent.VC_VOLUME_DOWN:

			case NativeKeyEvent.VC_APP_MAIL:
			case NativeKeyEvent.VC_APP_CALCULATOR:
			case NativeKeyEvent.VC_APP_MUSIC:
			case NativeKeyEvent.VC_APP_PICTURES:

			case NativeKeyEvent.VC_BROWSER_SEARCH:
			case NativeKeyEvent.VC_BROWSER_HOME:
			case NativeKeyEvent.VC_BROWSER_BACK:
			case NativeKeyEvent.VC_BROWSER_FORWARD:
			case NativeKeyEvent.VC_BROWSER_STOP:
			case NativeKeyEvent.VC_BROWSER_REFRESH:
			case NativeKeyEvent.VC_BROWSER_FAVORITES:
			// End Media Control Keys */

                // Begin Japanese Language Keys
                case KeyEvent.VK_KATAKANA -> NativeKeyEvent.VC_KATAKANA;
                case KeyEvent.VK_UNDERSCORE -> NativeKeyEvent.VC_UNDERSCORE;

                //case VC_FURIGANA:

                case KeyEvent.VK_KANJI -> NativeKeyEvent.VC_KANJI;
                case KeyEvent.VK_HIRAGANA -> NativeKeyEvent.VC_HIRAGANA;

                //case VC_YEN:
                // End Japanese Language Keys

                // Begin Sun keyboards
                case KeyEvent.VK_HELP -> NativeKeyEvent.VC_SUN_HELP;
                case KeyEvent.VK_STOP -> NativeKeyEvent.VC_SUN_STOP;

                //case VC_SUN_FRONT:

                //case VC_SUN_OPEN:

                case KeyEvent.VK_PROPS -> NativeKeyEvent.VC_SUN_PROPS;
                case KeyEvent.VK_FIND -> NativeKeyEvent.VC_SUN_FIND;
                case KeyEvent.VK_AGAIN -> NativeKeyEvent.VC_SUN_AGAIN;

                //case NativeKeyEvent.VC_SUN_INSERT:

                case KeyEvent.VK_COPY -> NativeKeyEvent.VC_SUN_COPY;
                case KeyEvent.VK_CUT -> NativeKeyEvent.VC_SUN_CUT;
                default -> keyCode;
            };

            int modifers = 0;
            int swingModifer = stroke.getModifiers();
            //NativeKeyEvent.getModifiersText()

            if((swingModifer & KeyEvent.SHIFT_MASK) != 0 || (swingModifer & KeyEvent.SHIFT_DOWN_MASK) != 0){
                modifers |= NativeInputEvent.SHIFT_MASK;
            }

            if((swingModifer & KeyEvent.META_MASK) != 0 || (swingModifer & KeyEvent.META_DOWN_MASK) != 0){
                modifers |= NativeInputEvent.META_MASK;
            }

            if((swingModifer & KeyEvent.CTRL_MASK) != 0 || (swingModifer & KeyEvent.CTRL_DOWN_MASK) != 0){
                modifers |= NativeInputEvent.CTRL_MASK;
            }

            if((swingModifer & KeyEvent.ALT_MASK) != 0 || (swingModifer & KeyEvent.ALT_DOWN_MASK) != 0){
                modifers |= NativeInputEvent.ALT_MASK;
            }

            if((swingModifer & KeyEvent.BUTTON1_MASK) != 0 || (swingModifer & KeyEvent.BUTTON1_DOWN_MASK) != 0){
                modifers |= NativeInputEvent.BUTTON1_MASK;
            }

            if((swingModifer & KeyEvent.BUTTON2_MASK) != 0 || (swingModifer & KeyEvent.BUTTON2_MASK) != 0){
                modifers |= NativeInputEvent.BUTTON2_MASK;
            }

            if((swingModifer & KeyEvent.BUTTON3_MASK) != 0 || (swingModifer & KeyEvent.BUTTON3_MASK) != 0){
                modifers |= NativeInputEvent.BUTTON3_MASK;
            }

            return new NativeKeyEvent(0, modifers, 0, keyCode,'a');
        }
        return null;
    }

    public static KeyStroke getKeyStroke(String action){
        if(keyMap.containsKey(action)){
            return keyMap.get(action).keystroke;
        }
        return null;
    }
}
