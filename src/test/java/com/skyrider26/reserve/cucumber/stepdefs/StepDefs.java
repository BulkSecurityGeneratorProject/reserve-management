package com.skyrider26.reserve.cucumber.stepdefs;

import com.skyrider26.reserve.ReserveManagementApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ReserveManagementApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
