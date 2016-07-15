package com.akubis.obraz.zad1.processing;

import java.util.LinkedList;
import javax.swing.text.StyledEditorKit;

public class Node {
// dane przechowywane w węźle

    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private boolean uniform;
    private int segment;


// referencja do rodzica
    private Node parent;
// lista dzieci
    private LinkedList<Node> children;

    public Node(Node parent, int startCol, int endCol, int startRow, int endRow) {
        this.parent = parent;
        this.startCol = startCol;
        this.endCol = endCol;
        this.startRow = startRow;
        this.endRow = endRow;
        uniform = true;
        children = new LinkedList<Node>();
        segment=0;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getEndCol() {
        return endCol;
    }

    public void setEndCol(int endCol) {
        this.endCol = endCol;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isUniform() {
        return uniform;
    }

    public void setUniform(boolean uniform) {
        this.uniform = uniform;
    }

    public LinkedList<Node> getChildren() {
        return children;
    }

    public void setChildren(LinkedList<Node> children) {
        this.children = children;
    }

    public void addChild(Node child) {
        children.add(child);
    }
    
    
    public int getSegment() {
        return segment;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }
}
