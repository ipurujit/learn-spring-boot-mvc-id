package chapter.one.LearnSpringBoot.repositories;

import chapter.one.LearnSpringBoot.entities.Role;
import chapter.one.LearnSpringBoot.enums.RoleNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    public Optional<Role> findByRoleName(final RoleNameEnum roleName);
}
