package javaapp2;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

import java.io.File;

public class JavaApp2 {
    // NOTE : FREE_DISK 5GB !
    private static final String PATH = "C:\\Users\\cmpghugh\\.berkeley";
    private static final String ES = "GlynH";

    private static Environment env = null;
    private static EntityStore es = null;

    public static void main(String[] args) {
        Course c1 = new Course("C1", "JavaFX", (byte)20);
        Course c2 = new Course("C2", "Oracle", (byte)30);

        // ToDo : LocalDate ?
        Student s1 = new Student(27, "GlynH", "1939-09-09", "UK", "C1");

        try {
            // NOTE : Setup !
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setAllowCreate(true);

            StoreConfig stConfig = new StoreConfig();
            stConfig.setAllowCreate(true);

            env = new Environment(new File(PATH), envConfig);
            es = new EntityStore(env, ES, stConfig);

            // NOTE : Course !
            PrimaryIndex<String, Course> piC = es.getPrimaryIndex(String.class, Course.class);
            // piC.put(c1);
            // piC.put(c2);

            // piC.delete("C1");

            showCourses(piC);

            // NOTE : Student !
            PrimaryIndex<Integer, Student> piS = es.getPrimaryIndex(Integer.class, Student.class);
            // piS.put(s1);

            showStudents(piS);

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
