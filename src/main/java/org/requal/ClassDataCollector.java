package org.requal;

/**
 * Created by IntelliJ IDEA.
 * User: mburnett
 * Date: Apr 28, 2010
 * Time: 6:58:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClassDataCollector {
    void appendValueNode(String name, Object value);

    void startCompositeNode(String name);

    void stopCompositeNode();
}
