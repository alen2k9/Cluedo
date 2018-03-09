/*
  Code to handle the notes sheet.

  Authors Team11:  Jack Geraghty - 16384181
                   Conor Beenham - 16350851
                   Alen Thomas   - 16333003
 */

package com.team11.cluedo.players;

import com.team11.cluedo.board.room.RoomData;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.ui.Resolution;
import com.team11.cluedo.weapons.WeaponData;
import sun.swing.table.DefaultTableCellHeaderRenderer;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

public class Notes extends JTable {
    private SuspectData suspectData = new SuspectData();
    private WeaponData weaponData = new WeaponData();
    private RoomData roomData = new RoomData();

    private HashSet<Point> cellToPaint = new HashSet<>();

    private Resolution resolution;

    public Notes(Resolution resolution){
        this.resolution = resolution;
        this.setBackground(Color.DARK_GRAY);
        setupComponents();
    }

    private void setupComponents(){
        //Make sure you can't edit the cells
        DefaultTableModel noteModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        //Add all of the columns
        noteModel.addColumn("");
        noteModel.addColumn("G");
        noteModel.addColumn("P1");
        noteModel.addColumn("P2");
        noteModel.addColumn("P3");
        noteModel.addColumn("P4");
        noteModel.addColumn("P5");
        noteModel.addColumn("P6");

        this.setModel(noteModel);

        //Table Header Stuff
        this.getTableHeader().setBackground(Color.DARK_GRAY);
        this.getTableHeader().setForeground(Color.WHITE);
        this.getTableHeader().setDefaultRenderer(new NotesHeadRenderer());
        this.getTableHeader().setReorderingAllowed(false);
        this.getTableHeader().setFont(new Font("Orange Kid", Font.BOLD, (int)(18*resolution.getScalePercentage())));


        this.setGridColor((Color.BLACK));
        this.setFont(new Font("Orange Kid", Font.BOLD, (int)(18*resolution.getScalePercentage())));
        this.setRowHeight((int)(27.5 * resolution.getScalePercentage()));

        //Set the preferred with and make all the columns non resizable
        int dataColumnWidth = (int)(120 * resolution.getScalePercentage());
        int headerColumnWidth = (int)(40 * resolution.getScalePercentage());
        for (int i = 0; i < this.getColumnCount(); i++){
            if (i == 0) {
                this.getColumnModel().getColumn(i).setWidth(dataColumnWidth);
                this.getColumnModel().getColumn(i).setPreferredWidth(dataColumnWidth);
                this.getColumnModel().getColumn(i).setMaxWidth(dataColumnWidth);
                this.getColumnModel().getColumn(i).setMinWidth(dataColumnWidth);
            } else {
                this.getColumnModel().getColumn(i).setWidth(headerColumnWidth);
                this.getColumnModel().getColumn(i).setPreferredWidth(headerColumnWidth);
                this.getColumnModel().getColumn(i).setMaxWidth(headerColumnWidth);
                this.getColumnModel().getColumn(i).setMinWidth(headerColumnWidth);
            }
            this.getColumnModel().getColumn(i).setResizable(false);
        }

        //Add the Suspects title, followed by all of the suspect names
        noteModel.addRow(new String[]{"SUSPECT"});
        for (int i = 0; i < suspectData.getSuspectAmount(); i++){
            noteModel.addRow(new String[]{suspectData.getSuspectName(i)});
        }

        noteModel.addRow(new String[]{""});

        //Add the weapons title, followed by all of the weapon names
        noteModel.addRow(new String[]{"WEAPONS"});
        for (int i = 0; i < weaponData.getWeaponAmount(); i++){
            noteModel.addRow(new String[]{weaponData.getWeaponName(i)});
        }

        noteModel.addRow(new String[]{""});

        //Add the room title, followed by the room names
        noteModel.addRow(new String[]{"ROOMS"});
        for (int i = 0; i < roomData.getRoomAmount(); i++){
            noteModel.addRow(new String[]{roomData.getRoomName(i)});
        }

        //Set the renderer for the table
        this.setDefaultRenderer(Object.class, new NotesRenderer());
    }

    //Method which will paint a desired cell based on the player number and the value of the row they highlight
    //To fill in global cards have playerNum = 0
    public void paintCell(int playerNum, String value){
        int offset = 1;

        int rowIndex = 2;
        int columnIndex = playerNum + offset;

        for (int i = 0; i < this.getRowCount(); i++){
            if (this.getModel().getValueAt(i,0).equals(value)){
                rowIndex = i;
            }
        }

        cellToPaint.add(new Point(rowIndex, columnIndex));
    }


    //Custom Renderer for the table cells
    public class NotesRenderer extends DefaultTableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);


            label.setForeground(Color.WHITE);

            if (column == 0){
                label.setBorder(BorderFactory.createMatteBorder(0,2,0,1,Color.BLACK));
            }
            if (cellToPaint.contains(new Point(row, column)) && column > 1){
                label.setBackground(Color.YELLOW);
                label.setForeground(Color.DARK_GRAY);
                label.setFont(new Font("Ariel", Font.BOLD, (int)(25*resolution.getScalePercentage())));
                label.setText("X");
            } else if (cellToPaint.contains(new Point(row, column)) && column == 1){
                label.setBackground(Color.YELLOW);
                label.setForeground(Color.DARK_GRAY);
                label.setFont(new Font("Ariel", Font.BOLD, (int)(25*resolution.getScalePercentage())));
                label.setText("A");

            } else{
                label.setBackground(Color.DARK_GRAY);
            }

            return label;
        }
    }

    //Custom Renderer for the Table headers
    public class NotesHeadRenderer extends DefaultTableCellHeaderRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            DefaultTableCellHeaderRenderer rendererComponent = (DefaultTableCellHeaderRenderer)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            rendererComponent.setBorder(BorderFactory.createMatteBorder(0,0,2,1,Color.BLACK));
            if (column == 0){
                rendererComponent.setBorder(BorderFactory.createMatteBorder(0,0,2,2,Color.BLACK));
            }
            return rendererComponent;
        }
    }
}