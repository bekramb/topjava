package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.NamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    public static final List<User> USERS= Arrays.asList(
           new User(1,"Petr1","o1@gmail.com","1234", Role.ROLE_USER),
           new User(2,"Petr2","o2@gmail.com","4321", Role.ROLE_ADMIN,Role.ROLE_USER)
    );
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    {
        USERS.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        return repository.keySet().removeIf(i -> i == id);
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
        }
        repository.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        return repository.getOrDefault(id,null);
    }

    @Override
    public List<User> getAll() {
         return repository.values().stream().sorted(Comparator.comparing(NamedEntity::getName))
                 .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        return repository.values().stream().filter((user)->user.getEmail().equals(email)).findFirst().get();
    }
}
