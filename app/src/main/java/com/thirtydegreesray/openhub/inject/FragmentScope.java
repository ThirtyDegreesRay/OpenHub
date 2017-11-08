

package com.thirtydegreesray.openhub.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Fragment 生命周期
 * Created on 2017/7/20.
 *
 * @author ThirtyDegreesRay
 */

@Documented
@Scope
@Retention(RUNTIME)
public @interface FragmentScope {
}
