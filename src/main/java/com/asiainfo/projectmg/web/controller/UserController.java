package com.asiainfo.projectmg.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/10
 * Time: 上午11:15
 * Description: No Description
 */
@RequestMapping("/sso")
@RestController
public class UserController {



    @RequestMapping("/login")
    public Object login(@RequestParam("username") String username,
                        @RequestParam("password") String password){

        return "";
    }
}
