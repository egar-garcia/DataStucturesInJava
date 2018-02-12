package datastructures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class SortedTreeListTest {

    private static final int MAX_SIZE = 100;

    private Random random = new Random();

    /*
    @Test
    void test() {
		TreeList<Integer> treeList = new TreeList<>();

		treeList.insert(4);
		treeList.insert(1);
        treeList.insert(8);
		treeList.insert(5);
		treeList.insert(3);
		treeList.insert(2);
        treeList.insert(7);
        treeList.insert(9);
        treeList.insert(6);

		System.out.println("Size: " + treeList.size());
		Iterator<Integer> i = treeList.iterator();
		while (i.hasNext()) {
		    System.out.println("--> " + i.next());
		}

		Assert.assertTrue(isBalanced(treeList.getRoot()));
	}
	*/

    @Test
    void test1() {
	    final int[] baseArr = getSecuentialTestingArray();
	    final int[] testArr = copyAndScrambleArray(baseArr);

        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>();
        for (int i = 0; i < testArr.length; i++) {
            sortedTreeList.insert(testArr[i]);
        }

        final int[] resultArr = getArrayFromSortedTreeList(sortedTreeList);
        Assert.assertEquals(baseArr.length, sortedTreeList.size());
        Assert.assertArrayEquals(baseArr, resultArr);
        Assert.assertTrue(isBalanced(sortedTreeList.getRoot()));
	}

    @Test
    void test2() {
        final int[] testArr = getRandomArray();

        final SortedTreeList<Integer> sortedTreeList = new SortedTreeList<>(true);
        for (int i = 0; i < testArr.length; i++) {
            sortedTreeList.insert(testArr[i]);
        }

        final int[] resultArr = getArrayFromSortedTreeList(sortedTreeList);
        Arrays.sort(testArr);
        Assert.assertEquals(testArr.length, sortedTreeList.size());
        Assert.assertArrayEquals(testArr, resultArr);
        Assert.assertTrue(isBalanced(sortedTreeList.getRoot()));
    }

    private int[] getSecuentialTestingArray() {
        final int n = random.nextInt(MAX_SIZE) + 1;
        final int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i;
        }

        return array;
    }

    private int[] getRandomArray() {
        final int n = random.nextInt(MAX_SIZE) + 1;
        final int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(n);
        }

        return array;
    }

    private int[] copyAndScrambleArray(final int[] array) {
        final int n = array.length;

        final int[] scrambled = Arrays.copyOf(array, array.length);

        for (int i = 0; i < n; i++) {
            int j = random.nextInt(n);
            int tmp = scrambled[i];
            scrambled[i] = scrambled[j];
            scrambled[j] = tmp;
        }

        return scrambled;
    }

    private int[] getArrayFromSortedTreeList(final SortedTreeList<Integer> sortedTreeList) {
        final Iterator<Integer> iterator = sortedTreeList.iterator();
        final int[] array = new int[(int) sortedTreeList.size()];
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
