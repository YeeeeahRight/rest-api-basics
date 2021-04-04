package com.epam.esm.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public void create() {
        System.out.println("CREATE GIFT...");
    }

    @GetMapping
    public void getAll() {
        System.out.println("GET ALL GIFT CERTIFICATES...");
    }

    @GetMapping("/{id}")
    public void getById(@PathVariable("id") int id) {
        System.out.println("GET " + id + " GIFT CERTIFICATE...");
    }

    @PatchMapping()
    public void update() {
        System.out.println("UPDATE CERTIFICATE...");
    }

    @GetMapping("/withTag")
    public void get(@RequestParam(required = false, defaultValue = "") String tagName,
                    @RequestParam(required = false, defaultValue = "") String tagPartInfo,
                    @RequestParam(required = false) List<String> sortTypes) {
        System.out.println("GET CERTIFICATES BY TAG...");
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") int id) {
        System.out.println("DELETE " + id + " GIFT");
    }
}
