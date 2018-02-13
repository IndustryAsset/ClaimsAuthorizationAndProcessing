package com.acc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TrainModelController {

	 @RequestMapping("prepareTrainingModel.htm")
     public ModelAndView testModel(HttpServletRequest request, HttpServletResponse response )
     {
                     ModelAndView modelandview = new ModelAndView();
                     modelandview.setViewName("prepareTrainingModel");
                     return modelandview;
     }
}
