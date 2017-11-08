

package com.thirtydegreesray.openhub.ui.adapter.base;

/**
 * Created by ThirtyDegreesRay on 2017/10/18 16:03:04
 */

public class DoubleTypesModel<M1, M2> {

    private M1 m1;
    private M2 m2;

    public DoubleTypesModel(M1 m1, M2 m2) {
        this.m1 = m1;
        this.m2 = m2;
    }

    public M1 getM1() {
        return m1;
    }

    public M2 getM2() {
        return m2;
    }

    public int getTypePosition(){
        if(m1 != null){
            return 0;
        } else if(m2 != null){
            return 1;
        } else {
            return 0;
        }
    }

}
