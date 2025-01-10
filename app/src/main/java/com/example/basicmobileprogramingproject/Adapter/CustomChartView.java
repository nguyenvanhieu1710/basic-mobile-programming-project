package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collections;
import java.util.List;

public class CustomChartView extends View {
    public enum ChartType {
        BAR_CHART,
        PIE_CHART,
        LINE_CHART
    }

    private Paint paint;
    private Paint textPaint;
    private Paint linePaint;

    private List<Float> values;
    private List<String> labels;
    private List<Integer> colors;

    private ChartType chartType;

    public CustomChartView(Context context) {
        this(context, null);
    }

    public CustomChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);

        linePaint = new Paint();
        linePaint.setColor(Color.GRAY);
        linePaint.setStrokeWidth(5);

        if (attrs != null) {
            String chartTypeString = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "chartType");
            chartType = parseChartType(chartTypeString);
        }
    }

    private ChartType parseChartType(String chartTypeString) {
        if (chartTypeString == null) return ChartType.BAR_CHART;
        switch (chartTypeString) {
            case "LINE_CHART":
                return ChartType.LINE_CHART;
            case "PIE_CHART":
                return ChartType.PIE_CHART;
            default:
                return ChartType.BAR_CHART;
        }
    }

    public void setData(List<Float> values, List<String> labels, List<Integer> colors, ChartType chartType) {
        this.values = values;
        this.labels = labels;
        this.colors = colors;
        this.chartType = chartType;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values == null || values.isEmpty()) return;

        switch (chartType) {
            case BAR_CHART:
                drawBarChart(canvas);
                break;
            case PIE_CHART:
                drawPieChart(canvas);
                break;
            case LINE_CHART:
                drawLineChart(canvas);
                break;
        }
    }

    private void drawBarChart(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int padding = 100;

        int chartWidth = width - padding * 2;
        int chartHeight = height - padding * 2;
        int barWidth = chartWidth / values.size();
        float maxValue = Collections.max(values);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);

        // Vẽ trục Y
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);
        canvas.drawLine(padding, padding, padding, height - padding, paint);

        // Vẽ thông số trên trục Y
        int ySteps = 5; // Số bước trên trục Y
        float stepValue = maxValue / ySteps;
        for (int i = 0; i <= ySteps; i++) {
            float yValue = stepValue * i;
            float yPosition = height - padding - (chartHeight * i / ySteps);

            // Vẽ đường kẻ ngang (grid lines)
            paint.setColor(Color.LTGRAY);
            canvas.drawLine(padding, yPosition, width - padding, yPosition, paint);

            // Vẽ giá trị trên trục Y
            textPaint.setColor(Color.BLACK);
            canvas.drawText(String.format("%.0f", yValue), padding - 70, yPosition + 10, textPaint);
        }

        // Vẽ trục X
        paint.setColor(Color.BLACK);
        canvas.drawLine(padding, height - padding, width - padding, height - padding, paint);

        // Vẽ các cột (bars)
        for (int i = 0; i < values.size(); i++) {
            float value = values.get(i);
            float barHeight = (value / maxValue) * chartHeight;

            float left = padding + i * barWidth + 20;
            float top = height - padding - barHeight;
            float right = left + barWidth - 40;
            float bottom = height - padding;

            paint.setColor(colors.get(i % colors.size()));
            canvas.drawRect(left, top, right, bottom, paint);

            // Vẽ nhãn cho mỗi cột
            drawText(canvas, labels.get(i), (left + right) / 2, height - padding + 50);
            drawText(canvas, String.valueOf(value), (left + right) / 2, top - 20);
        }

        // Vẽ chú thích (Legend)
        float legendX = width - padding + 20;
        float legendY = padding;
        for (int i = 0; i < values.size(); i++) {
            paint.setColor(colors.get(i % colors.size()));
            canvas.drawRect(legendX, legendY + i * 60, legendX + 50, legendY + i * 60 + 50, paint);

            textPaint.setColor(Color.BLACK);
            canvas.drawText(labels.get(i), legendX + 70, legendY + i * 60 + 40, textPaint);
        }
    }

    private void drawPieChart(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height) - 200;

        float total = 0;
        for (float value : values) total += value;

        RectF rect = new RectF(100, 100, 100 + size, 100 + size);
        float startAngle = 0;

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);

        // Vẽ biểu đồ
        for (int i = 0; i < values.size(); i++) {
            float angle = (values.get(i) / total) * 360;
            paint.setColor(colors.get(i % colors.size()));

            // Vẽ từng phần biểu đồ
            canvas.drawArc(rect, startAngle, angle, true, paint);

            // Tính toán vị trí để vẽ phần trăm
            float middleAngle = startAngle + angle / 2;
            float radius = size / 2 + 50; // Khoảng cách từ tâm để đặt phần trăm
            float x = (float) (rect.centerX() + radius * Math.cos(Math.toRadians(middleAngle)));
            float y = (float) (rect.centerY() + radius * Math.sin(Math.toRadians(middleAngle)));
            String percentText = String.format("%.1f%%", (values.get(i) / total) * 100);

            // Vẽ phần trăm
            canvas.drawText(percentText, x - 20, y, textPaint);

            startAngle += angle;
        }

        // Vẽ chú thích (Legend)
        float legendX = rect.right + 50;
        float legendY = rect.top + 50;

        for (int i = 0; i < values.size(); i++) {
            paint.setColor(colors.get(i % colors.size()));

            // Vẽ ô màu
            canvas.drawRect(legendX, legendY + i * 60, legendX + 50, legendY + i * 60 + 50, paint);

            // Vẽ tên chú thích
            canvas.drawText(labels.get(i), legendX + 70, legendY + i * 60 + 40, textPaint);
        }
    }

    private void drawLineChart(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int padding = 100;

        int chartWidth = width - padding * 2;
        int chartHeight = height - padding * 2;
        float maxValue = Collections.max(values);
        float xStep = (float) chartWidth / (values.size() - 1);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);

        // Vẽ trục Y
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);
        canvas.drawLine(padding, padding, padding, height - padding, paint);

        // Vẽ thông số trên trục Y
        int ySteps = 5; // Số bước trên trục Y
        float stepValue = maxValue / ySteps;
        for (int i = 0; i <= ySteps; i++) {
            float yValue = stepValue * i;
            float yPosition = height - padding - (chartHeight * i / ySteps);

            // Vẽ đường kẻ ngang (grid lines)
            paint.setColor(Color.LTGRAY);
            canvas.drawLine(padding, yPosition, width - padding, yPosition, paint);

            // Vẽ giá trị trên trục Y
            textPaint.setColor(Color.BLACK);
            canvas.drawText(String.format("%.0f", yValue), padding - 70, yPosition + 10, textPaint);
        }

        // Vẽ trục X
        paint.setColor(Color.BLACK);
        canvas.drawLine(padding, height - padding, width - padding, height - padding, paint);

        // Vẽ các điểm và đường nối chúng
        float prevX = 0, prevY = 0;
        for (int i = 0; i < values.size(); i++) {
            float x = padding + i * xStep;
            float y = height - padding - (values.get(i) / maxValue) * chartHeight;

            if (i > 0) canvas.drawLine(prevX, prevY, x, y, linePaint);// Vẽ đường nối giữa các điểm

            paint.setColor(colors.get(i % colors.size()));
            canvas.drawCircle(x, y, 10, paint);// Vẽ điểm trên biểu đồ

            // Vẽ nhãn cho mỗi điểm
            drawText(canvas, labels.get(i), x, height - padding + 50);
            drawText(canvas, String.valueOf(values.get(i)), x, y - 20);

            prevX = x;
            prevY = y;
        }

        // Vẽ chú thích (Legend)
        float legendX = width - padding + 20;
        float legendY = padding;
        for (int i = 0; i < values.size(); i++) {
            paint.setColor(colors.get(i % colors.size()));
            canvas.drawRect(legendX, legendY + i * 60, legendX + 50, legendY + i * 60 + 50, paint);

            textPaint.setColor(Color.BLACK);
            canvas.drawText(labels.get(i), legendX + 70, legendY + i * 60 + 40, textPaint);
        }
    }

    private void drawText(Canvas canvas, String text, float x, float y) {
        canvas.drawText(text, x, y, textPaint);
    }
}
