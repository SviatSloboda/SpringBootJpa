package ua.foxminded.springbootjdbcapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class GenerateService {
    private final static Random random = new Random();
    private final StudentService studentService;
    private final SchoolService schoolService;
    private final CourseService courseService;
    private final GroupService groupService;

    @Autowired
    public GenerateService(StudentService studentService, SchoolService schoolService, CourseService courseService, GroupService groupService) {
        this.studentService = studentService;
        this.schoolService = schoolService;
        this.courseService = courseService;
        this.groupService = groupService;
    }

    public void deleteAll(){
        schoolService.deleteAll();
        groupService.deleteAll();
    }


    private static String generateRandomName() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";

        StringBuilder nameBuilder = new StringBuilder();

        for (int i = 0; i < 2; i++) {
            int charIndex = random.nextInt(characters.length());
            nameBuilder.append(characters.charAt(charIndex));
        }

        nameBuilder.append("-");

        for (int i = 0; i < 2; i++) {
            int numIndex = random.nextInt(numbers.length());
            nameBuilder.append(numbers.charAt(numIndex));
        }

        return nameBuilder.toString();
    }

    public void generateGroups() {
        for (int i = 0; i < 10; i++) {
            String groupName = generateRandomName();
            boolean wasSaved = groupService.save(new Group(groupName));

            if (!wasSaved) {
                throw new IllegalStateException("Group with name: " + groupName + " was not created");
            }
        }
    }

    public void generateCourses() {
        String[] courses = {"Math", "Biology", "Chemistry", "Physics", "History",
                "English", "Art", "Computer Science", "Economics", "Music"};

        for (String i : courses) {
            boolean wasSaved = courseService.save(new Course(i, i + "Basics"));

            if (!wasSaved) {
                throw new IllegalStateException("Course with name: " + i + "was not created");
            }
        }
    }

    public void generateStudents() {
        String[] firstNames = {
                "Liam", "Olivia", "Noah", "Emma", "Oliver", "Ava", "Elijah", "Charlotte", "William", "Sophia",
                "James", "Amelia", "Benjamin", "Isabella", "Lucas", "Mia", "Henry", "Evelyn", "Alexander", "Harper"
        };

        String[] lastNames = {
                "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
                "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"
        };

        for (int i = 0; i < 200; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];

            List<String> groupIds = groupService.getAllIds();

            String groupId = groupIds.get(random.nextInt(groupIds.size()));

            Group group = groupService.getById(groupId);

            boolean wasSaved = studentService.save(new Student(group, firstName, lastName));
            if (!wasSaved) {
                throw new IllegalStateException("Student:" + firstName + " " + lastName + " was not created. Current value is: " +i);
            }
        }
    }

    public void assignStudentsToCourses() {
        List<String> studentIds = studentService.getAllIds();
        List<String> courseIds = courseService.getAllIds();

        for (String studentId : studentIds) {
            Set<String> assignedCourses = new HashSet<>();
            int coursesCount = random.nextInt(3) + 1;

            for (int i = 0; i < coursesCount; i++) {
                String courseId;
                do {
                    courseId = courseIds.get(random.nextInt(courseIds.size()));
                } while (assignedCourses.contains(courseId));
                assignedCourses.add(courseId);

                boolean wasAdded = schoolService.addStudentToCourse(studentId, courseId);
                if (!wasAdded) {
                    throw new IllegalStateException("Student with ID: " + studentId +
                                                    " was not added to course with Id" + courseId);
                }
            }
        }
    }
}
