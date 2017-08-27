package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealController {
    private static final Logger log = LoggerFactory.getLogger(MealController.class);

    @Autowired
    private MealService service;

    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    public String meals(Model model) {
        int userId = AuthorizedUser.id();
        log.info("getAll for userId={}", userId);

        model.addAttribute("meals", MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @RequestMapping(value = "/meals/filter", method = RequestMethod.POST)
    public String mealsFiltered(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");

        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        int userId = AuthorizedUser.id();
        log.info("getBetween dates({} - {}) time({} - {}) for userId={}", startDate, endDate, startTime, endTime, userId);

        model.addAttribute("meals", MealsUtil.getFilteredWithExceeded(
                service.getBetweenDates(
                        startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                        endDate != null ? endDate : DateTimeUtil.MAX_DATE, userId),
                startTime != null ? startTime : LocalTime.MIN,
                endTime != null ? endTime : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()
        ));
        return "meals";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.POST)
    public String mealSave(HttpServletRequest request) {
        int userId = AuthorizedUser.id();

        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (request.getParameter("id").isEmpty()) {
            log.info("create {} for userId={}", meal, userId);
            checkNew(meal);
            service.create(meal, userId);
        } else {
            int id = getId(request);

            log.info("update {} with id={} for userId={}", meal, id, userId);
            assureIdConsistent(meal, id);
            service.update(meal, userId);
        }

        return "redirect:/meals";
    }

    @RequestMapping(value = "/meals/create", method = RequestMethod.GET)
    public String mealCreate(HttpServletRequest request) {
        int userId = AuthorizedUser.id();

        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        return processMeal(request, meal);
    }

    @RequestMapping(value = "/meals/update", method = RequestMethod.GET)
    public String mealUpdate(HttpServletRequest request) {
        int id = getId(request);
        int userId = AuthorizedUser.id();

        final Meal meal = service.get(id, userId);
        return processMeal(request, meal);
    }

    private String processMeal(HttpServletRequest request, Meal meal) {
        request.setAttribute("meal", meal);
        request.setAttribute("header", "Edit meal");

        return "forward:/mealForm";
    }

    @RequestMapping(value = "/meals/delete", method = RequestMethod.GET)
    public String mealsDelete(HttpServletRequest request) {
        int id = getId(request);
        int userId = AuthorizedUser.id();
        log.info("delete meal {} for userId={}", id, userId);
        service.delete(id, userId);

        return "redirect:/meals";
    }

    @RequestMapping(value = "/mealForm", method = RequestMethod.GET)
    public String mealForm(HttpServletRequest request) {
        return "/mealForm";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}