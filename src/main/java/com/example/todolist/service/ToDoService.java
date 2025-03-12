package com.example.todolist.service;

import com.example.todolist.entity.ToDo;
import com.example.todolist.entity.User;
import com.example.todolist.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {
    private final ToDoRepository toDoRepository;

    @Autowired
    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public List<ToDo> findUncompletedToDos(User user) {
        return toDoRepository.findByUserAndCompleted(user, false);
    }

    public List<ToDo> findCompletedToDos(User user) {
        return toDoRepository.findByUserAndCompleted(user, true);
    }

    public ToDo markAsDone(Integer id) {
        Optional<ToDo> toDo = toDoRepository.findById(id);
        if (toDo.isPresent()) {
            toDo.get().setCompleted(true);
            return toDoRepository.save(toDo.get());
        } else {
            return null;
        }
    }

    public ToDo create(ToDo toDo) {
        return toDoRepository.save(toDo);
    }

    public void delete(Integer id) {
        toDoRepository.delete(toDoRepository.findById(id).get());
    }
}
