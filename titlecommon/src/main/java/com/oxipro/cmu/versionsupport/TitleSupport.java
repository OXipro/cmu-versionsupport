package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface TitleSupport {

    /**
     * Send a title or a subtitle.
     *
     * @param p        target player.
     * @param title    msg, null if you want just a subtitle.
     * @param subtitle msg, null if you want just a title.
     * @param fadeIn   fade in in ticks.
     * @param fadeOut  fade out in ticks.
     * @param stay     keep msg in ticks.
     */
    void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut);

    /**
     * Send an action bar message.
     *
     * @param p    target player.
     * @param text message.
     */
    void playAction(Player p, String text);

    class SupportBuilder {

        /**
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static TitleSupport load() {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            Class<?> c;
            try {
                c = Class.forName("com.oxipro.cmu.versionsupport.title_" + version);
            } catch (ClassNotFoundException e) {
                //I can't run on your version
                return null;
            }
            try {
                return (TitleSupport) c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        }
    }
}
