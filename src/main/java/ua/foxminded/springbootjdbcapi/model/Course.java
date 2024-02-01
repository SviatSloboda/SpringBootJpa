package ua.foxminded.springbootjdbcapi.model;

public record Course(
        int id,
        String name,
        String description
) {
    public Course(String name, String description){
        this(-1, name, description);
    }
}
