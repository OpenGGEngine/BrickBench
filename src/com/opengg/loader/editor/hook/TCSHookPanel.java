package com.opengg.loader.editor.hook;

import com.opengg.core.console.GGConsole;
import com.opengg.core.math.Vector3f;
import com.opengg.loader.MapXml;
import com.opengg.loader.SwingUtil;
import com.opengg.loader.editor.MapInterface;
import com.opengg.loader.editor.components.WrapLayout;
import com.opengg.loader.editor.hook.TCSHookManager.GameExecutable;
import com.opengg.loader.editor.tabs.EditorPane;
import com.opengg.loader.editor.tabs.EditorTabAutoRegister;
import com.opengg.loader.game.nu2.LevelsTXTParser;

import net.miginfocom.swing.MigLayout;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.opengg.core.Configuration;
import com.opengg.core.engine.Executor;
import com.opengg.loader.BrickBench;
import com.opengg.loader.editor.tabs.EditorTab;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@EditorTabAutoRegister
public class TCSHookPanel extends JPanel implements EditorTab {
    private AIMessageTableModel aiMessageModel = new AIMessageTableModel(new ArrayList<>());
    private JButton connectButton;
    private JTextField mapID;
    private JCheckBox door, reset;
    private JComboBox<String> mapCombo, doorCombo;
    private Map<String, Integer> mapNameToID = new HashMap<>();
    private Map<Integer, String> mapIDToDirectory = new HashMap<>();

    public TCSHookPanel() {
        setLayout(new BorderLayout());
        TCSHookManager.panel = this;

        JPanel hookManagerPanel = new JPanel(new WrapLayout());

        
        connectButton = new JButton("Start Hook");
        connectButton.addActionListener(a -> {
            if(!TCSHookManager.isEnabled()){
                generateGameSelectMenu(null).show(connectButton, 0 , connectButton.getHeight());
            }else{
                TCSHookManager.endHook();
            }
        });
        hookManagerPanel.add(connectButton);

        mapID = new JTextField("0");
        mapID.setColumns(3);
        hookManagerPanel.add(mapID);

        mapCombo = new JComboBox<>();

        doorCombo = new JComboBox<>();
        doorCombo.setEnabled(false);

        mapCombo.addActionListener(m -> {
            int mapIntId = mapNameToID.get((String) mapCombo.getSelectedItem());
            mapID.setText(String.valueOf(mapIntId));
            String levelDir = mapIDToDirectory.get(mapIntId);

            try{
                var newMap = Path.of(TCSHookManager.currentHook.getDirectory().normalize().toString(), "levels",levelDir,mapCombo.getSelectedItem()+".txt");
                try(Scanner fis = new Scanner(new File(newMap.normalize().toString()))){
                    boolean inDoor = false;

                    Vector<String> doors = new Vector<>();

                    doors.add("<Default Start>");

                    while(fis.hasNext()){
                        String line = fis.nextLine();
                        if(inDoor && line.toLowerCase().contains("spline")){
                            String spline = line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
                            doors.add(spline);
                        }

                        if(line.toLowerCase().contains("door_start")){
                            inDoor = true;
                        } else if (line.toLowerCase().contains("door_end")){
                            inDoor = false;
                        }
                    }

                    doorCombo.setModel(new DefaultComboBoxModel<>(doors));
                } catch (FileNotFoundException e){
                    GGConsole.log("Unable to read txt file for map. Are your files fully extracted?");
                } catch (Exception e){
                    System.out.println(e);
                }
            } catch (InvalidPathException e) {
                //no real map yet
            }
        });
        hookManagerPanel.add(mapCombo);
        hookManagerPanel.add(doorCombo);

        var loadMap = new JButton("Load map");
        hookManagerPanel.add(loadMap);

        this.add(hookManagerPanel, BorderLayout.NORTH);

        JTabbedPane tabPane = new JTabbedPane();
        JPanel aiMessagePanel = new JPanel();
        aiMessagePanel.setLayout(new BorderLayout());

        JTable aiMessageTable = new JTable();
        aiMessageTable.setShowVerticalLines(true);
        aiMessageTable.setShowHorizontalLines(true);
        aiMessageTable.setModel(aiMessageModel);
        aiMessagePanel.add(new JScrollPane(aiMessageTable), BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            if(TCSHookManager.currentHook != null) {
                aiMessageModel.set(TCSHookManager.currentHook.getAIMessages());
                aiMessageModel.fireTableDataChanged();
            }
        });

