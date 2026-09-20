class LibraryMember {
    int booksBorrowed = 0;

    LibraryMember(String id, int limit) {
    }

    void borrowBook() {
        booksBorrowed++;
    }

    int getBooksBorrowed() {
        return booksBorrowed;
    }

    void displayInfo() {
        System.out.println(
            "General Member | Books Borrowed: " + booksBorrowed
        );
    }
}

class StudentMember extends LibraryMember {
    String course;

    StudentMember(String id, int limit, String course) {
        super(id, limit);
        this.course = course;
    }

    @Override
    void displayInfo() {
        System.out.println(
            "Student Member | Course: " + course +
            " | Books Borrowed: " + booksBorrowed
        );
    }
}

class HonorsStudentMember extends StudentMember {
    int bonusLimit;

    HonorsStudentMember(
        String id, int limit, String course, int bonusLimit) {

        super(id, limit, course);
        this.bonusLimit = bonusLimit;
    }

    @Override
    void displayInfo() {
        System.out.println(
            "Honors Student Member | Course: " + course +
            " | Bonus Limit: " + bonusLimit +
            " | Books Borrowed: " + booksBorrowed
        );
    }
}

class FacultyMember extends LibraryMember {
    String department;

    FacultyMember(String id, int limit, String department) {
        super(id, limit);
        this.department = department;
    }

    @Override
    void displayInfo() {
        System.out.println(
            "Faculty Member | Department: " + department +
            " | Books Borrowed: " + booksBorrowed
        );
    }
}

public class MembershipTree {

    static String classifyGeneration(LibraryMember member) {

        if (member instanceof HonorsStudentMember)
            return "Multilevel descendant (3 generations deep)";

        if (member instanceof FacultyMember)
            return "Hierarchical sibling (independent branch)";

        return "General member";
    }

    static int getTotalBooksBorrowed(LibraryMember[] members) {

        int total = 0;

        for (LibraryMember member : members)
            total += member.getBooksBorrowed();

        return total;
    }

    public static void main(String[] args) {

        StudentMember student =
            new StudentMember("STU2", 3, "CSE");

        HonorsStudentMember honors =
            new HonorsStudentMember("STU3", 3, "ECE", 2);

        FacultyMember faculty =
            new FacultyMember("STU4", 5, "Physics");

        student.borrowBook();
        student.borrowBook();

        honors.borrowBook();

        faculty.borrowBook();
        faculty.borrowBook();
        faculty.borrowBook();

        student.displayInfo();
        honors.displayInfo();
        faculty.displayInfo();

        System.out.println(classifyGeneration(honors));
        System.out.println(classifyGeneration(faculty));

        LibraryMember[] members = {
            student, honors, faculty
        };

        System.out.println(
            getTotalBooksBorrowed(members)
        );
    }
}
