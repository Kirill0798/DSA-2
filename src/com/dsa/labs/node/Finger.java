package com.dsa.labs.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Finger {
    private int start;
    private int[] interval;
    private Node node;

    public Finger(int start, int[] interval, Node node){
        this.start = start;
        this.interval = interval;
        this.node = node;
    }

    public static List<Finger> generateTable(int m, int n) {
        List<Finger> fingerTable = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int start = generateStart(m, n, i);
            int[] interval = new int[]{start, generateStart(m, n, i + 1)};
            fingerTable.add(new Finger(start, interval, null));
        }
        return fingerTable;
    }

    private static int generateStart(int m, int n, int i) {
        return (int) ((n + Math.pow(2, i)) % Math.pow(2, m));
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int[] getInterval() {
        return interval;
    }

    public void setInterval(int[] interval) {
        this.interval = interval;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "Finger{" +
                "start=" + start +
                ", interval=" + Arrays.toString(interval) +
                ", node=" + node +
                '}';
    }
}
