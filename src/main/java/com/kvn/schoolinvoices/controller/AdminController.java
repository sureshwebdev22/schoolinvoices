package com.kvn.schoolinvoices.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  @GetMapping("/dashboard")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> dashboard() {

    List<String> list = new ArrayList<>();
    list.add("a");
    System.out.println(list.size());
    return ResponseEntity.ok("Hello from ADMIN dashboard");
  }
}