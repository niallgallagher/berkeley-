package javaapp2;

import static com.sleepycat.persist.model.DeleteAction.CASCADE;
import static com.sleepycat.persist.model.DeleteAction.NULLIFY;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import static com.sleepycat.persist.model.Relationship.MANY_TO_ONE;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class Student {
    @PrimaryKey
    private int num;

    private String name;
    // ToDo : LocalDate ?
    private String DOB;
    private String nationality;

    @SecondaryKey(relate=MANY_TO_ONE, relatedEntity=Course.class, onRelatedEntityDelete=CASCADE)
    // @SecondaryKey(relate=MANY_TO_ONE, relatedEntity=Course.class, onRelatedEntityDelete=NULLIFY)
    private String code;

    public Student() {
        
    }

    public Student(int num, String name, String DOB, String nationality, String code) {
        this.num = num;
        this.name = name;
        this.DOB = DOB;
        this.nationality = nationality;
        this.code = code;
    }

    @Override
    public String toString() {
        return String.format("%s%s%s%s%s",
                ("No : " + num + "   "),
                ("Name : " + name + "   "),
                ("DOB : " + DOB + "   "),
                ("Nationality : " + nationality + "   "),
                ("Code : " + code + "   ")
        );
    }

    // ToDo : Get & Set Methods ?
}
