package com.thirtydegreesray.openhub.common;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by ThirtyDegreesRay on 2017/11/27 10:50:25
 */

public class SizedTreeSet<E> extends TreeSet<E> {

    private int maxSize ;

    public SizedTreeSet(int maxSize){
        super();
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        if(size() >= maxSize && !contains(e)){
            E last = null;
            Iterator<E> iterator = iterator();
            for( ; iterator.hasNext(); ){
                last = iterator.next();
            }
            remove(last);
        }
        return super.add(e);
    }

}
