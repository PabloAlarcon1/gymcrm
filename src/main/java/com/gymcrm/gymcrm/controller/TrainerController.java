package com.gymcrm.gymcrm.controller;

import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrainerController {

    @Autowired
    TrainerService trainerService;
    @PostMapping("/trainer")
    public void create(@RequestBody Trainer trainer){
        trainerService.create(trainer);
    }
}
