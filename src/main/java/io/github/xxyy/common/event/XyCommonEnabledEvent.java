package io.github.xxyy.common.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This is called when XYC is being enabled.
 * 
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 *
 * @deprecated This is no longer needed. (Was ist ever needed?)
 */
@Deprecated
public class XyCommonEnabledEvent extends Event implements Cancellable {
	private static HandlerList handlers=new HandlerList();
	private boolean cancelled=false;
	private boolean sql=true;
	@Override
	public HandlerList getHandlers() {
		return XyCommonEnabledEvent.handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * @return If SQL is going to be enabled for XYC.
	 * @author <a href="http://xxyy.github.io/">xxyy</a>
	 */
	public boolean isSql(){
		return this.sql;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled=cancel;

	}
	
	/**
	 * Sets if SQL is going to be enabled for XYC.
	 * @param sql boolean
	 */
	public void setSql(boolean sql){
		this.sql=sql;
	}

}
