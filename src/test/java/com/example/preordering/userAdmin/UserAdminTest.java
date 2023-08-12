package com.example.preordering.userAdmin;


import com.example.preordering.entity.UserAdmin;
import com.example.preordering.repository.UserAdminRepository;
import lombok.RequiredArgsConstructor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;


@SpringBootTest
@State(Scope.Benchmark)
public class UserAdminTest {

    @Autowired
    private UserAdminRepository userAdminRepository;



    @CachePut(value = "useradmins")
    public void addUser(String username){
        for (int i = 0; i < 100; i++){
            userAdminRepository.save(
                    UserAdmin.builder()
                            .username(i+username)
                            .build()
            );
        }
    }
    @BeforeClass
    public void addUsers8(){
        addUser("siroj");
    }
    @Test
    @Benchmark
    @Cacheable(value = "useradmins")
    public void findFromQuery(){
        userAdminRepository.findByUsername("99siroj");
        userAdminRepository.findByUsername("100siroj");

    }
    @Test
    @Benchmark

    public void findFromCache(){
        userAdminRepository.findByUsername("99siroj");
        userAdminRepository.findByUsername("100siroj");

    }
    @AfterClass
    public void delete(){
        userAdminRepository.deleteAll();
    }




}
