package com.example.todolist.repository;

import com.example.todolist.entity.ToDo;
import com.example.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
    List<ToDo> findByUserAndCompleted(User user, boolean completed);
}
