package datastructures;

import java.util.Iterator;

public class TreeList<T extends Comparable<T>> {

    private static class Node<T> implements TreeNode<T>, ListNode<T> {
        T data;
        Node<T> left, right, parent, prev, next;
        long height;

        public Node(final T data, final Node<T> parent) {
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

    private Node<T> root, head, tail;
    private long size;
    private boolean allowRepetitions;

    public TreeList(final boolean allowRepetitions) {
        root = head = tail = null;
        size = 0L;
        this.allowRepetitions = allowRepetitions;
    }

    public TreeList() {
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

    private void insertBeforeInList(final Node<T> target, final Node<T> n) {
        n.prev = target.prev;
        n.next = target;

        if (target.prev != null) {
            target.prev.next = n;
        } else {
            head = n;
        }
        target.prev = n;
    }

    private void insertAfterInList(final Node<T> target, final Node<T> n) {
        n.prev = target;
        n.next = target.next;

        if (target.next != null) {
            target.next.prev = n;
        } else {
            tail = n;
        }
        target.next = n;
    }

    private void insertLeftInTree(final Node<T> target, final T data) {
        target.left = new Node<T>(data, target);
        insertBeforeInList(target, target.left);
        size++;
    }

    private void insertRightInTree(final Node<T> target, final T data) {
        target.right = new Node<T>(data, target);
        insertAfterInList(target, target.right);
        size++;
    }

    private static <T> long getBalanceFactor(final Node<T> n) {
        return (n.left != null ? n.left.height : -1L) - (n.right != null ? n.right.height : -1);
    }

    private static <T> void setHeight(final Node<T> n) {
        n.height = 1 + Math.max(n.left != null ? n.left.height : -1L, n.right != null ? n.right.height : -1);
    }

    private void swapInParent(final Node<T> n, final Node<T> newChild) {
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

    private Node<T> rotateLeft(Node<T> n) {
        final Node<T> newRoot = n.right;
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

    private Node<T> rotateRight(Node<T> n) {
        final Node<T> newRoot = n.left;
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

    private Node<T> balance(Node<T> n) {
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

    public void insert(final T data) {
        if (root == null) {
            root = head = tail = new Node<T>(data);
            size++;
            return;
        }

        Node<T> current = null;
        Node<T> target = root;

        while (target != null) {
            current = target;

            if (data.compareTo(current.data) < 0) {
                target = current.left;
                if (current.left == null) {
                    insertLeftInTree(current, data);
                }
            } else if (data.compareTo(current.data) > 0) {
                target = current.right;
                if (current.right == null) {
                    insertRightInTree(current, data);
                }
            } else if (allowRepetitions) {
                target = current.left;
                if (current.left == null) {
                    insertLeftInTree(current, data);
                }
            } else {
                return;
            }
        }

        while (current != null) {
            setHeight(current);
            current = balance(current);
            current = current.parent;
        }
    }

    public Iterator<T> iterator() {
        return new TreeListIterator<T>(head);
    }

    private static class TreeListIterator<T> implements Iterator<T> {
        private Node<T> current;

        TreeListIterator(final Node<T> current) {
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
