package datastructures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

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
        for (int i = 0; i < testSampleArray.length; i++) {
            sortedTreeList.insert(testSampleArray[i]);
        }

        final Integer[] resultArray
            = getArrayFromSortedTreeList(sortedTreeList, new Integer[(int) sortedTreeList.size()]);
        Arrays.sort(testSampleArray);

        Assert.assertArrayEquals(testSampleArray, resultArray);
        Assert.assertTrue(isBalanced(sortedTreeList.getRoot()));
    }

    @Test
    void testInsert_withRepetitionsAllowed() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);

        final Integer[] testSampleArray = getSampleArray(1);
        for (int i = 0; i < testSampleArray.length; i++) {
            sortedTreeList.insert(testSampleArray[i]);
        }

        final Integer[] resultArray
            = getArrayFromSortedTreeList(sortedTreeList, new Integer[(int) sortedTreeList.size()]);
        Arrays.sort(testSampleArray);

        Assert.assertArrayEquals(testSampleArray, resultArray);
        Assert.assertTrue(isBalanced(sortedTreeList.getRoot()));
    }

    @Test
    void testFirstAndLast() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        final Integer[] testSampleArray = getSampleArrayWithUniqueElements(1);
        insertElementsFromArray(testSampleArray, sortedTreeList);
        final Integer first = sortedTreeList.first();
        final Integer last = sortedTreeList.last();

        Arrays.sort(testSampleArray);

        Assert.assertEquals(testSampleArray[0], first);
        Assert.assertEquals(testSampleArray[testSampleArray.length - 1], last);
    }

    @Test
    void testFirstAndLast_whenNoElements() {
        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();

        final Integer first = sortedTreeList.first();
        final Integer last = sortedTreeList.last();

        Assert.assertNull(first);
        Assert.assertNull(last);
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

    private Integer[] getSampleArray(final int minSize) {
        final int n = random.nextInt(MAX_SIZE - minSize + 1) + minSize;
        final Integer[] array = new Integer[n];

        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(n);
        }

        return array;
    }

    /*
    private int[] getSecuentialTestingArray() {
        final int n = random.nextInt(MAX_SIZE) + 1;
        final int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i;
        }

        return array;
    }

    private int[] getRandomArray(final int minSize, final int maxSize) {
        final int n = random.nextInt(maxSize - minSize + 1) + minSize;
        final int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(n);
        }

        return array;
    }

    private int[] getRandomArray() {
        return getRandomArray(1, MAX_SIZE);
    }

    private int[] copyAndScrambleArray(final int[] array) {
        final int[] scrambled = Arrays.copyOf(array, array.length);
        scrambleArray(scrambled);
        return scrambled;
    }

    private void scrambleArray(final int[] array) {
        int n = array.length;
        for (int i = 0; i < n; i++) {
            int j = random.nextInt(n);
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }
    */

    private <T extends Comparable<T>> void insertElementsFromArray(
            final T[] array, final SortedTreeList<T> sortedTreeList) {
        for (int i = 0; i < array.length; i++) {
            sortedTreeList.insert(array[i]);
        }
    }

    private <T extends Comparable<T>> T[] getArrayFromSortedTreeList(
            final SortedTreeList<T> sortedTreeList, final T[] array) {
        final Iterator<T> iterator = sortedTreeList.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            array[i++] = iterator.next();
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
}
