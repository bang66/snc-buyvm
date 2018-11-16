package handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.EventExecutorGroup;
import org.apache.log4j.Logger;
import pojo.Vm;
import pojo.VmPay;

import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class BuyVmHandler extends ChannelInboundHandlerAdapter {
    private static Logger log=Logger.getLogger(BuyVmHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        FullHttpResponse response = null;
        if ((fhr.uri()).equals("/snc/buy/vm")){
            BuyVm buyVm=new BuyVm();
            ByteBuf bbuf= fhr.content();
            Charset charset = Charset.forName("utf-8");
            String json=bbuf.toString(charset);
            log.info("url----->"+fhr.uri()+"  请求的json : "+json);
            Vm vm=buyVm.getMessage(json);
            bbuf.release();

            String gson=buyVm.buy(vm);

//            String gson= "{\"status\":\"1\"}";
            log.info("url----->"+fhr.uri()+"  返回的json : "+gson);

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(gson.getBytes("UTF-8")));
            response.headers().set("Access-Control-Allow_Origin","*");
            response.headers().set(CONTENT_TYPE, "text/html");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);


        }else {
            ctx.fireChannelRead(msg);
        }
    }
}
