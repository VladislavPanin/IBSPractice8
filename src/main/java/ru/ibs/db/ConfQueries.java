package ru.ibs.db;


public class ConfQueries {
    public static final String INSERT =
            "INSERT INTO food (food_id, food_name, food_type, food_exotic) VALUES (?, ?, ?, ?)";
    public static final String SELECT =
            "SELECT * FROM food WHERE food_id = ?";
    public static final String DELETE =
            "DELETE FROM food WHERE food_id = ?";
}
