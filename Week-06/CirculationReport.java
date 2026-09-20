class LibraryMember {

    void displayInfo() {
        System.out.print("General | Books: 0 | ");
    }
}

class StudentMember extends LibraryMember {

    String course;

    StudentMember(String course) {
        this.course = course;
    }

    @Override
    void displayInfo() {
        System.out.print(
            "Student | Course: " + course +
            " | Books: 0 "
        );
    }
}

public class CirculationReport {

    static String batchPrint(LibraryMember[] members) {

        StringBuilder sb = new StringBuilder();

        for (LibraryMember member : members) {

            member.displayInfo();

            if (member instanceof StudentMember) {

                StudentMember student =
                    (StudentMember) member;

                sb.append(
                    "[Course via downcast: "
                    + student.course + "] | "
                );
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {

        LibraryMember[] members = {
            new LibraryMember(),
            new StudentMember("ECE")
        };

        System.out.println(
            batchPrint(members)
        );
    }
}
