package com.example.fdmod.learn.walk;

import com.example.fdmod.entity.PlayerBotEntity;
import com.example.fdmod.entity.BotMode;
import net.minecraft.network.chat.Component;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class runLearnWalk {

    private final startLearnWalk instance;
    private final PlayerBotEntity bot;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> task;
    private volatile boolean isStopping = false;

    // Run the learning process
    public runLearnWalk(startLearnWalk instance, PlayerBotEntity bot) {
        this.instance = instance;
        this.bot = bot;
        startScheduler();
    }

    // Start the scheduler, making sure any leftover threads are cleared first
    private void startScheduler() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        task = scheduler.scheduleAtFixedRate(this::execute, 0, 1, TimeUnit.SECONDS);
    }

    // Executed every second
    public void execute() {
        if (isStopping || bot.getMode() == BotMode.STOP || !bot.isLearningWalk) {
            stop();
            return;
        }

        // Save data to trainingData
        this.instance.trainingData.add((float) this.bot.getX());

        // Send message to chat only if not stopping
        if (!bot.level().isClientSide() && bot.level().getServer() != null) {
            bot.level().getServer().getPlayerList().broadcastSystemMessage(
                Component.literal("1"),
                false
            );
        }
    }

    // Stop the scheduler when learning is complete
    public void stop() {
        if (isStopping) {
            return;
        }
        isStopping = true;

        // Cancel the scheduled task immediately
        if (task != null) {
            task.cancel(true);
        }

        // Shutdown scheduler
        if (scheduler != null) {
            scheduler.shutdownNow();
        }

        // Stop learning on the bot entity side
        bot.stopLearnWalk();
    }

    // Hard cleanup method to kill the old scheduler when restarting without triggering bot callbacks
    public void forceKill() {
        this.isStopping = true;
        if (task != null) {
            task.cancel(true);
        }
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}