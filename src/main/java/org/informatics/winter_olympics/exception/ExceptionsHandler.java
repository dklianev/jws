package org.informatics.winter_olympics.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "org.informatics.winter_olympics.controller.view")
public class ExceptionsHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public String handleException(ObjectNotFoundException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "/errors/not-found-error";
    }

    @ExceptionHandler(Exception.class)
    protected String handleException(Exception exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "/errors/error";
    }
}
