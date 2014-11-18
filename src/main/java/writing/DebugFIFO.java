package writing;

import java.util.LinkedList;
/**
 * Created by mar on 12.11.14.
 */
public class DebugFIFO<E> extends LinkedList<E> {
    private int limit;

    public DebugFIFO(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }
}