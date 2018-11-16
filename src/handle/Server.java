package handle;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import org.apache.log4j.Logger;


import java.io.BufferedReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    Logger logger=Logger.getLogger(Server.class);
    private static Map<String, String> configs= new ConcurrentHashMap();
    private String server_host;
    private int server_port;

    public void bind(String host,int port){
        this.server_port=port;
        this.server_host=host;
    }
    public void init() {
        BufferedReader serverIn = null;
        String conf = "";

            bind("0.0.0.0",9999);
            logger.info("server_host---->"+"0.0.0.0");
            logger.info("server_port---->"+9999);


    }

    public void  run(){
        EventLoopGroup bossgroup = new NioEventLoopGroup();

        EventLoopGroup workgroup = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();

        server.group(bossgroup, workgroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializerImp());

            ChannelFuture future = null;
        try {
            future = server.bind(server_host,server_port).sync();
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

            logger.info("wait for connection.....");
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error("catch InterruptedException Server.class 79");
            e.printStackTrace();
        }


    }

}
