package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PolicyRequestDTO;
import com.example.demo.dto.PolicyResponseDTO;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.PolicyNotFoundException;
import com.example.demo.mapper.PolicyMapper;
import com.example.demo.model.Customer;
import com.example.demo.model.Policy;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PolicyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public PolicyResponseDTO createPolicy(PolicyRequestDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Policy policy = new Policy();

        policy.setPolicyNumber(dto.getPolicyNumber());
        policy.setPolicyType(dto.getPolicyType());
        policy.setPremiumAmount(dto.getPremiumAmount());
        policy.setCoverageAmount(dto.getCoverageAmount());
        policy.setStartDate(dto.getStartDate());
        policy.setEndDate(dto.getEndDate());
        policy.setStatus("ACTIVE");
        policy.setCustomer(customer);

        Policy savedPolicy = policyRepository.save(policy);

        return PolicyMapper.toDTO(savedPolicy);
    }

    public PolicyResponseDTO getPolicyById(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found"));

        return PolicyMapper.toDTO(policy);
    }

    public PolicyResponseDTO updatePolicy(Long id, PolicyRequestDTO dto) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found"));

        policy.setPolicyType(dto.getPolicyType());
        policy.setPremiumAmount(dto.getPremiumAmount());
        policy.setCoverageAmount(dto.getCoverageAmount());
        policy.setStartDate(dto.getStartDate());
        policy.setEndDate(dto.getEndDate());

        Policy updated = policyRepository.save(policy);

        return PolicyMapper.toDTO(updated);
    }

    public PolicyResponseDTO cancelPolicy(Long id) {

        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found"));

        policy.setStatus("CANCELLED");

        Policy updatedPolicy = policyRepository.save(policy);

        return PolicyMapper.toDTO(updatedPolicy);
    }

    public List<PolicyResponseDTO> getPoliciesByType(String type) {
        List<Policy> policies = policyRepository.findByPolicyType(type);
        List<PolicyResponseDTO> responseList = new ArrayList<>();

        for (Policy policy : policies) {
            responseList.add(PolicyMapper.toDTO(policy));
        }

        return responseList;
    }

    public List<PolicyResponseDTO> getPoliciesByPremiumRange(double min, double max) {
        List<Policy> policies = policyRepository.findByPremiumAmountBetween(min, max);
        List<PolicyResponseDTO> responseList = new ArrayList<>();

        for (Policy policy : policies) {
            responseList.add(PolicyMapper.toDTO(policy));
        }

        return responseList;
    }
    public Page<Policy> getAllPolicies(int page, int size, String sortBy, String direction) {

        Sort sort;

        if (direction.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return policyRepository.findAll(pageable);
    }
}