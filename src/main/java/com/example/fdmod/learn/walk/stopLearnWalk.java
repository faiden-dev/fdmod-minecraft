package com.example.fdmod.learn.walk;

import com.example.fdmod.entity.PlayerBotEntity;
import com.example.fdmod.entity.BotMode;

public class stopLearnWalk {
    
    // Function to stop learning and reset bot mode to STOP
    public stopLearnWalk(PlayerBotEntity bot) {
        bot.setMode(BotMode.STOP);
    }
}
