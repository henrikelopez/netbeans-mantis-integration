
package eu.doppel_helix.netbeans.mantisintegration.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ListBackedComboBoxModel<E> implements ComboBoxModel<E> {
    private E selectedItem = null;
    private List<E> backingList = new ArrayList<E>();
    Set<ListDataListener> listener = new HashSet<ListDataListener>();
    private Class<E> klass;
    
    public ListBackedComboBoxModel(Class<E> klass) {
        this.klass = klass;
    }
    
    /**
     * Set the backing List for this ComboBoxModel
     * 
     * @param elements 
     */
    public void setBackingList(final List<E> elements) {
        int oldLength = backingList.size();
        backingList.clear();
        if (oldLength > 0) {
            fireIntervalRemoved(0, oldLength - 1);
        }
        backingList.addAll(elements);
        if (backingList.size() > 0) {
            fireIntervalAdded(0, backingList.size() - 1);
        }
        if (!backingList.contains(selectedItem)) {
            setSelectedItem(null);
        }
    }
    
    public void addElement(final E element) {
        backingList.add(element);
        fireIntervalAdded(backingList.size() - 1, backingList.size() - 1);
    }
    
    public void addElement(final int position, final E element) {
        backingList.add(position, element);
        fireIntervalAdded(position, position);
    }
    
    /**
     * Retrieve an unmodifiable reference to the backing list
     * 
     * WARNING: This reference is only unmodifiable for the caller!
     * 
     * @return 
     */
    public List<E> getBackingList() {
        return Collections.unmodifiableList(backingList);
    }
    
    protected void fireIntervalAdded(int start, int end) {
        Iterator<ListDataListener> i = listener.iterator();
        ListDataEvent lde = new ListDataEvent(
                this, ListDataEvent.INTERVAL_ADDED, start, end);
        while(i.hasNext()) {
            i.next().intervalAdded(lde);
        }
    }

    protected void fireIntervalRemoved(int start, int end) {
        Iterator<ListDataListener> i = listener.iterator();
        ListDataEvent lde = new ListDataEvent(
                this, ListDataEvent.INTERVAL_REMOVED, start, end);
        while(i.hasNext()) {
            i.next().intervalAdded(lde);
        }
    }
    
    @Override
    public void setSelectedItem(Object anItem) {
        if(anItem != null && (! (klass.isAssignableFrom(anItem.getClass())))) {
            throw new IllegalArgumentException("Wrong type for List");
        } else {
            selectedItem = (E) anItem;
        }
    }

    @Override
    public E getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getSize() {
        return backingList.size();
    }

    @Override
    public E getElementAt(int index) {
        return backingList.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listener.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listener.remove(l);
    }
    
}
