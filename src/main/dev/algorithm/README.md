# 基本数据结构与算法

### [链表](linkedlist)

### [树](tree)

1. 二叉树
    1. 平衡二叉树
2. BTree
    1. B+Tree

### [图](graph)

### 堆/栈/队列

1. [栈](stack)

1. [队列](queue)

1. [堆](heap)
    1. 大根堆
    2. 小根堆

# [排序算法](sort)

1. 链表
1. 树
1. 图
1. 排序
    1. comparison sort
        1. 插入排序 insertion
            1. 插入排序 insertion sort
            2. 希尔排序 shell sort
        2. 交换排序 swap
            1. 冒泡排序 bubble sort
            2. 快速排序 quick sort
        3. 选择排序
            1. 选择排序 selection sort
            2. 堆排序 heap sort
        4. 归并排序 merge sort
    1. integer sort
        1. 计数排序 counting sort
        2. 桶排序 bucket sort
        3. 基数排序 radix sort

## 复杂度分析

最基础的是选择和插入，基于选择和插入分别改进出了冒泡和希尔。基于二分思想又提出了归并、快排和堆排序。最后基于数据的分布特征，提出了基数排序。这些排序算法的主要指标总结如下。

| 算法 | 最好时间 | 最坏时间 |   平均时间    | 额外空间 | 稳定性 |
| :--: | :------: | :------: | :-----------: | :------: | :----: |
| 选择 |   N^2    |   N^2    |      N^2      |    1     | 不稳定 |
| 冒泡 |    N     |   N^2    |      N^2      |    1     |  稳定  |
| 插入 |    N     |   N^2    |      N^2      |    1     |  稳定  |
| 希尔 |    N     |   N^2    | N^1.3(不确定) |    1     | 不稳定 |
| 归并 |  N*logN  |  N*logN  |    N*logN     |    N     |  稳定  |
| 快排 |  N*logN  |   N^2    |    N*logN     | logN至N  | 不稳定 |
|  堆  |  N*logN  |  N*logN  |    N*logN     |    1     | 不稳定 |
| 基数 |   N*k    |   N*k    |      N*k      |   N+k    |  稳定  |


# [其他常见算法](五大常用算法.md)

1. [动态规划](dp)
1. [贪心](greedy)
1. [分治]()
1. [回溯]()
1. [分支界定]()

# 参考

1. [常见十大(内部)排序算法](https://blog.csdn.net/real_lisa/article/details/82685407)
1. [常见排序算法的总结 - 复杂度、实现和稳定性](https://www.jianshu.com/p/916b15eae350)