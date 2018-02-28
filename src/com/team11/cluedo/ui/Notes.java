/*
    Main Author : Jack Geraghty : 16384181
    Team11 : Jack Geraghty : 16384181
             Conor Beenham :
             Alen Thomas   :

 */

package com.team11.cluedo.ui;

import com.team11.cluedo.board.RoomData;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.weapons.WeaponData;
import sun.swing.table.DefaultTableCellHeaderRenderer;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

public class Notes {
    private JTable notesTable;
    private JPanel notesPanel;

    private SuspectData suspectData = new SuspectData();
    private WeaponData weaponData = new WeaponData();
    private RoomData roomData = new RoomData();

    private HashSet<Point> cellToPaint = new HashSet<>();

    private Resolution resolution;

    public Notes(Resolution resolution){
        this.resolution = resolution;
        this.notesPanel = new JPanel();
        setupComponents();
    }

    public JPanel getNotesPanel(){
        return this.notesPanel;
    }

    private void setupComponents(){
        JScrollPane notesSp;

        notesPanel.setLayout(new BorderLayout());
        notesSp = new JScrollPane();
        notesPanel.add(notesSp, BorderLayout.CENTER);

        //Make sure you can't edit the cells
        DefaultTableModel noteModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        //Add all of the columns
        noteModel.addColumn("");
        noteModel.addColumn("P1");
        noteModel.addColumn("P2");
        noteModel.addColumn("P3");
        noteModel.addColumn("P4");
        noteModel.addColumn("P5");
        noteModel.addColumn("P6");

        notesTable = new JTable();
        notesTable.setModel(noteModel);

        //Table Header Stuff
        notesTable.getTableHeader().setBackground(Color.DARK_GRAY);
        notesTable.getTableHeader().setForeground(Color.WHITE);
        notesTable.getTableHeader().setDefaultRenderer(new NotesHeadRenderer());
        notesTable.getTableHeader().setReorderingAllowed(false);
        notesTable.getTableHeader().setFont(new Font("Calibri", Font.BOLD, (int)(14*resolution.getScalePercentage())));


        notesTable.setGridColor((Color.BLACK));
        notesTable.setFont(new Font("Calibri", Font.BOLD, (int)(14*resolution.getScalePercentage())));
        notesTable.setRowHeight(27);

        //Set the preferred with and make all the columns non resizable
        for (int i = 0; i < notesTable.getColumnCount(); i++){
            notesTable.getColumnModel().getColumn(i).setPreferredWidth(10);
            notesTable.getColumnModel().getColumn(i).setResizable(false);
        }

        //Add the Suspects title, followed by all of the suspect names
        noteModel.addRow(new String[]{"SUSPECT"});
        for (int i = 0; i < suspectData.getSuspectAmount(); i++){
            noteModel.addRow(new String[]{suspectData.getSuspectID(i)});
        }

        noteModel.addRow(new String[]{""});

        //Add the weapons title, followed by all of the weapon names
        noteModel.addRow(new String[]{"WEAPONS"});
        for (int i = 0; i < weaponData.getWeaponAmount(); i++){
            noteModel.addRow(new String[]{weaponData.getWeaponID(i)});
        }

        noteModel.addRow(new String[]{""});

        //Add the room title, followed by the room names
        noteModel.addRow(new String[]{"ROOMS"});
        for (int i = 0; i < roomData.getRoomAmount(); i++){
            noteModel.addRow(new String[]{roomData.getRoomID(i)});
        }

        notesSp.setViewportView(notesTable);

        //Set the renderer for the table
        notesTable.setDefaultRenderer(Object.class, new NotesRenderer());

        //Add the listener
        notesTable.addMouseListener(new NotesListener());
    }

    //Mouse Listener for the table
    public class NotesListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent arg0){
            super.mouseClicked(arg0);
            int rows;
            int cols;

            //Get the position clicked
            rows = notesTable.getSelectedRow();
            cols = notesTable.getSelectedColumn();

            //Check to see if the position can be drawn on and if it's already been coloured in
            //If it hasn't been coloured in and is in a valid row/column then add it to the list to be coloured
            if (!cellToPaint.contains(new Point(rows, cols)) && (cols != 0) && (rows != 0) &&(rows != 7) && (rows != 8) && (rows!= 15) && (rows != 16)){
                cellToPaint.add(new Point(rows, cols));
            }
            //Otherwise remove the point from the paint list
            else{
                cellToPaint.remove(new Point(rows,cols));
            }

            //Repaint the table
            notesTable.repaint();
        }
    }

    //Custom Renderer for the table cells
    public class NotesRenderer extends DefaultTableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Color c = Color.DARK_GRAY;
            label.setForeground(Color.WHITE);

            if (column == 0){
                label.setBorder(BorderFactory.createMatteBorder(0,2,0,1,Color.black));
            }
            if (cellToPaint.contains(new Point(row, column))){
                c = Color.GREEN;
            }

            label.setBackground(c);
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