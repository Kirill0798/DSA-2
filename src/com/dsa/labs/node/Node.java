package com.dsa.labs.node;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Node {
    private int id;
    private List<Finger> fingerTable;
    private Node predecessor;

    public Node(int m, int n) {
        this.id = n;
        this.fingerTable = Finger.generateTable(m, n);
        for (Finger finger : fingerTable) {
            finger.setNode(this);
        }
        this.predecessor = this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public Node getSuccessor() {
        return fingerTable.get(0).getNode();
    }

    public void setSuccessor(Node node) {
        fingerTable.get(0).setNode(node);
    }

    public List<Finger> getFingerTable() {
        return fingerTable;
    }

    public void setFingerTable(List<Finger> fingerTable) {
        this.fingerTable = fingerTable;
    }

    private boolean idInRange(int id, int a, int b) {
        if (a >= b) {
            if (a > id) {
                id += (int) Math.pow(2, fingerTable.size());
            }
            b += (int) Math.pow(2, fingerTable.size());
        }
        return ((id > a) && (id < b));
    }

    private boolean idInRangeLeft(int id, int a, int b) {
        return idInRange(id, a, b) || id == a;
    }

    private boolean idInRangeRight(int id, int a, int b) {
        return idInRange(id, a, b) || id == b;
    }

    private Node findSuccessor(int id) {
        Node node = this.findPredecessor(id);
        return node.getSuccessor();
    }

    private Node findPredecessor(int id) {
        Node node = this;
        while (!idInRangeRight(id, node.getId(), node.getSuccessor().getId())) {
            node = node.closestPrecedingFinger(id);
        }
        return node;
    }

    private Node closestPrecedingFinger(int id) {
        int m = this.fingerTable.size();
        for (int i = m - 1; i >= 0; i--) {
            Node node = this.getFingerTable().get(i).getNode();

            if (idInRange(node.getId(), this.getId(), id)) {
                return node;
            }
        }
        return this;
    }

    public void join(Node node) {
        if (node != null) {
            initFingerTable(node);
            updateOthers();
        } else {
            for (Finger finger : this.getFingerTable()) {
                finger.setNode(this);
            }
            this.setPredecessor(this);
        }
    }

    private void initFingerTable(Node node) {
        this.getFingerTable().get(0).setNode(
                node.findSuccessor(
                        this.getFingerTable().get(0).getStart()
                ));
        this.setPredecessor(getSuccessor().getPredecessor());
        this.getSuccessor().setPredecessor(this);

        int m = this.getFingerTable().size();
        for (int i = 0; i < m - 1; i++) {
            Finger finger = this.getFingerTable().get(i);
            Finger fingerNext = this.getFingerTable().get(i + 1);

            if (idInRangeRight(fingerNext.getStart(), this.getId(), finger.getNode().getId())) {
                fingerNext.setNode(finger.getNode());
            } else {
                fingerNext.setNode(node.findSuccessor(fingerNext.getStart()));
            }
        }
    }

    private void updateOthers() {
        int m = this.getFingerTable().size();

        for (int i = 0; i < m; i++) {
            int id = (int) (this.getId() - Math.pow(2, i));
            id = id < 0 ? (int) (id + Math.pow(2, m)) : id;
            Node node = this.findPredecessor(id);
            node.updateFingerTable(this, i);
        }
    }

    private void updateFingerTable(Node node, int i) {
        Finger finger = this.getFingerTable().get(i);
        if (idInRangeLeft(node.getId(), this.getId(), finger.getNode().getId())) {
            finger.setNode(node);
            Node predecessorNode = this.getPredecessor();
            predecessorNode.updateFingerTable(node, i);
        }
    }

    public void remove() {
        getPredecessor().setSuccessor(getSuccessor());
        getSuccessor().setPredecessor(getPredecessor());

        int m = this.getFingerTable().size();

        for (int i = 0; i < m; i++) {
            int id = (int) (this.getId() - Math.pow(2, i));
            id = id < 0 ? (int) (id + Math.pow(2, m)) : id;
            Node node = this.findPredecessor(id);
            node.updateFingerTable(getSuccessor(), i);
        }
    }

    public void joinStable(Node node) {
        if (node != null) {
            setPredecessor(null);
            setSuccessor(node.findSuccessor(this.getId()));
        } else {
            for (Finger finger : this.getFingerTable()) {
                finger.setNode(this);
            }
            this.setPredecessor(this);
        }
    }

    public void stabilize() {
        Node node = getSuccessor().getPredecessor();
        if (idInRange(node.getId(), this.getId(), getSuccessor().getId())) {
            setSuccessor(node);
        }
        getSuccessor().notify(this);
    }

    private void notify(Node node) {
        if (getPredecessor() == null || idInRange(node.getId(), getPredecessor().getId(), this.getId())) {
            setPredecessor(node);
        }
    }

    public void fixFingers() {
        Random random = new Random();
        int i = random.nextInt(fingerTable.size());
        getFingerTable().get(i).setNode(findSuccessor(getFingerTable().get(i).getStart()));
    }

    public Node findById(int id) {
        Node node = this;
        List<Node> history = new LinkedList<>();

        while (node.getId() != id) {
            history.add(node);
            for (Finger finger : node.fingerTable) {
                if (this.idInRangeLeft(id, finger.getInterval()[0], finger.getInterval()[1])) {
                    node = finger.getNode();
                }
            }
            if (history.contains(node)) {
                return null;
            }
        }
        return node;
    }
}

