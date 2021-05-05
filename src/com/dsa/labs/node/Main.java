package com.dsa.labs.node;

import java.util.ArrayList;
import java.util.Arrays;
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
        for (int i = 0; i < 100; i++) {
            for (Node node : nodes) {
                node.stabilize();
                node.fixFingers();
            }
        }
    }

    public static void main(String[] args) {
        int m = 3;

        List<Node> nodes = new ArrayList<>();

        Node head = new Node(m, 0);
        head.join(null);
        nodes.add(head);

        List<Integer> indexes = new ArrayList<>(Arrays.asList(1, 3));

        Node finalHead = head;

        indexes.forEach(
                x -> {
                    Node node = new Node(m, x);
                    node.joinStable(finalHead);
                    nodes.add(node);
                }
        );

        stabilize(nodes);
        printList(nodes);

        indexes.add(6);
        Node node = new Node(m, 6);
        node.joinStable(head);
        nodes.add(node);

        stabilize(nodes);
        System.out.println("\n" + "finger table after joining node 6");
        printList(nodes);

        System.out.println("\n" + "finger table after removing node 6");
        indexes.remove(2);

        nodes.get(3).remove();
        nodes.remove(3);
        stabilize(nodes);
        printList(nodes);

    }
}
