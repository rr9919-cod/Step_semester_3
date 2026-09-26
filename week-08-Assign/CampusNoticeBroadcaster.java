import java.util.*;

// ================ Question 5: The Campus Notice Broadcaster ================

/**
 * Interface for notification channels. NoticeBoard depends only on this
 * abstraction, so a new channel (e.g., WhatsApp) can be added without
 * modifying existing posting/delivery logic.
 */
interface NotificationChannel {
    String getChannelName();
    void send(Student3 student, String message);
}

class EmailChannel implements NotificationChannel {
    public String getChannelName() { return "Email"; }

    public void send(Student3 student, String message) {
        System.out.println("[Email -> " + student.getName() + "] " + message);
    }
}

class SmsChannel implements NotificationChannel {
    public String getChannelName() { return "SMS"; }

    public void send(Student3 student, String message) {
        System.out.println("[SMS -> " + student.getName() + "] " + message);
    }
}

class AppChannel implements NotificationChannel {
    public String getChannelName() { return "App"; }

    public void send(Student3 student, String message) {
        System.out.println("[App -> " + student.getName() + "] " + message);
    }
}

/**
 * A student belongs to one department and prefers one or more channels.
 */
class Student3 {
    private final String name;
    private final String department;
    private final List<NotificationChannel> preferredChannels;

    public Student3(String name, String department, List<NotificationChannel> preferredChannels) {
        this.name = name;
        this.department = department;
        this.preferredChannels = new ArrayList<>(preferredChannels);
    }

    public String getName() { return name; }
    public String getDepartment() { return department; }
    public List<NotificationChannel> getPreferredChannels() {
        return Collections.unmodifiableList(preferredChannels);
    }
}

/**
 * A notice with a title and one or more target departments.
 * Validates itself before being posted.
 */
class Notice {
    private final String title;
    private final List<String> targetDepartments;

    public Notice(String title, List<String> targetDepartments) {
        this.title = title;
        this.targetDepartments = targetDepartments == null ? new ArrayList<>() : new ArrayList<>(targetDepartments);
    }

    public String getTitle() { return title; }
    public List<String> getTargetDepartments() { return Collections.unmodifiableList(targetDepartments); }

    /** A notice is valid only if it has a title and at least one target department. */
    public String validate() {
        if (title == null || title.trim().isEmpty()) {
            return "Cannot post notice: A title is required.";
        }
        if (targetDepartments.isEmpty()) {
            return "Cannot post notice: At least one target department is required.";
        }
        return null; // valid
    }
}

/**
 * Coordinates posting notices: validates them, finds matching students,
 * and delivers via each student's preferred channels.
 */
class NoticeBoard {
    private final List<Student3> students = new ArrayList<>();

    public void registerStudent(Student3 student) {
        students.add(student);
    }

    public void post(Notice notice) {
        String error = notice.validate();
        if (error != null) {
            System.out.println(error);
            return;
        }

        System.out.println("Notice '" + notice.getTitle() + "' posted to " +
                String.join(", ", notice.getTargetDepartments()) + ".");

        for (Student3 student : students) {
            if (notice.getTargetDepartments().contains(student.getDepartment())) {
                for (NotificationChannel channel : student.getPreferredChannels()) {
                    channel.send(student, notice.getTitle());
                }
            }
        }
    }
}

public class CampusNoticeBroadcaster {
    public static void main(String[] args) {
        NoticeBoard board = new NoticeBoard();

        NotificationChannel email = new EmailChannel();
        NotificationChannel sms = new SmsChannel();
        NotificationChannel app = new AppChannel();

        // Asha (CSE) prefers Email and App.
        Student3 asha = new Student3("Asha", "CSE", Arrays.asList(email, app));
        // Ravi (ECE) prefers SMS.
        Student3 ravi = new Student3("Ravi", "ECE", Arrays.asList(sms));

        board.registerStudent(asha);
        board.registerStudent(ravi);

        // Admin posts notice 'Lab Closed Tomorrow' for CSE.
        board.post(new Notice("Lab Closed Tomorrow", Arrays.asList("CSE")));

        // Admin posts notice 'Fee Deadline Extended' for CSE and ECE.
        board.post(new Notice("Fee Deadline Extended", Arrays.asList("CSE", "ECE")));

        // Admin attempts to post notice 'Sports Day' with no target department.
        board.post(new Notice("Sports Day", new ArrayList<>()));
    }
}
