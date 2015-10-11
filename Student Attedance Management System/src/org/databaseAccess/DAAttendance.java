package org.databaseAccess;

import org.model.AttendanceReport;

import java.util.List;

/**
 * @author Admin
 */
public class DAAttendance {

    public List<AttendanceReport> getAll() {
        List<AttendanceReport> at;
        String sql = "SELECT attendance.student_id,students.name,courses.id," +
                "attendance.attendDate,attendance.status,attendance.isReported" +
                " FROM attendance" +
                " INNER JOIN courses" +
                " ON courses.id = attendance.course_id" +
                " INNER JOIN students" +
                " ON students.id = attendance.student_id";
        at = DatabaseConnect.connectWithQuerryAttendance(sql);
        return at;
    }

    public void add(AttendanceReport at) {
        String sql = "INSERT INTO attendance(course_id,student_id,attendDate,status,isReported) VALUES" +
                "('" + at.getCourseId() + "','" + at.getStudentId() + "','" + at.getDateCreated() + "','"
                + at.statusProperty().getValue().booleanValue() + "','" + at.getIsReported() + "')";
        DatabaseConnect.connect(sql);
    }

    public void edit(AttendanceReport at) {
        String sql = "UPDATE attendance SET status='" + at.statusProperty().getValue().booleanValue() + "' WHERE id=" + at.getStudentId();
        DatabaseConnect.connect(sql);
    }
}