        JCheckBox enableAutoRefresh = new JCheckBox("Auto-refresh");
        Executor.every(Duration.ofSeconds(1), () -> {
            if(enableAutoRefresh.isSelected()){
                SwingUtilities.invokeLater(() -> {
                    if(TCSHookManager.currentHook != null) {
                        aiMessageModel.set(TCSHookManager.currentHook.getAIMessages());
                        aiMessageModel.fireTableDataChanged();
                    }
                });
            }});

        JButton pushEdit = new JButton("Push edits");
        pushEdit.addActionListener(e->{
            if(TCSHookManager.currentHook != null) {
                pushEdit.setEnabled(false);
                List<AIMessage> updatedMessages = new ArrayList<>();
                HashMap<String,AIMessage> realMessages = new HashMap<>();

                TCSHookManager.currentHook.getAIMessages().forEach(a-> realMessages.put(a.name, a));

                for (AIMessage message : aiMessageModel.internal) {
                    AIMessage realMessage = realMessages.get(message.name);
                    if(realMessage != null){
                        message.address = realMessage.address;
                        updatedMessages.add(message);
                    }
                }

                TCSHookManager.currentHook.updateAIMessage(updatedMessages);
                aiMessageModel.set(TCSHookManager.currentHook.getAIMessages());
                aiMessageModel.fireTableDataChanged();
                pushEdit.setEnabled(true);
            }
        });

        JPanel buttonRow = new JPanel(new WrapLayout());
        buttonRow.add(enableAutoRefresh);
        buttonRow.add(refresh);
        buttonRow.add(pushEdit);

        aiMessagePanel.add(buttonRow, BorderLayout.NORTH);

