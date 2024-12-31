package io.github.buzzxu.spuddy.configure;

import io.github.buzzxu.spuddy.R;
import io.github.buzzxu.spuddy.errors.*;
import io.github.buzzxu.spuddy.errors.SecurityException;
import io.github.buzzxu.spuddy.exceptions.ApplicationException;
import io.github.buzzxu.spuddy.security.exceptions.TokenAuthException;
import io.github.buzzxu.spuddy.security.exceptions.TokenGenerateException;
import io.github.buzzxu.spuddy.security.exceptions.UnknownAccountException;
import io.github.buzzxu.spuddy.security.exceptions.ValiCodeException;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.sql.SQLException;
import java.util.Objects;

/**
 * @author xux
 * @date 2024年12月31日 10:50:15
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R securityException(HttpServletResponse response, SecurityException ex){
        return R.error(ex.status(),ex.getMessage());
    }

    @ExceptionHandler(UserFreezeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R userFreezeException(HttpServletResponse response, UserFreezeException ex){
        return R.error(ex.status(),ex.getMessage());
    }

    @ExceptionHandler(LockedAccountException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R securityException(HttpServletResponse response,LockedAccountException ex){
        response.setStatus(401);
        return R.error(401,ex.getMessage());
    }

    @ExceptionHandler(TokenAuthException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R tokenException(HttpServletResponse response, TokenAuthException ex){
        log.error("发生异常:{}",ex.getMessage(),ex);
        return R.error(ex.status(),"认证失败,拒绝访问");
    }
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R unauthorizedException(HttpServletResponse response, UnauthorizedException ex) throws Exception {
        return R.error(ex.status(),ex.getMessage());
    }

    @ExceptionHandler(TokenGenerateException.class)

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R tokenGenerateException(HttpServletResponse response, TokenGenerateException ex){
        return R.error(ex.status(),ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R forbiddenException(HttpServletResponse response, ForbiddenException ex){
        response.setStatus(ex.status());
        return R.error(ex.status(),ex.getMessage());
    }
    @ExceptionHandler(ValiCodeException.class)
    @ResponseBody
    public R valiCodeException(HttpServletResponse response, ValiCodeException ex){
        return R.error(400,ex.getMessage());
    }

    @ExceptionHandler(UnknownAccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public R unknownAccException(HttpServletResponse response, UnknownAccountException ex){
        return R.error(400,ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R notfoundException(HttpServletResponse response, NotFoundException ex){
        return R.error(404,ex.getMessage());
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R appException(HttpServletResponse response, ApplicationException ex){
        log.error("发生异常:{}",ex.getMessage(), ex);
        return R.error(ex.status(),"网络不稳定,请稍后尝试");
    }
    @ExceptionHandler(NofityUserException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R globalException(HttpServletResponse response, NofityUserException ex){
        log.error("发生异常:{}",ex.getMessage(),ex);
        return R.error(400,ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R globalException(HttpServletResponse response, Exception ex){
        log.error("发生异常:{}",ex.getMessage(),ex);
        return R.error(500,"网络不稳定,请稍后尝试");
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R globalException(HttpServletResponse response, RuntimeException ex){
        log.error("发生异常:{}",ex.getMessage(),ex);
        return R.error(500,"网络不稳定,请稍后尝试");
    }
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R globalException(HttpServletResponse response, Throwable ex){
        log.error("发生异常:{}",ex.getMessage(),ex);
        return R.error(500,"网络不稳定,请稍后尝试");
    }
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R defaultException(HttpServletResponse response, Exception ex) {
        log.error("发生异常:{}", ex.getMessage(), ex);
        return R.error(500, ex.getMessage());
    }


    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R recordException(HttpServletResponse response, MissingServletRequestPartException ex){
        log.error("发生异常:{}",ex.getMessage(),ex);
        return R.error(500,"请选择需要上传的文件");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R methodNotFountException(HttpServletResponse response, HttpRequestMethodNotSupportedException ex){
        return R.error(405,"不支持"+ex.getMethod()+"方式请求");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R messageNotReadableException(HttpServletResponse response, HttpMessageNotReadableException ex){
        return R.error(400,"数据读取失败,请检查数据格式:"+ex.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R sqlSyntax(HttpServletResponse response,SQLException ex){
        log.error("发生异常:{}",ex.getMessage(),ex);
        return R.error(500,"网络异常,数据处理失败,请稍后再试");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public R missingRequesttParam(HttpServletResponse response,MissingServletRequestParameterException ex){
        return R.error(400,"参数错误");
    }

    @ExceptionHandler(MultiPartParserDefinition.FileTooLargeException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public R fileTooLarge(HttpServletResponse response,MultiPartParserDefinition.FileTooLargeException ex){
        return R.error(400,"上传文件体积过大,请压缩后重试");
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return R.error(400, ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).filter(Objects::nonNull).findFirst().orElse("参数错误"));
    }

}
