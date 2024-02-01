package ua.foxminded.springbootjdbcapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.springbootjdbcapi.dao.CourseDao;
import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.dao.StudentCourses;
import ua.foxminded.springbootjdbcapi.dao.StudentDao;
import ua.foxminded.springbootjdbcapi.model.Course;
import ua.foxminded.springbootjdbcapi.model.Group;
import ua.foxminded.springbootjdbcapi.model.Student;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenerateService {
    private final static Random random = new Random();
    private final StudentDao studentDao;
    private final StudentCourses studentCourses;
    private final CourseDao courseDao;
    private final GroupDao groupDao;

    @Autowired
    public GenerateService(StudentDao studentDao, StudentCourses studentCourses, CourseDao courseDao, GroupDao groupDao) {
        this.studentDao = studentDao;
        this.studentCourses = studentCourses;
        this.courseDao = courseDao;
        this.groupDao = groupDao;
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

    public void deleteAll(){
        groupDao.deleteAll();
        studentCourses.deleteAll();
        studentDao.deleteAll();
        courseDao.deleteAll();
    }

    public void generateGroups() {
        for (int i = 0; i < 10; i++) {
            String groupName = generateRandomName();
            int result = groupDao.saveWithoutId(new Group(groupName));

            if (result <= 0) {
                throw new IllegalStateException("Group with name: " + groupName + " was not created");
            }
        }
        System.out.println(groupDao.getAll().stream().map(Group::id).collect(Collectors.toList()));
    }

    public void generateCourses() {
        String[] courses = {"Math", "Biology", "Chemistry", "Physics", "History",
                "English", "Art", "Computer Science", "Economics", "Music"};

        for (String i : courses) {
            int result = courseDao.saveWithoutId(new Course(i, i + "Basics"));

            if (result <= 0) {
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
            int groupId = random.nextInt(10) + 1;

            int result = studentDao.saveWithoutId(new Student(groupId, firstName, lastName));
            if (result <= 0) {
                throw new IllegalStateException("Student:" + firstName + " " + lastName + " was not created. Current value is: " +i);
            }
        }
    }

    public void assignStudentsToCourses() {
        for (int studentId = 1; studentId <= 200; studentId++) {
            Set<Integer> assignedCourses = new HashSet<>();
            int coursesCount = random.nextInt(3) + 1;

            for (int i = 0; i < coursesCount; i++) {
                int courseId;

                do {
                    courseId = random.nextInt(10) + 1;
                } while (assignedCourses.contains(courseId));
                assignedCourses.add(courseId);

                int result = studentCourses.addStudentToCourse(studentId, courseId);
                if (result <= 0) {
                    throw new IllegalStateException("Student with ID: " + studentId +
                                                    " was not added to course with Id" + courseId);
                }
            }
        }
    }
}