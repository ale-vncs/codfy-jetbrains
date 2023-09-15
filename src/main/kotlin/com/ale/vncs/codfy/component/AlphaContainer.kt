import java.awt.BorderLayout
import java.awt.Graphics
import javax.swing.JComponent


/**
 * https://tips4java.wordpress.com/2009/05/31/backgrounds-with-transparency/
 *
 *
 * A wrapper Container for holding components that use a background Color
 * containing an alpha value with some transparency.
 *
 * A Component that uses a transparent background should really have its
 * opaque property set to false so that the area it occupies is first painted
 * by its opaque ancestor (to make sure no painting artifacts exist). However,
 * if the property is set to false, then most Swing components will not paint
 * the background at all, so you lose the transparent background Color.
 *
 * This components attempts to get around this problem by doing the
 * background painting on behalf of its contained Component, using the
 * background Color of the Component.
 */
open class AlphaContainer(private val component: JComponent) : JComponent() {
    init {
        this.setLayout(BorderLayout())
        this.setOpaque(false)
        component.setOpaque(false)
        this.add(component)
    }

    /**
     * Paint the background using the background Color of the
     * contained component
     */
    public override fun paintComponent(g: Graphics) {
        g.color = component.getBackground()
        g.fillRect(0, 0, width, height)
    }
}
