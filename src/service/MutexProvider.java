package service;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public class MutexProvider {

    private final Map<Mutex, WeakReference> mutexMap;

    public MutexProvider() {
        mutexMap = new WeakHashMap();
    }

    public Mutex getMutex(int id) {
        Mutex key = new Mutex(id);
        synchronized (mutexMap) {
            WeakReference ref = mutexMap.get(key);
            if (ref == null) {
                mutexMap.put(key, new WeakReference(key));
                return key;
            }
            Mutex mutex = (Mutex) ref.get();
            if (mutex == null) {
                mutexMap.put(key, new WeakReference(key));
                return key;
            }
            return mutex;
        }
    }

    public int getMutexCount() {
        synchronized (mutexMap) {
            return mutexMap.size();
        }
    }

}
