package datastructures;

public interface TreeNode<T> {
    public TreeNode<T> getLeft();
    public TreeNode<T> getRight();
    public TreeNode<T> getParent();
    public T getData();
}
