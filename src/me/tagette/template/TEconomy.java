package me.tagette.template;

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Methods;
import org.bukkit.plugin.Plugin;

/**
 * @description Handles economics of plugin
 * @author Tagette
 */
public class TEconomy {

    public static Methods Methods;
    public static Method Economy;

    public static void initialize() {
        Methods = new Methods();
    }

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
//                    if (BEconomy.Economy.hasBanks()) {
//                        MethodBankAccount bankAccount = BEconomy.Economy.getBankAccount("BANK", "NAME");
//                        if(bankAccount != null)
//                            bankAccount.add(5000.00);
//                    } else {
//                        MethodAccount account = BEconomy.Economy.getAccount("NAME");
//                        if(account != null)
//                            account.add(5000.00);
//                    }
}
