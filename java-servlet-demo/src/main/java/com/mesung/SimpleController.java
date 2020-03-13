package com.mesung;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//beanNameHandlerMapping
@Controller("/simple")
public class SimpleController implements org.springframework.web.servlet.mvc.Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        //ModelAndView : 모델과 뷰의 정보를 준다.
        return new ModelAndView("WEB-INF/simple.jsp");
    }
}
