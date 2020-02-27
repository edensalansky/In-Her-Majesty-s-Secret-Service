package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.killAll;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private Inventory gadgets=Inventory.getInstance();;
	public Q() {
		super("Q");
	}
	private long tick;

	@Override
	protected void initialize() {
		this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {this.tick=b.getTick(); });

		this.subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent e) -> {this.complete(e,new Pair(gadgets.getItem(e.getName()),tick));});
		this.subscribeBroadcast(killAll.class, (killAll b) -> {this.terminate();});
	}
}
