package com.akubis.obraz.zad1.gui;

import com.akubis.obraz.zad1.processing.FilterIdentifyLines;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTable;

public class MatrixPanel extends JPanel {

    private final FilterIdentifyLines filterIdentifyLines;
    private final int ordinal;

    public MatrixPanel(FilterIdentifyLines filterIdentifyLines, int ordinal) {
        this.filterIdentifyLines = filterIdentifyLines;
        this.ordinal = ordinal;
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        String columnNames[] = {"1", "2", "3"};
        JTable table = new JTable(filterIdentifyLines.getMask()[ordinal], columnNames);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setEnabled(false);
        this.add(table);
        this.setSize(100, 50);
    }
}
