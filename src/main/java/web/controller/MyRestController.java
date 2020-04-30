package web.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootExceptionReporter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptEncoder;

    @PostMapping(value = "/add/{role}")
    public ResponseEntity<Void> saveUser(@RequestBody User user, @PathVariable Long role) {
        //todo Решить вопрос с паролем
        user.setPassword("1");
        ResponseEntity<Void> resp;

        if (user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getFirstName().isEmpty()
                || user.getLastName().isEmpty() || user.getAge() == null) {
            resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            try {
                user.setRoles(roleService.getAuthorityById(role));
                user.setPassword(bCryptEncoder.encode(user.getPassword()));
                userService.addUser(user);
                resp = new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                resp = new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }

        return resp;
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<User>> getList() {
        return ResponseEntity.ok(userService.listUsers());
    }
}
