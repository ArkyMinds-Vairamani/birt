/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.TemplateArea;

public class PDFTemplateLM extends PDFLeafItemLM
{

	public PDFTemplateLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
		assert ( content instanceof IAutoTextContent );
		handleAutoText( (IAutoTextContent) content );

	}

	public boolean layoutChildren( )
	{
		IAutoTextContent autoText = (IAutoTextContent) content;
		ContainerArea templateContainer = (ContainerArea) createInlineContainer(
				autoText, true, true );
		IStyle areaStyle = templateContainer.getStyle( );
		int maxWidth = parent.getCurrentMaxContentWidth( );
		validateBoxProperty( areaStyle, maxWidth, context.getMaxHeight( ) );

		int width = getDimensionValue( autoText.getWidth( ), maxWidth );
		templateContainer.setAllocatedWidth( maxWidth - parent.getCurrentIP( ) );
		int minContentWidth = getDimensionValue( areaStyle.getFontSize( ) ) * 4;
		int maxContentWidth = templateContainer.getWidth( );
		int preWidth = 0;
		if ( width >= maxContentWidth )
		{
			preWidth = Math.max( maxContentWidth, minContentWidth );
		}
		else
		{
			preWidth = Math.max( minContentWidth, width );
		}

		templateContainer.setWidth( preWidth );

		int height = getDimensionValue( autoText.getHeight( ), maxWidth );
		templateContainer.setContentHeight( Math.max(
				(int) ( getDimensionValue( areaStyle.getFontSize( ) ) * 1.35 ),
				height ) );

		Dimension templateDimension = new Dimension( );
		templateDimension.setDimension( templateContainer.getContentWidth( ),
				templateContainer.getContentHeight( ) );
		AbstractArea templateArea = createTemplateArea( autoText,
				templateDimension );
		templateContainer.addChild( templateArea );

		templateArea
				.setPosition(
						templateContainer.getContentX( ),
						templateContainer.getContentY( ) );
		parent.addArea( templateContainer, false, false );
		return false;
	}

	protected void handleAutoText(IAutoTextContent autoText )
	{
		if ( IAutoTextContent.TOTAL_PAGE == autoText.getType( ) )
		{
			context.addUnresolvedContent( autoText );
		}
		if ( IAutoTextContent.PAGE_NUMBER == autoText.getType( ) )
		{

			String originalPageNumber = autoText.getText( );
			NumberFormatter nf = new NumberFormatter( );
			String patternStr = autoText.getComputedStyle( ).getNumberFormat( );
			nf.applyPattern( patternStr );
			autoText.setText( nf
					.format( Integer.parseInt( originalPageNumber ) ) );
		}
	}

	/**
	 * create template area by autoText content
	 * 
	 * @param autoText
	 *            the autoText content
	 * @param contentDimension
	 *            content dimension
	 * @return
	 */
	private TemplateArea createTemplateArea( IAutoTextContent autoText,
			Dimension contentDimension )
	{
		TemplateArea templateArea = (TemplateArea) AreaFactory
				.createTemplateArea( autoText );
		templateArea.setWidth( contentDimension.getWidth( ) );
		templateArea.setHeight( contentDimension.getHeight( ) );
		return templateArea;
	}

}
