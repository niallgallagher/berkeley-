package javaapp5;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class JavaApp5 {
    // NOTE : FREE_DISK 5GB !
    private static final String PATH = "C:\\Users\\cmpghugh\\.berkeley";
    private static final String ES = "GlynH";

    private static Environment env = null;
    private static EntityStore es = null;

    public static void main(String[] args) {
        Course c1 = new Course("C1", "JavaFX", (byte)20);
        Course c2 = new Course("C2", "Oracle", (byte)30);

        // ToDo : LocalDate ?
        Student s1 = new Student(99, "AndyL", "1974-04-04", "UK", "C1");

        try {
            // NOTE : Setup !
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setAllowCreate(true);
            envConfig.setTransactional(true);
            // envConfig.setLockTimeout(3, TimeUnit.SECONDS);
            envConfig.setLockTimeout(5, TimeUnit.SECONDS);

            StoreConfig stConfig = new StoreConfig();
            stConfig.setAllowCreate(true);
            stConfig.setTransactional(true);

            env = new Environment(new File(PATH), envConfig);
            es = new EntityStore(env, ES, stConfig);

            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    try {
                        // NOTE : Course !
                        Transaction t1 = env.beginTransaction(null, null);

                        PrimaryIndex<String, Course> piC = es.getPrimaryIndex(String.class, Course.class);
                        piC.put(t1, c1);
                        piC.put(t1, c2);

                        // NOTE : t1 Delay !
                        Thread.sleep(3000);
                        // Thread.sleep(5000);

                        t1.commit();
                        // t1.abort();

                        // showCourses(piC);
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            };
            new Thread(r1).start();

            Runnable r2 = new Runnable() {
                @Override
                public void run() {
                    try {
                        // NOTE : Student !
                        Transaction t2 = env.beginTransaction(null, null);

                        PrimaryIndex<Integer, Student> piS = es.getPrimaryIndex(Integer.class, Student.class);
                        piS.put(t2, s1);

                        t2.commit();

                        // showStudents(piS);
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            };
            new Thread(r2).start();

            // NOTE : Bye Bye Delay !
            Thread.sleep(30000);

            System.out.println("Bye Bye :)");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        finally {
            es.close();
            env.close();
        }
    }

    private static void showCourses(PrimaryIndex<String, Course> pi) {
        System.out.println("-- Courses --");
        System.out.println("Size : " + pi.count());

        try (EntityCursor<Course> ec = pi.entities()) {            
            for (Course c : ec) {
                System.out.println(c.toString());
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void showStudents(PrimaryIndex<Integer, Student> pi) {
        System.out.println("-- Students --");
        System.out.println("Size : " + pi.count());

        try (EntityCursor<Student> ec = pi.entities()) {
            for (Student s : ec) {
                System.out.println(s.toString());
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
