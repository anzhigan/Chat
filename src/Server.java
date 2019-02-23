/* Created by _anzhigun. Training chat */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Server implements Runnable {
    private ServerSocket serverSocket = null;
    private Thread serverThread = null;
    static int count = 0;

    private static ServerForOnce[] list = new ServerForOnce[10];

    public static ServerForOnce[] getList() {
        return list;
    }



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

    private void addNewClient(Socket socket) throws IOException {
        ServerForOnce serverForOnce = new ServerForOnce(socket, this);
        list[count] = serverForOnce;
        try{
            list[count].run();
            list[count].open();
            count++;
        } catch (IOException ioe) {
            ioe.printStackTrace();        }
    }

    private void start(){//окрываем нить сервака
        if(serverThread == null){
            serverThread = new Thread(this);
            serverThread.start();
        }
    }

    private void stop(){//закрываем нить сервака
        if(serverThread == null){
            serverThread.stop();
        }
    }

    class ServerForOnce extends Thread{
        private BufferedReader in;
        private BufferedWriter out;
        private Socket socket;
        private Server server;
        private int ID = -1;

        ServerForOnce(Socket socket, Server server) throws IOException {
            this.socket = socket;
            this.ID = socket.getPort();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }

        @Override
        public void run(){//выводит в консоль то, что написал клиент
            System.out.println("Подключился новый клиент: " + ID);
            while (true) {
                try {
                    String word = in.readLine();
                    System.out.println(ID + ": " + word);
                    // отослать принятое сообщение с
                    send(word);
                } catch (IOException e) {
                    stop();
                }
            }
        }

        void open() throws IOException{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //System.out.println("Поток для клиента: "+ ID +" создан ");
        }
        private void close(){//юзанётся в удалении в серваке
            try {if(socket != null && in != null && out != null){
                socket.close();
                in.close();
                out.close();
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(ID +" Вышел из чата");
        }

        private void send(String word){
            try {
                for (ServerForOnce i : server.getList()){
                    if (!i.equals(this)) {
                        out.write(ID + ": " + word + "\n");
                        out.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public static void main(String[] args){
        final int PORT = 322;
        new Server(PORT);
    }

}
