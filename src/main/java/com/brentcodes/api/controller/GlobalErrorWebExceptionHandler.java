package com.brentcodes.api.controller;

import com.brentcodes.error_handling.AppException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalErrorWebExceptionHandler.class);

    public GlobalErrorWebExceptionHandler(GlobalErrorAttributes globalErrorAttributes, ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer) {
        super(globalErrorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable cause = getError(request);
        final AppErrorsResponse response;
        final HttpStatusCode httpStatus;
        if (cause instanceof AppException ex) {
            response = new AppErrorsResponse(ex);
            httpStatus = ex.getHttpStatus();
            if (log.isDebugEnabled()) {
                log.debug("ApplicationException [" + response + "] for request:" + request, ex);
            } else {
                log.info("ApplicationException [" + response + "] for request:" + request);
            }
        } else if (cause instanceof MethodArgumentNotValidException ex) {
            response = new AppErrorsResponse(ex);
            httpStatus = HttpStatus.BAD_REQUEST;
            log.info("MethodArgumentNotValidException [" + response + "] for request:" + ex);
        } else if (cause instanceof ConstraintViolationException ex) {
            response = new AppErrorsResponse(ex);
            httpStatus = HttpStatus.BAD_REQUEST;
            if (log.isDebugEnabled()) {
                log.debug("ConstraintViolationException [" + response + "] for request:" + request, ex);
            } else {
                log.info("ConstraintViolationException [" + response + "] for request:" + request);
            }
        } else if (cause instanceof ErrorResponseException ex) {
            response = new AppErrorsResponse();
            httpStatus = ex.getStatusCode();
            log.info("ErrorResponseException [" + response + "] for request:" + request, ex);
        } else {
            response = new AppErrorsResponse();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            log.info("Exception [" + response + "] for request:" + request, cause);
        }

        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(response));
    }
}
