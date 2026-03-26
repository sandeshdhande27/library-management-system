package com.sandesh.library.controller;

import com.sandesh.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/top-category")
    public List<Map<String, Object>> getTopCategories() {
        return reportService.getTopCategories();
    }
}