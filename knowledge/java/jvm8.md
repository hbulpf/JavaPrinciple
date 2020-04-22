# Dynamic Code Evolution for Java dcevm 原理

在[hostswap dcevm](http://www.cnblogs.com/redcreen/archive/2011/06/03/2071169.html)中我们对Dynamic Code Evolution VM有了一个简单的了解,这篇文章将介绍Dynamic Code Evolution VM的实现原理。

有两个概念需要区别下:

- Dynamic Code Evolution (下文简称DCE):泛指java在运行时修改程序的技术.例如aop等.
- Dynamic Code Evolution VM(下文简称DCE VM) 是在java hotspot的一个patch,修改后的vm,支持在运行期不受限制的代码修改(DCE),可以认为DCE VM是实现DCE的一种方式.

*英文水平有限,希望不要给读者造成勿扰（有些地方感觉说的好蹩脚）,如果英文ok,还是尽量直接阅读[原文](http://ssw.jku.at/Research/Papers/Wuerthinger10a/Wuerthinger10a.pdf).*

**概述:**

​    DCE是一种java在运行时修改程序（class)的技术（有点儿动态语言的感觉）。在java（面向对象语言）中，可以通过使用新版本class替换一组class的方式来实现。DCE VM通过修改JAVA HOSTSPOT虚拟机，允许对已加载的class进行任意的修改：增加或删除变量、方法，接口的继承关系等等， 这有效的提高了开发效率。在evaluation section中显示了对虚拟机的修改没有性能上的影响，fast in-place instant update算法保证了在性能上与full gc相当。DCE VM可以在标准的开发环境中使用，不需要额外的工具或依赖。

**1.Introduction**

DCE VM通过修改jvm,在执行程序与硬件(应该确切的说是code编译后的机械码)中增加了一层,来实现代码在运行期动态修改(DCE)。DCE在现有的产品级的vm中是受限的，但在某些领域是非常有用的。我们讨论下DCE的4种主要应用：

- Debugging

  开发人员频繁的去修改程序,在修改编译后,开发人员不需要重新启动程序(每次重启都需要不少时间),继续运行即可.
  任何时间、任意的修改不需要任何其他工作

- Server applications

  关键性服务可以通过DCE进行不停机升级.这依赖于升级的安全性与正确性。我们相信这可以做到，需要在设计应用时考虑到代码的变化，并对某些预定义的点限制更新。代码升级前或升级后服务速度不会降低。

- dynamic languages

  在动态语言中,DCE是最普通的特性,但为了在静态类型虚拟机上运行动态语言，需要做多方面努力[see for example 33]。把DCE作为VM的一个机制会简化动态语言的实现。The requirement here is that small incremental changes, e.g., adding a ﬁeld or method, can be carried out fast.

- dynamic aop

  DCE也是与面向切面编程（AOP）相关的一个特性。已经有一些动态aop的工具([eg:gluonj](http://www.cnblogs.com/redcreen/archive/2011/06/03/2071944.html))运行在java hotspot vm上。这些工具能使修改后的代码立即生效。（关于此部分内容，在后续的classworking文章中将专门介绍。）

​    DCE VM专注于提高开发人员的效率.如同开发人员可以在任意位置设置断点一样,DCE VM能够在程序的任何位置完成修改，同时，它能够要求所有的线程在next safe point停止,使得java程序进入暂停状态.这些点(points),常被用来在GC执行前暂停所有的线程。换句话来讲，DCE VM使用类似GC暂停的方式来暂停应用，并替换新的class。Java VM保证了程序在运行期间，在有限的时间范围内，所有的线程到达next safepoint。当VM处于暂停状态，代码的替换被执行。

​    增强Java HostSpot VM当前对code evolution的支持(只允许方法体修改)的需求是很强烈的,这个需求是JVM 5大改进需求之一。另外，代码的快速修改带来的开发效率的提升是动态语言相比静态类型语言（比如JAVA）的优势之一。开启JAVA虚拟机的DCE功能会给java编程语言带来这个优势。

​    The main contributions of this paper are:

- DCE VM是在产品级的VM上的修改,以支持DCE.
- DCE VM致力于代码的任意修改,包括子类关系的修改。然而，它不会产生任何间接的问题，也不会有性能损耗。
- DCE VM允许不同版本的代码共存。所以代码的更新可以发生在任何时候。
- DCE VM可以在任何符合标准java debug Wire Protocol（JDWP）的IDE下使用。例如NetBeans或Eclipse

**2.Levels of Code Evolution**

​    DCE是分多个层次的.从实现复杂度的层面和Java语义的影响上,本文试图将DCE分成4个级别,如图:

 ![img](https://images.cnblogs.com/cnblogs_com/redcreen/305432/r_levels_of_code_evolution.jpg)

**Swapping Method Bodies:**替换java方法体的字节码可能是最简单的修改.方法的实现不依赖其他的字节码或类型信息等数据,因此方法体修改可以独立完成.Java HostSpot VM支持这种类型的修改.

**Adding or Removing Methods:**当修改class的方法集时,虚拟方法表(用来做动态分派)需要同步修改。同时，class的修改可能会导致子类的虚拟方法表也要同步修改（详见section3.2）。虚拟方法表中方法的索引可能会改变并导致机器码（包含固定编码）不可用（详见Section3.6）。机器码也可能包含指向已有方法的静态链接，这些方法可能已不可用。

**Adding or Removing Fields:**直到这一级别,之前的修改只是影响到VM的元信息.现在对象实例需要根据类或父类的修改做相应处理.VM需要把对象从一个旧的版本转换成一个新的版本(该版本具有不同的Fields和不同的size),我们使用修改后的mark-and-compact gc来改变object layout(详见Section 3.5).与虚拟方法表类似,field offsets在解析器(interpreter)和编译后的机器码中有多处用到.这些地方需要被正确的调整或使其不可用.

**Adding or Removing Supertypes:**在java中修改类的父类集合是DCE中最复杂的情况.Supertypes的修改意味着修改类的方法和属性。同时，类的元信息也需要被修改，来反映出新的supertype的关系.

​    方法签名或者field类型、名称的修改，VM会用两个操作来实现：添加一个成员和删除另一个成员。接口（initerfaces）的修改可以被当做是类(classes)的修改.添加或删除一个接口方法会影响到子接口(subinterfaces)及类（实现了接口的类）的接口表(interface tables),但不会影响实例。superinterfaces的修改是类似的.

​    修改Java class的另外一种情况是静态fields或静态方法集合的修改。这种修改不会影响子类或实例，但可能会让当前的代码不可用，比如类中存在static filed offsets。同时，code evolution algorighm需要决定如何初始化静态fields:要么运行the new class的static initializer(类的初始化方法),要么从旧版本的lcass中复制静态fields到新新版的class中.

   

   Java程序的修改还可以根据程序在不同的版本是否二进制兼容来分类:兼容(图Figure1中浅灰色)和不兼容(图Figure1中深灰色).对于二进制兼容的修改,old code的有效性是不受影响的.我们在新的class中把被删除或替换方法的字节码定义成不同的方法，称为old code。当更新发生在任意point（想对与vm执行来说）时，java线程有可能还在方法中执行，这时old code能够在代码修改后继续执行。

   二进制不兼容的修改may break old code。在old code中的正确语义，在new class中会不可用.这在java 语言说明和Java 虚拟机说明都是不明确的.考虑到这种情况,我们讲二进制不兼容修改进行如下分类:

**Removing Fields or Methods:**已删除或替换的方法的二进制代码可能仍然被某个正在执行的程序引用,但在新的class中已经不存在这些方法了.在old code的执行中(既然有引用,就有可能被执行),VM可能会访问到这些old code,并需要决定当调用到被删除的方法或访问到被删除的fields时如何处理.

**Removing Suppertypes:**当去除类的父类时（when narrwing the type of a class),违反了运行的java程序一个重要的不变规则:静态或非静态变量不在保持子类关系。同时,调用者与被调用者已不在兼容.

​    Section4中会介绍下我们是如何处理二进制不兼容的情况。

**3.Implementation** 

​    DCE VM的实现(implementation)是对Java HostSpot VM的修改。JavaSpot VM是一个高性能的VM内置一个解析器和两个实时的编译器（client compiler and server compiler）。DCE VM基于已有的允许修改方法体的规则,扩展成支持对已加载类型任意的修改。我们把焦点集中于在现有VM上实现代码升级（code evolution),对VM做尽量少但必须的修改，包括垃圾回收器 ,system dictionary 和类的元数。特别要说的是，我们不会对解析器和实时编译器做任何修改，并且不会对VM造成影响。

​    图Figure2是对VM修改的一个总揽。Code evolution由Java Debug Wire Protocol(JDWP)命令触发.首先,算法收集所有影响的class,并根据子类关系进行排序,然后new classes会被加载并添加到VM中,并与old classes进行区。修改后的full garbage collection做版本替换,也就是code evolution.当对code状态处理完成后，VM继续执行程序。

![img](https://images.cnblogs.com/cnblogs_com/redcreen/305432/r_dcevm_figure2.PNG)

**3.1 Class Redefinition Command**

​    我们使用JDWP 类重定义命令来触发DCE.JDWP是VM与java调试器之间的规范接口.因此修改后的vm立即可以通过JDWP协议进行调试.也就是说我们可以使用常用的使用JDWP协议的调试工具例如NetBeans或eclipse的调试功能进行代码调试,并触发类的重定义.

​    类重定义命令需要的所有重定义类已经被VM加载,如果一个类没有被加载过,也就不需要进行类的重定义,新版本的class可以直接当成初始版本加载.每个类都有一个数字来进行唯一标识,并且有一个类字节码数组.修改后的VM对类重定义指令的实现完全基于JDWP规范,不需要额外的信息来进行code evolution.

​    类重定义的前几步骤(接下来要介绍的3步)是可以与程序运行并行执行的.只有接下来的GC,在更新实例时才需要暂停所有java线程.我们使用与gc相同的安全点(safepoint)规则来暂停所有活动的线程.

​    可以理解为redefination是个类似gc的过程,只是redifination只对对象的引用关系进行处理.

**3.2 Finding Affected Types**

​    当对class的修改不只是方法体时,会间接的影响到与其有关系的类,比如子类。类中添加一个field,会隐士的在所有的子类中添加。添加一个方法，可能会改变子类的虚拟方法表。

​    因此，算法需要对redifined class的集合扩展，将受影响的子类也放入其中。在Figure3中给出了一个列子。

![img](https://images.cnblogs.com/cnblogs_com/redcreen/305432/r_dcevm_figure3.PNG)

​    ClassA 和C 被重定义了，B是A的子类，所以Class B也需要加入到重定义类的集合中，并修改为B’,B’与B类是相同的,但可能因为A的修改而具有不同的属性。因此B的metadata(包含虚拟方法表)需要重新初始化,即重新载入B。

​    同样的规则也适用于接口的重定义。

**3.3 Ordering the Types**

​    类重定义命令并不对需要重定义的类进行排序。对用户来讲，类的修改应该是原子的。我们的算法是对类及子类的关系进行拓扑排序。类或接口应该在子类重定义之前被重定义。新版本的class可以与旧版本class的父类是不兼容的。因此，父类先被替换成新的版本子类才能正常的加载。

​    为了确保class的继承关系，我们的排序应该是作用在改进后代码的关系而不是当前代码的关系。类被VM加载后其关系信息才能获取，所以我们需要在class加载前就来分析类文件，从中获取新的类关系。在例子Figure3中，需要先重定义C为C’,然后重定义A到A’,因为在新的类中,A是C的子类.

**3.4 Building a Side Universe**

​    在系统中会共存新老版本的class,这对正在执行的代码(基于old calss)来说是必要的.不同版本的类实例对象在内存中也可能是共存的。这也是解决code evolution时循环依赖的惟一解决办法。例如：B在重定义前依赖A，但同时A在重定义前又依赖B。当增加一个新的类时，我们为新的类构造了一个单独的空间(side universe),使得类空间（type universe)始终保持一致性状态.因此老版本的class不会影响其他新版本class的加载和验证.

​    Java HotSpot VM 维护了一个class的系统词典,该词典根据class名称和classloader进行查询.加载一个新的class后,我们会立即用new class替换old class.提前排好序的重定义classes保证了side universe能够正常的创建.在实例Figure3中,当我们加载class A时,对Class C的检索将返回Class C’,因为Class C在class A之前已经被重定义过.如果静态类Field名及签名匹配的情况下,VM将复制旧的Class的Static fields值倒新的Class的static fields上(并不是通过初始化类的方式).Figure4 显示了创建了size universe后类空间(universe)的状态.

![img](https://images.cnblogs.com/cnblogs_com/redcreen/305432/r_dcevm_figure3.PNG)

​    我们在内存中保持了相同class的不同版本,不同版本的class通过双向链表互联.这有助于在gc时在不同版本跳转(This helps navigating through the versions during garbage collection).系统词典中维护指向最新版本Class的引用.

**3.5 Garbage Collector Adjestments**

类重定义算法的核心部分是mark-and-compact gc算法修改:

1. Forward Pointers:该算法计算出每个存活对象的forward pointer,forward pointer指向了内存压缩后对象的内存地址.
2. Adjust Pointers:接下来遍历内存,所有的引用指针(指向old class)将被修改到新的地址(new class).
3. Comaction phase:在最后的压缩阶段,对象将被移动到新的地址.

![img](https://images.cnblogs.com/cnblogs_com/redcreen/305432/r_dcevm_figure5.PNG)

在Pointer adjustment 阶段,指向old class的引用被修改到new class的forward pointer的地址,实例引用的修改类似于compact 阶段.因此,对gc进行修改后变实现了code evolution,不仅重用了代码,并且在实例更新时保持了高性能.这更加确定了我们不需要间接的或添加的数据接口,就可以实现code evolution.下面的两个小节将主要介绍2个主要修改.

**3.5.1 Swapping Pointers**

​    当更新Class C到Class C’后,我们需要确保所有的Class C的实例也同步更新成Class C’的实例。对象实例中保存了指向对象的引用.但是在Java HotSpot VM中,没有记录一个对象被哪些实例所引用,因此需要对内存进行遍历来找到所有的实例对象.同时,系统的其他引用例如native code也需要同步更新指向old class的引用.

​    Figure5显示了gc及我们所做的修改对指针的处理过程。假定在初始内存中存在Class A的一个实例对象x和新版本的ClassA，记作A’。第一步，收集器计算所有live对象在compaction后的地址，并在每个对象中加入(install)了一个指向新地址的指针：forward pointer。在pointer adjustment阶段，对象的引用指针及类的指针被修改到新的地址上，我们拦截了这个阶段的操作，以确保在compaction之后所有指向old class A的指针指向新的地址(x指向A’).

**3.5.2 Updating Instances**

​    我们需要一个初始化新对象实例fields的策略来更新实例对象.对于名称和类型相同的field,我们采用了一个简单的算法:从旧的实例中复制属性值到新的实例中,其他所有的fileds初始化为0、null或false。

​    使用这种方式，我们能够通过高效的内存操作完成实例的更新（拷贝或填充为默认值）。更新信息对于每个class只需要计算一次，并临时将该信息存入class的元信息中。修改后的gc读取该信息，对每个实例进行内存拷贝或清除操作。这种算法相比其他自定义的转换方法要快。我们相信开发人员在调试过程中权衡易用性后，更愿意采取这种缺少了灵活性但不需要额外输入的方式。

​    实例的更新是在gc的compaction阶段完成的，所以不需要额外的内存来保持新旧实例的共存，新对象实例创建并对field复制后，老的示例会立即被销毁。（*更新class后没有发生gc啊？奇怪，难道是修改后的gc与普通的gc是分别运行的？*）

​    考虑到新对象实例的大小可能发生变化(比如class字段增加),我们调整了forward pointer的算法,并在垃圾回收器中修改.这种情况下,在Mark-and-compact gc的compaction阶段,实例在某些必要条件下不一定是被分配到内存的低位空间.而是会被分配到称为side buffer的内存高位地址上(be rescued)。否则回收器会覆盖掉没有复制过的对象,并破坏其fields值(见下例)。在所有的实例全都被copied或rescued后,sife buffer中记录的数据被用来初始被rescued的新实例。为了减少需要被复制到side buffer中对象的个数,forward pointer 算法会自动将需要放到side buffer中的对象放到内存的最高位,以便为其他对象能够正常的被复制到内存地位空间。（有点儿不太好理解，见例子吧）

![img](https://images.cnblogs.com/cnblogs_com/redcreen/305432/r_dcevm_figure6.PNG)

​    在Figure6中，x的size变大了，因此x在其目标地址中覆盖了尚未被复制的其他对象,比如y,并且覆盖了y的fields值(可以这么说么??).被修改的forward pointer 算法检测到x是个需要被reduced的实例,然后将其放到了side buffer中(内存的最高位地址).这为y和z省出了空间.把需要reduce的对象放到side buffer中显著的减少了需要被reduce的对象个数(y,z不需要放到side buffer中了),也因此减少了side buffer需要的空间.在Compaction阶段,x被复制到side buffer中,y和z被正常处理,然后.依据在side buffer中x的数据构造x的新的实例。

**3.6 State Invalidation**

​    由code evolution引起的修改违反了VM中多个规则。Java HotSpot VM在设计中并不考虑code evolution,而且做了很多不变的假定,例如field 偏移量(offset)不会改变。由于打破了这些假定,VM中有些子模块需要修改,来避免出现错误.本节将做概要性的介绍.

**3.6.1 Compiled Code**

​    在code evolution之前由实时编译器生成的机器码需要验证其有效性.大多数比较明显潜在的无效的信息是虚拟方法表索引和fields offset。另外，关于类的继承(例如一个类是否是叶子节点)和调用(例如一个调用是否可以被statically bound)的假设也变得失效。

​    Java HotSpot VM有一个内置的叫做deoptimization的机制,它可以废除经优化后的方法机器码。在stack中如果有一个方法激活（there is an activation of the method on the stack) 。stack frame将被转换为interpreter frame，代码将在interpreter中继续执行。另外，vm将通过在entry point 控制的方式使机器码将不在被执行，而是进入interpreter 。我们可以通过这种方式deoptimaze所有编译过的方法，以确保没有由在错误的假定上生成的机器码在运行。

**3.6.2 Constant Pool Cache**

​    Java HotSpot VM维护了一个类的constant pool的缓存.这种方式相比于每次读取java class 文件中constant pool获取常量的方式,明显的提高了interpreter的执行速度。original entries仅包含到fields 方法 及类的符号引用，cached entries中包含了对象元信息的直接引用。与code evolution相关的entries包含fields entries（the offset of a ﬁeld is cached）、method entries(for a statically bound call a pointer to the method meta object, for a dynamically bound call the virtual method table index is cached)。我们遍历constant pool cache entries，清除与code evolution相关的entries(例如那些重定义类的成员)。当interpreter访问一个已被清除的entry时，将被重新获取。从system dictionary 查找类时,会自动返回新版本的类,因此,entry也会被重新初始化，保持了正确的field offset 或者方法的相关信息。

**3.6.3 Class Pointer Values**

​    在java HotSpot Vm中有些数据结构依赖于类的元信息对象的真实地址,例如一个映射了类与JDWP对象的hash table（这个例子什么意思啊？）。我们需要确保在code evolution后，要重新初始化这些数据结构。在gc运行中类对象也可能被移动了，pointer swapping也可能会改变两个类对象的顺序。The just-in-time compiler uses a binary search array for compiler interface objects that depends on the order of the class objects and therefore must be resorted after a code evolution step.

**4. Binary InCompatible Changes**

​    如果old code被破坏了，那么修改就是二进制不兼容的。本节就在Section2中提到过的两种二进制不兼容的修改给出我们的处理方法。在Section7中，将讨论我们在未来使用的其他解决方案。

**4.1 Deleted Fields and Methods**

​    类方法体的修改或者是添加field或方法，old code是可以被继续执行的，不需要调用新的方法或访问新的fields。由于在系统stack中的old code可以继续执行，所以当删除了一个方法或field时，可能会导致被删除了的方法被调用或被删除了的field被访问的情况，old code处于不可用状态。Figure7中显示了这种情况的一个示例:

 ![img](https://images.cnblogs.com/cnblogs_com/redcreen/305432/r_dcevm_figure7.PNG)

​    程序在调用bar前被暂停(modified gc),foo方法在重定义成为foo’方法,同时删除了bar方法.接下来的foo方法的调用理解指向了new code,但正在执行的foo方法在old code中,此时被删除的bar方法被调用了。（就上例而言，在开发环境不是每次都能遇到,因为我们很难控制当class被redifine的时候程序正好暂停在bar的调用之前或是foo方法的调用之前.）

​    新的foo‘是正确的，因为它不在调用bar方法。有没有可能状态的转换更智能呢?比如,根据获取stack中的值和二进制码的执行位置转换成新的stack值和二进制代码位置。这是有可能实现的。但就一般情况而言，这种转换不符合用户的直觉。

​    我们当前的做法是，old method可以继续在interpreter中执行，当运行到bar的调用时，bar的引用需要重新resolve（之前我们在redefinition阶段已经清楚了constant pool cache Section3.6).Resolution会找不到这个方法,然后抛出一个NoSuchMethodException的异常。（这很容易可以理解，就如同依赖的类做了修改一样，不同的只是这是运行时删除依赖类的方法）。

**4.2 Type Narrowing**

​    class的接口或父类集合增加，old code可以正常运行。It does not use instances of the class as instances of their added interfaces or supertypes, but executes as before。相反的，如果class的接口或父类集合减小了，old code可能不在可用。Figure8显示了一个这种情况的示例。

 ![img](https://images.cnblogs.com/cnblogs_com/redcreen/305432/r_dcevm_figure8.PNG)

​    ClassB被重定义后不在继承A.现在B的实例不在可以当成A的实例来处理.如上面代码中显示,可能存在A的实例变量,该变量是B的实例变量的引用。在code evolution后，这些变量变得不可用,因为B不再是A的子类.a.foo()不再有任何意义。

​    当前的的dcevm能够正确的进行code evolution,但是foo的调用会导致VM运行终止。我们认为这在debug环境是个可以被接受的方案，我们会在Section7中讨论其他可行的解决方案。

**5. Evalution**

​    本节将对我们的实现方案从以下3方面来进行评估：

1. 讨论我们对不同级别的code evolution的支持。
2. 修改后的VM与修改前的VM 性能处于同等水平。
3. 通过micro benchmarks 来讨论修改后的gc的性能特性。

**5.1 Functional Evaluation**

​    我们的方案支持了类的任何修改。当修改是二进制兼容的，code可以按照预期执行。二进制不兼容的修改，依据程序的运行状态，可能会导致异常(方法或field不存在)或VM停止(类型不匹配)等问题。然而这些问题是不太容易出现的，因为一般情况下增加方法或属性会比删除要常用些，即使是被删除了，这个方法在类重定义后处于active状态的可能性也比较小。除了删除父类(super type,也可能是接口),java程序(主要是old code)继续运行时,在语义上是兼容的。Table1给出了在Section2中讨论的支持修改类型:

 ![img](https://images.cnblogs.com/cnblogs_com/redcreen/305432/r_dcevm_table1.PNG)

​    在程序debug时，更新代码可能带来的上述问题,比重启服务要好多了。最坏的情况是开发人员重启服务，而这是没有code evolution时必须的。由于Java标准在设计时不考虑code evolution,当出现问题时,是不太容易描述清楚的，因此，我们相信相比于隐藏问题，抛出一个异常或终止VM是可以接受的，并且可以避免产生困惑。

​    Java1.4以后，JPDA定义了类重定义的指令。VM定义了三个标识来告知调试器其code evolution的能力:canRedefineClasses,可以从定义类;canAddMethod,可以向类中添加方法;canUnrestrictedlyRedefineClasses,可以任意的修改一个类。据我们所知，dce vm 是第一个在三种标识下都能返回true的VM。允许添加一个方法到可以做任意修改的跨度太大了。基于在Section2中关于实现的复杂度，我们计划对code evolution的级别做更细致的划分。

​    由于应用领域或开发习惯的不同，很难来度量code evolution的使用情况(不同类型的修改的比例等)。Gustavsson[20]给出了一个例子来研究一个web server项目在不同版本中的修改情况。结果方法体的修改占37%，方法的增删占16%，代码的任意修改占33%，其他(比如code不能变为inactive状态或需要vm外部的修改)14%。在这个例子中我们可以将可不需要重启服务的修改从37%提高到86%。

**5.2 Effects on Normal Execution**

**5.3 Micro Benchmarks**

**6 Related Work**

**6.1 General Discussions**

**6.2 Procedural Languages**

**6.3 Object-Oriented Languages**

**6.4 Java**

**7 Future Work**

​    dce vm当前的实现是在debug的基础上,我们计划将其扩展到更新服务上.相比于debug,更新服务的安全性更高,除此之外,需要更合适的更新点。我们想将类重定义指令扩展到安全更新请求时发生。。。。todo

**8 Conclutions**

 

译文参考文章：

[Dynamic Code Evolution for the Java HotSpot TM Virtual Machine](http://wikis.sun.com/download/attachments/172493511/wuerthinger-hotswap-20090401.pdf?version=1)（version1）

[Dynamic Code Evolution for Java](http://ssw.jku.at/Research/Papers/Wuerthinger10a/Wuerthinger10a.pdf)(current version)原文