/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.text.AttributedCharacterIterator;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.batik.gvt.text.Mark;

/**
 * Renders the attributed character iterator of a <tt>TextNode</tt>.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @version $Id$
 */
public interface TextPainter {

    /**
     * Paints the specified attributed character iterator using the specified
     * Graphics2D and context and font context.
     *
     * @param node the TextNode to paint
     * @param g2d the Graphics2D to use
     * @param context the rendering context.
     */
    void paint(TextNode node, 
	       Graphics2D g2d, 
	       GraphicsNodeRenderContext context);

    /**
     * Initiates a text selection on a particular AttributedCharacterIterator,
     * using the text/font metrics employed by this TextPainter instance.
     */
    Mark selectAt(double x, 
		  double y, 
		  AttributedCharacterIterator aci,
		  TextNode node);

    /**
     * Continues a text selection on a particular AttributedCharacterIterator,
     * using the text/font metrics employed by this TextPainter instance.
     */
    Mark selectTo(double x, double y, Mark beginMark,
		  AttributedCharacterIterator aci,
		  TextNode node);

    /**
     * Select all of the text represented by an AttributedCharacterIterator,
     * using the text/font metrics employed by this TextPainter instance.
     */
    Mark selectAll(double x, double y,
		   AttributedCharacterIterator aci,
		   TextNode node);


    /**
     * Selects the first glyph in the text node.
     */
    Mark selectFirst(double x, double y,
		     AttributedCharacterIterator aci,
		     TextNode node);


    /**
     * Selects the last glyph in the text node.
     */
    Mark selectLast(double x, double y,
		    AttributedCharacterIterator aci,
		    TextNode node);

    /*
     * Get an array of index pairs corresponding to the indices within an
     * AttributedCharacterIterator regions bounded by two Marks.
     *
     * Note that the instances of Mark passed to this function <em>must
     * come</em> from the same TextPainter that generated them via selectAt()
     * and selectTo(), since the TextPainter implementation may rely on hidden
     * implementation details of its own Mark implementation.  
     */
    int[] getSelected(AttributedCharacterIterator aci,
		      Mark start, 
		      Mark finish);
    

    /*
     * Get a Shape in userspace coords which encloses the textnode
     * glyphs bounded by two Marks.
     * Note that the instances of Mark passed to this function
     * <em>must come</em>
     * from the same TextPainter that generated them via selectAt() and
     * selectTo(), since the TextPainter implementation may rely on hidden
     * implementation details of its own Mark implementation.
     */
    Shape getHighlightShape(Mark beginMark, Mark endMark);

    /*
     * Get a Shape in userspace coords which defines the textnode glyph outlines.
     * @param node the TextNode to measure
     * @param frc the font rendering context.
     * @param includeDecoration whether to include text decoration
     *            outlines.
     * @param includeStroke whether to create the "stroke shape outlines"
     *            instead of glyph outlines.
     */
    Shape getShape(TextNode node);

    /*
     * Get a Shape in userspace coords which defines the textnode glyph outlines.
     * @param node the TextNode to measure
     * @param frc the font rendering context.
     * @param includeDecoration whether to include text decoration
     *            outlines.
     * @param includeStroke whether to create the "stroke shape outlines"
     *            instead of glyph outlines.
     */
    Shape getDecoratedShape(TextNode node);

    /*
     * Get a Rectangle2D in userspace coords which encloses the textnode
     * glyphs composed from an AttributedCharacterIterator.
     * @param node the TextNode to measure
     * @param g2d the Graphics2D to use
     * @param context rendering context.
     */
    Rectangle2D getBounds(TextNode node);

    /*
     * Get a Rectangle2D in userspace coords which encloses the textnode
     * glyphs composed from an AttributedCharacterIterator, inclusive of
     * glyph decoration (underline, overline, strikethrough).
     * @param node the TextNode to measure
     * @param g2d the Graphics2D to use
     * @param context rendering context.
     */
    Rectangle2D getDecoratedBounds(TextNode node);

    /*
     * Get a Rectangle2D in userspace coords which encloses the
     * textnode glyphs (as-painted, inclusive of decoration and stroke, but
     * exclusive of filters, etc.) composed from an AttributedCharacterIterator.
     * @param node the TextNode to measure
     * @param g2d the Graphics2D to use
     * @param context rendering context.
     */
    Rectangle2D getPaintedBounds(TextNode node);

}

