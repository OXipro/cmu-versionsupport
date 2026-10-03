package com.oxipro.cmu.versionsupport;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;

public interface EntityUtilsSupport {


    void setTag(Entity entity, String key, String value);

    String getTag(Entity entity, String key);

    class SupportBuilder {

        /**
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static EntityUtilsSupport load() {
            return VersionMapping.load(EntityUtilsSupport.class, "com.oxipro.cmu.versionsupport.EntityUtils_");
        }
    }
}
