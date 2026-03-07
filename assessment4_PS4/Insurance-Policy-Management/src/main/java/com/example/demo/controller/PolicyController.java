package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PolicyRequestDTO;
import com.example.demo.dto.PolicyResponseDTO;
import com.example.demo.model.Policy;
import com.example.demo.service.PolicyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @PostMapping
    public PolicyResponseDTO createPolicy(@Valid @RequestBody PolicyRequestDTO dto) {
        return policyService.createPolicy(dto);
    }

    @GetMapping("/{id}")
    public PolicyResponseDTO getPolicyById(@PathVariable Long id) {
        return policyService.getPolicyById(id);
    }

    @PutMapping("/{id}")
    public PolicyResponseDTO updatePolicy(@PathVariable Long id,
                                          @RequestBody PolicyRequestDTO dto) {
        return policyService.updatePolicy(id, dto);
    }

    @DeleteMapping("/{id}")
    public void cancelPolicy(@PathVariable Long id) {
        policyService.cancelPolicy(id);
    }
    @GetMapping("/type/{type}")
    public List<PolicyResponseDTO> getPoliciesByType(@PathVariable String type) {
        return policyService.getPoliciesByType(type);
    }
    @GetMapping("/premium")
    public List<PolicyResponseDTO> getPoliciesByPremiumRange(
            @RequestParam double min,
            @RequestParam double max) {

        return policyService.getPoliciesByPremiumRange(min, max);
    }
    @GetMapping
    public Page<Policy> getAllPolicies(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam String direction) {

        return policyService.getAllPolicies(page, size, sortBy, direction);
    }
}