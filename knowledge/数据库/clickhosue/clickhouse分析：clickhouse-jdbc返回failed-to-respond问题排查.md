原因
使用clickhouse jdbc时，经常返回failed to respond，首先判断该错误是clickhouse返回的还是jdbc的错误。

jdbc库分析
jdbc异常抛出的情况：

public class ClickHouseException extends SQLException {
    public ClickHouseException(int code, Throwable cause, String host, int port) {
        super("ClickHouse exception, code: " + code + ", host: " + host + ", port: " + port + "; "
                + (cause == null ? "" : cause.getMessage()), null, code, cause);
    }
    public ClickHouseException(int code, String message, Throwable cause, String host, int port) {
        super("ClickHouse exception, message: " + message + ", host: " + host + ", port: " + port + "; "
                + (cause == null ? "" : cause.getMessage()), null, code, cause);
    }
}
1
2
3
4
5
6
7
8
9
10
出现异常时抛出的情况：
ClickHouse exception, code: 1002, host: 127.0.0.1, port: 8123; 127.0.0.1:8123 failed to respond

通过抛出异常的提示定位到jdbc抛出异常的部分ClickHouseStatementImpl中：

HttpEntity entity = null;
        try {
            uri = followRedirects(uri);
            HttpPost post = new HttpPost(uri);
            post.setEntity(requestEntity);

            HttpResponse response = client.execute(post);
            entity = response.getEntity();
            //此处抛出错误
            checkForErrorAndThrow(entity, response);

            InputStream is;
            if (entity.isStreaming()) {
                is = entity.getContent();
            } else {
                FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
                entity.writeTo(baos);
                is = baos.convertToInputStream();
            }
            return is;
        } catch (ClickHouseException e) {
            //错误为此处抛出
            throw e;
        } catch (Exception e) {
            log.info("Error during connection to {}, reporting failure to data source, message: {}", properties, e.getMessage());
            EntityUtils.consumeQuietly(entity);
            log.info("Error sql: {}", sql);
            throw ClickHouseExceptionSpecifier.specify(e, properties.getHost(), properties.getPort());
        }
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
checkForErrorAndThrow方法
private void checkForErrorAndThrow(HttpEntity entity, HttpResponse response) throws IOException, ClickHouseException {
    if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
        InputStream messageStream = entity.getContent();
        byte[] bytes = StreamUtils.toByteArray(messageStream);
        if (properties.isCompress()) {
            try {
                messageStream = new ClickHouseLZ4Stream(new ByteArrayInputStream(bytes));
                bytes = StreamUtils.toByteArray(messageStream);
            } catch (IOException e) {
                log.warn("error while read compressed stream {}", e.getMessage());
            }
        }
        EntityUtils.consumeQuietly(entity);
        String chMessage = new String(bytes, StreamUtils.UTF_8);
        throw ClickHouseExceptionSpecifier.specify(chMessage, properties.getHost(), properties.getPort());
    }
}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
jdbc的实现使用了httpclient的库，可以猜测到这个错误可能是httpclient的报错。服务器的keep-alive时间已知为3s，客户端与服务端进行通信，httpclient会复用已创建的连接，若服务端已关闭连接，客户端在沿用这个连接就会出现 failed to respond的错误。

那我们就尝试一下，看能不能复现这个错误的瞬间。

失败复现
失败测试
我们将使用一个connect完成3个请求，每个请求sleep 3s，进行测试。
三次握手：
在这里插入图片描述
发现在其中一个报错了，错误跟我们预想的一样，仔细看发现有趣的现象，出错的时间戳：42.428675，45.327281，45.431927。我们从参考文章中得知，client会判断当前连接是否存活，如果存活则直接使用。从上述时间节点发现，可能判断存活时临界取值刚好导致了认为可用，但实际使用的时候却断开了。
在这里插入图片描述
程序报错：
在这里插入图片描述
复现不是每次都会出现问题，而且sleep的时间应该设为刚好alive的时间才有大可能捕获到该报错。

解决方案
禁用HttpClient的连接复用
重试方案:http请求使用重发机制，捕获NohttpResponseException的异常，重新发送请求，重发3次后还是失败才停止
根据keep Alive时间，调整validateAfterInactivity小于keepAlive Time,但这种方法依旧不能避免同时关闭
系统主动检查每个连接的空闲时间，并提前自动关闭连接，避免服务端主动断开
对于clickhouse的jdbc来说，如果要提供方案，就要在源码层面更改：

方案1 ：修改为短连接，当然这种方式看你接不接受了，ClickHousePreparedStatementImpl中设置：
post.setHeader("Connection", "close");
1
方案2：引入重试机制，当出现这种错误的时候，莫急莫慌，再试一次
参考
文章1较详细的介绍了httpclient的机制，再将源码改为短连接，及不复用http连接，可以短暂解决这个问题，但应该有更好的方法，或者官方提供部分解决方案。
参考3为我们在clickhouse官方jdbc的留言，使用这种方法并没有生效，ClickHouseStatementImpl-> sendStream 需要同样添加短连接语句，如果有更好的解决方案或分析也可以留言讨论。

[1] : HttpClient偶尔报NoHttpResponseException: xxx failed to respond 问题分析
[2] ：httpClient： fail to respond
[3] ：clickhouse jdbc： fail to respond