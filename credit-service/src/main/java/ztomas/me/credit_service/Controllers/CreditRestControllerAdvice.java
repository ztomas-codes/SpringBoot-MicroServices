package ztomas.me.credit_service.Controllers;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CreditRestControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleRuntimeException(RuntimeException e)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        if (e.getMessage().contains("request body is missing"))
            map.put("message", "You need to add body to this request");
        else map.put("message", e.getMessage());
        return map;
    }

}

