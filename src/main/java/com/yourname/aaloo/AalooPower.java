package com.yourname.aaloo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AalooPower extends JavaPlugin implements Listener {

    private NamespacedKey keyAalooType;

    @Override
    public void onEnable() {
        keyAalooType = new NamespacedKey(this, "aaloo_type");
        getServer().getPluginManager().registerEvents(this, this);
        registerItemsAndRecipes();
        getLogger().info("AalooPower enabled");
    }

    private void registerItemsAndRecipes() {
        ItemStack iron = makeGlowingAaloo("§fIron Aaloo", "iron");
        ItemStack gold = makeGlowingAaloo("§6Gold Aaloo", "gold");
        ItemStack diamond = makeGlowingAaloo("§bDiamond Aaloo", "diamond");
        ItemStack stone = makeGlowingAaloo("§7Stone Aaloo", "stone");

        addRecipe(iron, "iron_aaloo", Material.IRON_INGOT);
        addRecipe(gold, "gold_aaloo", Material.GOLD_INGOT);
        addRecipe(diamond, "diamond_aaloo", Material.DIAMOND);
        addRecipe(stone, "stone_aaloo", Material.COBBLESTONE);
    }

    private ItemStack makeGlowingAaloo(String displayName, String type) {
        ItemStack item = new ItemStack(Material.POTATO);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);

        // fake enchant to create glint (chamak) and hide actual enchant text:
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        // mark item so we can detect it reliably
        meta.getPersistentDataContainer().set(keyAalooType, PersistentDataType.STRING, type);

        item.setItemMeta(meta);
        return item;
    }

    private void addRecipe(ItemStack result, String key, Material surround) {
        NamespacedKey ns = new NamespacedKey(this, key);
        ShapedRecipe recipe = new ShapedRecipe(ns, result);
        recipe.shape("XXX", "XAX", "XXX");
        recipe.setIngredient('X', surround);
        recipe.setIngredient('A', Material.POTATO);
        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(keyAalooType, PersistentDataType.STRING)) return;

        String type = meta.getPersistentDataContainer().get(keyAalooType, PersistentDataType.STRING);
        Player p = e.getPlayer();

        switch (type) {
            case "iron":
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 15, 1));
                p.sendMessage("§7Iron Aaloo kha ke Resistance mila!");
                break;
            case "gold":
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 1));
                p.sendMessage("§6Gold Aaloo kha ke Regeneration mila!");
                break;
            case "diamond":
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 1));
                p.sendMessage("§bDiamond Aaloo kha ke Strength mila!");
                break;
            case "stone":
                p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 15, 1));
                p.sendMessage("§8Stone Aaloo kha ke Haste mila!");
                break;
            default:
                // nothing
        }
    }
}
