package com.example.fdmod.learn.walk;

import com.example.fdmod.entity.PlayerBotEntity;
import com.example.fdmod.entity.BotMode;
import java.util.ArrayList;
import java.util.List;

public class startLearnWalk {
    
    // Training data array for each individual bot
    public final List<Float> trainingData = new ArrayList<>();
    
    // Reference to the active scheduler runner
    public final runLearnWalk runner;

    // Function to initialize learning and stop current pathfinding execution
    public startLearnWalk(PlayerBotEntity bot) {
        this.trainingData.clear();
        bot.setMode(BotMode.LEARN_WALK);
        
        // Save the runner instance so we can kill its scheduler later if needed
        this.runner = new runLearnWalk(this, bot);
    }
}