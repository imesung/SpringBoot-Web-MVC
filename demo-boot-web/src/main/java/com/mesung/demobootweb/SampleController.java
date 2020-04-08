package com.mesung.demobootweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    //hello/mesung
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") Person person) {
        return "hello" + person.getName();
    }

    //hello?name=mesung
    @GetMapping("/hello")
    public String helloReq(@RequestParam("name") Person person) {
        return "hello" + person.getName();
    }
}
