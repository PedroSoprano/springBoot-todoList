package br.com.pedro.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
// import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
// import lombok.Getter;
// import lombok.Setter;

@Data
@Entity(name = "tb_users")
public class UserModel {

    // @Getter
    // @Setter
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    // @Column(name = "lalala")
    @Column(unique = true)
    private String username;
    // @Column
    private String name;
    // @Column
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // exemplo de getters e setters na "mão"
    // public void setUsername(String username){
    // this.username = username;
    // }

    // public String getUsername(){
    // return username;
    // }
}
