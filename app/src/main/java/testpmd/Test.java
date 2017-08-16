package testpmd;

/**
 * Created by cwj on 17/4/6.
 */

public class Test {

    public static void main(String[] args) {
        //reverse link
        Node node3 = getNode(3, null);
        Node node2 = getNode(2, node3);
        Node node1 = getNode(1, node2);
        reverseLink(node1);
        reverseLinkWhile(node1);

        //quick sort
        int[] arr = {5, 3, 2, 7, 8, 4, 1, 9, 0, 6};
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int low, int high) {
        if (arr == null || low < 0 || low >= arr.length || high < 0 || high >= arr.length) {
            return;
        }
        if (low < high) {
            int position = partition(arr, low, high);
            quickSort(arr, low, position - 1);
            quickSort(arr, position + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int tmp = arr[low];
        while (low < high) {
            while (low < high && arr[high] >= tmp) {
                --high;
            }
            arr[low] = arr[high];
            while (low < high && arr[low] <= tmp) {
                ++low;
            }
            arr[high] = arr[low];
        }
        arr[low] = tmp;
        return low;
    }

    private static Node getNode(int value, Node next) {
        Node node = new Node(value);
        node.next = next;
        return node;
    }

    private static void reverseLinkWhile(Node head) {
        if (head == null) {
            return;
        }
        Node first = head, second = head.next, tmp;
        while (second != null) {
            tmp = second.next;
            second.next = first;
            first = second;
            second = tmp;
        }
        head.next = null;
    }

    private static void reverseLink(Node head) {
        Node last = recursiveReverse(head);
        if (last != null) {
            last.next = null;
        }
    }

    private static Node recursiveReverse(Node node) {
        if (node == null) {
            return null;
        }
        Node previous = recursiveReverse(node.next);
        if (previous != null) {
            previous.next = node;
        }
        return node;
    }
}

class Node {

    public final int value;
    public Node next;

    public Node(int value) {
        this.value = value;
    }
}
