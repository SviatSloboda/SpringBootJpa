package ua.foxminded.springbootjdbcapi;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.foxminded.springbootjdbcapi.dao.GroupDao;
import ua.foxminded.springbootjdbcapi.service.GenerateService;

@Component
public class SchoolApplication implements ApplicationRunner {
    private final GenerateService generateService;
    private final GroupDao groupDao;

    public SchoolApplication(GenerateService generateService, GroupDao groupDao){
        this.generateService = generateService;
        this.groupDao = groupDao;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Running ApplicationRunner");

        generateService.deleteAll();

        if (groupDao.getAll().isEmpty()) {
            generateService.generateGroups();
            generateService.generateCourses();
            generateService.generateStudents();
            generateService.assignStudentsToCourses();
        }
    }
}
