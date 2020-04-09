package test.leetcode.solution;

import dev.leetcode.solution.Solution703;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @Author: RunAtWorld
 * @Date: 2020/4/3 0:00
 */
public class Solution703Test {

    @Test
    public void add(){
        System.out.println("=================");
        Solution703 solution = new Solution703();
        solution.KthLargest(5, new int[]{1, 2, 5, 4, 6, 2, 9, 21, 89});
        assertEquals(solution.add(5),5) ;
        solution.KthLargest(5, new int[]{1, 4, 6, 2});
        assertEquals(solution.add(3),1) ;
        solution.KthLargest(3, new int[]{1, 2, 6, 2, 9});
        assertEquals(solution.add(7),6) ;
        System.out.println("test OK~");
        System.out.println("=================");
    }
}
