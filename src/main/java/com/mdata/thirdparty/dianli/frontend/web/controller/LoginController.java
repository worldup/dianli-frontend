package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import com.mdata.thirdparty.dianli.frontend.web.controller.commons.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by administrator on 16/5/14.
 */
@Controller
public class LoginController   {
    @RequestMapping(method ={RequestMethod.POST} ,value = "/login")
    @ResponseBody
    public Result login(String name,String pwd,String validCode) {
        Result result=new Result();
        if("0".equals(validCode))
            result.setCode(-1);
       return  result;
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerGet(@RequestParam(value = "error", required = false) boolean failed,
                                    HttpServletRequest request) {
        ModelAndView model = new ModelAndView("register-get");

        //
        // model MUST contain HTML with <img src="/captcha.jpg" /> tag
        //

        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerPost(@RequestParam(value = "email", required = true) String email,
                                     @RequestParam(value = "password", required = true) String password, HttpServletRequest request) {
        ModelAndView model = new ModelAndView("register-post");

        if (email.isEmpty())
            throw new RuntimeException("email empty");

        if (password.isEmpty())
            throw new RuntimeException("empty password");

        String captcha = request.getParameter("captcha");

        if (!captcha.equals(getGeneratedKey(request)))
            throw new RuntimeException("bad captcha");

        //
        // eveyting is ok. proceed with your user registration / login process.
        //

        return model;
    }
    private Properties props = new Properties();
    private Producer kaptchaProducer = null;
    private String sessionKeyValue = null;
    private String sessionKeyDateValue = null;

    @PostConstruct
    public void init() {
        ImageIO.setUseCache(false);
        props.setProperty("kaptcha.border", "no");
        props.setProperty("kaptcha.border.color", "55,179,90");
        props.setProperty("kaptcha.textproducer.font.color", "blue");
        props.setProperty("kaptcha.image.width", "125");
        props.setProperty("kaptcha.image.height", "50");
        props.setProperty("kaptcha.border.color", "55,179,90");
        props.setProperty("kaptcha.textproducer.font.size", "40");
        props.setProperty("kaptcha.session.key", "code");
        props.setProperty("kaptcha.textproducer.char.length", "4");
        Config config = new Config(this.props);
        this.kaptchaProducer = config.getProducerImpl();
        this.sessionKeyValue = config.getSessionKey();
        this.sessionKeyDateValue = config.getSessionDate();
    }

    @RequestMapping(value = "/captcha.jpg", method = RequestMethod.GET)
    public void captcha(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-store, no-cache");
        resp.setContentType("image/jpeg");
        String capText = this.kaptchaProducer.createText();
        req.getSession().setAttribute(this.sessionKeyValue, capText);
        req.getSession().setAttribute(this.sessionKeyDateValue, new Date());
        BufferedImage bi = this.kaptchaProducer.createImage(capText);
        ServletOutputStream out = resp.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        req.getSession().setAttribute(this.sessionKeyValue, capText);
        req.getSession().setAttribute(this.sessionKeyDateValue, new Date());
    }

    public String getGeneratedKey(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (String)session.getAttribute("KAPTCHA_SESSION_KEY");
    }

}
