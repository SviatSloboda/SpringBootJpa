package ua.foxminded.springbootjdbcapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;
import ua.foxminded.springbootjdbcapi.service.GroupService;
import ua.foxminded.springbootjdbcapi.service.SchoolService;
import ua.foxminded.springbootjdbcapi.service.StudentService;

import java.util.List;

import ua.foxminded.springbootjdbcapi.service.GenerateService;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

@Component
public class SchoolApplication implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(SchoolApplication.class);
    private final GenerateService generateService;
    private final StudentService studentService;
    private final GroupService groupService;
    private final SchoolService schoolService;

    public SchoolApplication(GroupService groupService, StudentService studentService, SchoolService schoolService, GenerateService generateService) {
        this.generateService = generateService;
        this.studentService = studentService;
        this.groupService = groupService;
        this.schoolService = schoolService;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Starting application");

        setupInitialData();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                showMenu();
                running = processUserChoice(scanner);
            }
        }

    }

    private void setupInitialData() {
        generateService.deleteAll();
        generateService.generateGroups();
        generateService.generateCourses();
        generateService.generateStudents();
        generateService.assignStudentsToCourses();
    }

    private void showMenu() {
        System.out.println(
                """
                        Menu:
                        1. Find all the groups with less or equal student count
                        2. Find all the students related to the course with the given name
                        3. Add a new student
                        4. Delete a student
                        5. Add a student to course
                        6. Remove student from course
                        7. Find student by ID
                        8. Add a new student with own id
                        9. Exit
                        """);
        System.out.print("Enter choice: ");
    }

    private boolean processUserChoice(Scanner scanner) {
        try {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> handleFindGroups(scanner);
                case 2 -> handleFindStudents(scanner);
                case 3 -> handleSaveStudent(scanner);
                case 4 -> handleDeleteStudent(scanner);
                case 5 -> handleAddStudentToCourse(scanner);
                case 6 -> handleRemoveStudentFromCourse(scanner);
                case 7 -> handleFindStudentById(scanner);
                case 8 -> handleSaveStudentWithOwnId(scanner);
                case 9 -> {
                    return false;
                }
                default -> logger.warn("Invalid choice. Please enter 1-8.");
            }
        } catch (Exception e) {
            logger.error("Error processing choice: {}", e.getMessage(), e);
            scanner.nextLine();
        }
        return true;
    }

    private void handleAddStudentToCourse(Scanner scanner) {
        try {
            System.out.println("Enter the ID of the student: ");
            String studentId = scanner.next();

            System.out.println("Enter the ID of the course: ");
            String courseId = scanner.next();

            boolean added = schoolService.addStudentToCourse(studentId, courseId);
            if (added) {
                System.out.println("Student with ID " + studentId + " has been successfully enrolled in course with ID " + courseId + ".");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numeric values.");
            scanner.next();
        } catch (NoSuchElementException | IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while adding a student to a course: {}", e.getMessage(), e);
        }
    }

    private void handleRemoveStudentFromCourse(Scanner scanner) {
        try {
            System.out.println("Enter the ID of the student: ");
            String studentId = scanner.next();

            System.out.println("Enter the ID of the course: ");
            String courseId = scanner.next();

            boolean removed = schoolService.removeStudentFromCourse(studentId, courseId);
            if (removed) {
                System.out.println("Student with ID " + studentId + " has been successfully removed from course with ID " + courseId + ".");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numeric values.");
            scanner.next();
        } catch (NoSuchElementException | IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while removing a student from a course: {}", e.getMessage(), e);
        }
    }

    private void handleSaveStudent(Scanner scanner) {
        try {
            System.out.println("Enter the first name of the student: ");
            String firstName = scanner.next();

            System.out.println("Enter the last name of the student: ");
            String lastName = scanner.next();

            System.out.println("Enter the ID of the student's group: ");
            String groupId = scanner.next();

            Student student = new Student(groupService.getById(groupId), firstName, lastName);
            boolean created = studentService.save(student);

            if (created) {
                System.out.println("Student " + firstName + " " + lastName + " has been successfully added.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the correct value.");
            scanner.next();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while adding a new student: {}", e.getMessage(), e);
        }
    }

    private void handleSaveStudentWithOwnId(Scanner scanner) {
        try {
            System.out.println("Enter the id of the student: ");
            String id = scanner.next();

            System.out.println("Enter the first name of the student: ");
            String firstName = scanner.next();

            System.out.println("Enter the last name of the student: ");
            String lastName = scanner.next();

            System.out.println("Enter the ID of the student's group: ");
            String groupId = scanner.next();

            Student student = new Student(id, groupService.getById(groupId), firstName, lastName);
            boolean created = studentService.save(student);

            if (created) {
                System.out.println("Student " + firstName + " " + lastName + " has been successfully added.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the correct value.");
            scanner.next();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while adding a new student: {}", e.getMessage(), e);
        }
    }

    private void handleDeleteStudent(Scanner scanner) {
        try {
            System.out.println("Enter the ID of the student to delete: ");
            String studentId = scanner.next();

            boolean deleted = studentService.deleteById(studentId);
            if (deleted) {
                System.out.println("Student with ID " + studentId + " has been successfully deleted.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric value.");
            scanner.next();
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while deleting a student: {}", e.getMessage(), e);
        }
    }

    private void handleFindStudents(Scanner scanner) {
        try {
            System.out.println("Enter the course name: ");
            String courseName = scanner.next();

            List<Student> students = studentService.findAllStudentsByCourseName(courseName);

            if (students.isEmpty()) {
                System.out.println("No students were found enrolled in the course named '" + courseName + "'.");
            } else {
                System.out.println("Students enrolled in '" + courseName + "':");
                students.forEach(student -> System.out.println(student.toString()));
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while finding students by course name: {}", e.getMessage(), e);
        }
    }

    private void handleFindGroups(Scanner scanner) {
        try {
            System.out.println("Enter the maximum number of students: ");
            int maxStudents = scanner.nextInt();

            if (maxStudents < 0) {
                System.out.println("Student count cannot be less than 0. Please try again.");
                return;
            }

            List<Group> groups = groupService.findAllGroupsWithLessOrEqualsStudentCount(maxStudents);

            if (groups.isEmpty()) {
                System.out.println("No groups found with " + maxStudents + " or less students.");
            } else {
                System.out.println("Groups with " + maxStudents + " or less students:");
                groups.forEach(group -> System.out.println(group.toString()));
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric value.");
            scanner.next();
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while finding groups with less or equal student count: {}", e.getMessage(), e);
        }
    }

    private void handleFindStudentById(Scanner scanner) {
        try {
            System.out.println("Enter the ID of the student: ");
            String studentId = scanner.next();

            Student student = studentService.getById(studentId);

            System.out.println("Student found: " + student.toString());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric value.");
            scanner.next();
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while fetching the student by ID: {}", e.getMessage(), e);
        }
    }
}
