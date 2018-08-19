package com.neo.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(GlobalHandlerExceptionResolver.class);

    /**
     * 没有区分哪种情况下的异常
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        logger.error(e.getMessage(), e);
        HandlerMethod method = (HandlerMethod) o;
        // ResponseBody.class注解说明是否是Ajax调用 否则返回视图
        if (method.getMethod().isAnnotationPresent(ResponseBody.class)) {
            try {
                httpServletResponse.getWriter().write(e.getMessage());
                httpServletResponse.getWriter().flush();
                httpServletResponse.getWriter().close();// 这里很重要 否则ajax的readystatus为0
            } catch (Exception exception){
                logger.error(e.getMessage());
            }
            return null;
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("errorInfo", "服务器异常，异常提示：" + e.getMessage());
            return new ModelAndView("error/error", map);
        }
    }
}
