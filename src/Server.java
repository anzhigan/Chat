/* Created by _anzhigun. Training chat */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class Server implements Runnable {
    private ServerSocket serverSocket = null;
    private Thread serverThread = null;
    static ArrayList<ServerForOnce> list = new ArrayList<>();

    private Server(int PORT) {
        System.out.println("Сервер включен!");
        try {
            serverSocket = new ServerSocket(PORT);
            start();//создаем нить для сервера сразу в конструкторе
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void run(){
        System.out.println("Ожидаем клиентов...");
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                addNewClient(socket);
            } catch (IOException | NullPointerException e) {
                stop();
            }
        }
    }

    private void addNewClient(Socket socket){
        ServerForOnce serverForOnce = new ServerForOnce(socket);
        list.add(serverForOnce);
        Thread thread = new Thread(serverForOnce);
        thread.start();
    }

    private void start(){//окрываем нить сервака
        if(serverThread == null){
            serverThread = new Thread(this);
            serverThread.start();
        }
    }

    private void stop(){//закрываем нить сервака
        if(serverThread != null){
            serverThread.stop();
        }
    }

    public static void main(String[] args){
        final int PORT = 322;
        new Server(PORT);
    }

}
