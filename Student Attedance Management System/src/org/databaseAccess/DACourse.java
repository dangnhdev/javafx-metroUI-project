package org.databaseAccess;

import org.model.Course;

import java.util.List;

/**
 * @author Admin
 */
public class DACourse {

    public List<Course> getAll() {
        List<Course> cou;
        String sql = "SELECT * FROM courses";
        cou = DatabaseConnect.connectWithQuerryCourse(sql);
        return cou;
    }

    public void add(Course course) {
        String sql = "INSERT INTO courses(id,class_name,teacher_email,subject_id,startDate,endDate,learnTime) " +
                "VALUES('" + course.getCourseId() + "','" + course.getClassId() + "','" + course.getTeacherEmail() + "','" + course.getSubjectId() + "','"
                + course.getDefaultStartDate() + "','" + course.getDefaultEndDate() + "','" + course.getPartOfDay() + "')";
        DatabaseConnect.connect(sql);
    }

    public void edit(Course course) {

        String sql = "UPDATE courses SET class_name='" + course.getClassId() + "',teacher_email ='"
                + course.getTeacherEmail() + "',subject_id='" + course.getSubjectId() + "',startDate='"
                + course.getDefaultStartDate().toString() + "',endDate='" + course.getDefaultEndDate().toString()
                + "',learnTime='" + course.getPartOfDay() + "' WHERE id='" + course.getCourseId() + "'";
        DatabaseConnect.connect(sql);
    }

    public void delete(String courseId) {
        String sql = "DELETE FROM courses WHERE id='" + courseId + "'";
        DatabaseConnect.connect(sql);
    }


}
