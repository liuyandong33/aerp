package build.dream.aerp.order;

import java.math.BigInteger;

public interface OrderHandler {
    void play();

    void saveOrder(BigInteger orderId);
}
