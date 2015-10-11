package org.dump;

import org.model.Teacher;

import java.util.Random;

/**
 * @author 404NotFound
 */
public class TeacherDump {
    Random rand = new Random();
    String id = "teacher" + rand.nextInt(1000);
    String[] name = {"Vernia Pearce", "Ingrid Buckmaster", "Marie Deno",
            "An Thibeau", "Anton Santoro", "Daryl Reiber",
            "Maynard Kujawa", "Cory Rubio", "Jackson Jung",
            "Katrice Fenley", "Alica Horney", "Laurence Panetta",
            "Dick Rosati", "Mickey Wohlgemuth", "Aurea Lombardo",
            "Arlean Hitchings", "Libbie Dyer", "Marlene Martino",
            "Janina Varney", "Julius Bartlebaugh"};
    String[] email = {"frw0q-wd9mprzi.@1axvcr.com", "kk7@4j-3mfi8c.com", " y33-fhxogmo@58qk80uf4np.com",
            "4-nz..tx3jv8q@fiwrwp.com", "2kakq6y@5w2vtd.com", "c3f6.2@yo68amspz.com",
            "-woopaha44@u5-c81.com", "20oe4@jdd3t3mx19.com", "nbk@m3a7wz.com",
            "3giqcxu78z0zg1.@4p95sj72w.com", "tk5r5txa01x@qsrdpejddcs.com",
            "al5@28ptpzk7r3rs.com", "tb3u99kr8vim@hb2j9dyowe3z.com",
            "git9da06v6@z5junr7vt.com", "r2u7lzxkie@qpxltximgbb.com",
            "xo4s_ld635mwc_g@mgk3w8d.com", "ms3n@t1xedovbz.com",
            "qrg.uff8lry@8m1b7atwncm.com", "qp4scrms9sspxyj@9c75ov7.com",
            "q0_sj_@yympep7q82.com"};

    public TeacherDump() {
        this.id = "teacher" + rand.nextInt(1000);

    }

    public Teacher randomGenerate() {
        Teacher t = new Teacher();
        t.setId(id);
        t.setName(name[rand.nextInt(20)]);
        t.setEmail(email[rand.nextInt(20)]);

        return t;
    }
}
