package datastructures;

public interface ListNode<T> {
    public ListNode<T> getPrev();
    public ListNode<T> getNext();
    public T getData();
}
