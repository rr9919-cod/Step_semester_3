import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

// ================ Question 2: The Assignment Submission Portal ================

class Student2 {
    private final String name;

    public Student2(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}

enum SubmissionStatus {
    SUBMITTED, GRADED
}

/**
 * Abstract base for assignment types. Each subtype defines its own
 * late-penalty rule via applyLatePenalty(). New assignment types can be
 * added without changing the grading workflow in Submission.
 */
abstract class Assignment {
    private final String title;
    private final int maxMarks;
    private final LocalDate dueDate;

    public Assignment(String title, int maxMarks, LocalDate dueDate) {
        this.title = title;
        this.maxMarks = maxMarks;
        this.dueDate = dueDate;
    }

    public String getTitle() { return title; }
    public int getMaxMarks() { return maxMarks; }
    public LocalDate getDueDate() { return dueDate; }

    /** Each assignment type applies its own late-penalty percentage per day late. */
    public abstract double applyLatePenalty(double awardedMarks, long daysLate);

    /** Percentage lost per day late; used for display purposes. */
    public abstract double getPenaltyRatePerDay();
}

class CodingAssignment extends Assignment {
    public CodingAssignment(String title, int maxMarks, LocalDate dueDate) {
        super(title, maxMarks, dueDate);
    }

    public double getPenaltyRatePerDay() { return 0.10; } // 10% per day

    public double applyLatePenalty(double awardedMarks, long daysLate) {
        if (daysLate <= 0) return awardedMarks;
        double penaltyFraction = Math.min(1.0, getPenaltyRatePerDay() * daysLate);
        return awardedMarks * (1 - penaltyFraction);
    }
}

class WrittenAssignment extends Assignment {
    public WrittenAssignment(String title, int maxMarks, LocalDate dueDate) {
        super(title, maxMarks, dueDate);
    }

    public double getPenaltyRatePerDay() { return 0.20; } // 20% per day

    public double applyLatePenalty(double awardedMarks, long daysLate) {
        if (daysLate <= 0) return awardedMarks;
        double penaltyFraction = Math.min(1.0, getPenaltyRatePerDay() * daysLate);
        return awardedMarks * (1 - penaltyFraction);
    }
}

/**
 * A single submission. Guards its own status transitions so status
 * cannot be changed from outside except through submit()/grade().
 */
class Submission {
    private final Student2 student;
    private final Assignment assignment;
    private final LocalDate submissionDate;
    private SubmissionStatus status;
    private double finalMarks;

    public Submission(Student2 student, Assignment assignment, LocalDate submissionDate) {
        this.student = student;
        this.assignment = assignment;
        this.submissionDate = submissionDate;
        this.status = SubmissionStatus.SUBMITTED;
    }

    public Student2 getStudent() { return student; }
    public Assignment getAssignment() { return assignment; }
    public SubmissionStatus getStatus() { return status; }
    public double getFinalMarks() { return finalMarks; }

    /** Number of days late, calculated by the Submission itself (0 if on time). */
    public long calculateDaysLate() {
        long diff = ChronoUnit.DAYS.between(assignment.getDueDate(), submissionDate);
        return Math.max(0, diff);
    }

    public boolean isLate() {
        return calculateDaysLate() > 0;
    }

    /** Grades the submission, applying the assignment's late-penalty rule. Rejects re-grading. */
    public boolean grade(double awardedMarks) {
        if (status == SubmissionStatus.GRADED) {
            return false; // cannot resubmit/re-grade
        }
        long daysLate = calculateDaysLate();
        this.finalMarks = assignment.applyLatePenalty(awardedMarks, daysLate);
        this.status = SubmissionStatus.GRADED;
        return true;
    }

    public boolean canResubmit() {
        return status != SubmissionStatus.GRADED;
    }
}

/**
 * Facade coordinating assignments and submissions, printing required output.
 */
class SubmissionPortal {
    private final Map<String, Assignment> assignments = new LinkedHashMap<>();
    private final Map<String, Submission> submissions = new LinkedHashMap<>();

    public void addAssignment(Assignment assignment) {
        assignments.put(assignment.getTitle(), assignment);
    }

    public void submit(Student2 student, String assignmentTitle, LocalDate submissionDate) {
        Assignment assignment = assignments.get(assignmentTitle);
        String key = key(student, assignmentTitle);

        Submission existing = submissions.get(key);
        if (existing != null && !existing.canResubmit()) {
            System.out.println("Cannot resubmit: '" + assignmentTitle + "' has already been graded.");
            return;
        }

        Submission submission = new Submission(student, assignment, submissionDate);
        submissions.put(key, submission);

        long daysLate = submission.calculateDaysLate();
        String lateNote = daysLate > 0 ? "(" + daysLate + " days late)" : "(on time)";
        System.out.println(student.getName() + "'s submission for '" + assignmentTitle +
                "' received " + lateNote + ". Status: Submitted.");
    }

    public void grade(Student2 student, String assignmentTitle, double awardedMarks) {
        String key = key(student, assignmentTitle);
        Submission submission = submissions.get(key);
        if (submission == null) {
            System.out.println("No submission found for " + student.getName() + " on '" + assignmentTitle + "'.");
            return;
        }
        if (!submission.grade(awardedMarks)) {
            System.out.println("Cannot grade: already graded.");
            return;
        }

        Assignment assignment = submission.getAssignment();
        long daysLate = submission.calculateDaysLate();
        if (daysLate > 0) {
            double penaltyPercent = Math.min(100, assignment.getPenaltyRatePerDay() * 100 * daysLate);
            System.out.printf("%s graded: %.0f/%d after %.0f%% late penalty. Status: Graded.%n",
                    student.getName(), submission.getFinalMarks(), assignment.getMaxMarks(), penaltyPercent);
        } else {
            System.out.printf("%s graded: %.0f/%d. Status: Graded.%n",
                    student.getName(), submission.getFinalMarks(), assignment.getMaxMarks());
        }
    }

    private String key(Student2 student, String assignmentTitle) {
        return student.getName() + "::" + assignmentTitle;
    }
}

public class AssignmentSubmissionPortal {
    public static void main(String[] args) {
        SubmissionPortal portal = new SubmissionPortal();

        Assignment codingLab = new CodingAssignment("Linked List Lab", 50, LocalDate.of(2026, 3, 10));
        Assignment writtenEssay = new WrittenAssignment("Design Essay", 50, LocalDate.of(2026, 3, 12));
        portal.addAssignment(codingLab);
        portal.addAssignment(writtenEssay);

        Student2 asha = new Student2("Asha");
        Student2 ravi = new Student2("Ravi");

        // Asha submits 'Linked List Lab' on Mar 10 (on time).
        portal.submit(asha, "Linked List Lab", LocalDate.of(2026, 3, 10));

        // Ravi submits 'Design Essay' on Mar 14 (2 days late).
        portal.submit(ravi, "Design Essay", LocalDate.of(2026, 3, 14));

        // Faculty awards Asha 45 marks.
        portal.grade(asha, "Linked List Lab", 45);

        // Faculty awards Ravi 40 marks.
        portal.grade(ravi, "Design Essay", 40);

        // Asha attempts to resubmit 'Linked List Lab'.
        portal.submit(asha, "Linked List Lab", LocalDate.of(2026, 3, 15));
    }
}
