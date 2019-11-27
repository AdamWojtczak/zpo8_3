import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SynchronizedReflectionTest {
        private static String[] strings = {"aaaaaaaaaaa", "bbbbb", "cc", "dddddddddddddddddddddddddddd"};

        static List<WritingThread> queue = new CopyOnWriteArrayList<>();
        static Notificator notificator = new Notificator();
        public static void main(String[] args) {

            for (String s : strings) {
                new WritingThread(s,notificator).start();
            }
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            while (queue.size() != 0) {
                for (WritingThread thread : queue) {
                    synchronized (thread) {
                        if (thread.getState().equals(Thread.State.WAITING)) {
                            synchronized (thread.notificator) {
                                thread.notificator.notify();
                                try {
                                    notificator.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else break;
                    }
                }
            }

        }
        private static class Notificator {
            public Notificator() { }
        }
        private static class WritingThread extends Thread {

            String tobewritten;
            protected Notificator notificator;

            public WritingThread(String tobewritten , Notificator notificator) {
                this.tobewritten = tobewritten;
                this.notificator = notificator;
            }

            @Override
            public void run() {
                queue.add(this);
                for (char c : tobewritten.toCharArray()) {
                    synchronized (notificator) {
                        try {
                            notificator.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print(c);
                    synchronized (notificator){
                        notificator.notify();
                    }
                }
                queue.remove(this);

            }
        }

}