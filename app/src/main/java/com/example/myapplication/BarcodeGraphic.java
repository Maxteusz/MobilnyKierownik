package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;


/** Graphic instance for rendering Barcode position and content information in an overlay view. */
public class BarcodeGraphic extends GraphicOverlay.Graphic {

    private static final int TEXT_COLOR = Color.RED;
    private static final float TEXT_SIZE = 54.0f;
    private static final float STROKE_WIDTH = 4.0f;

    private final Paint rectPaint;
    private final Paint barcodePaint;
    private final FirebaseVisionBarcode barcode;
    int percentageOfOccupancy;

    BarcodeGraphic(GraphicOverlay overlay, FirebaseVisionBarcode barcode, int percentageOfOccupancy) {
        super(overlay);

        this.barcode = barcode;
        this.percentageOfOccupancy = percentageOfOccupancy;

        rectPaint = new Paint();

        rectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        rectPaint.setStrokeWidth(STROKE_WIDTH);

        barcodePaint = new Paint();
        barcodePaint.setColor(TEXT_COLOR);
        barcodePaint.setTextSize(TEXT_SIZE);
    }

    /**
     * Draws the barcode block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        if (barcode == null) {
            throw new IllegalStateException("Attempting to draw a null barcode.");
        }

        // Draws the bounding box around the BarcodeBlock.
        RectF rect = new RectF(barcode.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        if (percentageOfOccupancy > 0 && percentageOfOccupancy < 40)
        rectPaint.setColor(Color.GREEN);
        else if (percentageOfOccupancy > 40 && percentageOfOccupancy < 75)
            rectPaint.setColor(Color.YELLOW);
        else if (percentageOfOccupancy > 75 && percentageOfOccupancy < 100)
            rectPaint.setColor(Color.RED);
        rectPaint.setAlpha(150);


        canvas.drawRect(rect, rectPaint);



    }
}
