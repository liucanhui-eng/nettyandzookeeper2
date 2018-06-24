package com.baizhi.rpc;

import java.io.Serializable;
import java.util.Arrays;

public class MethodInvokeMeta implements Serializable {
    private Class<?> targetInterface;
    private String method;
    private Class<?>[] parameterTypes;
    private Object[] args;

    public Class<?> getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class<?> targetInterface) {
        this.targetInterface = targetInterface;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "MethodInvokeMeta{" +
                "targetInterface=" + targetInterface +
                ", method='" + method + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
