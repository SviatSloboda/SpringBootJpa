package ua.foxminded.springbootjdbcapi.model;


public record Student(
        int id,
        int groupId,
        String firstName,
        String lastName
) {
    public Student(int groupId, String firstName, String lastName) {
        this(-1, groupId, firstName, lastName);
    }
}
