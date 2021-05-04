package com.dsa.labs.node;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static void printList(List<Node> nodes) {
        System.out.println("___________________________________________________");

        for (Node node : nodes) {
            System.out.println("Finger Table: ");
            System.out.println("ID: " + node.getId());

            System.out.println("start  interval   successor");
            for (Finger finger : node.getFingerTable()) {
                System.out.println("  "
                        + finger.getStart()
                        + "  | [ "
                        + finger.getInterval()[0]
                        + ", "
                        + finger.getInterval()[1]
                        + " ) | "
                        + finger.getNode().getId());
            }
            System.out.println("______________________________________________________");
        }
    }

    private static void stabilize(List<Node> nodes) {
        for (int i = 0; i < 10; i++) {
            for (Node node : nodes) {
                node.stabilize();
                node.fixFingers();
            }
        }
    }

    public static void main(String[] args) {
        int count = 8;
        int m = (int) (Math.log(count) / Math.log(2));

        List<Integer> indexes = new ArrayList<>();
        indexes.add(0);
        indexes.add(1);
        indexes.add(3);

        List<Node> nodes = new ArrayList<>();

        Node head = null;

        for (int n: indexes) {
            Node node = new Node(m, n);
            node.joinStable(head);
            nodes.add(node);
            if (head == null) {
                head = nodes.get(0);
            }
        }

        stabilize(nodes);
        printList(nodes);

        int data = indexes.get(2);
        System.out.println("Data for node " + data + "\n");

        for (Finger finger : head.findById(data).getFingerTable()) {
            System.out.println(finger.getStart()
                    + " | [ "
                    + finger.getInterval()[0]
                    + ", "
                    + finger.getInterval()[1]
                    + " ] | "
                    + finger.getNode().getId());
        }
        indexes.add(5);
        Node node = new Node(m, 5);
        node.joinStable(head);
        nodes.add(node);

        node.join(nodes.get(0));
        node.join(nodes.get(1));
        node.join(nodes.get(2));


        stabilize(nodes);
        System.out.println("\n" + "finger table after joining node 5");
        printList(nodes);

        System.out.println("\n" + "finger table after removing node 5");
        indexes.remove(3);

        nodes.get(3).remove();
        nodes.remove(3);
        stabilize(nodes);
        printList(nodes);

    }
}
