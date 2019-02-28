import java.io.IOException;

public class Main1 {
    public static void main(String args[])
    {
        int PORT = 322;
        try {
            Client client = new Client(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
