package br.com.pedro.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    // isso é meio estranho mas só de escrever isso o spring entende que queremos
    // uma função que traga os users com o username passado
    UserModel findByUsername(String username);
}
