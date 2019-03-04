package ocean_article.controller;


import ocean_user.entity.Result;
import ocean_user.entity.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ArticleExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result error(Exception e){
        e.getStackTrace();
        return new Result(false, StatusCode.ERROR,e.getMessage());

    }
}
