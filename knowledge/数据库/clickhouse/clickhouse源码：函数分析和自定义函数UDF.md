### clickhouse函数介绍

clickhouse官方提供了许多的函数，包括常规的数学函数，聚合函数，时间函数，逻辑函数，比较函数等等，关于官方的函数可以在官方文档中查看：

[官方文档](https://clickhouse.tech/docs/zh/)

当然随着clickhouse的流行，国内也有不少的博主已经开始介绍函数的使用：

[clickhouse function](https://blog.csdn.net/vagabond6/article/details/79580371)

[clickhouse高阶函数](https://www.jianshu.com/p/5e8205edc7d9)

[clickhouse详细函数介绍](https://blog.csdn.net/u012111465/article/details/85250030)

clickhouse还支持一些自定义的逻辑函数：

例如：

```
select arrayFilter(x -> x = 10,[1,2,3,4,5,10]);
```

返回的结果为：

```
[10]
```

类似的函数还有多个，可以传入lambda表达式，上述的函数为过滤数组中等于10的数。

 

### clickhouse自定义函数

clickhouse除了上述的函数外，修改源码可以编写自己需要的函数，首先来看源码中一个简单的函数的实现过程。注：源码基于19.5.3.1版本。

- ### **源码分析sleep()函数：**

自定义函数存在于src文件夹下的Functions文件夹中。

sleep.h文件：

```
#include <unistd.h>



#include <Functions/IFunction.h>



#include <Functions/FunctionHelpers.h>



#include <Columns/ColumnConst.h>



#include <DataTypes/DataTypesNumber.h>



#include <Common/FieldVisitors.h>



#include <IO/WriteHelpers.h>



 



 



namespace DB



{



 



namespace ErrorCodes



{



    extern const int TOO_SLOW;



    extern const int ILLEGAL_COLUMN;



    extern const int BAD_ARGUMENTS;



}



 



/** sleep(seconds) - the specified number of seconds sleeps each block.



  */



 



enum class FunctionSleepVariant



{



    PerBlock,



    PerRow



};



 



template <FunctionSleepVariant variant>



class FunctionSleep : public IFunction



{



public:



    static constexpr auto name = variant == FunctionSleepVariant::PerBlock ? "sleep" : "sleepEachRow";



    static FunctionPtr create(const Context &)



    {



        return std::make_shared<FunctionSleep<variant>>();



    }



 



    /// Get the name of the function.



    String getName() const override



    {



        return name;



    }



 



    /// Do not sleep during query analysis.



    bool isSuitableForConstantFolding() const override



    {



        return false;



    }



 



    size_t getNumberOfArguments() const override



    {



        return 1;



    }



 



    DataTypePtr getReturnTypeImpl(const DataTypes & arguments) const override



    {



        WhichDataType which(arguments[0]);



 



        if (!which.isFloat()



            && !which.isNativeUInt())



            throw Exception("Illegal type " + arguments[0]->getName() + " of argument of function " + getName() + ", expected Float64",



                ErrorCodes::ILLEGAL_TYPE_OF_ARGUMENT);



 



        return std::make_shared<DataTypeUInt8>();



    }



 



    void executeImpl(Block & block, const ColumnNumbers & arguments, size_t result, size_t /*input_rows_count*/) override



    {



        const IColumn * col = block.getByPosition(arguments[0]).column.get();



 



        if (!col->isColumnConst())



            throw Exception("The argument of function " + getName() + " must be constant.", ErrorCodes::ILLEGAL_COLUMN);



 



        Float64 seconds = applyVisitor(FieldVisitorConvertToNumber<Float64>(), static_cast<const ColumnConst &>(*col).getField());



 



        if (seconds < 0)



            throw Exception("Cannot sleep negative amount of time (not implemented)", ErrorCodes::BAD_ARGUMENTS);



 



        size_t size = col->size();



 



        /// We do not sleep if the block is empty.



        if (size > 0)



        {



            /// When sleeping, the query cannot be cancelled. For abitily to cancel query, we limit sleep time.



            if (seconds > 3.0)   /// The choice is arbitrary



                throw Exception("The maximum sleep time is 3 seconds. Requested: " + toString(seconds), ErrorCodes::TOO_SLOW);



 



            UInt64 useconds = seconds * (variant == FunctionSleepVariant::PerBlock ? 1 : size) * 1e6;



            ::usleep(useconds);



        }



 



        /// convertToFullColumn needed, because otherwise (constant expression case) function will not get called on each block.



        block.getByPosition(result).column = block.getByPosition(result).type->createColumnConst(size, 0u)->convertToFullColumnIfConst();



    }



};



 



}
```

sleep.cpp文件：

```
#include <Functions/sleep.h>



#include <Functions/FunctionFactory.h>



 



 



namespace DB



{



 



void registerFunctionSleep(FunctionFactory & factory)



{



    factory.registerFunction<FunctionSleep<FunctionSleepVariant::PerBlock>>();



}



 



}
```

分析：

cpp文件中需要将函数注册即registerFunctionSleep函数。h文件中需要实现IFunction中的一些方法，主要有getName函数名，getNumberOfArguments传入参数，getReturnTypeImpl返回的类型，executeImpl为具体的执行过程，官方的sleep函数的实现还是比较简单明了的，主要部分函数的类型，在自定义中需要一一对应。

关于isSuitableForConstantFolding，sleep函数调用时是个反例，返回为false，类似于分析时是否应该评估该函数，具体的还需要再研究，以下是官方的解析，在IFunction.h中：

```
/** Should we evaluate this function while constant folding, if arguments are constants?



  * Usually this is true. Notable counterexample is function 'sleep'.



  * If we will call it during query analysis, we will sleep extra amount of time.



  */



virtual bool isSuitableForConstantFolding() const { return true; }
```

- ### 简单的无参数自定义函数sayHello()：

先看执行效果：

```
select sayHello();
```

返回结果为：

```
hello clickhouse by iceyung test!
```

具体的实现代码：

sayHello.h:

```
#include <Functions/IFunction.h>



#include <Functions/FunctionHelpers.h>



#include <DataTypes/DataTypeString.h>



#include <DataTypes/DataTypesNumber.h>



 



namespace DB



{



 



    class FunctionSayHello : public IFunction



    {



    public:



        static constexpr auto name = "sayHello";



        static FunctionPtr create(const Context &)



        {



            return std::make_shared<FunctionSayHello>();



        }



 



        /// Get the name of the function.



        String getName() const override



        {



            return name;



        }



        



        size_t getNumberOfArguments() const override



        {



            return 0;



        }



 



        DataTypePtr getReturnTypeImpl(const DataTypes & /*arguments*/) const override



        {



            return std::make_shared<DataTypeString>();



        }



 



        void executeImpl(Block & block, const ColumnNumbers & /*arguments*/, size_t result, size_t /*input_rows_count*/) override



        {



            block.getByPosition(result).column = DataTypeString().createColumnConst(1, "hello clickhouse by iceyung test!");



        }



    };



 



}
```

sayHello.cpp:

```
#include <Functions/sayHello.h>



#include <Functions/FunctionFactory.h>



 



namespace DB



{



void registerFunctionSayHello(FunctionFactory & factory)



{



    factory.registerFunction<FunctionSayHello>(FunctionFactory::CaseInsensitive);



}



 



}
```

简单分析：

cpp中registerFunctionSayHello注册函数，registerFunctionsString.cpp中注册该函数，当然你也可以在其它的文件中注册，注册比较简单，直接仿照正常的进行注册即可，此处不再赘述。

注意返回参数和返回类型的问题，返回的类型可在src的DataTypes中找到，最终executeImpl返回的类型为ColumnPtr类型，不能简单的输出，具体也可以看目前已有的函数的样例，找到符合自己的参数。

- ### 有参数自定义函数sayHello(String str)：

分析同上，将传入的参数打印出，效果如下：

```
select sayHello('clickhouse args');
```

返回：

```
hello clickhouse args by iceyung test!
```

![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC9hNmZkNjg4MTRmMjNhYjQzMzZkNTMwMWQ3MTljY2EzOC8w?x-oss-process=image/format,png)

主要为sayHello.h修改，如下:

```
#include <Functions/IFunction.h>
#include <Functions/FunctionHelpers.h>
#include <DataTypes/DataTypeString.h>
#include <DataTypes/DataTypesNumber.h>



namespace DB


{



    class FunctionSayHello : public IFunction



    {



    public:



        static constexpr auto name = "sayHello";



        static FunctionPtr create(const Context &)



        {



            return std::make_shared<FunctionSayHello>();



        }


        /// Get the name of the function.



        String getName() const override



        {



            return name;



        }

        size_t getNumberOfArguments() const override


        {


            return 1;



        }


        DataTypePtr getReturnTypeImpl(const DataTypes & arguments) const override



        {



            WhichDataType which(arguments[0]);



            if (!which.isString())

                throw Exception("Illegal type " + arguments[0]->getName() + " of argument of function " + getName() + ", expected String",                  ErrorCodes::ILLEGAL_TYPE_OF_ARGUMENT);

            return std::make_shared<DataTypeString>();

        }

        void executeImpl(Block & block, const ColumnNumbers & arguments, size_t result, size_t input_rows_count) override

        {

            const auto & col = static_cast<const ColumnConst *>(block.getByPosition(arguments[0]).column.get())->getValue<String>();

            block.getByPosition(result).column = DataTypeString().createColumnConst(input_rows_count, "hello " + col  + " by iceyung test!");

        }

    };

}
```

当出错了或者参数类型不对时，可提示错误：

```
sql> select sayHello()



[2020-03-01 23:47:56] Code: 42, e.displayText() = DB::Exception: Number of arguments for function sayHello doesn't match: passed 0, should be 1 (version 19.5.3.1)



sql> select sayHello(1)



[2020-03-01 23:48:04] Code: 43, e.displayText() = DB::Exception: Illegal type UInt8 of argument of function sayHello, expected String (version 19.5.3.1)
```

 

注意：添加新的文件后需要重新Cmake编译才能正常获取编译的内容。