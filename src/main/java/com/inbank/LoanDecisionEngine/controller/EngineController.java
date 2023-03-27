package com.inbank.LoanDecisionEngine.controller;


import com.inbank.LoanDecisionEngine.dto.DecisionData;
import com.inbank.LoanDecisionEngine.dto.InputData;
import com.inbank.LoanDecisionEngine.service.EngineService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class EngineController {

    @CrossOrigin
    @PostMapping()
    public DecisionData decide(@RequestBody InputData inputData){
        EngineService engine = new EngineService();
        return engine.decide(inputData);
    }
}
