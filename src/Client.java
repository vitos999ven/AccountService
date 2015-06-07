
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Client {

    private Set<MyThread> threads = new HashSet<>();

    private ConfigFile config = null;

    public Client() {
        try {
            try {
                config = new ConfigFile();
            } catch (IOException | NumberFormatException ex) {
                System.out.println(ex);
                System.exit(1);
            }
            Registry registry = LocateRegistry.getRegistry(config.getHost(), config.getPort());

            for (int i = 0; i < config.getRCount(); i++) {
                threads.add(new MyThread(registry, true, config.getFirst(), config.getLast()));
            }
            for (int i = 0; i < config.getWCount(); i++) {
                threads.add(new MyThread(registry, false, config.getFirst(), config.getLast()));
            }
        } catch (RemoteException | NotBoundException ex) {
            System.out.println(ex);
            System.exit(1);
        }
    }

    public void listen() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Commands:\n\"info\" - get info\n\"exit\" - close server");
            String command = scan.next();
            if (command.equalsIgnoreCase("info")) {
                System.out.println("All threads count: " + threads.size());
                Iterator<MyThread> iter = threads.iterator();
                int i = 0;
                while (iter.hasNext()) {
                    i++;
                    MyThread thread = iter.next();
                    System.out.println("Thread " + i + ": " + ((thread.isReader()) ? "reader, " : "writer, ") + "count of requests: " + thread.getCount());
                }
            } else if (command.equalsIgnoreCase("exit")) {
                interruptThreads();
                System.exit(0);
            }
        }
    }

    private void interruptThreads() {
        Iterator<MyThread> iter = threads.iterator();
        while (iter.hasNext()) {
            iter.next().interrupt();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.listen();
    }
}
