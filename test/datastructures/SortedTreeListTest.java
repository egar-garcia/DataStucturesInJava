package datastructures;

import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class SortedTreeListTest {

    private static final int MAX_SIZE = 100;

    private Random random = new Random();


    @Test
    void testSize() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        final Integer[] testSampleArray = getSampleArrayWithUniqueElements(1);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        Assert.assertEquals(testSampleArray.length, sortedTreeList.size());
    }

    @Test
    void testSize_withNoElements() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        Assert.assertEquals(0, sortedTreeList.size());
    }

    @Test
    void testInsert() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        final Integer[] testSampleArray = getSampleArrayWithUniqueElements(1);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        final Integer[] resultArray = getArrayFromSortedTreeList(sortedTreeList);
        Arrays.sort(testSampleArray);

        Assert.assertArrayEquals(testSampleArray, resultArray);
        Assert.assertTrue(isBalanced(sortedTreeList));
    }

    @Test
    void testInsert_withNoRepetitionsAllowed() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(false);

        final Integer[] testSampleArray = getSampleArrayWithRepeatedElements(2, 0.5);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        final Integer[] resultArray = getArrayFromSortedTreeList(sortedTreeList);
        final Integer[] uniqueTestSampleArray = getUniqueElements(testSampleArray);
        Arrays.sort(uniqueTestSampleArray);

        Assert.assertArrayEquals(uniqueTestSampleArray, resultArray);
        Assert.assertTrue(isBalanced(sortedTreeList));
    }

    @Test
    void testInsert_withRepetitionsAllowed() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final Integer[] testSampleArray = getSampleArrayWithRepeatedElements(2, 0.5);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        final Integer[] resultArray = getArrayFromSortedTreeList(sortedTreeList);
        Arrays.sort(testSampleArray);

        Assert.assertArrayEquals(testSampleArray, resultArray);
        Assert.assertTrue(isBalanced(sortedTreeList));
    }

    @Test
    void testFirst() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        final Integer[] testSampleArray = getSampleArrayWithUniqueElements(1);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        Arrays.sort(testSampleArray);

        Assert.assertEquals(testSampleArray[0], sortedTreeList.first());
    }

    @Test
    void testFirst_whenNoElements() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        Assert.assertNull(sortedTreeList.first());
    }

    @Test
    void testLast() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        final Integer[] testSampleArray = getSampleArrayWithUniqueElements(1);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        Arrays.sort(testSampleArray);

        Assert.assertEquals(testSampleArray[testSampleArray.length - 1], sortedTreeList.last());
    }

    @Test
    void testLast_whenNoElements() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        Assert.assertNull(sortedTreeList.last());
    }

    @Test
    void testContains() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        final Integer[] testSampleArray = getSampleArrayWithUniqueElements(2);
        int mid = testSampleArray.length / 2 + 1;
        final Integer[] inserted = Arrays.copyOfRange(testSampleArray, 0, mid);
        final Integer[] notInserted = Arrays.copyOfRange(testSampleArray, mid, testSampleArray.length);
        insertElementsFromArray(inserted, sortedTreeList);

        for (Integer i : inserted) {
            Assert.assertTrue(sortedTreeList.contains(i));
        }
        for (Integer j : notInserted) {
            Assert.assertFalse(sortedTreeList.contains(j));
        }
    }

    @Test
    void testFind() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final Integer[] testSampleArray = getSampleArrayWithRepeatedElements(1, 0.75);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        final Integer[] uniqueTestSampleArray = getUniqueElements(testSampleArray);
        for (Integer i : uniqueTestSampleArray) {
            final ListNode<Integer> node = sortedTreeList.find(i);

            Assert.assertNotNull(node);
            Assert.assertEquals(i, node.getData());
        }
    }

    @Test
    void testFind_whenNotContained() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        final Integer[] testSampleArray = getSampleArrayWithUniqueElements(2);
        int mid = testSampleArray.length / 2 + 1;
        final Integer[] inserted = Arrays.copyOfRange(testSampleArray, 0, mid);
        final Integer[] notInserted = Arrays.copyOfRange(testSampleArray, mid, testSampleArray.length);
        insertElementsFromArray(inserted, sortedTreeList);

        for (Integer i : notInserted) {
            Assert.assertNull(sortedTreeList.find(i));
        }
    }

    @Test
    void testFind_withNoElements() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final ListNode<Integer> node = sortedTreeList.find(random.nextInt(MAX_SIZE));

        Assert.assertNull(node);
    }

    @Test
    void testFindFirst() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final Integer[] testSampleArray = getSampleArrayWithRepeatedElements(1, 0.75);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        final Integer[] uniqueTestSampleArray = getUniqueElements(testSampleArray);
        for (Integer i : uniqueTestSampleArray) {
            final ListNode<Integer> node = sortedTreeList.findFirst(i);

            Assert.assertNotNull(node);
            Assert.assertEquals(i, node.getData());
            Assert.assertTrue(node.getPrev() == null || node.getPrev().getData().compareTo(i) < 0);
        }
    }

    @Test
    void testFindFirst_whenNotContained() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        final Integer[] testSampleArray = getSampleArrayWithUniqueElements(2);
        int mid = testSampleArray.length / 2 + 1;
        final Integer[] inserted = Arrays.copyOfRange(testSampleArray, 0, mid);
        final Integer[] notInserted = Arrays.copyOfRange(testSampleArray, mid, testSampleArray.length);
        insertElementsFromArray(inserted, sortedTreeList);

        for (Integer i : notInserted) {
            Assert.assertNull(sortedTreeList.findFirst(i));
        }
        Assert.assertNull(sortedTreeList.findFirst(MAX_SIZE));
    }

    @Test
    void testFindFirst_withNoElements() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final ListNode<Integer> node = sortedTreeList.findFirst(random.nextInt(MAX_SIZE));

        Assert.assertNull(node);
    }

    @Test
    void testFindlast() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final Integer[] testSampleArray = getSampleArrayWithRepeatedElements(1, 0.75);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        final Integer[] uniqueTestSampleArray = getUniqueElements(testSampleArray);
        for (Integer i : uniqueTestSampleArray) {
            final ListNode<Integer> node = sortedTreeList.findLast(i);

            Assert.assertNotNull(node);
            Assert.assertEquals(i, node.getData());
            Assert.assertTrue(node.getNext() == null || node.getNext().getData().compareTo(i) > 0);
        }
    }

    @Test
    void testFindLast_whenNotContained() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        final Integer[] testSampleArray = getSampleArrayWithUniqueElements(2);
        int mid = testSampleArray.length / 2 + 1;
        final Integer[] inserted = Arrays.copyOfRange(testSampleArray, 0, mid);
        final Integer[] notInserted = Arrays.copyOfRange(testSampleArray, mid, testSampleArray.length);
        insertElementsFromArray(inserted, sortedTreeList);

        for (Integer i : notInserted) {
            Assert.assertNull(sortedTreeList.findLast(i));
        }
        Assert.assertNull(sortedTreeList.findLast(-1));
    }

    @Test
    void testFindLast_withNoElements() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final ListNode<Integer> node = sortedTreeList.findLast(random.nextInt(MAX_SIZE));

        Assert.assertNull(node);
    }

    @Test
    void testPopFirst() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final Integer[] testSampleArray = getSampleArray(1);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        final Integer first = sortedTreeList.popFirst();
        final Integer[] resultArray = getArrayFromSortedTreeList(sortedTreeList);
        Arrays.sort(testSampleArray);

        Assert.assertEquals(testSampleArray[0], first);
        Assert.assertEquals(testSampleArray.length - 1, sortedTreeList.size());
        Assert.assertArrayEquals(Arrays.copyOfRange(testSampleArray, 1, testSampleArray.length), resultArray);
        Assert.assertTrue(isBalanced(sortedTreeList));
    }

    @Test
    void testPopFirst_whenNoElements() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        Assert.assertNull(sortedTreeList.popFirst());
    }

    @Test
    void testPopLast() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final Integer[] testSampleArray = getSampleArray(1);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        final Integer last = sortedTreeList.popLast();
        final Integer[] resultArray = getArrayFromSortedTreeList(sortedTreeList);
        Arrays.sort(testSampleArray);

        Assert.assertEquals(testSampleArray[testSampleArray.length - 1], last);
        Assert.assertEquals(testSampleArray.length - 1, sortedTreeList.size());
        Assert.assertArrayEquals(Arrays.copyOfRange(testSampleArray, 0, testSampleArray.length - 1), resultArray);
        Assert.assertTrue(isBalanced(sortedTreeList));
    }

    @Test
    void testPopLast_whenNoElements() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        Assert.assertNull(sortedTreeList.popLast());
    }

    @Test
    void testRemove() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(false);

        final Integer[] testSampleArray = getSampleArrayWithUniqueElements(1);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        final int r = random.nextInt(testSampleArray.length);
        final Integer sample = testSampleArray[r];

        sortedTreeList.remove(sample);

        Assert.assertFalse(sortedTreeList.contains(sample));
        Assert.assertEquals(testSampleArray.length - 1, sortedTreeList.size());
        Assert.assertTrue(isBalanced(sortedTreeList));
    }

    @Test
    void testRemove_allElements() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final Integer[] testSampleArray = getSampleArray(100);
        insertElementsFromArray(testSampleArray, sortedTreeList);

        scarambleArray(testSampleArray);
        for (int i = 0; i < testSampleArray.length; i++) {
            Integer sample = testSampleArray[i];
            sortedTreeList.remove(sample);

            //Assert.assertFalse(sortedTreeList.contains(sample));
            Assert.assertEquals(testSampleArray.length - 1 - i, sortedTreeList.size());
            Assert.assertTrue(isBalanced(sortedTreeList));
        }
    }


    private Integer[] getSampleArrayWithUniqueElements(final int minSize) {
        final int n = random.nextInt(MAX_SIZE - minSize + 1) + minSize;
        final Integer[] array = new Integer[n];

        for (int i = 0; i < n; i++) {
            array[i] = i;
        }

        for (int i = 0; i < n; i++) {
            int j = random.nextInt(n);
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }

        return array;
    }

    private Integer[] getSampleArrayWithRepeatedElements(final int minSize, final double uniquenessRate) {
        final int n = random.nextInt(MAX_SIZE - minSize + 1) + minSize;
        final Integer[] array = new Integer[n];

        final int m = (int) (n * uniquenessRate) + 1;

        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(m);
        }

        return array;
    }

    private Integer[] getSampleArray(final int minSize) {
        final int n = random.nextInt(MAX_SIZE - minSize + 1) + minSize;
        final Integer[] array = new Integer[n];

        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(n);
        }

        return array;
    }

    private Integer[] getUniqueElements(final Integer[] array) {
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (Integer i : array) {
            set.add(i);
        }

        return set.toArray(new Integer[set.size()]);
    }

    private <T> void scarambleArray(final T[] array) {
        final int n = array.length;
        for (int i = 0; i < n; i++) {
            int j = random.nextInt(n);
            T tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    private Integer[] getArrayFromSortedTreeList(final SortedTreeList<Integer> sortedTreeList) {
        return getArrayFromSortedTreeList(sortedTreeList, new Integer[(int) sortedTreeList.size()]);
    }

    private <T extends Comparable<T>> void insertElementsFromArray(
            final T[] array, final SortedTreeList<T> sortedTreeList) {
        for (int i = 0; i < array.length; i++) {
            sortedTreeList.insert(array[i]);
        }
    }

    private <T extends Comparable<T>> T[] getArrayFromSortedTreeList(
            final SortedTreeList<T> sortedTreeList, final T[] array) {
        ListNode<T> current = sortedTreeList.getHead();

        int i = 0;
        while (current != null) {
            array[i++] = current.getData();
            current = current.getNext();
        }

        return array;
    }

    private <T> long getHeight(final TreeNode<T> root) {
        if (root == null) {
            return -1;
        }

        return 1 + Math.max(getHeight(root.getLeft()), getHeight(root.getRight()));
    }

    private <T> long getBalancefactor(final TreeNode<T> root) {
        if (root == null) {
            return 0;
        }

        return getHeight(root.getLeft()) - getHeight(root.getRight());
    }

    private <T> boolean isBalanced(final TreeNode<T> root) {
        if (root == null) {
            return true;
        }

        return isBalanced(root.getLeft()) && 
               isBalanced(root.getRight()) &&
               Math.abs(getBalancefactor(root)) < 2;
    }

    private <T extends Comparable<T>> boolean isBalanced(final SortedTreeList<T> sortedTreeList) {
        return isBalanced(sortedTreeList.getRoot());
    }
}
