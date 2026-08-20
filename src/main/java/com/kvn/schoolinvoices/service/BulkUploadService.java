package com.kvn.schoolinvoices.service;

import com.kvn.schoolinvoices.*;
import com.kvn.schoolinvoices.entity.Student1;
import com.kvn.schoolinvoices.service.repository.Student1Respository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BulkUploadService {

    private static final Logger logger = LoggerFactory.getLogger(BulkUploadService.class);

    private final Student1Respository repository;
    private final ProgressService progressService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Async("csvExecutor")
    public void importCsv(File file,String jobId){
        logger.info("Thread Name : {}", Thread.currentThread().getName());
        logger.info("Starting CSV import for jobId: {} from file: {}", jobId, file.getName());
        try{

            BufferedReader reader=
                    new BufferedReader(
                            new FileReader(file));

            reader.readLine();

            List<Student1> batch=new ArrayList<>();

            String line;

            int processed=0;

            int total=(int)java.nio.file.Files.lines(file.toPath()).count()-1;

            logger.info("Total records to process: {}", total);
            progressService.create(jobId,total);

            while((line=reader.readLine())!=null){

                String[] c=line.split(",");

                Student1 s=new Student1();

                s.setRollNo(c[0]);

                s.setName(c[1]);

                s.setStandard(c[2]);

                batch.add(s);

                processed++;

                progressService.update(jobId,processed);

                if(batch.size()==500){

                    logger.debug("Saving batch of 500 student records");
                    repository.saveAll(batch);

                    repository.flush();

                    batch.clear();

                }

            }

            if(!batch.isEmpty()){

                logger.debug("Saving remaining {} student records", batch.size());
                repository.saveAll(batch);

            }

            logger.info("CSV import completed successfully for jobId: {}", jobId);
            progressService.complete(jobId);

        }
        catch(Exception ex){

            logger.error("Error during CSV import for jobId: {}", jobId, ex);
            ex.printStackTrace();

        }
    }

    @Async("csvExecutor")
    public void importCsv1(File file,String jobId){
        logger.info("Thread Name : {}", Thread.currentThread().getName());
        logger.info("Starting user CSV import for jobId: {} from file: {}", jobId, file.getName());
        try{

            BufferedReader reader=
                    new BufferedReader(
                            new FileReader(file));

            reader.readLine();

            List<AppUser> batch=new ArrayList<>();

            String line;

            int processed=0;

            int total=(int)java.nio.file.Files.lines(file.toPath()).count()-1;

            logger.info("Total records to process: {}", total);
            progressService.create(jobId,total);
            int i=0;
            while((line=reader.readLine())!=null){
         /*       if(i==2){
                    logger.debug("Reached limit of 2 records, breaking loop");
                    break;
                }
                i++; */

                String[] c=line.split(",");

           //     Set rolesreq =  new HashSet();
             //   rolesreq.add("ROLE_SCHOOL_ADMIN");
            //    request.setRoles(rolesreq);



               // Set<Role> roles = resolveRoles(rolesreq);
                Set roles = new HashSet();
                Role role = new Role();
                role.setId(3L);
                role.setName(RoleName.ROLE_SCHOOL_ADMIN);
                roles.add(role);

                logger.debug("Creating user: {}", c[0]);
                AppUser user = AppUser.builder()
                        .fullName(c[0])
                        .email(c[1]+"@example.com")
                        .password(passwordEncoder.encode(c[2]))
                        .roles(roles).role("schooladmin")
                        .build();


                batch.add(user);

                processed++;

                progressService.update(jobId,processed);

                if(batch.size()==500){

                    logger.debug("Saving batch of 500 user records");
                    userRepository.saveAll(batch);

                    userRepository.flush();

                    batch.clear();

                }

            }

            if(!batch.isEmpty()){

                logger.debug("Saving remaining {} user records", batch.size());
                userRepository.saveAll(batch);

            }

            logger.info("User CSV import completed successfully for jobId: {}", jobId);
            progressService.complete(jobId);

        }
        catch(Exception ex){

            logger.error("Error during user CSV import for jobId: {}", jobId, ex);
            ex.printStackTrace();

        }
    }

    private Set<Role> resolveRoles(Set<String> roleNames) {
        logger.debug("Resolving roles for: {}", roleNames);
        Set<Role> roles = new HashSet<>();

        if (roleNames == null || roleNames.isEmpty()) {
            logger.debug("No roles provided, using default ROLE_USER");
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new IllegalStateException("ROLE_USER not configured"));
            roles.add(userRole);
            return roles;
        }

        for (String roleNameStr : roleNames) {
            logger.debug("Processing role: {}", roleNameStr);
            RoleName roleName = RoleName.valueOf(roleNameStr);
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalStateException(roleName + " not configured"));
            roles.add(role);
        }
        logger.debug("Resolved {} roles", roles.size());
        return roles;
    }

}