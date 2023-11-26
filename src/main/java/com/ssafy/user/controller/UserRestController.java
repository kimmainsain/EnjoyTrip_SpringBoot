package com.ssafy.user.controller;

import com.ssafy.user.dto.User;
import com.ssafy.user.dto.UserSecret;
import com.ssafy.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserRestController {
    UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody HashMap<String, String> param, HttpSession session) {
        if (!param.containsKey("id") || !param.containsKey("pw"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String id = param.get("id");
        String pw = param.get("pw");

        User user = userService.login(id, pw);

        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        session.setAttribute("user", user);

        if (user.isAdmin()) {
            session.setAttribute("admin", true);
        }

        return new ResponseEntity<>(user.getNickname(), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpSession session) {
        session.invalidate();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Boolean> singUp(@RequestBody UserSecret user) {
        if (userService.signUp(user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/drop")
    public ResponseEntity<Boolean> dropOut(@RequestBody Map<String, String> dat, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null || !dat.containsKey("pw"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        boolean rst = userService.dropOut(user.getId(), dat.get("pw"));

        if (rst) {
            session.invalidate();
            return new ResponseEntity<>(HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/reset")
    public ResponseEntity<String> reset(@RequestBody String id) {
        boolean rst = userService.reset(id);

        if (!rst)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userService.select(id);

        if (user == null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(user.getNickname(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Boolean> edit(@RequestBody UserSecret secret, HttpSession session) {
        boolean rst = userService.edit(secret);

        if (rst) {
            User user = (User) session.getAttribute("user");
            user.setEmail(secret.getEmail());
            user.setNickname(secret.getNickname());
            session.setAttribute("user", user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
