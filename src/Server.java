
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import service.AccountServiceImpl;

public class Server {

    private ConfigFile config = null;
    private AccountServiceImpl centralAccServ = null;

    public Server() {
        try {
            config = new ConfigFile();
        } catch (IOException | NumberFormatException ex) {
            System.out.println(ex);
            System.exit(1);
        }
        System.getProperties().setProperty("java.rmi.server.hostname", config.getHost());
        Registry registry;
        try {
            centralAccServ = new AccountServiceImpl();

            registry = LocateRegistry.createRegistry(config.getPort());
            registry.bind("centralAccServ", centralAccServ);

        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println(e.toString());
            System.exit(1);
        }
    }

    public void listen() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Commands:\n\"reqnum\" - number of reguests for addAmount and getAmount in a second\n\"allreqnum\" - total number of reguests for addAmount and getAmount\n\"reset\" - reset of statistics\n\"exit\" - close server");
            String command = scan.next();
            if (command.equalsIgnoreCase("reqnum")) {
                System.out.println("request numer for addAmount in a second: " + centralAccServ.getLastReqNumForAddAmount()
                        + "\nrequest numer for getAmount in a second: " + centralAccServ.getLastReqNumForGetAmount());
            } else if (command.equalsIgnoreCase("allreqnum")) {
                System.out.println("request numer for addAmount: " + centralAccServ.getIncrementForAddAmount()
                        + "\nrequest numer for getAmount: " + centralAccServ.getIncrementForGetAmount());
            } else if (command.equalsIgnoreCase("reset")) {
                centralAccServ.resetInc();
            } else if (command.equalsIgnoreCase("exit")) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.listen();
    }
}
