package handle;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;



public class ChannelInitializerImp extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel sc)  {

//        sc.pipeline().addLast(new HttpRequestDecoder());//inbound
//        sc.pipeline().addLast(new HttpObjectAggregator(65536));//inbound
//        sc.pipeline().addLast(new HttpResponseEncoder());//outbound

        /*websocket*/
        sc.pipeline().addLast("http-codec",new HttpServerCodec());
        sc.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
        sc.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
        //购买云主机的处理类
        sc.pipeline().addLast(new BuyVmHandler());



    }
}
