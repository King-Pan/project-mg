package com.asiainfo.projectmg.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/4
 * Time: 上午11:47
 * Description: No Description
 */
@RestController
public class HomeController {

    @RequestMapping("/")
    public ModelAndView home(){
        return new ModelAndView("home");
    }
}
