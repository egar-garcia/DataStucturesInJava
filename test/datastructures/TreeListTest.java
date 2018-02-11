package datastructures;

import java.util.Iterator;
import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class TreeListTest {

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
	    Random random = new Random();
	    final int n = random.nextInt(100) + 1;
	    final int[] baseArr = new int[n];
	    final int[] testArr = new int[n];

	    for (int i = 0; i < n; i++) {
	        baseArr[i] = testArr[i] = i;
	    }
	    for (int i = 0; i < n; i++) {
	        int j = random.nextInt(n);
	        int tmp = testArr[i];
	        testArr[i] = testArr[j];
	        testArr[j] = tmp;
	    }

        final TreeList<Integer> treeList = new TreeList<>();
        for (int i = 0; i < n; i++) {
            treeList.insert(testArr[i]);
        }

        final int[] resultArr = new int[n];
        int k = 0;
        Iterator<Integer> it = treeList.iterator();
        while (it.hasNext()) {
            int r = it.next();
            resultArr[k++] = r;
            System.out.print(r + " ");
            //resultArr[k++] = it.next();
        }
        System.out.println("\nSIZE: " + treeList.size());
        System.out.println("HEIGHT: " + getHeight(treeList.getRoot()));
        Assert.assertEquals(n, treeList.size());
        Assert.assertArrayEquals(baseArr, resultArr);
        Assert.assertTrue(isBalanced(treeList.getRoot()));
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
