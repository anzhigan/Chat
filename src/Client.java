/* Created by _anzhigun. Training chat */

import java.io.*;
import java.net.Socket;

class Client{
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private BufferedReader console;

    Client(int Port) throws IOException {//создаем клинта и подключаем его к серваку "localhost" с портом 322
        System.out.println("Соединение с портом " + Port +  " устанавливается");
        try{//считываем написанные клинтом символы и отправляем их на сервак
            socket = new Socket("localhost", Port);
            open();
            System.out.println("Соединение установлено!");
            new ReadMsg().start();
            new WriteMsg().start();

        }catch (IOException ioe){
            close();
        }

    }

    private void open() throws  IOException{
        console = new BufferedReader(new InputStreamReader(System.in));//поток считывания символов с консоли
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//поток на запись
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//поток на чтение
    }

    private void close() throws IOException{
        if (console != null) console.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
    }


    // нить чтения
    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                    str = in.readLine(); // ждем сообщения с сервера
                    if (str.equals("stop")) {
                        Client.this.close();
                        this.stop();
                        break; // выходим из цикла если пришло "stop"
                    }else{
                        System.out.println(str); // пишем сообщение с сервера на консоль
                    }
                }
            } catch (IOException e) {
                try {
                    Client.this.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // отправляющая нить
    public class WriteMsg extends Thread {
        @Override
        public void run() {
            while (true) {
                String userWord;
                try {
                    userWord = console.readLine(); // считываем поток с консоли
                    if (userWord.equals("stop")) {
                        Client.this.close();
                        this.stop();
                        break; // выходим из цикла если пришло "stop"
                    } else {
                        out.write(userWord + "\n"); // отправляем на сервер
                        out.flush();
                    }
                } catch (IOException e) {
                    try {
                        Client.this.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        }
    }

}


