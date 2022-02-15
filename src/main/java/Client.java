import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String host = "netology.homework";
        int port = 8089;

        try (Socket clientSocket = new Socket(host, port);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader input = new BufferedReader((new InputStreamReader(clientSocket.getInputStream())))) {
            String answer = inputData(input);
            System.out.println(answer);
            while (true) {
                if (answer.equals("Работа с заметками завершена.")) {
                    break;
                } else {
                    Scanner scanner = new Scanner(System.in);
                    output(scanner.nextLine(), out);
                    answer = inputData(input);
                    System.out.println(answer);
                    ;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void output(String message, PrintWriter out) {
        out.println(message);

    }

    private static String inputData(BufferedReader input) throws IOException {
        String resp = input.readLine();
        String text = resp.replace('©', '\n');
        return text;
    }
}