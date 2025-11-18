package com.supercoding.hrms.hr;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hr")
public class hrController {
    @GetMapping("/test")
    public String hrTest() {
        return "test";
    }
}
