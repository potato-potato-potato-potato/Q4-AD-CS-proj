import HashMap.*;

public class Manager {

    private MyHashMap<Thread, ServerThread> threadList;
    private boolean start;
    private ManagerThread managerThread;

    public Manager() {
        start = false;
        threadList = new MyHashMap<Thread, ServerThread>();
        managerThread = new ManagerThread(this);
    }

    public void add(Thread t, ServerThread s) {
        threadList.put(t, s);
        t.start();
        s.getName();
        System.out.println(s.getName());

    }

    // broadcast to all threads but the sender
    public void broadcast(Pair s, Thread sender) {
        for (int i = 0; i < threadList.size(); i++) {
            Object[] t = threadList.keySet().toArray();
            ServerThread st = threadList.get(t[i]);
            if (t[i] != sender) {
                st.send(s);
            }
        }
    }

    // broadcast to all threads
    // Game data is sent with this method by ManagerThread
    public void broadcast(Pair s) {
        for (int i = 0; i < threadList.size(); i++) {
            Object[] t = threadList.keySet().toArray();
            ServerThread st = threadList.get(t[i]);
            System.out.println("Sending to " + st.getName());
            st.send(s);
        }
    }

    public void start() {// start running the game
        System.out.println("Game Starting");
        Thread mThread = new Thread(managerThread);
        System.out.println("Thread made");
        managerThread.setThreads(threadList);
        System.out.println("Thread Starting");

        mThread.start();
    }

    public void threadQuit(Boolean isHost, Thread sender) {// remove a thread if client disconnects, reassign host if
                                                           // nessescary
        threadList.remove(sender);
        if (isHost) {
            threadList.get(threadList.keySet().toArray()[0]).setHost();
        }
    }

    public void updateThread(int[] keys, Thread sender) {
        managerThread.updateThread(keys, sender);
        System.out.println(keys);
    }
}