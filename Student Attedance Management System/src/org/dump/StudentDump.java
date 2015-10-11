package org.dump;

import org.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 404NotFound
 */


public class StudentDump {
    List<Student> teacherList = new ArrayList<>();
    Random rand = new Random();
    String id = "teacher" + rand.nextInt(1000);
    String classId = "class" + rand.nextInt(3);
    String[] name = {"Quinton Quesinberry", "Sheryl Silvia", "Caridad Clemmer",
            "Ma Mccloskey", "Mirella Mungia", "Tonita Tally", "Eulah Espiritu",
            "Agripina Apicella", "Carry Cesare", "Allyson Alden", "Shona Sizer",
            "Roman Rosel", "Clinton Carranco", "Trevor Tompson", "Ada Appleton",
            "Franklyn Fleitas", "Norris Nease", "Mandi Moorer", "Fernande Ferrante",
            "Rhonda Redick"};
    String[] email = {"frw0q-wd9mprzi.@1axvcr.com", "kk7@4j-3mfi8c.com", " y33-fhxogmo@58qk80uf4np.com",
            "4-nz..tx3jv8q@fiwrwp.com", "2kakq6y@5w2vtd.com", "c3f6.2@yo68amspz.com",
            "-woopaha44@u5-c81.com", "20oe4@jdd3t3mx19.com", "nbk@m3a7wz.com",
            "3giqcxu78z0zg1.@4p95sj72w.com", "tk5r5txa01x@qsrdpejddcs.com",
            "al5@28ptpzk7r3rs.com", "tb3u99kr8vim@hb2j9dyowe3z.com",
            "git9da06v6@z5junr7vt.com", "r2u7lzxkie@qpxltximgbb.com",
            "xo4s_ld635mwc_g@mgk3w8d.com", "ms3n@t1xedovbz.com",
            "qrg.uff8lry@8m1b7atwncm.com", "qp4scrms9sspxyj@9c75ov7.com",
            "q0_sj_@yympep7q82.com"};

    public StudentDump() {
        this.id = "std" + rand.nextInt(1000);
        this.classId = "class" + rand.nextInt(3);
    }

    public Student randomGenerate() {
        Student std = new Student();
        std.setClassName(classId);
        std.setName(name[rand.nextInt(20)]);
        std.setEmail(email[rand.nextInt(20)]);
        std.setStudentId(id);

        return std;
    }
}
