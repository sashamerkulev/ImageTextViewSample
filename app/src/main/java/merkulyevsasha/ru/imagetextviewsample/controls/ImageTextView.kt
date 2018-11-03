package merkulyevsasha.ru.imagetextviewsample.controls

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.View
import merkulyevsasha.ru.imagetextviewsample.R
import org.jetbrains.annotations.Nullable

class ImageTextView(
    context: Context,
    @Nullable attrs: AttributeSet
) : View(context, attrs) {

    private val title: String
    private var titleSizeDp: Int
    private val titleSizePx: Float
    private val titlePaint: Paint
    private val titleBounds: Rect
    private val textWidth: Float

    private val textMarginRightPx: Float
    private val imageMarginLeftPx: Float
    private val imageTextSpacePx: Float

    private val imageSizePx: Float
    private var drawable: Drawable

    private val linePaint: Paint

    private val isLeftDivider: Boolean
    private val isRightDivider: Boolean
    private val isTopDivider: Boolean
    private val isBottomDivider: Boolean

    private val isCenter: Boolean

    init {

        val attributes = context.theme.obtainStyledAttributes(
            attrs, R.styleable.ImageTextView, 0, 0)

        try {
            val density = context.resources.displayMetrics.density

            val xmlns = "http://schemas.android.com/apk/res/android"

            val xmlText = attrs.getAttributeValue(xmlns, "text")
            title = if (xmlText.startsWith("@")) {
                resources.getString(attrs.getAttributeResourceValue(xmlns, "text", -1))
            } else {
                xmlText
            }

            try {
                val xmlTextSize = attrs.getAttributeValue(xmlns, "textSize")
                val textSize = xmlTextSize
                    .replace(".0sp", "")
                    .replace(".0dp", "")
                titleSizeDp = textSize.toInt()
            } catch (e: IllegalStateException) {
                titleSizeDp = 24
            }
            titleSizePx = titleSizeDp * density
            val textColorResId = attrs.getAttributeResourceValue(xmlns, "textColor", R.color.black_80)

            imageSizePx = attributes.getDimension(R.styleable.ImageTextView_imageSize, 34f * density)
            drawable = attributes.getDrawable(R.styleable.ImageTextView_image)!!
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = DrawableCompat.wrap(drawable).mutate()
            }
            val imageColor = attributes.getColor(R.styleable.ImageTextView_imageColor, ContextCompat.getColor(context, R.color.black_80))
            drawable.setColorFilter(imageColor, PorterDuff.Mode.SRC_ATOP)

            imageMarginLeftPx = attributes.getDimension(R.styleable.ImageTextView_imageMarginLeft, 24f * density)
            textMarginRightPx = attributes.getDimension(R.styleable.ImageTextView_textMarginRight, 24f * density)
            imageTextSpacePx = attributes.getDimension(R.styleable.ImageTextView_imageTextSpace, 24f * density)

            val lineStrokePx = attributes.getDimension(R.styleable.ImageTextView_dividerWidth, 1f * density)
            val colorLine = attributes.getColor(R.styleable.ImageTextView_dividerColor, ContextCompat.getColor(context, R.color.black_12))

            isLeftDivider = attributes.getBoolean(R.styleable.ImageTextView_left, false)
            isRightDivider = attributes.getBoolean(R.styleable.ImageTextView_right, false)
            isTopDivider = attributes.getBoolean(R.styleable.ImageTextView_top, false)
            isBottomDivider = attributes.getBoolean(R.styleable.ImageTextView_bottom, false)
            isCenter = attributes.getBoolean(R.styleable.ImageTextView_center, true)

            titlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            titlePaint.style = Paint.Style.STROKE
            titlePaint.color = ContextCompat.getColor(context, textColorResId)
            titlePaint.textSize = titleSizeDp * density
            titlePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

            titleBounds = Rect()
            titlePaint.getTextBounds(title, 0, title.length - 1, titleBounds)
            textWidth = titlePaint.measureText(title)

            linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            linePaint.style = Paint.Style.STROKE
            linePaint.color = colorLine
            linePaint.strokeWidth = lineStrokePx

        } finally {
            attributes.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.apply {

            val y = height * 0.5 - imageSizePx * 0.5
            val x = if (isCenter) {
                (width * 0.5 - (imageTextSpacePx + imageSizePx + textWidth) * 0.5).toFloat()
            } else {
                imageMarginLeftPx
            }
            drawable.setBounds(x.toInt(), y.toInt(), imageSizePx.toInt() + x.toInt(), imageSizePx.toInt() + y.toInt());
            drawable.draw(canvas)

            val yText = height * 0.5 - titleBounds.exactCenterY()
            val xText = if (isCenter) {
                x + imageSizePx + imageTextSpacePx
            } else {
                width - textWidth - textMarginRightPx
            }
            drawText(title, xText.toFloat(), yText.toFloat(), titlePaint)

            if (isTopDivider) drawLine(0f, 0f, width.toFloat(), 0f, linePaint)
            if (isLeftDivider) drawLine(0f, 0f, 0f, height.toFloat(), linePaint)
            if (isRightDivider) drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), linePaint)
            if (isBottomDivider) drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), linePaint)
        }

    }

    fun setColor(@ColorRes textColorResId: Int) {
        val color = ContextCompat.getColor(context, textColorResId)
        titlePaint.color = color
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        invalidate()
    }

    fun setIcon(@DrawableRes drawableResId: Int) {
        drawable = ContextCompat.getDrawable(context, drawableResId)!!
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable).mutate()
        }
        invalidate()
    }

}