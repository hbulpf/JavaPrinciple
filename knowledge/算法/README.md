# 基本数据结构与算法

1. 线性表
   1. 顺序存储
      1. 顺序表
   2. 链式存储
      1. 指针实现
         1. 单链表
         2. 双链表
         3. 循环链表
      2. 数组实现
         1. 静态链表   
2. 树
   1. 二叉树
      1. 线索二叉树
      2. 排序二叉树
         1. 二叉排序树
         2. [平衡二叉树](tree/bitree/link/balanced/BalancedTree.java): 一个二叉树每个节点的左右两个子树的高度差的绝对值不超过1。
      3. BTree
         1. 基本BTree 
         2. B+Tree
      4. 哈夫曼树
      5. 红黑树
   2. 多叉树
3. 图
   1. 图的存储
      1. 邻接矩阵，邻接表
      2. 邻接多重表，狮子链表
   2. 图的遍历
      1. BFS
      2. DFS
   3. 应用
      1. 最小生成树
         1. Prim算法
         2. Kruskal算法
      2. 最短路径
         1. Dijkstra算法
         2. Floy算法
      3. 拓扑排序 AOV网
      4. 关键路径 AOE网
4. 排序
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
    2. integer sort
        1. 计数排序 counting sort
        2. 桶排序 bucket sort
        3. 基数排序 radix sort
5. 堆/栈/队列
   1. [栈](../../src/main/dev/algorithm/stack)
      1. 顺序栈
      2. 链栈
      3. 共享栈
   2. [队列](queue)
      1. 链式队列
      2. 循环队列
      3. 双端队列
   3. [堆](../../src/main/dev/algorithm/heap)
       1. 大根堆
       2. 小根堆
6. 查找
   1. 顺序查找
   2. 折半查找
   3. 分块查找
   4. Hash查找

## 线性表

### 顺序表

1. 使用连续存储的单元
2. 线性表数组大小和空间在动态分配下可以调整，但也是申请一块更大的空间替换原始空间
3. 随机访问：查找某个下标的元素, 复杂度为O(1)；按元素值查找某个元素，复杂度为O(N) ； 插入和增加需要大量元素，复杂度为O(N)

### [链表](../../src/main/dev/algorithm/linkedlist)


## [树](../../src/main/dev/algorithm/tree)

1. 二叉树
    * [平衡二叉树](tree/bitree/link/balanced/BalancedTree.java): 一个二叉树每个节点的左右两个子树的高度差的绝对值不超过1。
2. BTree
    * 基本BTree 
    * B+Tree
3. 多叉树
4. 红黑树

## [图](../../src/main/dev/algorithm/graph)


## [排序算法](../../src/main/dev/algorithm/sort)


### 复杂度概览

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


## 堆/栈/队列

1. [栈](../../src/main/dev/algorithm/stack)
1. [队列](queue)
1. [堆](../../src/main/dev/algorithm/heap)
    1. 大根堆
    2. 小根堆


# [五大常用算法](五大常用算法.md)

1. [动态规划](../../src/main/dev/algorithm/dp)
1. [贪心](../../src/main/dev/algorithm/greedy)
1. [分治]()
1. [回溯]()
1. [分支界定]()

# 参考

1. [常见十大(内部)排序算法](https://blog.csdn.net/real_lisa/article/details/82685407)
1. [常见排序算法的总结 - 复杂度、实现和稳定性](https://www.jianshu.com/p/916b15eae350)