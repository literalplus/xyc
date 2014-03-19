package io.github.xxyy.common;

import io.github.xxyy.common.localisation.XycLocale;
import io.github.xxyy.common.xyplugin.GenericXyPlugin;
import org.bukkit.command.CommandSender;

import java.util.HashMap;


/**
 * A simple class that manages displaying command help.
 * 
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class HelpManager {
	/**
	 * A collection of available {@link HelpManager}s.
	 */
	public static HashMap<String,HelpManager> helpMans = new HashMap<>();
    /**
     * The name of this command, will be inserted as 1st argument in <i>XYC-lang-header</i>.
     */
	public String helpPageTitle;
	/**
	 * Some lines to describe this command. Will be displayed on the first page.
	 */
	public String[] cmdDescLines;
	/**
	 * Sub-commands of this command.
	 * i.e. for /xyc test, the mapping would be as follows:
	 * "test" =&gt; "An example of a sub-command."
	 */
	public HashMap<String,String> subCmds;
	
	/**
	 * Constructs a new {@link HelpManager}.
	 * @param cmdName Name of the command (see {@link HelpManager#helpPageTitle})
	 * @param cmdDescLines A few loines to describe this command to be displayed on the first help page.
	 * @param subCmds see: {@link HelpManager#subCmds}
	 */
	public HelpManager(String cmdName,String[] cmdDescLines,HashMap<String,String> subCmds){
		this.helpPageTitle=cmdName; this.subCmds=subCmds; 
		this.cmdDescLines=cmdDescLines;
	}
	
	/**
	 * Prints a help page to a {@link CommandSender}.
	 * @param sender Who shall receive help?
	 * @param label The alias this coammnd was called with.
	 * @param pageNum Page number - provide "1" to show the first page.
	 *                    If this is not an int, an error message will be displayed.
	 * @param helpCmdLabel Help command to display a specific page, i.e. "/xyc help" will be
	 *                      displayed as "/xyc help [next Page]".
	 */
	public void printHelpToSender(CommandSender sender,String label,String pageNum,String helpCmdLabel){
		//6 Commands/page
		int intPageNum; int pageCount=1; int i=0;
		if(this.subCmds.size() > 6){
			if((this.subCmds.size() % 6) == 0) {
                pageCount=this.subCmds.size()/6;
            } else {
                pageCount=(this.subCmds.size()/6) + 1;
            }
		}
		if(pageNum.isEmpty()){
			intPageNum=1;
		}else{
			try{
				intPageNum = Integer.parseInt(pageNum);
			}catch(Exception e){
				sender.sendMessage(XycLocale.getString("XYC-lang-nan", sender.getName()));
				return;
			}
		}
		//render begin
		sender.sendMessage(String.format(XycLocale.getString("XYC-lang-header", sender.getName()),this.helpPageTitle,intPageNum,pageCount));
		//command description
		if(intPageNum <= 1){//to capture page 0, too.
    		for(String ln : this.cmdDescLines) {
                sender.sendMessage("§8"+ln);
            }
		}
		//subcommands
		for(String key : this.subCmds.keySet()){
			i=i+1;
			if(i >= 6*intPageNum) {
                break;
            }
			if(i < 6*(intPageNum-1)) {
                continue;
            }
			sender.sendMessage("§3/"+label+" "+key+" §7"+this.subCmds.get(key));
		}
		if(pageCount != 1 && intPageNum != pageCount) {
            sender.sendMessage(String.format(XycLocale.getString("XYC-lang-nextpage", sender.getName()), helpCmdLabel,(intPageNum + 1)));
        }
		sender.sendMessage(String.format(XycLocale.getString("XYC-lang-header", sender.getName()),this.helpPageTitle,intPageNum,pageCount));
	}
	
	/**
	 * Clears all {@link HelpManager}s.
	 * 
	 * @author <a href="http://xxyy.github.io/">xxyy</a>
	 */
	public static void clearHelpManagers(){
		HelpManager.helpMans.clear();
	}
	
	/**
	 * Gets a {@link HelpManager} by its key in {@link HelpManager#helpMans}.
	 * @return a {@link HelpManager} or <code>null</code> if no such entry exists.
	 * @author <a href="http://xxyy.github.io/">xxyy</a>
	 */
	public static HelpManager getHelpManager(String helpManId){
		return HelpManager.helpMans.get(helpManId);
	}
	
	/**
	 * Tries to print help for a command or displays an error message.
	 * @param commandKey Key that the {@link HelpManager} is stored with in {@link HelpManager#helpMans}.
	 * @param sender {@link CommandSender} to receive the messages.
	 * @param label Alias this comamnd was called with.
	 * @param pageNum page to be displayed or "1" for the first page.
	 * @param helpCmdLabel Help command to display a specific page, i.e. "/xyc help" will be
     *                      displayed as "/xyc help [next Page]".
	 * @return <code>true</code> if help was given, else <code>false</code>. If <code>false</code>, an error message has already been sent.
	 */
	//INITIALIZATION AND STATIC VALUES
	
	public static boolean tryPrintHelp(String commandKey,CommandSender sender, String label, String pageNum, String helpCmdLabel){
		try{
			HelpManager helpMan = HelpManager.getHelpManager(commandKey);
			if(helpMan == null){
				sender.sendMessage(GenericXyPlugin.pluginPrefix+"Konnte Hilfe für "+XyHelper.codeChatCol+commandKey+XyHelper.priChatCol+" nicht laden!");
				return false;
			}
			helpMan.printHelpToSender(sender, label, pageNum, helpCmdLabel);
		}catch(Exception e){
			sender.sendMessage(GenericXyPlugin.pluginPrefix+"Konnte Hilfe für "+XyHelper.codeChatCol+commandKey+XyHelper.priChatCol+" nicht laden :(");
			return false;
		}
		return true;
	}
}
