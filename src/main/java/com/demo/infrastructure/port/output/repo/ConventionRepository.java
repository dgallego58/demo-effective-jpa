package com.demo.infrastructure.port.output.repo;

import com.demo.infrastructure.port.output.data.Convention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConventionRepository extends JpaRepository<Convention, UUID> {


}
