package com.magus.enviroment.ep.util;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/7/29.
 */
public class ViewUtils  {
    /**
     * ExpandTextView����
     */
    private static ExpandHandler expandHandler;

    public static void expandTextView(TextView tv) {
        if (expandHandler == null
                || (expandHandler != null && expandHandler.expand != tv))
            expandHandler = new ExpandHandler(tv);

        expandHandler.sendEmptyMessage(1);
    }

    /**
     * ExpandTextView����
     */
    private static class ExpandHandler extends Handler {

        private TextView expand;

        private int CollapseHeight;
        private int ExpandHeight;

        private int currentHeight;
        private int distance = 10;
        private int timeDelay = 1;
        private boolean isExpand;

        ExpandHandler(TextView tv) {
            expand = tv;
            CollapseHeight = expand.getMeasuredHeight();

            int lines = expand.getLineCount();
            int lineHeight = expand.getLineHeight();
            ExpandHeight = lineHeight * lines;
        }

        @Override
        public void handleMessage(Message msg) {

                    currentHeight = expand.getMeasuredHeight();

                    if (!isExpand) {
                        if (currentHeight < ExpandHeight - distance) {

                            expand.setHeight(currentHeight + distance);
                            this.sendEmptyMessageDelayed(1, timeDelay);

                        } else if (currentHeight < ExpandHeight) {

                            expand.setHeight(ExpandHeight);
                            isExpand = !isExpand;
                        }
                    } else {
                        if (currentHeight > CollapseHeight + distance) {
                            expand.setHeight(currentHeight - distance);
                            this.sendEmptyMessageDelayed(1, timeDelay);
                        } else {
                            expand.setHeight(CollapseHeight);
                            isExpand = !isExpand;
                        }
                    }

        }
    }


}
