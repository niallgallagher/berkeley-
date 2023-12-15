package javaapp5;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class Course {
    @PrimaryKey
    private String code;

    private String name;
    private byte credits;

    // NOTE : Classic Way !
    // private List<Student> students = new ArrayList<>();

    public Course() {
        
    }

    public Course(String code, String name, byte credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
    }

    @Override
    public String toString() {
        return String.format("%s%s%s",
                ("Code : " + code + "   "),
                ("Name : " + name + "   "),
                ("Credits : " + credits + "   ")
        );
    }

    // ToDo : Get & Set Methods ?
}
