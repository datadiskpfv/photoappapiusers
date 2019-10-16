package uk.co.datadisk.photoapp.api.users.repositories;

import org.springframework.data.repository.CrudRepository;
import uk.co.datadisk.photoapp.api.users.data.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}