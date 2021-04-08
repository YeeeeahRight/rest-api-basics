package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.logic.GiftCertificateService;
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
    public void create(@RequestBody GiftCertificateDto giftCertificateDto,
                       HttpServletResponse response) {
        long id = giftCertificateService.create(giftCertificateDto);
        response.addHeader("Location:", "/certificates/" + id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificate> getAll() {
        return giftCertificateService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificate getById(@PathVariable("id") long id) {
        return giftCertificateService.getById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateById(@PathVariable("id") long id,
                                      @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.updateById(id, giftCertificateDto);
    }

    @GetMapping("/with_tags")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> getAllWithTags(
            @RequestParam(name = "tag_name", required = false) String tagName,
            @RequestParam(name = "part_info", required = false) String partInfo,
            @RequestParam(name = "sort", required = false) List<String> sortColumns,
            @RequestParam(name = "order", required = false) List<String> orderTypes) {
        return giftCertificateService.getAllWithTags(tagName, partInfo, sortColumns, orderTypes);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") long id) {
        giftCertificateService.deleteById(id);
    }
}
