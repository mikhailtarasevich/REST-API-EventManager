package com.mikhail.tarasevich.eventmanager.repository;

import com.mikhail.tarasevich.eventmanager.entity.Contract;
import com.mikhail.tarasevich.eventmanager.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ContractRepository extends JpaRepository<Contract, Integer> {

    Optional<Contract> findById(int id);

    List<Contract> findContractByUserIdAndStatusOrderById(int userId, Status status);

    List<Contract> findContractByStatusOrderById(Status status);

    void deleteContractsByUserId(int userId);

}
