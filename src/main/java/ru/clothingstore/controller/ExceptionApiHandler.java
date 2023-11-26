package ru.clothingstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.clothingstore.service.CategoryService;
import ru.clothingstore.util.AppError;
import ru.clothingstore.util.exception.*;

@ControllerAdvice
public class ExceptionApiHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler
//    public AppError handleException(ResourceNotFoundException exception) {
//        return new AppError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
//    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({GoodNotFoundException.class, CategoryNotFoundException.class, OrderNotFoundException.class, UserNotFoundException.class, RoleNotFoundException.class})
    public String handleException(GoodNotFoundException exception, Model model) {
        model.addAttribute("error", new AppError(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        LOGGER.error(exception.getMessage(), exception);
        return "404";
    }
}
