import HashMap.*;

public class Manager {

    private MyHashMap<Thread, ServerThread> threadList;
    private boolean start;
    private ManagerThread threadManager;
    public Manager() {
        start = false;
        threadList = new MyHashMap<Thread, ServerThread>();
    }

    public void add(Thread t, ServerThread s) {
        threadList.put(t, s);
        t.start();
    }

    public void broadcast(Pair s, Thread sender) {
        for (int i = 0; i < threadList.size(); i++) {
            Object[] t = threadList.keySet().toArray();
            ServerThread st = threadList.get(t[i]);
            if (t[i] != sender) {
                st.send(s);
            }

        }
    }

    public void start() {//start running the game
        Thread mThread = new Thread(new ManagerThread(this));
        mThread.start();
        threadManager.setThreads(threadList);
    }

    public Map getMap() {
        return null;
    }
}