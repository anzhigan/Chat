import java.io.IOException;

public class Main2 {
    public static void main(String args[])
    {
        int PORT = 322;
        try {
            Client client2 = new Client(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}