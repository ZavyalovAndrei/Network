import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
    private static final int PORT = 8089;

    public static void main(String[] args) throws IOException {
        System.out.println("Server started");
        while (true) {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));
            System.out.printf("New connection accepted. Port: %d%n", clientSocket.getPort());
            final int socket = clientSocket.getPort();
            outputData("Привет, вы подключились к службе хранения заметок. Ваш порт: " + socket +
                    ".©Пожалуйста, введите Ваше имя.", out);
            final String name = inputData(input);

            String[] actionList = new String[]{
                    "Выход.",
                    "Добавить задачу.",
                    "Вывести список задач.",
                    "Удалить задачу."
            };

            ArrayList<String> notes = new ArrayList<>();
            boolean exit = true;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Программа для создания заметок запущена.©");
            while (exit == true) {
                stringBuilder.append(name + ", выберите действие:©");
                for (int i = 0; i < actionList.length; i++) {
                    stringBuilder.append("[" + i + "] " + actionList[i] + "©");
                }
                outputData(stringBuilder.toString(), out);
                int action = checkMenuEnter(actionList, inputData(input), out);
                switch (action) {
                    case 0:
                        outputData("Работа с заметками завершена.", out);
                        exit = false;
                        break;
                    case 1:
                        outputData(notesList(notes), out);
                        String newNote;
                        stringBuilder = new StringBuilder();
                        while (true) {
                            outputData("Введите задачу и нажмите <Enter>. Для выхода введите \"end\" и " +
                                    "нажмите <Enter>.", out);
                            newNote = inputData(input);
                            if (newNote.equals("end")) {
                                stringBuilder.append("Ввод новых задач закончен, нажмите <Enter> чтобы увидеть " +
                                        "список задач.©");
                                stringBuilder.append(notesList(notes));
                                outputData(stringBuilder.toString(), out);
                                break;
                            } else {
                                notes.add(newNote);
                            }
                        }
                        break;
                    case 2:
                        outputData(notesList(notes), out);
                        break;
                    case 3:
                        if (notesList(notes).equals("У Вас нет ни одной задачи.")) {
                            break;
                        } else {
                            String message = "Ошибка ввода. Задача с таким номером не найден. Повторите ввод.";
                            while (true) {
                                try {
                                    int enter = Integer.parseInt(inputData(input));
                                    if (enter > 0 && (enter - 1) < notes.size()) {
                                        notes.remove((enter - 1));
                                        outputData(notesList(notes), out);
                                        break;
                                    } else {
                                        outputData(message, out);
                                    }
                                } catch (NumberFormatException err) {
                                    outputData(message, out);
                                }
                            }
                        }
                        break;
                }
            }
        }
    }

    private static void outputData(String message, PrintWriter out) {
        out.println(message);
    }

    private static String inputData(BufferedReader input) throws IOException {
        String resp = input.readLine();
        return resp;
    }

    public static int checkMenuEnter(String[] action, String text, PrintWriter out) {
        String message = "Ошибка ввода.©Введите правильный номер действия";
        while (true) {
            try {
                int enter = (Integer.parseInt(text));
                System.out.println(enter);
                if (enter >= 0 && enter < action.length) {
                    return enter;
                } else {
                    outputData(message, out);
                }
            } catch (NumberFormatException err) {
                outputData(message, out);
            }
        }
    }

    public static String notesList(ArrayList<String> notes) {
        StringBuilder sb = new StringBuilder();
        if (!notes.isEmpty()) {
            Iterator<String> printNotes = notes.iterator();
            sb.append("Список Ваших текущих задач:©");
            int posNumber = 1;
            while (printNotes.hasNext()) {
                sb.append("   " + posNumber + ". " + printNotes.next() + "©");
                posNumber++;
            }
        } else {
            sb.append("Введите задачу и нажмите <Enter>. Для выхода введите \"end\" и нажмите <Enter>");
        }
        return sb.toString();
    }
}