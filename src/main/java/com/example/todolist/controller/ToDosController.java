package com.example.todolist.controller;


import com.example.todolist.entity.ToDo;
import com.example.todolist.entity.User;
import com.example.todolist.service.ToDoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class ToDosController {
    private final ToDoService toDoService;

    @Autowired
    public ToDosController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping()
    public String loadToDos(Model model, HttpSession session) {
        User user = (User) session.getAttribute("signedInUser");

        List<ToDo> toDoList = toDoService.findUncompletedToDos(user);
        List<ToDo> doneToDoList = toDoService.findCompletedToDos(user);

        model.addAttribute("userInfo", user.getFirstName() + " " + user.getLastName());

        model.addAttribute("todoList", toDoList);
        model.addAttribute("doneToDoList", doneToDoList);

        return "todos";
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodoStatus(@PathVariable Integer id) {
        ToDo toDo = toDoService.markAsDone(id);
        if (toDo != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).body("Ошибка при обновлении задачи");
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ToDo> createTodo(@RequestBody ToDo todo, HttpSession session) {
        try {
            User user = (User) session.getAttribute("signedInUser");
            todo.setUser(user);

            ToDo createdTodo = toDoService.create(todo);

            return ResponseEntity.ok(createdTodo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Integer id) {
        try {
            toDoService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
