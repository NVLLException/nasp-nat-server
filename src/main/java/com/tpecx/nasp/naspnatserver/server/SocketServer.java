package com.tpecx.nasp.naspnatserver.server;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Component
public class SocketServer {

    @Value("${server.socket.hostname}")
    private String hostName;

    @Value("${server.socket.port}")
    private Integer port;

    @Value("${server.accept.timeout}")
    private Integer timeout;

    @Value("${data.transfer.size}")
    private Integer dataSize;

   @Async
   public void run() {
        try {
            InetSocketAddress address = new InetSocketAddress(hostName, port);
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(address);
            serverChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(true) {
                int keysCount = selector.select(timeout);
                if(keysCount < 1) {
                    continue;
                }
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while (iterator.hasNext()) {
                    SelectionKey sKey = iterator.next();
                    if (sKey.isAcceptable()) {
                        //新的连接请求
                        ServerSocketChannel sChannel = (ServerSocketChannel)sKey.channel();
                        SocketChannel clientChannel = sChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                    } else if(sKey.isConnectable()) {
                        //todo
                    } else if(sKey.isReadable()) {
                        doReadable(sKey);
                    } else if(sKey.isWritable()) {
                        //todo
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理读请求
     * @param sKey
     */
    private void doReadable(SelectionKey sKey) throws IOException {
        SocketChannel clientChannel = (SocketChannel)sKey.channel();
        System.out.println(clientChannel.socket().getInetAddress());
        System.out.println(clientChannel.socket().getPort());
      /*  SocketChannel clientChannel = (SocketChannel)sKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(dataSize);
        int readSize = clientChannel.read(buffer);
        sKey.attachment();
        while (readSize != -1) {
            buffer.flip();
            while(buffer.hasRemaining()) {
                byte bytez = buffer.get();
            }
            buffer.clear();
            readSize = clientChannel.read(buffer);
        }
        clientChannel.close();*/
    }

}
