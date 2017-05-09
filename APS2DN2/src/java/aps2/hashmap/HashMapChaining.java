package aps2.hashmap;

import java.util.LinkedList;

/**
 * Hash map employing chaining on collisions.
 */
public class HashMapChaining {
    private LinkedList<Element> table[];
    private HashFunction.HashingMethod h;

    public HashMapChaining(int m, HashFunction.HashingMethod h) {
        this.h = h;
        this.table = new LinkedList[m];
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<Element>();
        }
    }

    public LinkedList<Element>[] getTable() {
        return this.table;
    }

    /**
     * If the element doesn't exist yet, inserts it into the set.
     *
     * @param k Element key
     * @param v Element value
     * @return true, if element was added; false otherwise.
     */
    public boolean add(int k, String v) {
        Element element = new Element(k, v);
        int id;
        if (this.h == HashFunction.HashingMethod.DivisionMethod)
            id = HashFunction.DivisionMethod(k, this.table.length);
        else id = HashFunction.KnuthMethod(k, this.table.length);
        LinkedList<Element> linkedList = this.table[id];
        if (linkedList.contains(element)) return false;
        linkedList.add(element);
        this.table[id] = linkedList;
        return true;
    }

    /**
     * Removes the element from the set.
     *
     * @param k Element key
     * @return true, if the element was removed; otherwise false
     */
    public boolean remove(int k) {
        int id;
        if (this.h == HashFunction.HashingMethod.DivisionMethod)
            id = HashFunction.DivisionMethod(k, this.table.length);
        else id = HashFunction.KnuthMethod(k, this.table.length);

        LinkedList<Element> linkedList = this.table[id];
        for (int i = 0; i < linkedList.size(); i++){
            if (linkedList.get(i).key == k){
                linkedList.remove(i);
                this.table[id] = linkedList;
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the element.
     *
     * @param k Element key
     * @return true, if the element was found; false otherwise.
     */
    public boolean contains(int k) {
        int id;
        if (this.h == HashFunction.HashingMethod.DivisionMethod)
            id = HashFunction.DivisionMethod(k, this.table.length);
        else id = HashFunction.KnuthMethod(k, this.table.length);

        for (Element elt : this.table[id]) {
            if (elt.key == k) return true;
        }
        return false;
    }

    /**
     * Maps the given key to its value, if the key exists in the hash map.
     *
     * @param k Element key
     * @return The value for the given key or null, if such a key does not exist.
     */
    public String get(int k) {
        int id;
        if (this.h == HashFunction.HashingMethod.DivisionMethod)
            id = HashFunction.DivisionMethod(k, this.table.length);
        else id = HashFunction.KnuthMethod(k, this.table.length);

        for (Element elt : this.table[id]) {
            if (elt.key == k) return elt.value;
        }
        return null;
    }
}

