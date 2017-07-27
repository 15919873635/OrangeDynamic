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
    //select�����ȴ��ŵ�׼���õ��ʱ��
    private static final int TIMEOUT = 3000;
	private ServerSocketChannel serverSocket;
	private Logger logger = Logger.getAnonymousLogger(); 
	
	@Override
	public void start() {
		try {
			//����һ��ѡ����
			Selector selector = Selector.open();
			//ʵ����һ���ŵ�
			serverSocket = ServerSocketChannel.open();
	        //�����ŵ��󶨵�ָ���˿�
			serverSocket.socket().bind(new InetSocketAddress(serverPort));
	        //�����ŵ�Ϊ������ģʽ
			serverSocket.configureBlocking(false);
	        //��ѡ����ע�ᵽ�����ŵ�
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);
			ServerHandler handler = new DefaultServerHandler();
			while (true){
	            //һֱ�ȴ�,ֱ�����ŵ�׼������I/O����
	            if (selector.select(TIMEOUT) == 0){
	                //�ڵȴ��ŵ�׼����ͬʱ��Ҳ�����첽��ִ����������
	                System.out.print(".");
	                continue;
	            }
	            //��ȡ׼���õ��ŵ���������Key���ϵ�iteratorʵ��
	            Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
	            //ѭ��ȡ�ü����е�ÿ����ֵ
	            while (keyIter.hasNext()){
	                SelectionKey key = keyIter.next(); 
	                //���������ŵ�����Ȥ��I/O����Ϊaccept
	                if (key.isAcceptable()){
	                	handler.handleAccept(key);
	                }
	                //����ͻ����ŵ�����Ȥ��I/O����Ϊread
	                if (key.isReadable()){
	                	handler.handleRead(key);
	                }
	                //����ü�ֵ��Ч���������Ӧ�Ŀͻ����ŵ�����Ȥ��I/O����Ϊwrite
	                if (key.isValid() && key.isWritable()) {
	                	handler.handleWrite(key);
	                }
	                //������Ҫ�ֶ��Ӽ������Ƴ���ǰ��key
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
