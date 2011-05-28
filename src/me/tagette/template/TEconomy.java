package me.tagette.template;

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Methods;
import org.bukkit.plugin.Plugin;

/**
 * @description Handles the economics of the plugin
 * @author Tagette
 */
public class TEconomy {

    public static Methods Methods;
    public static Method Economy;

    /*
     * Initializes the economy methods
     */
    public static void initialize() {
        Methods = new Methods();
    }

    /*
     * Loads the economy method.
     * 
     * @param plugin    The plugin that was enabled.
     */
    public static void onEnable(Plugin plugin) {
        // Initialize according to what economy plugin is being enabled
        if (!TEconomy.Methods.hasMethod()) {
            if (TEconomy.Methods.setMethod(plugin)) {
                TEconomy.Economy = TEconomy.Methods.getMethod();
                TLogger.info(TEconomy.Economy.getName() + " version " + TEconomy.Economy.getVersion() + " loaded.");
            }
        }
    }
    // Sample economy usage
//    if (TEconomy.Economy.hasBanks()) {
//        MethodBankAccount bankAccount = TEconomy.Economy.getBankAccount("BANK", "NAME");
//        if(bankAccount != null)
//            bankAccount.add(5000.00);
//    } else {
//        MethodAccount account = TEconomy.Economy.getAccount("NAME");
//        if(account != null)
//            account.add(5000.00);
//    }
}
