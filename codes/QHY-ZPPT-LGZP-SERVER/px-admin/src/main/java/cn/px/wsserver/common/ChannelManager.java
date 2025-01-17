package cn.px.wsserver.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @@author 品讯科技
 * @Desciption: TODO
 * @DateTime: 2022/2/12 14:20
 **/
public class ChannelManager {

    private static ChannelGroup GlobalGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ConcurrentMap<String, ChannelId> ChannelMap=new ConcurrentHashMap();

    public static void addChannel(Channel channel){
        GlobalGroup.add(channel);
        ChannelMap.put(channel.id().asShortText(),channel.id());
    }
    public static void removeChannel(Channel channel){
        GlobalGroup.remove(channel);
        ChannelMap.remove(channel.id().asShortText());
    }

    public static Channel findChannel(ChannelId id){
        return GlobalGroup.find(ChannelMap.get(id.asShortText()));
    }

    public static void send2All(TextWebSocketFrame tws){
        GlobalGroup.writeAndFlush(tws);
    }
}
