package com.Acrobot.ChestShop.Listeners.PreTransaction;

import com.Acrobot.Breeze.Utils.InventoryUtil;
import com.Acrobot.ChestShop.Economy.Economy;
import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.Acrobot.ChestShop.Events.PreTransactionEvent.TransactionOutcome.*;
import static com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType.BUY;
import static com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType.SELL;

/**
 * @author Acrobot
 */
public class AmountAndPriceChecker implements Listener {
    @EventHandler
    public static void onItemCheck(PreTransactionEvent event) {
        if (event.isCancelled() || event.getTransactionType() != BUY) {
            return;
        }

        ItemStack[] stock = event.getStock();
        Inventory ownerInventory = event.getOwnerInventory();

        if (!Economy.hasEnough(event.getClient().getName(), event.getPrice())) {
            event.setCancelled(CLIENT_DOES_NOT_HAVE_ENOUGH_MONEY);
            return;
        }

        if (!InventoryUtil.hasItems(stock, ownerInventory)) {
            event.setCancelled(NOT_ENOUGH_STOCK_IN_CHEST);
        }
    }

    @EventHandler
    public static void onSellItemCheck(PreTransactionEvent event) {
        if (event.isCancelled() || event.getTransactionType() != SELL) {
            return;
        }

        ItemStack[] stock = event.getStock();
        Inventory clientInventory = event.getClientInventory();

        if (Economy.isOwnerEconomicallyActive(event.getOwnerInventory()) && !Economy.hasEnough(event.getOwner().getName(), event.getPrice())) {
            event.setCancelled(SHOP_DOES_NOT_HAVE_ENOUGH_MONEY);
            return;
        }

        if (!InventoryUtil.hasItems(stock, clientInventory)) {
            event.setCancelled(NOT_ENOUGH_STOCK_IN_INVENTORY);
        }
    }
}
