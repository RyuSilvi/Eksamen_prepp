package algorithms;

/**
 * Binary Search Examples
 *
 * Binary search finds a target value in a SORTED array in O(log n) time
 * by repeatedly halving the search interval.
 *
 * Three implementations:
 *   1. Iterative (most common, no stack overhead)
 *   2. Recursive
 *   3. Finding the first / last occurrence (for duplicates)
 *
 * Plus a Binary Search Tree (BST) with insert, search, and in-order traversal.
 */
public class BinarySearch {

    // =========================================================================
    // 1. Iterative Binary Search
    //    Returns the index of target in arr, or -1 if not found.
    // =========================================================================
    public static int iterativeSearch(int[] arr, int target) {
        int left  = 0;
        int right = arr.length - 1;

        while (left <= right) {
            // Use (left + (right - left) / 2) to avoid integer overflow
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;             // found
            } else if (arr[mid] < target) {
                left = mid + 1;         // target is in the right half
            } else {
                right = mid - 1;        // target is in the left half
            }
        }
        return -1;                      // not found
    }

    // =========================================================================
    // 2. Recursive Binary Search
    // =========================================================================
    public static int recursiveSearch(int[] arr, int target, int left, int right) {
        if (left > right) return -1;    // base case: not found

        int mid = left + (right - left) / 2;

        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            return recursiveSearch(arr, target, mid + 1, right);
        } else {
            return recursiveSearch(arr, target, left, mid - 1);
        }
    }

    // Convenience overload
    public static int recursiveSearch(int[] arr, int target) {
        return recursiveSearch(arr, target, 0, arr.length - 1);
    }

    // =========================================================================
    // 3. Find FIRST occurrence of target (handles duplicates)
    // =========================================================================
    public static int findFirst(int[] arr, int target) {
        int left = 0, right = arr.length - 1, result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                result = mid;           // record, but keep searching left
                right  = mid - 1;
            } else if (arr[mid] < target) {
                left   = mid + 1;
            } else {
                right  = mid - 1;
            }
        }
        return result;
    }

    // =========================================================================
    // 4. Find LAST occurrence of target (handles duplicates)
    // =========================================================================
    public static int findLast(int[] arr, int target) {
        int left = 0, right = arr.length - 1, result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                result = mid;           // record, but keep searching right
                left   = mid + 1;
            } else if (arr[mid] < target) {
                left   = mid + 1;
            } else {
                right  = mid - 1;
            }
        }
        return result;
    }

    // =========================================================================
    // Binary Search Tree (BST)
    // =========================================================================
    static class BST {
        private static class Node {
            int   val;
            Node  left, right;
            Node(int val) { this.val = val; }
        }

        private Node root;

        /** Insert a value into the BST */
        public void insert(int val) { root = insert(root, val); }

        private Node insert(Node node, int val) {
            if (node == null) return new Node(val);
            if (val < node.val)       node.left  = insert(node.left,  val);
            else if (val > node.val)  node.right = insert(node.right, val);
            // duplicate values are ignored
            return node;
        }

        /** Return true if val exists in the BST */
        public boolean search(int val) { return search(root, val); }

        private boolean search(Node node, int val) {
            if (node == null)    return false;
            if (val == node.val) return true;
            if (val < node.val)  return search(node.left,  val);
            return                      search(node.right, val);
        }

        /** In-order traversal: prints values in ascending order */
        public void inOrder() { inOrder(root); System.out.println(); }

        private void inOrder(Node node) {
            if (node == null) return;
            inOrder(node.left);
            System.out.print(node.val + " ");
            inOrder(node.right);
        }

        /** Find the minimum value in the BST */
        public int min() {
            if (root == null) throw new RuntimeException("Empty tree");
            Node curr = root;
            while (curr.left != null) curr = curr.left;
            return curr.val;
        }

        /** Find the maximum value in the BST */
        public int max() {
            if (root == null) throw new RuntimeException("Empty tree");
            Node curr = root;
            while (curr.right != null) curr = curr.right;
            return curr.val;
        }
    }

    // =========================================================================
    // main – run all examples
    // =========================================================================
    public static void main(String[] args) {
        int[] sorted = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};

        System.out.println("Array: 1, 3, 5, 7, 9, 11, 13, 15, 17, 19");

        // Iterative search
        System.out.println("\n--- Iterative search ---");
        System.out.println("Search 7  -> index " + iterativeSearch(sorted, 7));   // 3
        System.out.println("Search 1  -> index " + iterativeSearch(sorted, 1));   // 0
        System.out.println("Search 19 -> index " + iterativeSearch(sorted, 19));  // 9
        System.out.println("Search 6  -> index " + iterativeSearch(sorted, 6));   // -1

        // Recursive search
        System.out.println("\n--- Recursive search ---");
        System.out.println("Search 13 -> index " + recursiveSearch(sorted, 13));  // 6
        System.out.println("Search 20 -> index " + recursiveSearch(sorted, 20));  // -1

        // First / last occurrence
        int[] withDups = {2, 4, 4, 4, 6, 8, 8, 10};
        System.out.println("\n--- First/Last occurrence in [2,4,4,4,6,8,8,10] ---");
        System.out.println("First 4 -> index " + findFirst(withDups, 4));  // 1
        System.out.println("Last  4 -> index " + findLast(withDups,  4));  // 3
        System.out.println("First 8 -> index " + findFirst(withDups, 8));  // 5
        System.out.println("Last  8 -> index " + findLast(withDups,  8));  // 6

        // BST
        System.out.println("\n--- Binary Search Tree ---");
        BST bst = new BST();
        int[] values = {10, 4, 17, 1, 9, 12, 20};
        for (int v : values) bst.insert(v);

        System.out.print("In-order (sorted): ");
        bst.inOrder();                           // 1 4 9 10 12 17 20
        System.out.println("Min: " + bst.min()); // 1
        System.out.println("Max: " + bst.max()); // 20
        System.out.println("Search 9  -> " + bst.search(9));   // true
        System.out.println("Search 15 -> " + bst.search(15));  // false
    }
}
