package org.dump;

import org.model.Course;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 404NotFound
 */


public class CourseDump {
    private final String datePattern = "dd/MM/yyyy";
    List<Course> listCourse = new ArrayList<>();
    Random rnd = new Random();
    String[] date = {"18/09/2015", "21/09/2015", "27/09/2015", "09/09/2015", "22/10/2015", "19/09/2015", "27/09/2015", "11/08/2015", "12/10/2015", "11/10/2015"};
    String[] timeSlot = {"7AM - 11AM", "1PM - 5PM"};

    public CourseDump() {
    }

    public List<Course> randomGenerate() {
        for (int i = 0; i < 10; i++) {
            Course c = new Course();
            c.setClassId("class" + ThreadLocalRandom.current().nextInt(1, 11));
            c.setCourseId("course" + ThreadLocalRandom.current().nextInt(1, 11));
            c.setSubjectId("subject" + ThreadLocalRandom.current().nextInt(1, 11));
            c.setTeacherEmail("teacher" + ThreadLocalRandom.current().nextInt(1, 11));
            LocalDate startDate = LocalDate.parse(date[ThreadLocalRandom.current().nextInt(1, 11)], DateTimeFormatter.ofPattern(datePattern));
            c.setStartDate(startDate);
            c.setEndDate(startDate.plusDays(ThreadLocalRandom.current().nextInt(7, 11)));
            c.setPartOfDay(timeSlot[rnd.nextInt(1)]);

            listCourse.add(c);
        }

        return listCourse;
    }
}
