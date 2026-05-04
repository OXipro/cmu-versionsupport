package com.oxipro.cmu.versionsupport;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class itemstack_v1_21_R5 implements ItemStackSupport {

    private static final String NAMESPACE = "cmu";

    @Override
    public ItemStack getInHand(@NotNull Player player) {
        return player.getInventory().getItemInMainHand();
    }

    @Override
    public ItemStack getInOffHand(@NotNull Player player) {
        return player.getInventory().getItemInOffHand();
    }

    @Nullable
    @Override
    public ItemStack createItem(String material, int amount, byte data) {
        try {
            Material m = Material.valueOf(material.toUpperCase());
            return new ItemStack(m, amount);
        } catch (Exception e) {
            return null;
        }
    }

    public ItemStack createItem(Material material, int amount, byte data) {
        return new ItemStack(material, amount);
    }

    private NamespacedKey key(String key) {
        return new NamespacedKey(NAMESPACE, key);
    }

    public ItemStack addTag(ItemStack item, String k, String value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.getPersistentDataContainer().set(
                key(k),
                PersistentDataType.STRING,
                value
        );

        item.setItemMeta(meta);
        return item;
    }

    public boolean hasTag(ItemStack item, String k) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(key(k), PersistentDataType.STRING);
    }

    @Nullable
    public String getTag(ItemStack item, String k) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        return meta.getPersistentDataContainer().get(
                key(k),
                PersistentDataType.STRING
        );
    }

    public ItemStack removeTag(ItemStack item, String k) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.getPersistentDataContainer().remove(key(k));
        item.setItemMeta(meta);
        return item;
    }

    public void setUnbreakable(@NotNull ItemStack itemStack, boolean unbreakable) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        meta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(meta);
    }

    public double getDamage(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return 0;

        return itemStack.getType().getMaxDurability(); // approximation stable
    }

    public boolean isArmor(ItemStack item) {
        if (item == null) return false;
        return item.getType().name().endsWith("_HELMET")
                || item.getType().name().endsWith("_CHESTPLATE")
                || item.getType().name().endsWith("_LEGGINGS")
                || item.getType().name().endsWith("_BOOTS");
    }

    public boolean isTool(ItemStack item) {
        if (item == null) return false;
        String n = item.getType().name();
        return n.endsWith("_PICKAXE")
                || n.endsWith("_AXE")
                || n.endsWith("_SHOVEL")
                || n.endsWith("_HOE");
    }

    public boolean isSword(ItemStack item) {
        return item != null && item.getType().name().endsWith("_SWORD");
    }

    public boolean isAxe(ItemStack item) {
        return item != null && item.getType().name().endsWith("_AXE");
    }

    public boolean isBow(ItemStack item) {
        return item != null && item.getType() == Material.BOW;
    }

    public boolean isProjectile(ItemStack item) {
        return item != null && (
                item.getType() == Material.ARROW ||
                        item.getType() == Material.SPECTRAL_ARROW ||
                        item.getType() == Material.TIPPED_ARROW
        );
    }

    @Override
    public boolean isPlayerHead(@NotNull ItemStack itemStack) {
        return itemStack.getType() == Material.PLAYER_HEAD;
    }

    public void minusAmount(Player p, @NotNull ItemStack i, int amount) {
        if (i == null) return;

        if (i.getAmount() - amount <= 0) {
            p.getInventory().removeItem(i);
            return;
        }

        i.setAmount(i.getAmount() - amount);
        p.updateInventory();
    }

    @Override
    public ItemStack applyPlayerSkinOnHead(Player player, ItemStack copyTagFrom) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        if (copyTagFrom != null) {
            head.setItemMeta(copyTagFrom.getItemMeta());
        }

        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            head.setItemMeta(meta);
        }

        return head;
    }

    @Override
    public ItemStack applySkinTextureOnHead(String texture, ItemStack copyTagFrom) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        if (copyTagFrom != null) {
            head.setItemMeta(copyTagFrom.getItemMeta());
        }

        return head;
    }

    public boolean isItem(Material mat) {
        return mat.isItem();
    }

    private static final Random RANDOM = new Random();

    @Override
    public void giveRandomItem(Player player, int slot, List<String> blacklist) {
        if (player == null) return;
        Set<String> blacklistSet = blacklist.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
        List<Material> validMaterials = Arrays.stream(Material.values())
                .filter(material -> material != Material.AIR)
                .filter(this::isItem)
                .filter(material -> !blacklistSet.contains(material.name()))
                .collect(Collectors.toList());

        if (validMaterials.isEmpty()) return;

        Material randomMaterial = validMaterials.get(RANDOM.nextInt(validMaterials.size()));

        ItemStack item = new ItemStack(randomMaterial);
        if (slot < 0) {
            player.getInventory().addItem(item);
        } else if (slot < player.getInventory().getSize()) {
            player.getInventory().setItem(slot, item);
        } else {
            player.getInventory().addItem(item);
        }
    }
}