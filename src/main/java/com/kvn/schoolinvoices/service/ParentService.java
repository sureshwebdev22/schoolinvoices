package com.kvn.schoolinvoices.service;

import com.kvn.schoolinvoices.AppUser;
import com.kvn.schoolinvoices.UserRepository;
import com.kvn.schoolinvoices.dto.ParentDTO;
import com.kvn.schoolinvoices.dto.ParentSearchDTO;
import com.kvn.schoolinvoices.entity.Parent;
import com.kvn.schoolinvoices.service.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Map<String,String>> createParent(ParentDTO dto) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();


      AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Parent parent = new Parent();
        parent.setFatherName(dto.getFatherName());
        parent.setMotherName(dto.getMotherName());
        parent.setAddress(dto.getAddress());
        parent.setUser(user);

        Parent p= parentRepository.save(parent);
        return ResponseEntity.ok(
                Map.of("message", "Student deleted successfully")
        );


     //   return null;
    }

    public Page<ParentDTO> searchParents(
            ParentSearchDTO searchDTO,
            Pageable pageable) {

        return parentRepository
                .findByFatherNameContainingIgnoreCaseAndMotherNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
                        searchDTO.getFatherName() == null ? "" : searchDTO.getFatherName(),
                        searchDTO.getMotherName() == null ? "" : searchDTO.getMotherName(),
                        searchDTO.getAddress() == null ? "" : searchDTO.getAddress(),
                        pageable)
                .map(parent -> new ParentDTO(
                        parent.getFatherName(),
                        parent.getMotherName(),
                        parent.getAddress(),
                        parent.getParentId()
                ));
    }
}