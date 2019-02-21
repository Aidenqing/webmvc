package com.aiden.mvc;

import com.aiden.mvc.anotation.Controller;
import com.aiden.mvc.anotation.RequestMapping;
import com.aiden.mvc.anotation.Scope;

/**
 * /* @author aiden
 * /* @creat  2019/2/21 14:12
 * /* @Description
 **/

@Controller
@RequestMapping("/aiden")
@Scope("prototype")
//singleton
public class IndexController extends BaseController{
    private int age = 1;

    @RequestMapping("/index")
    public void index(){
        age ++;
        System.out.println(age);
        System.out.println("index方法执行了"+"username: "+request.getParameter("username"));
    }

    @RequestMapping("/delete")
    public void delete(){
        System.out.println("delete方法执行了");
    }


    @RequestMapping("/search")
    public void search(){
        System.out.println("search方法执行了");
    }


    @RequestMapping("/update")
    public void update(){
        System.out.println("update方法");
    }



}
