##AbstractRoutingDataSource动态数据源切换
>  上周末，室友通宵达旦的敲代码处理他的多数据源的问题，搞的非常的紧张，也和我聊了聊天，大概的了解了他的业务的需求。一般的情况下我们都是使用SSH或者SSM框架进行处理我们的数据源的信息。
>   操作数据一般都是在DAO层进行处理，可以选择直接使用JDBC进行编程（<http://blog.csdn.net/yanzi1225627/article/details/26950615/>）
>   或者是使用多个DataSource 然后创建多个SessionFactory，在使用Dao层的时候通过不同的SessionFactory进行处理，不过这样的入侵性比较明显，一般的情况下我们都是使用继承HibernateSupportDao进行封装了的处理，如果多个SessionFactory这样处理就是比较的麻烦了，修改的地方估计也是蛮多的
>   最后一个，也就是使用AbstractRoutingDataSource的实现类通过AOP或者手动处理实现动态的使用我们的数据源，这样的入侵性较低，非常好的满足使用的需求。比如我们希望对于读写分离或者其他的数据同步的业务场景

