package com.mesung;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloController {

    @Autowired
    HelloService helloService;


    /*
    * /hello/1?name=mesung
    * @param id
    * @param name
    * */
    @GetMapping("/hello/{id}")
    @ResponseBody
    public String hello(@PathVariable int id, @RequestParam String name) {
        return "hello, " + helloService.getName();
    }

    @GetMapping("/sample")
    public String sample() {
        return "sample";
    }
}
