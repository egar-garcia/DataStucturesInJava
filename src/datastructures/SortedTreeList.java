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

    private void swapInParent(final Node n, final Node newChild) {
        newChild.parent = n.parent;
        if (n.parent != null) {
            if (n.parent.left == n) {
                n.parent.left = newChild;
            } else {
                n.parent.right = newChild;
            }
        } else {
            root = newChild;
        }
    }

    private Node rotateLeft(final Node n) {
        final Node newRoot = n.right;
        n.right = newRoot.left;
        newRoot.left = n;

        swapInParent(n, newRoot);
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

        swapInParent(n, newRoot);
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

            if (data.compareTo(current.data) < 0) {
                target = current.left;
                if (current.left == null) {
                    newNode = insertLeftInTree(current, data);
                }
            } else if (data.compareTo(current.data) > 0) {
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
