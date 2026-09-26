import java.util.*;

// ================ Question 3: Online Examination System ================

class Student {
    private final String name;

    public Student(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}

/**
 * Abstract base for all question types. Each type implements its own
 * self-evaluation logic via evaluate(). New question types can be added
 * without altering Attempt/Examination submission logic.
 */
abstract class Question {
    private final String questionId;
    private final int points;

    public Question(String questionId, int points) {
        this.questionId = questionId;
        this.points = points;
    }

    public String getQuestionId() { return questionId; }
    public int getPoints() { return points; }

    /** Evaluates the given answer against the correct answer for this question type. */
    public abstract boolean evaluate(String givenAnswer);
}

class MultipleChoiceQuestion extends Question {
    private final String correctOption;

    public MultipleChoiceQuestion(String questionId, int points, String correctOption) {
        super(questionId, points);
        this.correctOption = correctOption;
    }

    public boolean evaluate(String givenAnswer) {
        return correctOption.equalsIgnoreCase(givenAnswer);
    }
}

class TrueFalseQuestion extends Question {
    private final boolean correctAnswer;

    public TrueFalseQuestion(String questionId, int points, boolean correctAnswer) {
        super(questionId, points);
        this.correctAnswer = correctAnswer;
    }

    public boolean evaluate(String givenAnswer) {
        return Boolean.toString(correctAnswer).equalsIgnoreCase(givenAnswer);
    }
}

class ShortAnswerQuestion extends Question {
    private final String correctAnswer;

    public ShortAnswerQuestion(String questionId, int points, String correctAnswer) {
        super(questionId, points);
        this.correctAnswer = correctAnswer;
    }

    public boolean evaluate(String givenAnswer) {
        return correctAnswer.trim().equalsIgnoreCase(givenAnswer == null ? "" : givenAnswer.trim());
    }
}

/**
 * An examination made up of multiple questions.
 */
class Examination {
    private final String name;
    private final List<Question> questions = new ArrayList<>();

    public Examination(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public int getTotalPoints() {
        int total = 0;
        for (Question q : questions) total += q.getPoints();
        return total;
    }
}

enum AttemptStatus {
    IN_PROGRESS, SUBMITTED
}

/** Result of evaluating a single answer. */
class QuestionResult {
    private final Question question;
    private final boolean correct;

    public QuestionResult(Question question, boolean correct) {
        this.question = question;
        this.correct = correct;
    }

    public Question getQuestion() { return question; }
    public boolean isCorrect() { return correct; }
    public int getPointsAwarded() { return correct ? question.getPoints() : 0; }
}

/**
 * Tracks a student's attempt at an examination: recorded answers and status.
 * Guards against changes once the attempt is Submitted.
 */
class Attempt {
    private final Student student;
    private final Examination examination;
    private final Map<String, String> answers = new LinkedHashMap<>();
    private AttemptStatus status;
    private List<QuestionResult> results;

    public Attempt(Student student, Examination examination) {
        this.student = student;
        this.examination = examination;
        this.status = AttemptStatus.IN_PROGRESS;
    }

    public Student getStudent() { return student; }
    public Examination getExamination() { return examination; }
    public AttemptStatus getStatus() { return status; }

    /** Records an answer; returns false if the attempt has already been submitted. */
    public boolean recordAnswer(String questionId, String answer) {
        if (status == AttemptStatus.SUBMITTED) {
            return false;
        }
        answers.put(questionId, answer);
        return true;
    }

    /** Submits the attempt, evaluating each question and locking further changes. */
    public boolean submit() {
        if (status == AttemptStatus.SUBMITTED) {
            return false;
        }
        results = new ArrayList<>();
        for (Question q : examination.getQuestions()) {
            String given = answers.get(q.getQuestionId());
            boolean correct = q.evaluate(given);
            results.add(new QuestionResult(q, correct));
        }
        status = AttemptStatus.SUBMITTED;
        return true;
    }

    public List<QuestionResult> getResults() { return results; }

    public int getTotalScore() {
        int total = 0;
        for (QuestionResult r : results) total += r.getPointsAwarded();
        return total;
    }
}

/**
 * Facade coordinating exams and attempts, printing required output.
 * Ensures only one submitted attempt per student per exam.
 */
class ExaminationSystem {
    private final Map<String, Attempt> attempts = new LinkedHashMap<>();

    private String key(Student student, Examination exam) {
        return student.getName() + "::" + exam.getName();
    }

    public Attempt start(Student student, Examination exam) {
        String key = key(student, exam);
        Attempt existing = attempts.get(key);
        if (existing != null && existing.getStatus() == AttemptStatus.SUBMITTED) {
            System.out.println(student.getName() + " already has a submitted attempt for " + exam.getName() + ".");
            return existing;
        }
        Attempt attempt = new Attempt(student, exam);
        attempts.put(key, attempt);
        System.out.println(exam.getName() + " started by " + student.getName() + ".");
        return attempt;
    }

    public void answer(Attempt attempt, String questionId, String answer) {
        if (attempt.recordAnswer(questionId, answer)) {
            System.out.println("Answer recorded for " + questionId + ".");
        } else {
            System.out.println("Cannot change answers for a submitted examination.");
        }
    }

    public void submit(Attempt attempt) {
        if (!attempt.submit()) {
            System.out.println("Attempt already submitted.");
            return;
        }
        System.out.println(attempt.getExamination().getName() + " submitted by " + attempt.getStudent().getName() + ".");

        StringBuilder sb = new StringBuilder("Result: ");
        List<QuestionResult> results = attempt.getResults();
        for (int i = 0; i < results.size(); i++) {
            QuestionResult r = results.get(i);
            sb.append(r.getQuestion().getQuestionId()).append(": ")
              .append(r.isCorrect() ? "Correct" : "Incorrect")
              .append(" (").append(r.getPointsAwarded()).append(" points)");
            if (i < results.size() - 1) sb.append(", ");
        }
        sb.append(". Total score: ").append(attempt.getTotalScore()).append("/").append(attempt.getExamination().getTotalPoints()).append(".");
        System.out.println(sb);
    }
}

public class OnlineExaminationSystem {
    public static void main(String[] args) {
        Examination examA = new Examination("Exam A");
        examA.addQuestion(new MultipleChoiceQuestion("Question 1", 5, "C"));
        examA.addQuestion(new TrueFalseQuestion("Question 2", 5, false));

        ExaminationSystem system = new ExaminationSystem();
        Student student1 = new Student("Student 1");

        // Student 1 starts Exam A.
        Attempt attempt = system.start(student1, examA);

        // Student 1 answers Question 1 (MCQ) with option C.
        system.answer(attempt, "Question 1", "C");

        // Student 1 answers Question 2 (TF) with True.
        system.answer(attempt, "Question 2", "true");

        // Student 1 submits Exam A.
        system.submit(attempt);

        // Student 1 attempts to change answer for Question 1.
        system.answer(attempt, "Question 1", "A");
    }
}
