package merkulyevsasha.ru.imagetextviewsample.controls

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.View
import merkulyevsasha.ru.imagetextviewsample.R
import org.jetbrains.annotations.Nullable

class ImageMarginView(
    context: Context,
    @Nullable attrs: AttributeSet
) : View(context, attrs) {

    private val imageSizePx: Float
    private var drawable: Drawable

    private val linePaint: Paint

    private val isLeftDivider: Boolean
    private val isRightDivider: Boolean
    private val isTopDivider: Boolean
    private val isBottomDivider: Boolean

    private val titlePaint: Paint
    private val titleBounds: Rect = Rect()
    private var circleText: String? = null
    private var textColorResId: Int = R.color.white

    private val backgroundTextColor: Int
    private val circlePaint: Paint
    private var radiusPx: Float

    private val density: Float
    private var textWidth: Float = 0f

    init {

        val attributes = context.theme.obtainStyledAttributes(
            attrs, R.styleable.ImageTextView, 0, 0)

        try {
            density = context.resources.displayMetrics.density
            radiusPx = 10f * density

            val xmlns = "http://schemas.android.com/apk/res/android"

            imageSizePx = attributes.getDimension(R.styleable.ImageTextView_imageSize, 34f * density)
            drawable = attributes.getDrawable(R.styleable.ImageTextView_image)!!
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = DrawableCompat.wrap(drawable).mutate()
            }
            val imageColor = attributes.getColor(R.styleable.ImageTextView_imageColor, ContextCompat.getColor(context, R.color.black_80))
            drawable.setColorFilter(imageColor, PorterDuff.Mode.SRC_ATOP)

            textColorResId = attrs.getAttributeResourceValue(xmlns, "textColor", R.color.white)
            backgroundTextColor = ContextCompat.getColor(context, R.color.blood_orange)

            isLeftDivider = attributes.getBoolean(R.styleable.ImageTextView_left, false)
            isRightDivider = attributes.getBoolean(R.styleable.ImageTextView_right, false)
            isTopDivider = attributes.getBoolean(R.styleable.ImageTextView_top, false)
            isBottomDivider = attributes.getBoolean(R.styleable.ImageTextView_bottom, false)

            val lineStrokePx = attributes.getDimension(R.styleable.ImageTextView_dividerWidth, 1f * density)
            val colorLine = attributes.getColor(R.styleable.ImageTextView_dividerColor, ContextCompat.getColor(context, R.color.black_12))

            linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            linePaint.style = Paint.Style.STROKE
            linePaint.color = colorLine
            linePaint.strokeWidth = lineStrokePx

            titlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            titlePaint.style = Paint.Style.STROKE
            titlePaint.color = ContextCompat.getColor(context, textColorResId)
            titlePaint.textSize = 16f * density
            titlePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

            circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            circlePaint.style = Paint.Style.FILL
            circlePaint.color = backgroundTextColor

        } finally {
            attributes.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.apply {

            val y = height * 0.5 - imageSizePx * 0.5
            val x = width * 0.5 - imageSizePx * 0.5
            drawable.setBounds(x.toInt(), y.toInt(), imageSizePx.toInt() + x.toInt(), imageSizePx.toInt() + y.toInt());
            drawable.draw(canvas)

            circleText?.let {

                val yCircle = y + imageSizePx * 0.8
                val xCircle = x + imageSizePx * 0.8
                drawCircle(xCircle.toFloat(), yCircle.toFloat(), radiusPx, circlePaint)

                val yText = yCircle - titleBounds.exactCenterY()
                val xText = xCircle - textWidth * 0.5
                drawText(it, xText.toFloat(), yText.toFloat(), titlePaint)
            }

            if (isTopDivider) drawLine(0f, 0f, width.toFloat(), 0f, linePaint)
            if (isLeftDivider) drawLine(0f, 0f, 0f, height.toFloat(), linePaint)
            if (isRightDivider) drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), linePaint)
            if (isBottomDivider) drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), linePaint)

        }
    }

    fun addCircleText(text: String) {
        circleText = text
        circleText?.let {
            textWidth = titlePaint.measureText(it)
            titlePaint.getTextBounds(it, 0, it.length, titleBounds)
            if (it.length == 1) {
                radiusPx = 10f * density
            } else {
                radiusPx = 15f * density
            }
        }
        invalidate()
    }

    fun removeCircleText() {
        circleText = null
        invalidate()
    }
}