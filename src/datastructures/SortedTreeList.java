package datastructures;

import java.util.Iterator;

/**
 * A data structure that combines an AVL Binary Search Tree and
 * a Double Linked List maintaining the order of the elements.
 *
 * @author Egar Garcia
 *
 * @param <T>
 */
public class SortedTreeList<T extends Comparable<T>> {

    private Node root, head, tail;
    private long size;
    private boolean allowRepetitions;

    public SortedTreeList(final boolean allowRepetitions) {
        root = head = tail = null;
        size = 0L;
        this.allowRepetitions = allowRepetitions;
    }

    public SortedTreeList() {
        this(false);
    }

    public TreeNode<T> getRoot() {
        return root;
    }

    public ListNode<T> getHead() {
        return head;
    }

    public ListNode<T> getTail() {
        return tail;
    }

    public long size() {
        return size;
    }

    public T first() {
        if (head == null) {
            return null;
        }
        return head.data;
    }

    public T last() {
        if (tail == null) {
            return null;
        }
        return tail.data;
    }

    public void insert(final T data) {
        doInsert(data);
    }

    public boolean contains(final T data) {
        return doFind(data) != null;
    }

    public ListNode<T> find(final T data) {
        return doFind(data);
    }

    public ListNode<T> findFirst(final T data) {
        return findFirstFirstOrLast(data, true);
    }

    public ListNode<T> findLast(final T data) {
        return findFirstFirstOrLast(data, false);
    }

    public T popFirst() {
        if (head == null) {
            return null;
        }
        return popNode(head);
    }

    public T popLast() {
        if (tail == null) {
            return null;
        }
        return popNode(tail);
    }

    public void remove(final T data) {
        final Node current = doFind(data);
        if (current != null) {
            removeNode(current);
        }
    }

    public Iterator<T> iterator() {
        return new TreeListIterator(head);
    }

    private void insertBeforeInList(final Node target, final Node n) {
        n.prev = target.prev;
        n.next = target;

        if (target.prev != null) {
            target.prev.next = n;
        } else {
            head = n;
        }
        target.prev = n;
    }

    private void insertAfterInList(final Node target, final Node n) {
        n.prev = target;
        n.next = target.next;

        if (target.next != null) {
            target.next.prev = n;
        } else {
            tail = n;
        }
        target.next = n;
    }

    private Node insertLeftInTree(final Node target, final T data) {
        target.left = new Node(data, target);
        insertBeforeInList(target, target.left);
        size++;
        return target.left;
    }

    private Node insertRightInTree(final Node target, final T data) {
        target.right = new Node(data, target);
        insertAfterInList(target, target.right);
        size++;
        return target.right;
    }

    private long getBalanceFactor(final Node n) {
        return (n.left != null ? n.left.height : -1L) - (n.right != null ? n.right.height : -1);
    }

    private void setHeight(final Node n) {
        n.height = 1 + Math.max(n.left != null ? n.left.height : -1L, n.right != null ? n.right.height : -1);
    }

    private void swapChild(final Node parent, final Node currentChild, final Node newChild) {
        if (parent != null) {
            if (parent.left == currentChild) {
                parent.left = newChild;
            } else {
                parent.right = newChild;
            }
        } else {
            root = newChild;
        }
    }

    private Node rotateLeft(final Node n) {
        final Node newRoot = n.right;
        n.right = newRoot.left;
        newRoot.left = n;

        swapChild(n.parent, n, newRoot);
        newRoot.parent = n.parent;
        n.parent = newRoot;
        if (n.right != null) {
            n.right.parent = n;
        }

        setHeight(n);
        setHeight(newRoot);

        return newRoot;
    }

    private Node rotateRight(final Node n) {
        final Node newRoot = n.left;
        n.left = newRoot.right;
        newRoot.right = n;

        swapChild(n.parent, n, newRoot);
        newRoot.parent = n.parent;
        n.parent = newRoot;
        if (n.left != null) {
            n.left.parent = n;
        }

        setHeight(n);
        setHeight(newRoot);

        return newRoot;
    }

    private Node balance(final Node n) {
        long balanceFactor = getBalanceFactor(n);

        if (balanceFactor >= 2) {
            if (getBalanceFactor(n.left) <= -1) {
                rotateLeft(n.left);
                setHeight(n);
            }
            return rotateRight(n);
        } else if (balanceFactor <= -2) {
            if (getBalanceFactor(n.right) >= 1) {
                rotateRight(n.right);
                setHeight(n);
            }
            return rotateLeft(n);
        }
        return n;
    }

    private void andjustAndBalanceUpToRoot(final Node n) {
        Node current = n;

        while (current != null) {
            setHeight(current);
            current = balance(current);
            current = current.parent;
        }
    }

    private Node doInsertWhenEmpty(final T data) {
        root = head = tail = new Node(data);
        size++;
        return root;
    }

    private Node doInsert(final T data) {
        if (root == null) {
            return doInsertWhenEmpty(data);
        }

        Node current = null;
        Node target = root;
        Node newNode = null;

        while (target != null) {
            current = target;

            int comparison = data.compareTo(current.data);
            if (comparison < 0) {
                target = current.left;
                if (current.left == null) {
                    newNode = insertLeftInTree(current, data);
                }
            } else if (comparison > 0) {
                target = current.right;
                if (current.right == null) {
                    newNode = insertRightInTree(current, data);
                }
            } else if (allowRepetitions) {
                target = current.left;
                if (current.left == null) {
                    newNode = insertLeftInTree(current, data);
                }
            } else {
                return null;
            }
        }

        andjustAndBalanceUpToRoot(current);

        return newNode;
    }

