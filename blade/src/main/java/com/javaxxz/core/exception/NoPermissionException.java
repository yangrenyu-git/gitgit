
package com.javaxxz.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.javaxxz.core.constant.ConstShiro;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = ConstShiro.NO_PERMISSION)
public class NoPermissionException extends RuntimeException {

}
