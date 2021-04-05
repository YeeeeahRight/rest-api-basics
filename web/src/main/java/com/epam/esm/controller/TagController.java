package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Tag tag, HttpServletResponse response) {
        long id = tagService.create(tag);
        response.addHeader("Location:", "/tags/" + id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<Tag> getAll() {
        return tagService.getAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Tag getById(@PathVariable("id") int id) {
        return tagService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") int id) {
        tagService.deleteById(id);
    }
}
