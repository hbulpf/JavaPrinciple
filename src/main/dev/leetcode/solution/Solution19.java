package dev.leetcode.solution;

/**
 * 19. 删除链表的倒数第N个节点
 * https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list/
 *
 * @Author: RunAtWorld
 * @Date: 2020/4/15 23:28
 */
public class Solution19 {
    public static void main(String[] args) {
        Solution19 solution19 = new Solution19();
        ListNode[] arr = new ListNode[]{
                new ListNode(1),
                new ListNode(2),
                new ListNode(3),
                new ListNode(4),
                new ListNode(5),
        };
        ListNode head = arr[0];
        ListNode cur = arr[0];
        for (int i = 1; i < arr.length; i++) {
            arr[i - 1].next = arr[i];
        }
        int n = 2;
        System.out.println("\n初始链表");
        while (cur != null) {
            System.out.printf("%s,", cur.val);
            cur = cur.next;
        }
        ListNode res = solution19.removeNthFromEnd(head, n);
        System.out.println("\n处理结果");
        while (res != null) {
            System.out.printf("%s,", res.val);
            res = res.next;
        }
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        return removeNthFromEnd2(head, n);
    }

    public ListNode removeNthFromEnd1(ListNode head, int n) {
        return null;
    }

    /**
     * 一次遍历算法
     * 注意:
     * head 是第1个节点，在处理第1个节点之前，要加上头结点。
     * 否则，对于[1,2],2 这样的测试用例就会输出 []
     *
     * @param head
     * @param n
     * @return
     */
    public ListNode removeNthFromEnd2(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        for (int i = 1; i <= n; i++) {
            second = second.next;
        }
        while (second.next != null) {
            first = first.next;
            second = second.next;
        }
        first.next = first.next.next;
        return dummy.next;
    }
}
