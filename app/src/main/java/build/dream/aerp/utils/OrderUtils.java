package build.dream.aerp.utils;

import android.content.Context;
import android.content.Intent;

import build.dream.aerp.services.OrderService;

public class OrderUtils {
    public static void saveOrder(Context context, long orderId) {
        Intent intent = new Intent(context, OrderService.class);
        intent.putExtra("orderId", orderId);
        context.startService(intent);
    }
}
