package com.epam.esm.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagController {

    @PostMapping
    public void create() {
        System.out.println("CREATE TAG...");
    }

    @GetMapping
    public void getAll() {
        System.out.println("GET ALL TAGS...");
    }

    @GetMapping("/{id}")
    public void getById(@PathVariable("id") int id) {
        System.out.println("GET " + id + " TAG...");
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") int id) {
        System.out.println("DELETE " + id + " TAG");
    }
}
