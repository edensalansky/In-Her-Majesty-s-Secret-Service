package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import sun.misc.Queue;

import java.util.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
    private ConcurrentHashMap<Class<? extends Message>, LinkedBlockingQueue<Subscriber>> mapOfMessages = new ConcurrentHashMap<Class<? extends Message>, LinkedBlockingQueue<Subscriber>>();
    private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> mapOfSubEvent = new ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>>();
    private ConcurrentHashMap<Event, Future> mapOfFutures = new ConcurrentHashMap<Event, Future>();
    private static Object key = new Object();

    private static class SingletonHolder {
        private static MessageBroker instance = new MessageBrokerImpl();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBroker getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        try {
            if (mapOfSubEvent.containsKey(m)) {
                synchronized (key) {

                    if (mapOfMessages.get(type) == null) {
                        LinkedBlockingQueue<Subscriber> q = new LinkedBlockingQueue<Subscriber>(Integer.MAX_VALUE);
                        q.put(m);
                        mapOfMessages.put(type, q);
                    } else
                        mapOfMessages.get(type).put(m);
                }
            }
        } catch (InterruptedException e) {
            m.terminate();
        }
    }


    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        try {
            if (mapOfSubEvent.containsKey(m)) {
                synchronized (key) {
                    if (mapOfMessages.get(type) == null) {
                        LinkedBlockingQueue<Subscriber> q = new LinkedBlockingQueue<Subscriber>(Integer.MAX_VALUE);
                        q.put(m);
                        mapOfMessages.put(type, q);
                    } else
                        mapOfMessages.get(type).put(m);
                }
            }
        } catch (InterruptedException e) {
            m.terminate();
        }
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        synchronized (mapOfFutures) {
            mapOfFutures.get(e).resolve(result);
        }
    }

    @Override
    public void sendBroadcast(Broadcast b) throws InterruptedException {
        if (mapOfMessages.containsKey(b.getClass())) {
            int i = mapOfMessages.get(b.getClass()).size();
            while ((!mapOfMessages.get(b.getClass()).isEmpty()) & i > 0) {
                synchronized (key) {
                    Subscriber subscriber = mapOfMessages.get(b.getClass()).poll();
                    mapOfMessages.get(b.getClass()).put(subscriber);
                    mapOfSubEvent.get(subscriber).put(b);
                    i--;
                }
            }
        }
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {

        synchronized (key) {
            if (!mapOfMessages.containsKey(e.getClass())) {
                return null;
            }
        }
        Future fut = null;
        synchronized (mapOfFutures) {
            fut = new Future<>();
            mapOfFutures.put(e, fut);
        }
        try {
            synchronized (mapOfMessages) {
                synchronized (mapOfSubEvent) {
                    Subscriber subscriber = mapOfMessages.get(e.getClass()).poll();
                    if (subscriber != null) {
                        if (mapOfSubEvent.containsKey(subscriber)) {
                            mapOfSubEvent.get(subscriber).put(e);
                            mapOfMessages.get(e.getClass()).put(subscriber);
                        } else {
                            subscriber.terminate();
                        }
                    }
                }
            }
        } catch (InterruptedException w) {
            w.printStackTrace();
        }

        return fut;
    }


    @Override
    public void register(Subscriber m) {
        mapOfSubEvent.put(m, new LinkedBlockingQueue<Message>());
    }

    @Override
    public void unregister(Subscriber m) {
        if (mapOfSubEvent.containsKey(m)) {
            while (!mapOfSubEvent.get(m).isEmpty()) {
                try {
                    for (Message message : mapOfSubEvent.get(m)) {
                        if (message != null)
                            mapOfFutures.get(message).resolve(null);
                    }
                    mapOfMessages.get(mapOfSubEvent.get(m).take().getClass()).remove(m);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        mapOfSubEvent.remove(m);
    }

    @Override
    public Message awaitMessage(Subscriber m) {
        if (!mapOfSubEvent.containsKey(m)) {
            throw new IllegalStateException("Subscriber not registered!");
        }
        try {
            return mapOfSubEvent.get(m).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}




