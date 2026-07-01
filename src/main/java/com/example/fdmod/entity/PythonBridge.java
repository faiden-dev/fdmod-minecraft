package com.example.fdmod.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class PythonBridge {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5555;

    // async socket connection to prevent server lags
    public static void sendAsync(PlayerBotEntity bot, String message, ServerPlayer player) {

        CompletableFuture.supplyAsync(() -> {

            // open connection and wait for python input
            try (Socket socket = new Socket(HOST, PORT)) {
                socket.setSoTimeout(10000);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // send data
                out.println(bot.getClassName() + "|" + message);

                // read response
                String response = in.readLine();

                if (response != null) {
                    return response
                        .replace("<BOS>", "")
                        .replace("<EOS>", "")
                        .trim();
                }

            } catch (Exception e) {
                return "AI temporary unavailable";
            }

            return "Empty response";

        }).thenAcceptAsync(response -> {

            // send final text to minecraft chat safely
            player.getServer().execute(() -> {
                player.sendSystemMessage(
                    Component.literal("[" + bot.getClassName() + "] " + response)
                );
            });

        });
    }
}