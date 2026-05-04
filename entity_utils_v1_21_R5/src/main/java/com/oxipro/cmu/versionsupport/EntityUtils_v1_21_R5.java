package com.oxipro.cmu.versionsupport;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class EntityUtils_v1_21_R5 implements EntityUtilsSupport {
    @Override
    public void setTag(Entity entity, String key, String value) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey nsKey = new NamespacedKey("cmu-version-support" , key);
        pdc.set(nsKey, PersistentDataType.STRING, value);
    }

    @Override
    public String getTag(Entity entity, String key) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey nsKey = new NamespacedKey("cmu-version-support" , key);
        return pdc.get(nsKey, PersistentDataType.STRING);
    }
}