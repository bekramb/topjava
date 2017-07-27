package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class AuthorizedUser {
    public static final List<User> USERS = Arrays.asList(
            new User(1,"Vasay","g@gmail.com","333", Role.ROLE_USER));



    public static int id() {
        return USERS.get(0).getId();
    }

    public static int getCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static void setID(int id){
        USERS.get(0).setId(id);
    }
}