package com.orange.dynamic.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.orange.dynamic.Compent;
import com.orange.dynamic.consts.ServerConsts;
import com.orange.dynamic.listener.DefaultServerHandler;
import com.orange.dynamic.listener.ServerHandler;

public class DomainServer implements Compent{
	private int serverPort = ServerConsts.SERVER_DEFAULT_PORT; 
	public DomainServer(){}
	public DomainServer(int serverPort){
		this.serverPort = serverPort;
	}
    //select方法等待信道准备好的最长时间
    private static final int TIMEOUT = 3000;
	private ServerSocketChannel serverSocket;
	private Logger logger = Logger.getAnonymousLogger(); 
	
	@Override
	public void start() {
		try {
			//创建一个选择器
			Selector selector = Selector.open();
			//实例化一个信道
			serverSocket = ServerSocketChannel.open();
	        //将该信道绑定到指定端口
			serverSocket.socket().bind(new InetSocketAddress(serverPort));
	        //配置信道为非阻塞模式
			serverSocket.configureBlocking(false);
	        //将选择器注册到各个信道
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);
			ServerHandler handler = new DefaultServerHandler();
			while (true){
	            //一直等待,直至有信道准备好了I/O操作
	            if (selector.select(TIMEOUT) == 0){
	                //在等待信道准备的同时，也可以异步地执行其他任务，
	                System.out.print(".");
	                continue;
	            }
	            //获取准备好的信道所关联的Key集合的iterator实例
	            Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
	            //循环取得集合中的每个键值
	            while (keyIter.hasNext()){
	                SelectionKey key = keyIter.next(); 
	                //如果服务端信道感兴趣的I/O操作为accept
	                if (key.isAcceptable()){
	                	handler.handleAccept(key);
	                }
	                //如果客户端信道感兴趣的I/O操作为read
	                if (key.isReadable()){
	                	handler.handleRead(key);
	                }
	                //如果该键值有效，并且其对应的客户端信道感兴趣的I/O操作为write
	                if (key.isValid() && key.isWritable()) {
	                	handler.handleWrite(key);
	                }
	                //这里需要手动从键集中移除当前的key
	                keyIter.remove(); 
	            }
	        }
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public void stop() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
