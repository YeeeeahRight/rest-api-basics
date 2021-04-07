package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody GiftCertificateDto giftCertificateDto, HttpServletResponse response) {
        long id = giftCertificateService.create(giftCertificateDto);
        response.addHeader("Location:", "/certificates/" + id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificate getById(@PathVariable("id") long id) {
        return giftCertificateService.getById(id);
    }

    @PatchMapping("/{id}")
    public GiftCertificate updateById(@PathVariable("id") long id,
                                      @RequestBody GiftCertificate giftCertificate) {
        return giftCertificateService.updateById(id, giftCertificate);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificate> getAll() {
        return giftCertificateService.getAll();
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificate> search(@RequestParam(required = false, defaultValue = "") String tagName,
                       @RequestParam(required = false, defaultValue = "") String partInfo,
                       @RequestParam(required = false) List<String> sortTypes) {
        return giftCertificateService.search(tagName, partInfo, sortTypes);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") long id) {
        giftCertificateService.deleteById(id);
    }
}
