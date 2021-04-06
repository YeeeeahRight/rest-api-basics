package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @GetMapping(value = "/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Tag getById(@PathVariable("id") long id) {
        return tagService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") long id) {
        tagService.deleteById(id);
    }
}
