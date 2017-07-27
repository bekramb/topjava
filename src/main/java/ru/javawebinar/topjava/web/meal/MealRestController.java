package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Controller
public class MealRestController {
    @Autowired
    private MealService mealService;

    public Meal get(int id) {
        return mealService.get(id, AuthorizedUser.id());
    }

    public void delete(int id) {
        mealService.delete(id,AuthorizedUser.id());
    }

    public void update(Meal meal, int id) {
        meal.setId(id);
        mealService.update(meal,AuthorizedUser.id());
    }

    public void create(Meal meal) {
        mealService.save(meal);
    }

    public List<MealWithExceed> getAll(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return MealsUtil.getFilteredWithExceeded(mealService.getAll(AuthorizedUser.id(),startDate,endDate),
                startTime, endTime,AuthorizedUser.getCaloriesPerDay());
    }

    public List<MealWithExceed> getAll() {
        return MealsUtil.getWithExceeded(mealService.getAll(AuthorizedUser.id(),LocalDate.MIN,LocalDate.MAX),
                AuthorizedUser.getCaloriesPerDay());
    }

}