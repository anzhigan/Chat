import java.io.*;
import java.net.Socket;

class ServerForOnce implements Runnable{
    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;
    private int ID = -1;

    ServerForOnce(Socket socket){
        this.socket = socket;
        this.ID = socket.getPort();
        open();
    }

    void open(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){//выводит в консоль Server то, что написал клиент
        System.out.println("Подключился новый клиент: " + ID);

        try {
            while (true) {
                    String word = in.readLine();
                    // отослать принятое сообщение
                    if(!word.equals("stop")) {
                        for (ServerForOnce serverForOnce : Server.list){
                            if (!serverForOnce.equals(this)) {
                                serverForOnce.out.write(ID + ": " + word + "\n");
                                serverForOnce.out.flush();
                            }
                        }
                    }else{
                        break;
                    }
                }
            } catch (IOException | NullPointerException e) {
                close();
        }
    }

    private void close(){//закрываем все потоки
        try {
            if(socket != null && in != null && out != null){
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(ID +" Вышел из чата");
        }

    }

}