    private Node doFind(final T data) {
        Node current = root;

        while (current != null) {
            int comparison = data.compareTo(current.data);
            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                return current;
            }
        }

        return null;
    }

    private Node findClosest(final T data, final boolean before) {
        Node current = null;
        Node target = root;

        while (target != null) {
            current = target;

            int comparison = data.compareTo(current.data);
            if (comparison < 0 || (before && comparison == 0)) {
                target = current.left;
            } else if (comparison > 0 || (!before && comparison == 0)) {
                target = current.right;
            }
        }

        return current;
    }

    private ListNode<T> findFirstFirstOrLast(final T data, final boolean first) {
        Node closests = findClosest(data, first);
        if (closests != null) {
            if (closests.data.equals(data)) {
                return closests;
            } else if (first && closests.next != null && closests.next.data.equals(data)) {
                return closests.next;
            } else if (!first && closests.prev != null && closests.prev.data.equals(data)) {
                return closests.prev;
            }
        }
        return null;
    }

    private void swapNodesTreePointers(final Node n1, final Node n2) {
        Node tmp = n1.parent;
        n1.parent = n2.parent;
        n2.parent = tmp;

        tmp = n1.left;
        n1.left = n2.left;
        n2.left = tmp;

        tmp = n1.right;
        n1.right = n2.right;
        n2.right = tmp;
    }

    private void swapNodesTreePointersParentChild(final Node parent, final Node child) {
        child.parent = parent.parent;
        parent.parent = child;

        if (parent.left == child) {
            parent.left = child.left;
            child.left = parent;
            Node tmp = child.right;
            child.right = parent.right;
            parent.right = tmp;
        } else {
            parent.right = child.right;
            child.right = parent;
            Node tmp = child.left;
            child.left = parent.left;
            parent.left = tmp;
        }
    }

    private void swapNodesInTree(final Node n1, final Node n2) {
        if (n2.parent == n1) {
            swapChild(n1.parent, n1, n2);
            if (n2.left != null) {
                n2.left.parent = n1;
            }
            if (n2.right != null) {
                n2.right.parent = n1;
            }
            if (n1.left == n2) {
                if (n1.right != null) {
                    n1.right.parent = n2;
                }
            } else {
                if (n1.left != null) {
                    n1.left.parent = n2;
                }
            }
            swapNodesTreePointersParentChild(n1, n2);
        } else if (n1.parent == n2) {
            swapNodesInTree(n2, n1);
        } else {
            swapChild(n1.parent, n1, n2);
            swapChild(n2.parent, n2, n1);
            if (n1.left != null) {
                n1.left.parent = n2;
            }
            if (n1.right != null) {
                n1.right.parent = n2;
            }
            if (n2.left != null) {
                n2.left.parent = n1;
            }
            if (n2.right != null) {
                n2.right.parent = n1;
            }
            swapNodesTreePointers(n1, n2);
        }
    }

    private void removeNodeInList(final Node n) {
        if (n.prev == null) {
            head = n.next;
        } else {
            n.prev.next = n.next;
        }

        if (n.next == null) {
            tail = n.prev;
        } else {
            n.next.prev = n.prev;
        }

        n.prev = n.next = null;
    }

    private void removeNodeInTree(final Node n) {
        if (n.left == null || n.right == null) {

            final Node parent = n.parent;
            if (n.left != null)  {
                n.left.parent = parent;
                swapChild(parent, n, n.left);
            } else if (n.right != null) {
                n.right.parent = parent;
                swapChild(parent, n, n.right);
            } else {
                swapChild(parent, n, null);
            }

            n.left = n.right = n.parent = null;
            n.height = 0;
            andjustAndBalanceUpToRoot(parent);

        } else {
            swapNodesInTree(n, n.next);
            removeNodeInTree(n);
        }
    }

    private void removeNode(final Node n) {
        removeNodeInTree(n);
        removeNodeInList(n);
        n.data = null;
        size--;
    }

    private T popNode(final Node n) {
        final T result = n.data;
        removeNode(n);
        return result;
    }


    private class Node implements TreeNode<T>, ListNode<T> {
        T data;
        Node left, right, parent, prev, next;
        long height;

        public Node(final T data, final Node parent) {
            this.data = data;
            this.parent = parent;
            left = right = prev = next = null;
            height = 0L;
        }

        public Node(final T data) {
            this(data, null);
        }

        @Override
        public ListNode<T> getPrev() {
            return prev;
        }

        @Override
        public ListNode<T> getNext() {
            return next;
        }

        @Override
        public TreeNode<T> getLeft() {
            return left;
        }

        @Override
        public TreeNode<T> getRight() {
            return right;
        }

        @Override
        public TreeNode<T> getParent() {
            return parent;
        }

        @Override
        public T getData() {
            return data;
        }
    }

    private class TreeListIterator implements Iterator<T> {
        private Node current;

        TreeListIterator(final Node current) {
            this.current = current;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (current == null) {
                return null;
            }
            final T data = current.data;
            current = current.next;
            return data;
        }
    }
}
