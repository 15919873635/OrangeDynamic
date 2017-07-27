package com.orange.dynamic.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.orange.dynamic.Compent;
import com.orange.dynamic.consts.ClientConsts;

public class DomainClient implements Compent{

	private String address = ClientConsts.CLIENT_DEFAULT_ADDRESS;
	private int serverPort = ClientConsts.SERVER_DEFAULT_PORT;
	
	private SocketChannel socketChannel = null;
	
	public DomainClient(){}
	
	public DomainClient(int serverPort){
		this.serverPort = serverPort;
	}
	
	public DomainClient(String address,int serverPort){
		this.address = address;
		this.serverPort = serverPort;
	}
	
	@Override
	public void start() {
		SocketChannel socketChannel = null;  
        try {  
            socketChannel = SocketChannel.open();  
            SocketAddress socketAddress = new InetSocketAddress(address, serverPort);  
            socketChannel.connect(socketAddress);  

            String myRequestObject = "sdsdsds";
            sendData(myRequestObject);
            Object myResponseObject = receiveData();  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } finally {  
            try {  
                socketChannel.close();  
            } catch(Exception ex) {}  
        }  
	}
	private void sendData(Object obj) throws IOException {  
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        ObjectOutputStream oos = null;
        try {  
            oos = new ObjectOutputStream(baos);  
            oos.writeObject(obj);  
            byte[] bytes = baos.toByteArray();  
            ByteBuffer buffer = ByteBuffer.wrap(bytes);  
            socketChannel.write(buffer); 
        } catch(IOException ex) {  
            throw new RuntimeException(ex.getMessage(), ex);  
        } finally {  
            try {  
                oos.close();  
            } catch (Exception e) {}  
        }
    } 
	
	private Object receiveData() 
			throws IOException{
		Object obj = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectInputStream ois = null;  
        try {  
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);  
            byte[] bytes;  
            int count = 0;  
            while ((count = socketChannel.read(buffer)) >= 0) {  
                buffer.flip();  
                bytes = new byte[count];  
                buffer.get(bytes);  
                baos.write(bytes);  
                buffer.clear();  
            }  
            bytes = baos.toByteArray(); 
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);  
            obj = ois.readObject();
            socketChannel.socket().shutdownInput();  
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {  
            try {  
                baos.close();
                ois.close();
            } catch(Exception ex) {}  
        }  
        return obj; 
	}
	
	@Override
	public void stop() {
		
	}
}
