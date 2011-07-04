package me.tagette.template;

import com.couk.Adamki11s.AutoUpdater.AUCore;

/**
 * @description Handles automatic updating for the plugin.
 * @author Tagette
 */
public class TUpdater {
    
    private static Template plugin;
    private static AUCore core;
    
    public static void initialize(Template instance){
        plugin = instance;
        if(TConstants.updaterEnabled && !TConstants.updateHistoryUrl.equals("")){
            core = new AUCore(TConstants.updateHistoryUrl, TLogger.getLog(), TLogger.getPrefix());
            
            if(core != null && !core.checkVersion(TConstants.currentVersion, TConstants.currentSubVersion, plugin.name)){
                core.forceDownload(TConstants.downloadUrl, plugin.name);
            }
        }
    }
    
    public static AUCore getCore(){
        return core;
    }
}
