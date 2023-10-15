package br.com.pedro.todolist.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//Estudar esta notation
// Usado para definir classes globais no momento de tratamento de escessões,
//toda excessão que existir vai passar por aqui e se ela for do tipo que a gente 
//tratar aqui ela  vai para aqui
@ControllerAdvice
public class ExceptionHandlerController {

    // uso isso para informar o tipo de exception que vou tratar aqui
    // ou seja, toda excessão deste tipo vai passar por aqui antes de retornar para
    // o usuario
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(400).body(e.getMostSpecificCause().getMessage());
    }
}
