import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SynchronizedReflectionTest {
    public static void main(String[] args) {
        SynchronizedReflectionTest test = new SynchronizedReflectionTest();
        String[] strings = {"aaaa", "bb", "ccccccccccccc", "dddddd"};
        for(String s : strings)
        {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for( char c : s.toCharArray())
                        {
                        try {
                            Method someThing = SynchronizedReflectionTest.class.getDeclaredMethod("someThing", new Class[]{char.class});
                            someThing.invoke(test, c);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        }
                    }
                }).start();


        }
    }

    public void someThing(char c)
    {
        System.out.print(c);
        try {
        Thread.sleep(200);
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
    }
}