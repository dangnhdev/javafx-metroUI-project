package org.dump;

import org.model.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 404NotFound
 */

public class SubjectDump {

    List<Subject> listSubject = new ArrayList<>();
    Random rand = new Random();
    String[] name = {"Java", "C#", "Node.js, IO.js and AngularJS", "Functional Programming", "Haskell",
            "Clojure", "Web Development", "Object-Oriented Programming",
            "Data Structure and Algorithm", "Scala"};

    public List<Subject> randomGenerate() {
        for (int i = 0; i < 10; i++) {
            Subject s = new Subject();
            s.setId("subject" + i);
            s.setName(name[i]);
            s.setPeriod(ThreadLocalRandom.current().nextInt(6, 16));
            listSubject.add(s);
        }
        return listSubject;
    }
}
