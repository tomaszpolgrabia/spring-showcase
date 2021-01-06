package ch.polgrabia.springshowcaseweb.controllers;

import ch.polgrabia.springshowcaseweb.models.User;
import ch.polgrabia.springshowcaseweb.services.JpaUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:application.properties")
@RequestMapping("/api/users")
public class UserController {

    private final JpaUserDao jpaUserDao;
    private final Integer pageSize;

    public UserController(
            @Autowired JpaUserDao jpaUserDao,
            @Value("${springshowcase.users.controller.page.size}") Integer pageSize) {
        this.jpaUserDao = jpaUserDao;
        this.pageSize = pageSize;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<User> handleGetAllUsers(@RequestParam(name = "page", defaultValue = "0", required = false) Integer page) {
        return jpaUserDao.findAll(PageRequest.of(page, pageSize))
                .get()
                .collect(Collectors.toList());
    }

    @RequestMapping(path = "/", method = RequestMethod.PUT)
    public User handlePutUser(@RequestBody User user) {
        return jpaUserDao.save(user);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> handleGetById(@PathVariable("id") Long id) {
        Optional<User> data = jpaUserDao.findById(id);
        return ResponseEntity.status(data.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(data.orElse(null));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<User> handleUpdateById(@PathVariable("id") Long id, @RequestBody User user) {
        Optional<User> oldUserResult = jpaUserDao.findById(id);
        if (oldUserResult.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User oldUser = oldUserResult.get();
        oldUser.setUserName(user.getUserName());
        oldUser.setFirstName(user.getFirstName());
        jpaUserDao.save(oldUser);
        return ResponseEntity.ok(oldUser);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> handleRemoveUser(@PathVariable("id") Long id) {
        try {
            jpaUserDao.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
