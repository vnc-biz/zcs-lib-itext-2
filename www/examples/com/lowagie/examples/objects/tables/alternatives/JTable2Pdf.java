/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * --> Copyright 2001-2005 by Bruno Lowagie <--
 *
 * This code is part of the 'iText Tutorial'.
 * You can find the complete tutorial at the following address:
 * http://www.lowagie.com/iText/tutorial/
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext-questions@lists.sourceforge.net
 */
package com.lowagie.examples.objects.tables.alternatives;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Constructing a JTable and printing it to PDF.
 */
public class JTable2Pdf extends JFrame {
    /** The JTable we will show in a Swing app and print to PDF. */
    private JTable table;
    
    /**
     * Constructor for PrintJTable.
     */
    public JTable2Pdf() {
        getContentPane().setLayout(new BorderLayout());
        setTitle("JTable test");
        createToolbar();
        createTable();
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {System.exit(0);}
        });        
    }
    
    /**
     * Create a table with some dummy data
     */
    private void createTable() {
        Object[][] data ={
            {"Mary", "Campione", "Snowboarding", new
             Integer(5), new Boolean(false)},
             {"Alison", "Huml", "Rowing", new
              Integer(3), new Boolean(true)},
              {"Kathy", "Walrath", "Chasing toddlers",
               new Integer(2), new Boolean(false)},
               {"Mark", "Andrews", "Speed reading", new
                Integer(20), new Boolean(true)},
                {"Angela", "Lih", "Teaching high school", new Integer(4), new Boolean(false)}
        };
        
        String[] columnNames =
        {"First Name", "Last Name", "Sport", "# of Years", "Vegetarian"};
        
        table = new JTable(data, columnNames);
        
        // Use a panel to contains the table and add it the frame
        JPanel tPanel = new JPanel(new BorderLayout());
        tPanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tPanel.add(table, BorderLayout.CENTER);
        
        getContentPane().add(tPanel, BorderLayout.CENTER);
    }
    
    /**
     * Toolbar for print and exit
     */
    private void createToolbar() {
        JToolBar tb = new JToolBar();
        
        JButton printBtn = new JButton("Print");
        printBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                print();
            }
        });
        
        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        
        tb.add(printBtn);
        tb.add(exitBtn);
        
        getContentPane().add(tb, BorderLayout.NORTH);
    }
    /**
     * Print the table into a PDF file
     */
    private void print() {
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter writer =
            PdfWriter.getInstance(document, new FileOutputStream("jTable.pdf"));
            
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            
            // Create the graphics as shapes
            cb.saveState();
            Graphics2D g2 = cb.createGraphicsShapes(500, 500);
            
            // Print the table to the graphics
            Shape oldClip = g2.getClip();
            g2.clipRect(0, 0, 500, 500);
            table.print(g2);
            g2.setClip(oldClip);
            
            g2.dispose();
            cb.restoreState();
            
            document.newPage();
            
            // Create the graphics with pdf fonts
            cb.saveState();
            g2 = cb.createGraphics(500, 500);
            
            // Print the table to the graphics
            oldClip = g2.getClip();
            g2.clipRect(0, 0, 500, 500);
            table.print(g2);
            g2.setClip(oldClip);
            
            g2.dispose();
            cb.restoreState();
            
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        
        document.close();
    }
    
    /**
     * Exit app
     */
    private void exit() {
        System.exit(0);
    }    
	/**
	 * A very simple PdfPTable example.
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args) {
        JTable2Pdf frame = new JTable2Pdf();
        frame.pack();
        frame.setVisible(true);
        frame.print();
        frame.exit();
	}
}