        var configPanel = new JPanel(new MigLayout("wrap 1"));
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));

        var autoload = new JCheckBox("Autoload maps from current hooked game");
        reset = new JCheckBox("Reset map on load");
        door = new JCheckBox("Reset door on load");
        door.addActionListener(e->{
            doorCombo.setEnabled(door.isSelected());
        });
        autoload.setSelected(Boolean.parseBoolean(Configuration.getConfigFile("editor.ini").getConfig("autoload-hook")));
        autoload.addActionListener(a -> {
            Configuration.getConfigFile("editor.ini").writeConfig("autoload-hook", String.valueOf(autoload.isSelected())); BrickBench.CURRENT.reloadConfigFileData();
        });
        autoload.setToolTipText("Automatically loads the current map in the hooked game instance.");
        configPanel.add(autoload);
        configPanel.add(reset);
        configPanel.add(door);

        loadMap.addActionListener(a -> {
            loadCurrentMap();
        });

        var global = new JCheckBox("Enable global hotkeys");
        global.addActionListener(a -> {
            try {
                if(global.isSelected()){
                    if(!GlobalScreen.isNativeHookRegistered()) {
                        GlobalScreen.registerNativeHook();
                    }
                    Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
                    logger.setLevel(Level.SEVERE);
                    logger.setUseParentHandlers(false);

                    GlobalScreen.addNativeKeyListener(BrickBench.CURRENT);

                    JOptionPane.showMessageDialog(this, "Registered global key hook.");
                }else{
                    GlobalScreen.removeNativeKeyListener(BrickBench.CURRENT);
                    JOptionPane.showMessageDialog(this, "De-registered global key hook.");
                }


            } catch (NativeHookException e) {
                SwingUtil.showErrorAlert("Failed to register the global hook", e);
            }
        });

        configPanel.add(global);

        tabPane.add("Options", configPanel);
        tabPane.add("AI Messages", aiMessagePanel);

        var camSpeedField = new JTextField("1");
        var camYawSpeedField = new JTextField("1");
        var camPitchSpeedField = new JTextField("1");

        JPanel freeCamPanel = new JPanel(new MigLayout("ins 15"));
        freeCamPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        freeCamPanel.setAlignmentY(java.awt.Component.TOP_ALIGNMENT);

        JCheckBox freeCamCheckBox = new JCheckBox("Enable Free Cam");
        freeCamCheckBox.addItemListener(e -> {
            if(TCSHookManager.currentHook != null) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    TCSHookManager.currentHook.setCamEnable(true);

                    TCSHookManager.currentHook.setCamSpeeds(Float.parseFloat(camSpeedField.getText()),
                            Float.parseFloat(camYawSpeedField.getText()),Float.parseFloat(camPitchSpeedField.getText()));
                } else {
                    TCSHookManager.currentHook.setCamEnable(false);
                }
            }
        });

        JCheckBox disableUICheckBox = new JCheckBox("Disable UI");
        disableUICheckBox.addItemListener(e -> {
            if(TCSHookManager.currentHook != null) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    TCSHookManager.currentHook.setUIDisable(true);
                } else {
                    TCSHookManager.currentHook.setUIDisable(false);
                }
            }
        });

        freeCamPanel.add(freeCamCheckBox);
        freeCamPanel.add(disableUICheckBox , "pushx, growx, wrap");
        freeCamPanel.add(new JLabel("Cam Speed"));

        ((AbstractDocument)camSpeedField.getDocument()).setDocumentFilter(EditorPane.floatFilter);
        freeCamPanel.add(camSpeedField , "pushx, growx, wrap");

        freeCamPanel.add(new JLabel("Cam Yaw Speed"));

        ((AbstractDocument)camYawSpeedField.getDocument()).setDocumentFilter(EditorPane.floatFilter);
        freeCamPanel.add(camYawSpeedField , "pushx, growx, wrap");

        freeCamPanel.add(new JLabel("Cam Pitch Speed"));

        ((AbstractDocument)camPitchSpeedField.getDocument()).setDocumentFilter(EditorPane.floatFilter);
        freeCamPanel.add(camPitchSpeedField , "pushx, growx, wrap");

        JButton updateCamSpeeds = new JButton("Update Cam Speeds");
        updateCamSpeeds.addActionListener((e)->{
            if(TCSHookManager.currentHook != null && freeCamCheckBox.isSelected()) {
                TCSHookManager.currentHook.setCamSpeeds(Float.parseFloat(camSpeedField.getText()),
                        Float.parseFloat(camYawSpeedField.getText()),Float.parseFloat(camPitchSpeedField.getText()));
            }
        });
        freeCamPanel.add(updateCamSpeeds,"span 3, split 3, center, gaptop 15");

        JButton resetCamPos = new JButton("Set Cam Position to Editor View");
        resetCamPos.addActionListener((e)->{
            if(TCSHookManager.currentHook != null && freeCamCheckBox.isSelected()){
                TCSHookManager.currentHook.setCamPosition(BrickBench.CURRENT.ingamePosition);
            }
        });

        JButton resetCamPos2 = new JButton("Set Cam Position to Player 1");
        resetCamPos2.addActionListener((e)->{
            if(TCSHookManager.currentHook != null && freeCamCheckBox.isSelected()){
                TCSHookManager.currentHook.setCamPosition(TCSHookManager.currentHook.readPlayerLocation(0));
            }
        });

        freeCamPanel.add(resetCamPos);
        freeCamPanel.add(resetCamPos2);

        tabPane.add("Free Cam", freeCamPanel);
        add(tabPane, BorderLayout.CENTER);
    }

    public static JPopupMenu generateGameSelectMenu(Runnable onSelect) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem header = new JMenuItem("<html><b>Select Game</b></html>");
        header.setBackground(new JButton().getBackground());
        header.setEnabled(false);
        header.setOpaque(true);

        for (var executable : GameExecutable.values()) {
            var execItem = new JMenuItem(executable.NAME);
            execItem.addActionListener(a -> {
                TCSHookManager.beginHook(executable);
                if (onSelect != null) onSelect.run();
            });
            menu.add(execItem);
        }

        return menu;
    }

    public void loadCurrentMap() {
        if(TCSHookManager.isEnabled()) {
            String selectedDoor = ((doorCombo.getSelectedIndex() == 0) || (doorCombo.getSelectedItem() == null)) ? "" : (String)doorCombo.getSelectedItem();
            selectedDoor = selectedDoor.toLowerCase();

            if(door.isSelected()){
                TCSHookManager.currentHook.resetDoor(selectedDoor);
            }

            if(reset.isSelected()) {
                TCSHookManager.currentHook.setResetBit();
            }

            TCSHookManager.currentHook.setTargetMap(Integer.parseInt(mapID.getText()));
        }
    }

    public void reloadMaps(Path gamePath){
        var levelsFile = gamePath.resolve("levels/levels.txt");
        try {
            var levelsParser = new LevelsTXTParser();
            levelsParser.parseFile(levelsFile);

            var levels = levelsParser.getEntries().stream()
                    .filter(e -> e.type() != MapXml.MapType.TEST).toList();

            mapNameToID.clear();

            for(var level : levels){
                int index = levels.indexOf(level);
                mapNameToID.put(level.name(), index);
                mapIDToDirectory.put(index,level.path());
            }

            var namesStream = levels.stream()
                    .map(LevelsTXTParser.LevelTXTEntry::name);

            if (Configuration.getBoolean("alphabetical-order-hook-map-list"))
                namesStream = namesStream.sorted(Comparator.comparing(String::toLowerCase));

            var names = namesStream.toArray(String[]::new);
            mapCombo.setModel(new DefaultComboBoxModel<>(names));
        } catch (IOException e) {
            GGConsole.error("Unable to find levels.txt file at " + levelsFile.toAbsolutePath() + ". Are your files fully extracted?");
        }

    }

    @Override
    public String getTabName() {
        return "Runtime hook";
    }

    @Override
    public String getTabID() {
        return "hook-pane";
    }

    @Override
    public MapInterface.InterfaceArea getPreferredArea() {
        return MapInterface.InterfaceArea.TOP_RIGHT;
    }

    @Override
    public boolean getDefaultActive() {
        return false;
    }

    static class AIMessageTableModel extends AbstractTableModel {
        List<AIMessage> internal;
        String[] aiColumns = {"Name", "Value"};
        public AIMessageTableModel(List<AIMessage> initial) {
            internal = initial;
        }
        public void set(List<AIMessage> newList){
            internal = newList;
        }

        @Override
        public int getRowCount() {
            return internal.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex){
            return columnIndex == 1; //Or whatever column index you want to be editable
        }
        public Class<?> getColumnClass(int column) {
            return switch (column) {
                case 0 -> String.class;
                case 1 -> Float.class;
                default -> Boolean.class;
            };
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(columnIndex == 0){
                return internal.get(rowIndex).name;
            }else if(columnIndex == 1){
                return internal.get(rowIndex).value;
            }
            return -1;
        }
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            AIMessage row = internal.get(rowIndex);
            if(1 == columnIndex) {
                row.value = ((float) aValue);
            }
        }

        public String getColumnName(int column) {
            return aiColumns[column];
        }

    }
    public static class AIMessage{
        public String name;
        public float value;
        public int address;

        public AIMessage(String name,float value, int address){
            this.name = name;
            this.value = value;
            this.address = address;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AIMessage aiMessage = (AIMessage) o;
            return Objects.equals(name, aiMessage.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    public void updateConnectionUIState(){
        if(TCSHookManager.isEnabled()){
            reloadMaps(TCSHookManager.currentHook.getDirectory());
            connectButton.setText("Close Hook");
        }else{
            connectButton.setText("Open Hook");
        }
    }
}
