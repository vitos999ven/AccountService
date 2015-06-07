
import hibernate.Exceptions.AmountException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Random;
import service.AccountService;

class MyThread implements Runnable {

    private final Thread t;
    private final Registry registry;
    private AccountService centralAccServ = null;
    private boolean reader = false;
    private final int first, last;
    private int count;

    MyThread(Registry registry, boolean reader, int first, int last) throws NotBoundException, RemoteException {
        t = new Thread(this);
        this.registry = registry;
        this.reader = reader;
        this.centralAccServ = (AccountService) registry.lookup("centralAccServ");
        this.first = first;
        this.last = last;
        count = 0;
        t.start();
    }

    @Override
    public void run() {
        try {
            Random rand = new Random();

            while (true) {
                int id = rand.nextInt(last - first + 1) + first;
                count++;
                if (reader) {
                    try {
                        long value = centralAccServ.getAmount(id);
                    } catch (AmountException ex) {
                        System.out.println(ex);
                    }
                } else {
                    try {
                        long value = rand.nextLong();
                        centralAccServ.addAmount(id, value);
                    } catch (NumberFormatException e) {
                        System.out.println(e.toString());
                    } catch (AmountException ex) {
                        System.out.println(ex + ": " + ex.getAmountId());
                    }

                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println(e.toString() + ": Thread was interrupted");
        } catch (ConnectException e) {
            if (reader) {
                System.out.println("getAmountThread was disconnected");
            } else {
                System.out.println("addAmountThread was disconnected");
            }
        }  catch (RemoteException e) {
            System.out.println(e.toString());
        }
    }
    
    public boolean isReader(){
        return reader;
    }

    public int getCount() {
        return count;
    }
    
    public void interrupt(){
        t.interrupt();
    }
}
