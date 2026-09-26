import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

// ================ Question 2: Employee Leave Request Workflow ================

enum LeaveStatus {
    PENDING, APPROVED, REJECTED
}

/**
 * Abstract base for employee types. Each type can define its own leave
 * policy check via isWithinPolicy(). New employee types (e.g., Intern)
 * can be added without modifying LeaveRequest's core workflow.
 */
abstract class Employee {
    private final String name;

    public Employee(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public abstract String getEmployeeType();

    /** Each employee type enforces its own leave-eligibility rule (e.g., max days allowed). */
    public abstract boolean isWithinPolicy(int requestedDays);
}

class FullTimeEmployee extends Employee {
    private static final int MAX_LEAVE_DAYS = 20;

    public FullTimeEmployee(String name) { super(name); }
    public String getEmployeeType() { return "Full-Time"; }
    public boolean isWithinPolicy(int requestedDays) { return requestedDays <= MAX_LEAVE_DAYS; }
}

class PartTimeEmployee extends Employee {
    private static final int MAX_LEAVE_DAYS = 10;

    public PartTimeEmployee(String name) { super(name); }
    public String getEmployeeType() { return "Part-Time"; }
    public boolean isWithinPolicy(int requestedDays) { return requestedDays <= MAX_LEAVE_DAYS; }
}

class Contractor extends Employee {
    private static final int MAX_LEAVE_DAYS = 5;

    public Contractor(String name) { super(name); }
    public String getEmployeeType() { return "Contractor"; }
    public boolean isWithinPolicy(int requestedDays) { return requestedDays <= MAX_LEAVE_DAYS; }
}

/**
 * A leave request. Manages its own status transitions and guards against
 * invalid changes (e.g., reverting an Approved/Rejected request to Pending).
 */
class LeaveRequest {
    private final Employee employee;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private LeaveStatus status;

    public LeaveRequest(Employee employee, LocalDate startDate, LocalDate endDate) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = LeaveStatus.PENDING;
    }

    public Employee getEmployee() { return employee; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LeaveStatus getStatus() { return status; }

    public long getDurationDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public String getDateRangeLabel() {
        String monthName = startDate.getMonth().toString();
        String monthAbbrev = monthName.substring(0, 1) + monthName.substring(1, 3).toLowerCase();
        return monthAbbrev + " " + startDate.getDayOfMonth() + "-" + endDate.getDayOfMonth();
    }

    /** Approves the request only if it is currently Pending. */
    public boolean approve() {
        if (status != LeaveStatus.PENDING) return false;
        status = LeaveStatus.APPROVED;
        return true;
    }

    /** Rejects the request only if it is currently Pending. */
    public boolean reject() {
        if (status != LeaveStatus.PENDING) return false;
        status = LeaveStatus.REJECTED;
        return true;
    }

    /** Any attempt to revert to Pending (or otherwise change a finalized request) is rejected. */
    public boolean revertToPending() {
        return false; // status can never revert once Approved or Rejected
    }
}

/**
 * Facade coordinating leave requests and printing required output.
 */
class LeaveWorkflow {
    private final List<LeaveRequest> requests = new ArrayList<>();

    public LeaveRequest submit(Employee employee, LocalDate start, LocalDate end) {
        if (!employee.isWithinPolicy((int) (ChronoUnit.DAYS.between(start, end) + 1))) {
            System.out.println("Leave request for " + employee.getName() + " exceeds allowed policy limit.");
            return null;
        }

        LeaveRequest request = new LeaveRequest(employee, start, end);
        requests.add(request);
        System.out.println("Leave request submitted for " + employee.getName() +
                " (" + request.getDateRangeLabel() + "). Status: Pending.");
        return request;
    }

    public void approve(LeaveRequest request, String managerName) {
        if (request.approve()) {
            System.out.println(request.getEmployee().getName() + "'s leave request (" +
                    request.getDateRangeLabel() + ") approved. Status: Approved.");
        } else {
            System.out.println("Cannot approve: request is already " + request.getStatus() + ".");
        }
    }

    public void reject(LeaveRequest request, String managerName) {
        if (request.reject()) {
            System.out.println(request.getEmployee().getName() + "'s leave request (" +
                    request.getDateRangeLabel() + ") rejected. Status: Rejected.");
        } else {
            System.out.println("Cannot reject: request is already " + request.getStatus() + ".");
        }
    }

    public void attemptRevertToPending(LeaveRequest request) {
        LeaveStatus before = request.getStatus();
        request.revertToPending();
        System.out.println("Cannot change leave request status from " + capitalize(before) + " to Pending.");
    }

    private String capitalize(LeaveStatus status) {
        String s = status.name().toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}

public class EmployeeLeaveRequestWorkflow {
    public static void main(String[] args) {
        LeaveWorkflow workflow = new LeaveWorkflow();

        Employee john = new FullTimeEmployee("John");
        Employee jane = new PartTimeEmployee("Jane");

        // FullTimeEmployee John submits leave request for 5 days (Jan 1-5).
        LeaveRequest johnRequest = workflow.submit(john, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5));

        // Manager Alice reviews John's request and approves it.
        workflow.approve(johnRequest, "Alice");

        // PartTimeEmployee Jane submits leave request for 2 days (Feb 10-11).
        LeaveRequest janeRequest = workflow.submit(jane, LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 11));

        // Manager Bob reviews Jane's request and rejects it.
        workflow.reject(janeRequest, "Bob");

        // John attempts to change his approved leave request to Pending.
        workflow.attemptRevertToPending(johnRequest);
    }
}
