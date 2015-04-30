package teameval;

/*
Author:
Joseph Manenti
*/

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TeamEval extends JFrame implements ActionListener, ItemListener, ChangeListener
{
    private Connection myConnection;
    private Statement myStatement;
    private ResultSet myResultSet;
    private JFrame jfrm;
    private JPanel tcpnl, sldrpnl1, sldrpnl2, sldrpnl3, sldrpnl4, capnl, compnl, btnpnl;
    private JComboBox teamComboBox;
    private JButton jbtnSub, jbtnClr, jbtnavg;
    private JSlider techsldr, usflsldr, clarsldr, oversldr;
    private JLabel teamLabel, sl1, sl2, sl3, sl4, com;
    private JTextField avg;
    private JTextArea comment;
    private double sum, average;
    private String myteamname;
          
    public static void main(String args[])
    {
        String databaseDriver = "org.apache.derby.jdbc.ClientDriver";
        String databaseURL = "jdbc:derby://localhost:1527/MyEval;create=true";
     
        TeamEval eval = new TeamEval( databaseDriver, databaseURL );
        eval.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
    }

    public TeamEval(String databaseDriver, String databaseURL)
    {
        try
        {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            myConnection = DriverManager.getConnection( databaseURL );
            myStatement = myConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }
      
        catch ( SQLException exception )
        {
            
        }

        createUserInterface();
    }
   
    private void createUserInterface()
    {
        jfrm = new JFrame("Group Presentation Evaluation");
        jfrm.setLayout(new FlowLayout(FlowLayout.CENTER));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        jfrm.setSize(400, 700);
        jfrm.setLocationRelativeTo(null);
        jfrm.setDefaultCloseOperation(EXIT_ON_CLOSE);

        loadTeamCombo();
        loadSliders();
        loadCalcAvg();
        loadComments();
        loadButtons();
        loadTeams();

        jfrm.setVisible(true);
    }
    
    private void loadTeamCombo()
    {  
        teamLabel = new JLabel();
        teamLabel.setText( "Team:" );
        
        teamComboBox = new JComboBox();
        teamComboBox.addItem( "" );
        teamComboBox.addItemListener(this);
        
        tcpnl = new JPanel();
        tcpnl.add(teamLabel);
        tcpnl.add(teamComboBox);
        jfrm.add(tcpnl);
    }
    
    private void loadSliders()
    {

        Dictionary<Integer, Component> labelTable = new Hashtable<>();
        labelTable.put(1, new JLabel("C-"));
        labelTable.put(2, new JLabel("C"));
        labelTable.put(3, new JLabel("C+"));
        labelTable.put(4, new JLabel("B-"));
        labelTable.put(5, new JLabel("B"));
        labelTable.put(6, new JLabel("B+"));
        labelTable.put(7, new JLabel("A-"));
        labelTable.put(8, new JLabel("A"));
        
        sl1 = new JLabel();
        sl1.setText ("Technical");
        techsldr = new JSlider(1,8,8);
        techsldr.setPaintTicks(true);
        techsldr.setPaintLabels(true);
        techsldr.setSnapToTicks(true);
        techsldr.setMajorTickSpacing(1);
        techsldr.setLabelTable(labelTable);
        techsldr.addChangeListener(this);
        
        sl2 = new JLabel();
        sl2.setText ("Useful");
        usflsldr = new JSlider(1,8,8);
        usflsldr.setPaintTicks(true);
        usflsldr.setPaintLabels(true);
        usflsldr.setSnapToTicks(true);
        usflsldr.setMajorTickSpacing(1);
        usflsldr.setLabelTable(labelTable);
        usflsldr.addChangeListener(this);
        
        sl3 = new JLabel();
        sl3.setText ("Clarity");
        clarsldr = new JSlider(1,8,8);
        clarsldr.setPaintTicks(true);
        clarsldr.setPaintLabels(true);
        clarsldr.setSnapToTicks(true);
        clarsldr.setMajorTickSpacing(1);
        clarsldr.setLabelTable(labelTable);
        clarsldr.addChangeListener(this);
        
        sl4 = new JLabel();
        sl4.setText ("Overall");
        oversldr = new JSlider(1,8,8);
        oversldr.setPaintTicks(true);
        oversldr.setPaintLabels(true);
        oversldr.setSnapToTicks(true);
        oversldr.setMajorTickSpacing(1);
        oversldr.setLabelTable(labelTable);
        oversldr.addChangeListener(this);
        
        sldrpnl1 = new JPanel();
        sldrpnl2 = new JPanel();
        sldrpnl3 = new JPanel();
        sldrpnl4 = new JPanel();
        sldrpnl1.add(sl1);
        sldrpnl1.add(techsldr);
        sldrpnl2.add(sl2);
        sldrpnl2.add(usflsldr);
        sldrpnl3.add(sl3);
        sldrpnl3.add(clarsldr);
        sldrpnl4.add(sl4);
        sldrpnl4.add(oversldr);
        jfrm.add(sldrpnl1);
        jfrm.add(sldrpnl2);
        jfrm.add(sldrpnl3);
        jfrm.add(sldrpnl4);

    }    
    
    private void loadCalcAvg()
    {
        jbtnavg = new JButton("Calculate Average");
        jbtnavg.addActionListener(this);
        
        avg = new JTextField(5);
        avg.setEditable(false);
        
        capnl = new JPanel();
        capnl.add(jbtnavg);
        capnl.add(avg);
        jfrm.add(capnl);
    }        
    
    private void CalcAvg()
    {        
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumFractionDigits(2);
        
        sum = techsldr.getValue()+usflsldr.getValue()+clarsldr.getValue()+oversldr.getValue();
        average = sum/4;

        avg.setText(Double.toString(average));
        
        jbtnSub.setEnabled(true);
    }
    
    private void loadComments()
    {
        com = new JLabel();
        com.setText ("Comments: ");
        
        comment = new JTextArea(15,20);
        comment.setLineWrap(true);
        comment.setEditable(true);
        
        compnl = new JPanel();
        compnl.add(com);
        compnl.add(comment);
        jfrm.add(compnl);
    }
    
    private void loadButtons()
    {     
        jbtnClr = new JButton("Clear");
        jbtnClr.addActionListener(this);
        
        jbtnSub = new JButton("Submit");
        jbtnSub.addActionListener(this);
        jbtnSub.setEnabled(false);
        
        btnpnl = new JPanel();
        btnpnl.add(jbtnClr);
        btnpnl.add(jbtnSub);
        jfrm.add(btnpnl);
    }

    private void loadTeams()
    {
        try
        {
            myResultSet = myStatement.executeQuery( "SELECT DISTINCT TEAMNAME FROM APP.TEAMS");
       
            while ( myResultSet.next() )
            {
                teamComboBox.addItem(
                myResultSet.getString( "TeamName" ) );
            }

            myResultSet.close();
        }

        catch ( SQLException exception )
        {
            
        }
    }
    
    private void updateTeams()
    {     
        String cmnt = comment.getText();
        double q1 = techsldr.getValue();
        double q2 = usflsldr.getValue();
        double q3 = clarsldr.getValue();
        double q4 = oversldr.getValue();
        
        try
        {
            String sql = "UPDATE APP.TEAMS SET Q1_TECHNICAL = " + q1 + ", " + "Q2_USEFUL = " + q2 + ", " + 
                         "Q3_CLARITY = " + q3 + ", " + "Q4_OVERALL = " + q4 + ", " + "AVGSCORE = " + average + 
                         ", " + "COMMENTS = '" +  cmnt + "' WHERE TEAMNAME = '" + myteamname + "'";
       
            myStatement.executeUpdate(sql);
        }
        
        catch (SQLException exception)
        {
            
        }

    }
    
   
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == jbtnSub)
        { 
            updateTeams();  
            JOptionPane.showMessageDialog(null, myteamname + " has been Updated to the Database");
        }  
        
        if (e.getSource() == jbtnClr)
        {
            teamComboBox.setSelectedItem("");
            techsldr.setValue(8);
            usflsldr.setValue(8);
            clarsldr.setValue(8);
            oversldr.setValue(8);
            avg.setText("");
            comment.setText("");
            jbtnSub.setEnabled(false);
        }  
        
        if (e.getSource() == jbtnavg)
        {
            CalcAvg();
        }  
    }   

    @Override
    public void itemStateChanged(ItemEvent e) 
    {
        myteamname = (String)teamComboBox.getSelectedItem();
    }

    @Override
    public void stateChanged(ChangeEvent e) 
    {
       
    }

}