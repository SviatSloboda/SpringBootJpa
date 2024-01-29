package ua.foxminded.springbootjdbcapi.model;

public record Group(
        int id,
        String groupName
) {
    public Group(String groupName){
        this(-1, groupName);
    }
}
