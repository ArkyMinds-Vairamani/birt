/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.metadata;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.validators.SimpleValueValidator;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.eclipse.birt.report.model.validators.AbstractSemanticValidator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * SAX handler for reading the XML meta data definition file.
 */

class MetaDataHandler extends XMLParserHandler
{

	MetaDataDictionary dictionary = MetaDataDictionary.getInstance( );

	private static final String ROOT_TAG = "ReportMetaData"; //$NON-NLS-1$ 
	private static final String STYLE_TAG = "Style"; //$NON-NLS-1$
	private static final String ELEMENT_TAG = "Element"; //$NON-NLS-1$ 
	private static final String PROPERTY_TAG = "Property"; //$NON-NLS-1$ 
	private static final String STYLE_PROPERTY_TAG = "StyleProperty"; //$NON-NLS-1$ 
	private static final String SLOT_TAG = "Slot"; //$NON-NLS-1$ 
	private static final String TYPE_TAG = "Type"; //$NON-NLS-1$
	private static final String DEFAULT_TAG = "Default"; //$NON-NLS-1$
	private static final String CHOICE_TAG = "Choice"; //$NON-NLS-1$
	private static final String PROPERTY_GROUP_TAG = "PropertyGroup"; //$NON-NLS-1$
	private static final String CHOICE_TYPE_TAG = "ChoiceType"; //$NON-NLS-1$
	private static final String STRUCTURE_TAG = "Structure"; //$NON-NLS-1$
	private static final String ALLOWED_TAG = "Allowed"; //$NON-NLS-1$
	private static final String MEMBER_TAG = "Member"; //$NON-NLS-1$
	private static final String VALUE_VALIDATOR_TAG = "ValueValidator"; //$NON-NLS-1$
	private static final String VALIDATORS_TAG = "Validators"; //$NON-NLS-1$
	private static final String METHOD_TAG = "Method"; //$NON-NLS-1$
	private static final String ARGUMENT_TAG = "Argument"; //$NON-NLS-1$
	private static final String CLASS_TAG = "Class"; //$NON-NLS-1$
	private static final String CONSTRUCTOR_TAG = "Constructor"; //$NON-NLS-1$
	private static final String SEMANTIC_VALIDATOR_TAG = "SemanticValidator"; //$NON-NLS-1$
	private static final String TRIGGER_TAG = "Trigger"; //$NON-NLS-1$
	private static final String DEFAULT_UNIT_TAG = "DefaultUnit"; //$NON-NLS-1$
	private static final String PROPERTY_VISIBILITY_TAG = "PropertyVisibility"; //$NON-NLS-1$

	private static final String NAME_ATTRIB = "name"; //$NON-NLS-1$ 
	private static final String DISPLAY_NAME_ID_ATTRIB = "displayNameID"; //$NON-NLS-1$ 
	private static final String EXTENDS_ATTRIB = "extends"; //$NON-NLS-1$ 
	private static final String TYPE_ATTRIB = "type"; //$NON-NLS-1$ 
	private static final String HAS_STYLE_ATTRIB = "hasStyle"; //$NON-NLS-1$ 
	private static final String SELECTOR_ATTRIB = "selector"; //$NON-NLS-1$ 
	private static final String ALLOWS_USER_PROPERTIES_ATTRIB = "allowsUserProperties"; //$NON-NLS-1$ 
	private static final String CAN_EXTEND_ATTRIB = "canExtend"; //$NON-NLS-1$ 
	private static final String MULTIPLE_CARDINALITY_ATTRIB = "multipleCardinality"; //$NON-NLS-1$ 
	private static final String IS_MANAGED_BY_NAME_SPACE_ATTRIB = "isManagedByNameSpace"; //$NON-NLS-1$
	private static final String CAN_INHERIT_ATTRIBUTE = "canInherit"; //$NON-NLS-1$ 
	private static final String IS_INTRINSIC_ATTRIB = "isIntrinsic"; //$NON-NLS-1$ 
	private static final String IS_STYLE_PROPERTY_ATTRIB = "isStyleProperty"; //$NON-NLS-1$ 
	private static final String IS_LIST_ATTRIB = "isList"; //$NON-NLS-1$
	private static final String NAME_SPACE_ATTRIB = "nameSpace"; //$NON-NLS-1$
	private static final String IS_NAME_REQUIRED_ATTRIB = "isNameRequired"; //$NON-NLS-1$
	private static final String IS_ABSTRACT_ATTRIB = "isAbstract"; //$NON-NLS-1$
	private static final String DETAIL_TYPE_ATTRIB = "detailType"; //$NON-NLS-1$
	private static final String JAVA_CLASS_ATTRIB = "javaClass"; //$NON-NLS-1$
	private static final String TOOL_TIP_ID_ATTRIB = "toolTipID"; //$NON-NLS-1$
	private static final String RETURN_TYPE_ATTRIB = "returnType"; //$NON-NLS-1$
	private static final String TAG_ID_ATTRIB = "tagID"; //$NON-NLS-1$
	private static final String DATA_TYPE_ATTRIB = "dataType"; //$NON-NLS-1$
	private static final String IS_STATIC_ATTRIB = "isStatic"; //$NON-NLS-1$
	private static final String VALIDATOR_ATTRIB = "validator"; //$NON-NLS-1$
	private static final String CLASS_ATTRIB = "class"; //$NON-NLS-1$
	private static final String NATIVE_ATTRIB = "native"; //$NON-NLS-1$
	private static final String PRE_REQUISITE_ATTRIB = "preRequisite"; //$NON-NLS-1$
	private static final String TARGET_ELEMENT_ATTRIB = "targetElement"; //$NON-NLS-1$
	private static final String VALUE_REQUIRED_ATTRIB = "valueRequired"; //$NON-NLS-1$
	private static final String PROPERTY_VISIBILITY_ATTRIB = "visibility"; //$NON-NLS-1$
	private static final String SINCE_ATTRIB = "since"; //$NON-NLS-1$
	private static final String XML_NAME_ATTRIB = "xmlName"; //$NON-NLS-1$
	private static final String RUNTIME_SETTABLE_ATTRIB = "runtimeSettable"; //$NON-NLS-1$
	private static final String CONTEXT_ATTRIB = "context"; //$NON-NLS-1$
	private static final String MODULES_ATTRIB = "modules"; //$NON-NLS-1$

	private String groupNameID;

	// Cached state. Can be done here because nothing in this grammar is
	// recursive.

	protected ElementDefn elementDefn = null;
	protected SlotDefn slotDefn = null;
	protected SystemPropertyDefn propDefn = null;
	protected StructureDefn struct = null;
	protected ArrayList choices = new ArrayList( );

	/**
	 * Constructor.
	 */

	MetaDataHandler( )
	{
		super( new MetaDataErrorHandler( ) );
	}

	public AbstractParseState createStartState( )
	{
		return new StartState( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */

	public void endDocument( ) throws SAXException
	{
		// 
		if ( !errorHandler.getErrors( ).isEmpty( ) )
		{
			throw new MetaDataParserException( errorHandler.getErrors( ) );
		}

		try
		{
			dictionary.build( );
		}
		catch ( MetaDataException e )
		{
			errorHandler.semanticError( new MetaDataParserException( e,
					MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
			throw new MetaDataParserException( errorHandler.getErrors( ) );
		}

		super.endDocument( );
	}

	/**
	 * Convert the array list of choices to an array.
	 * 
	 * @return an array of the choices.
	 */

	private Choice[] getChoiceArray( )
	{
		Choice[] choiceArray = new Choice[choices.size( )];
		for ( int i = 0; i < choices.size( ); i++ )
			choiceArray[i] = (Choice) choices.get( i );
		return choiceArray;
	}

	class StartState extends InnerParseState
	{

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( ROOT_TAG ) )
				return new RootState( );
			return super.startElement( tagName );
		}
	}

	class RootState extends InnerParseState
	{

		public AbstractParseState startElement( String tagName )
		{
			if ( CHOICE_TYPE_TAG.equalsIgnoreCase( tagName ) )
				return new ChoiceTypeState( );
			if ( STRUCTURE_TAG.equalsIgnoreCase( tagName ) )
				return new StructDefnState( );
			if ( ELEMENT_TAG.equalsIgnoreCase( tagName ) )
				return new ElementDefnState( );
			if ( STYLE_TAG.equalsIgnoreCase( tagName ) )
				return new StyleState( );
			if ( CLASS_TAG.equalsIgnoreCase( tagName ) )
				return new ClassState( );
			if ( VALIDATORS_TAG.equalsIgnoreCase( tagName ) )
				return new ValidatorsState( );
			return super.startElement( tagName );
		}
	}

	class ChoiceTypeState extends InnerParseState
	{

		ChoiceSet choiceSet = null;

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			choices.clear( );
			String name = attrs.getValue( NAME_ATTRIB );
			if ( StringUtil.isBlank( name ) )
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
			else
			{
				choiceSet = new ChoiceSet( name );
				try
				{
					dictionary.addChoiceSet( choiceSet );
				}
				catch ( MetaDataException e )
				{
					choiceSet = null;
					errorHandler.semanticError( e );
				}
			}
		}

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( CHOICE_TAG ) )
				return new ChoiceState( );
			return super.startElement( tagName );
		}

		public void end( ) throws SAXException
		{
			if ( !choices.isEmpty( ) && choiceSet != null )
				choiceSet.setChoices( getChoiceArray( ) );
		}
	}

	class StyleState extends InnerParseState
	{

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String name = attrs.getValue( NAME_ATTRIB );
			String displayNameID = attrs.getValue( DISPLAY_NAME_ID_ATTRIB );

			if ( StringUtil.isBlank( name ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
			}
			else if ( StringUtil.isBlank( displayNameID ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED ) );
			}
			else
			{
				PredefinedStyle style = new PredefinedStyle( );
				style.setName( name );
				style.setDisplayNameKey( displayNameID );
				try
				{
					dictionary.addPredefinedStyle( style );
				}
				catch ( MetaDataException e )
				{
					errorHandler
							.semanticError( new MetaDataParserException(
									e,
									MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
				}
			}
		}
	}

	class StructDefnState extends InnerParseState
	{

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String name = attrs.getValue( NAME_ATTRIB );
			String displayNameID = attrs.getValue( DISPLAY_NAME_ID_ATTRIB );
			if ( StringUtil.isBlank( name ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
			}
			if ( StringUtil.isBlank( displayNameID ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED ) );
			}
			else
			{
				struct = new StructureDefn( name );
				struct.setDisplayNameKey( attrs
						.getValue( DISPLAY_NAME_ID_ATTRIB ) );
				struct.setSince( attrs.getValue( SINCE_ATTRIB ) );

				try
				{
					dictionary.addStructure( struct );
				}
				catch ( MetaDataException e )
				{
					errorHandler
							.semanticError( new MetaDataParserException(
									e,
									MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
				}
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			super.end( );
			struct = null;
		}

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( MEMBER_TAG ) )
				return new MemberState( );
			return super.startElement( tagName );
		}
	}

	class MemberState extends InnerParseState
	{

		StructPropertyDefn memberDefn = null;

		public void parseAttrs( Attributes attrs )
		{
			String name = getAttrib( attrs, NAME_ATTRIB );
			String displayNameID = getAttrib( attrs, DISPLAY_NAME_ID_ATTRIB );
			String type = getAttrib( attrs, TYPE_ATTRIB );
			String validator = getAttrib( attrs, VALIDATOR_ATTRIB );

			boolean ok = ( struct != null );
			if ( StringUtil.isBlank( name ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				ok = false;
			}
			if ( StringUtil.isBlank( displayNameID ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED ) );
				ok = false;
			}
			if ( StringUtil.isBlank( type ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_TYPE_REQUIRED ) );
				ok = false;
			}
			if ( !ok )
				return;
			PropertyType typeDefn = dictionary.getPropertyType( type );

			if ( typeDefn == null )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_INVALID_TYPE ) );
				return;
			}

			if ( !ok )
				return;
			String detailName = getAttrib( attrs, DETAIL_TYPE_ATTRIB );
			ChoiceSet choiceSet = null;
			StructureDefn structDefn = null;
			switch ( typeDefn.getTypeCode( ) )
			{

				case PropertyType.DIMENSION_TYPE :
				case PropertyType.DATE_TIME_TYPE :
				case PropertyType.STRING_TYPE :
				case PropertyType.FLOAT_TYPE :
				case PropertyType.INTEGER_TYPE :
				case PropertyType.NUMBER_TYPE :

					if ( detailName != null )
					{
						choiceSet = validateChoiceSet( detailName );
						if ( choiceSet == null )
							return;
					}

					break;

				case PropertyType.CHOICE_TYPE :

					if ( detailName == null )
					{
						errorHandler
								.semanticError( new MetaDataParserException(
										MetaDataParserException.DESIGN_EXCEPTION_CHOICE_TYPE_REQUIRED ) );
						return;
					}

					choiceSet = validateChoiceSet( detailName );
					if ( choiceSet == null )
						return;

					break;

				case PropertyType.COLOR_TYPE :

					choiceSet = validateChoiceSet( ColorPropertyType.COLORS_CHOICE_SET );
					if ( choiceSet == null )
						return;

					break;

				case PropertyType.STRUCT_TYPE :
					if ( detailName == null )
					{
						errorHandler
								.semanticError( new MetaDataParserException(
										MetaDataParserException.DESIGN_EXCEPTION_STRUCT_TYPE_REQUIRED ) );
						return;
					}
					structDefn = (StructureDefn) dictionary
							.getStructure( detailName );
					if ( structDefn == null )
					{
						errorHandler
								.semanticError( new MetaDataParserException(
										MetaDataParserException.DESIGN_EXCEPTION_INVALID_STRUCT_TYPE ) );
						return;
					}
					break;

			}

			memberDefn = new StructPropertyDefn( );

			memberDefn.setName( name );
			memberDefn.setType( typeDefn );
			memberDefn.setDisplayNameID( displayNameID );
			memberDefn.setValueRequired( getBooleanAttrib( attrs,
					VALUE_REQUIRED_ATTRIB, false ) );
			memberDefn.setSince( attrs.getValue( SINCE_ATTRIB ) );
			memberDefn.setRuntimeSettable( getBooleanAttrib( attrs,
					RUNTIME_SETTABLE_ATTRIB, true ) );
			if ( memberDefn.getTypeCode( ) == PropertyType.EXPRESSION_TYPE )
			{
				memberDefn.setReturnType( attrs.getValue( RETURN_TYPE_ATTRIB ) );
				memberDefn.setContext( attrs.getValue( CONTEXT_ATTRIB ) );
			}

			if ( !StringUtil.isBlank( validator ) )
			{
				memberDefn.setValueValidator( validator );
			}

			if ( typeDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE )
				memberDefn.setIsList( getBooleanAttrib( attrs, IS_LIST_ATTRIB,
						false ) );

			if ( choiceSet != null )
				memberDefn.setDetails( choiceSet );
			else if ( structDefn != null )
				memberDefn.setDetails( structDefn );

			memberDefn.setIntrinsic( getBooleanAttrib( attrs,
					IS_INTRINSIC_ATTRIB, false ) );
			try
			{
				struct.addProperty( memberDefn );
			}
			catch ( MetaDataException e )
			{
				errorHandler.semanticError( e );
			}

		}

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DEFAULT_TAG ) )
				return new DefaultValueState( memberDefn );

			return super.startElement( tagName );
		}
	}

	class ElementDefnState extends InnerParseState
	{

		private static final String NO_NS_NAME = "none"; //$NON-NLS-1$
		private static final String MASTER_PAGE_NS_NAME = "masterPage"; //$NON-NLS-1$
		private static final String PARAMETER_NS_NAME = "parameter"; //$NON-NLS-1$
		private static final String ELEMENT_NS_NAME = "element"; //$NON-NLS-1$
		private static final String DATA_SOURCE_NS_NAME = "dataSource"; //$NON-NLS-1$
		private static final String DATA_SET_NS_NAME = "dataSet"; //$NON-NLS-1$
		private static final String STYLE_NS_NAME = "style"; //$NON-NLS-1$
		private static final String THEME_NS_NAME = "theme"; //$NON-NLS-1$
		private static final String TEMPLATE_PARAMETER_DEFINITION_NS_NAME = "templateParameterDefinition"; //$NON-NLS-1$

		public void parseAttrs( Attributes attrs )
		{
			String name = attrs.getValue( NAME_ATTRIB );
			String displayNameID = attrs.getValue( DISPLAY_NAME_ID_ATTRIB );

			boolean ok = true;
			if ( StringUtil.isBlank( name ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				ok = false;
			}
			if ( StringUtil.isBlank( displayNameID ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED ) );
				ok = false;
			}

			if ( !ok )
				return;

			elementDefn = new ElementDefn( );
			elementDefn.setName( name );
			elementDefn.setAbstract( getBooleanAttrib( attrs,
					IS_ABSTRACT_ATTRIB, false ) );
			elementDefn.setDisplayNameKey( displayNameID );
			elementDefn.setExtends( attrs.getValue( EXTENDS_ATTRIB ) );
			elementDefn.setHasStyle( getBooleanAttrib( attrs, HAS_STYLE_ATTRIB,
					false ) );
			elementDefn.setSelector( attrs.getValue( SELECTOR_ATTRIB ) );
			elementDefn.setAllowsUserProperties( getBooleanAttrib( attrs,
					ALLOWS_USER_PROPERTIES_ATTRIB, true ) );
			elementDefn.setJavaClass( attrs.getValue( JAVA_CLASS_ATTRIB ) );
			elementDefn.setCanExtend( getBooleanAttrib( attrs,
					CAN_EXTEND_ATTRIB, true ) );
			elementDefn.setSince( attrs.getValue( SINCE_ATTRIB ) );
			elementDefn.setXmlElement( attrs.getValue( XML_NAME_ATTRIB ) );
			String nameRequired = attrs.getValue( IS_NAME_REQUIRED_ATTRIB );
			if ( nameRequired != null )
			{
				boolean flag = parseBoolean( nameRequired, false );
				elementDefn.setNameOption( flag
						? MetaDataConstants.REQUIRED_NAME
						: MetaDataConstants.OPTIONAL_NAME );
			}

			String ns = attrs.getValue( NAME_SPACE_ATTRIB );
			if ( ns == null || ns.trim( ).length( ) == 0 )
			{
				// Inherit default name space
			}
			else if ( ns.equalsIgnoreCase( STYLE_NS_NAME ) )
				elementDefn.setNameSpaceID( ReportDesign.STYLE_NAME_SPACE );
			else if ( ns.equalsIgnoreCase( THEME_NS_NAME ) )
				elementDefn.setNameSpaceID( Library.THEME_NAME_SPACE );
			else if ( ns.equalsIgnoreCase( DATA_SET_NS_NAME ) )
				elementDefn.setNameSpaceID( Module.DATA_SET_NAME_SPACE );
			else if ( ns.equalsIgnoreCase( DATA_SOURCE_NS_NAME ) )
				elementDefn.setNameSpaceID( Module.DATA_SOURCE_NAME_SPACE );
			else if ( ns.equalsIgnoreCase( ELEMENT_NS_NAME ) )
				elementDefn.setNameSpaceID( Module.ELEMENT_NAME_SPACE );
			else if ( ns.equalsIgnoreCase( PARAMETER_NS_NAME ) )
				elementDefn.setNameSpaceID( Module.PARAMETER_NAME_SPACE );
			else if ( ns.equalsIgnoreCase( MASTER_PAGE_NS_NAME ) )
				elementDefn.setNameSpaceID( Module.PAGE_NAME_SPACE );
			else if ( ns.equalsIgnoreCase( NO_NS_NAME ) )
				elementDefn.setNameSpaceID( MetaDataConstants.NO_NAME_SPACE );
			else if ( ns
					.equalsIgnoreCase( TEMPLATE_PARAMETER_DEFINITION_NS_NAME ) )
				elementDefn
						.setNameSpaceID( ReportDesign.TEMPLATE_PARAMETER_NAME_SPACE );
			else
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_INVALID_NAME_SPACE ) );

			try
			{
				dictionary.addElementDefn( elementDefn );
			}
			catch ( MetaDataException e )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								e,
								MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
			}
		}

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( PROPERTY_TAG ) )
				return new PropertyState( );
			if ( tagName.equalsIgnoreCase( PROPERTY_GROUP_TAG ) )
				return new PropertyGroupState( );
			if ( tagName.equalsIgnoreCase( STYLE_PROPERTY_TAG ) )
				return new StylePropertyState( );
			if ( tagName.equalsIgnoreCase( SLOT_TAG ) )
				return new SlotState( );
			if ( tagName.equalsIgnoreCase( METHOD_TAG ) )
				return new ElementMethodState( elementDefn );
			if ( tagName.equalsIgnoreCase( SEMANTIC_VALIDATOR_TAG ) )
				return new TriggerState( );
			if ( tagName.equalsIgnoreCase( PROPERTY_VISIBILITY_TAG ) )
				return new PropertyVisibilityState( );

			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		public void end( ) throws SAXException
		{
			super.end( );
			elementDefn = null;
		}

		/**
		 * Parses the property visiblity.
		 */

		private class PropertyVisibilityState extends InnerParseState
		{

			public void parseAttrs( Attributes attrs )
			{
				String name = attrs.getValue( NAME_ATTRIB );
				String visible = attrs.getValue( PROPERTY_VISIBILITY_ATTRIB );

				if ( StringUtil.isBlank( name ) )
				{
					errorHandler
							.semanticError( new MetaDataParserException(
									MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
					return;
				}

				elementDefn.addPropertyVisibility( name, visible );
			}
		}
	}

	class PropertyGroupState extends InnerParseState
	{

		public void parseAttrs( Attributes attrs )
		{
			groupNameID = attrs.getValue( DISPLAY_NAME_ID_ATTRIB );
			if ( StringUtil.isBlank( groupNameID ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_GROUP_NAME_ID_REQUIRED ) );
			}
		}

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( PROPERTY_TAG ) )
				return new PropertyState( );
			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			groupNameID = null;
		}
	}

	class PropertyState extends InnerParseState
	{

		private static final String THIS_KEYWORD = "this"; //$NON-NLS-1$ 

		public void parseAttrs( Attributes attrs )
		{
			choices.clear( );
			propDefn = null;
			String name = getAttrib( attrs, NAME_ATTRIB );
			String displayNameID = getAttrib( attrs, DISPLAY_NAME_ID_ATTRIB );
			String type = getAttrib( attrs, TYPE_ATTRIB );
			String validator = getAttrib( attrs, VALIDATOR_ATTRIB );

			boolean ok = ( elementDefn != null );
			if ( name == null )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				ok = false;
			}

			if ( displayNameID == null )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED ) );
				ok = false;
			}
			if ( type == null )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_TYPE_REQUIRED ) );
				ok = false;
			}

			if ( !ok )
				return;

			// Look up the choice set name, if any.

			PropertyType typeDefn = dictionary.getPropertyType( type );
			if ( typeDefn == null )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_INVALID_TYPE ) );
				return;
			}

			String detailName = getAttrib( attrs, DETAIL_TYPE_ATTRIB );
			ChoiceSet choiceSet = null;
			StructureDefn struct = null;
			switch ( typeDefn.getTypeCode( ) )
			{
				case PropertyType.DIMENSION_TYPE :
				case PropertyType.DATE_TIME_TYPE :
				case PropertyType.STRING_TYPE :
				case PropertyType.FLOAT_TYPE :
				case PropertyType.INTEGER_TYPE :
				case PropertyType.NUMBER_TYPE :

					if ( detailName != null )
					{
						choiceSet = validateChoiceSet( detailName );
						if ( choiceSet == null )
							return;
					}

					break;

				case PropertyType.CHOICE_TYPE :

					if ( detailName == null )
					{
						errorHandler
								.semanticError( new MetaDataParserException(
										MetaDataParserException.DESIGN_EXCEPTION_CHOICE_TYPE_REQUIRED ) );
						return;
					}
					choiceSet = validateChoiceSet( detailName );
					if ( choiceSet == null )
						return;

					break;

				case PropertyType.COLOR_TYPE :

					choiceSet = validateChoiceSet( ColorPropertyType.COLORS_CHOICE_SET );
					if ( choiceSet == null )
						return;

					break;

				case PropertyType.STRUCT_TYPE :
				case PropertyType.STRUCT_REF_TYPE :
					if ( detailName == null )
					{
						errorHandler
								.semanticError( new MetaDataParserException(
										MetaDataParserException.DESIGN_EXCEPTION_STRUCT_TYPE_REQUIRED ) );
						return;
					}
					struct = (StructureDefn) dictionary
							.getStructure( detailName );
					if ( struct == null )
					{
						errorHandler
								.semanticError( new MetaDataParserException(
										MetaDataParserException.DESIGN_EXCEPTION_INVALID_STRUCT_TYPE ) );
						return;
					}
					break;

				case PropertyType.ELEMENT_REF_TYPE :
					if ( detailName == null )
					{
						errorHandler
								.semanticError( new MetaDataParserException(
										MetaDataParserException.DESIGN_EXCEPTION_ELEMENT_REF_TYPE_REQUIRED ) );
						return;
					}
					if ( detailName.equals( THIS_KEYWORD ) )
						detailName = elementDefn.getName( );
					break;

				default :
					// Ignore the detail name for other types.

					detailName = null;
			}

			propDefn = new SystemPropertyDefn( );

			propDefn.setName( name );
			propDefn.setDisplayNameID( displayNameID );
			propDefn.setType( typeDefn );
			propDefn.setGroupNameKey( groupNameID );
			propDefn.setCanInherit( getBooleanAttrib( attrs,
					CAN_INHERIT_ATTRIBUTE, true ) );
			propDefn.setIntrinsic( getBooleanAttrib( attrs,
					IS_INTRINSIC_ATTRIB, false ) );
			propDefn.setStyleProperty( getBooleanAttrib( attrs,
					IS_STYLE_PROPERTY_ATTRIB, false ) );
			propDefn.setValueRequired( getBooleanAttrib( attrs,
					VALUE_REQUIRED_ATTRIB, false ) );
			propDefn.setSince( attrs.getValue( SINCE_ATTRIB ) );
			propDefn.setRuntimeSettable( getBooleanAttrib( attrs,
					RUNTIME_SETTABLE_ATTRIB, true ) );
			if ( propDefn.getTypeCode( ) == PropertyType.EXPRESSION_TYPE )
			{
				propDefn.setReturnType( attrs.getValue( RETURN_TYPE_ATTRIB ) );
				propDefn.setContext( attrs.getValue( CONTEXT_ATTRIB ) );
			}

			if ( !StringUtil.isBlank( validator ) )
			{
				propDefn.setValueValidator( validator );
			}

			if ( typeDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE )
				propDefn.setIsList( getBooleanAttrib( attrs, IS_LIST_ATTRIB,
						false ) );

			if ( choiceSet != null )
				propDefn.setDetails( choiceSet );
			else if ( struct != null )
				propDefn.setDetails( struct );
			else if ( detailName != null )
				propDefn.setDetails( detailName );
			try
			{
				elementDefn.addProperty( propDefn );
			}
			catch ( MetaDataException e )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								e,
								MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
			}
		}

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DEFAULT_TAG ) )
				return new DefaultValueState( propDefn );
			else if ( tagName.equalsIgnoreCase( ALLOWED_TAG ) )
				return new AllowedState( );
			else if ( tagName.equalsIgnoreCase( TRIGGER_TAG ) )
				return new TriggerState( );
			else if ( tagName.equalsIgnoreCase( DEFAULT_UNIT_TAG ) )
				return new DefaultUnitState( );
			else
				return super.startElement( tagName );
		}

		public void end( ) throws SAXException
		{
			propDefn = null;
		}
	}

	class DefaultUnitState extends InnerParseState
	{

		public void end( ) throws SAXException
		{
			if ( propDefn == null )
				return;

			int type = propDefn.getTypeCode( );

			if ( type != PropertyType.DIMENSION_TYPE )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_DEFAULT_UNIT_NOT_ALLOWED ) );

				return;
			}
			propDefn.setDefaultUnit( text.toString( ) );
		}
	}

	class AllowedState extends InnerParseState
	{

		public void end( ) throws SAXException
		{
			if ( propDefn == null )
				return;

			int type = propDefn.getTypeCode( );

			if ( type != PropertyType.DIMENSION_TYPE
					&& type != PropertyType.CHOICE_TYPE )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_RESTRICTION_NOT_ALLOWED ) );

				return;
			}

			ChoiceSet allowedChoices = new ChoiceSet( );
			ArrayList allowedList = new ArrayList( );

			String choicesStr = StringUtil.trimString( text.toString( ) );

			// blank string.

			if ( choicesStr == null )
				return;

			String[] nameArray = choicesStr.split( "," ); //$NON-NLS-1$

			if ( type == PropertyType.DIMENSION_TYPE )
			{
				// units restriction on a dimension property.

				IChoiceSet units = dictionary
						.getChoiceSet( DesignChoiceConstants.CHOICE_UNITS );
				assert units != null;

				for ( int i = 0; i < nameArray.length; i++ )
				{
					IChoice unit = units.findChoice( nameArray[i].trim( ) );

					if ( unit != null )
					{
						allowedList.add( unit );
					}
					else
					{
						errorHandler
								.semanticError( new MetaDataParserException(
										MetaDataParserException.DESIGN_EXCEPTION_INVALID_RESTRICTION ) );

						return;
					}
				}
			}
			else
			{
				// choices type restriction.

				IChoiceSet choices = propDefn.getChoices( );
				assert choices != null;

				for ( int i = 0; i < nameArray.length; i++ )
				{
					IChoice choice = choices.findChoice( nameArray[i].trim( ) );

					if ( choice != null )
					{
						allowedList.add( choice );
					}
					else
					{
						errorHandler
								.semanticError( new MetaDataParserException(
										MetaDataParserException.DESIGN_EXCEPTION_INVALID_RESTRICTION ) );

						return;
					}
				}

			}

			allowedChoices.setChoices( (Choice[]) allowedList
					.toArray( new Choice[0] ) );

			propDefn.setAllowedChoices( allowedChoices );
		}
	}

	class ValidatorsState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */
		public AbstractParseState startElement( String tagName )
		{
			if ( VALUE_VALIDATOR_TAG.equalsIgnoreCase( tagName ) )
				return new ValueValidatorState( );
			if ( SEMANTIC_VALIDATOR_TAG.equalsIgnoreCase( tagName ) )
				return new SemanticValidatorState( );
			return super.startElement( tagName );
		}
	}

	class ValueValidatorState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String name = getAttrib( attrs, NAME_ATTRIB );
			String className = getAttrib( attrs, CLASS_ATTRIB );

			if ( StringUtil.isBlank( name ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_VALIDATOR_NAME_REQUIRED ) );
				return;
			}

			if ( StringUtil.isBlank( className ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_CLASS_NAME_REQUIRED ) );
				return;
			}

			try
			{
				Class c = Class.forName( className );
				SimpleValueValidator validator = (SimpleValueValidator) c
						.newInstance( );
				validator.setName( name );

				try
				{
					dictionary.addValueValidator( validator );
				}
				catch ( MetaDataException e )
				{
					errorHandler
							.semanticError( new MetaDataParserException(
									e,
									MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
				}

			}
			catch ( Exception e )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_INVALID_META_VALIDATOR ) );
			}
		}
	}

	class SemanticValidatorState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String name = getAttrib( attrs, NAME_ATTRIB );
			String modules = getAttrib( attrs, MODULES_ATTRIB );
			String className = getAttrib( attrs, CLASS_ATTRIB );

			if ( StringUtil.isBlank( name ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_VALIDATOR_NAME_REQUIRED ) );
				return;
			}
			if ( StringUtil.isBlank( className ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_CLASS_NAME_REQUIRED ) );
				return;
			}

			try
			{
				Class c = Class.forName( className );
				Method m = c.getMethod( "getInstance", null ); //$NON-NLS-1$
				AbstractSemanticValidator validator = (AbstractSemanticValidator) m
						.invoke( null, null );
				validator.setName( name );
				validator.setModules( modules );

				try
				{
					dictionary.addSemanticValidator( validator );
				}
				catch ( MetaDataException e )
				{
					errorHandler
							.semanticError( new MetaDataParserException(
									e,
									MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
				}
			}
			catch ( Exception e )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_INVALID_META_VALIDATOR ) );
			}
		}

	}

	class DefaultValueState extends InnerParseState
	{

		/**
		 * Reference to a member or a property.
		 */

		PropertyDefn propertyDefn = null;

		DefaultValueState( PropertyDefn propDefn )
		{
			this.propertyDefn = propDefn;
		}

		public void end( ) throws SAXException
		{
			if ( this.propertyDefn == null )
				return;
			try
			{
				Object value = propertyDefn
						.validateXml( null, text.toString( ) );
				propertyDefn.setDefault( value );
			}
			catch ( PropertyValueException e )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_INVALID_DEFAULT ) );
			}
		}
	}

	class ChoiceState extends InnerParseState
	{

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String displayNameID = attrs.getValue( DISPLAY_NAME_ID_ATTRIB );
			String xmlName = attrs.getValue( NAME_ATTRIB );
			if ( StringUtil.isBlank( displayNameID ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED ) );
			}
			else if ( StringUtil.isBlank( xmlName ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_XML_NAME_REQUIRED ) );
			}
			else
			{
				Choice choice = new Choice( xmlName, displayNameID );

				boolean found = false;
				Iterator iter = choices.iterator( );
				while ( iter.hasNext( ) )
				{
					Choice tmpChoice = (Choice) iter.next( );
					if ( tmpChoice.getName( ).equalsIgnoreCase(
							choice.getName( ) ) )
					{
						found = true;
						break;
					}
				}
				if ( found )
					errorHandler
							.semanticError( new MetaDataParserException(
									MetaDataException.DESIGN_EXCEPTION_DUPLICATE_CHOICE_NAME ) );
				else
					choices.add( choice );
			}
		}
	}

	class StylePropertyState extends InnerParseState
	{

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String name = attrs.getValue( NAME_ATTRIB );

			boolean ok = ( elementDefn != null );
			if ( StringUtil.isBlank( name ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				ok = false;
			}

			if ( ok )
				elementDefn.addStyleProperty( name );

		}
	}

	class SlotState extends InnerParseState
	{

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String name = attrs.getValue( NAME_ATTRIB );
			String displayNameID = attrs.getValue( DISPLAY_NAME_ID_ATTRIB );
			String multipleCardinality = attrs
					.getValue( MULTIPLE_CARDINALITY_ATTRIB );

			boolean ok = ( elementDefn != null );
			if ( StringUtil.isBlank( name ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				ok = false;
			}
			else if ( StringUtil.isBlank( displayNameID ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED ) );
				ok = false;
			}
			else if ( StringUtil.isBlank( multipleCardinality ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_MULTIPLE_CARDINALITY_REQUIRED ) );
				ok = false;
			}

			if ( !ok )
				return;

			slotDefn = new SlotDefn( );
			slotDefn.setName( name );
			slotDefn.setDisplayNameID( displayNameID );
			slotDefn.setAddToNameSpace( getBooleanAttrib( attrs,
					IS_MANAGED_BY_NAME_SPACE_ATTRIB, true ) );
			slotDefn.setMultipleCardinality( parseBoolean( multipleCardinality,
					true ) );
			slotDefn.setSelector( attrs.getValue( SELECTOR_ATTRIB ) );
			slotDefn.setSince( attrs.getValue( SINCE_ATTRIB ) );
			slotDefn.setXmlName( attrs.getValue( XML_NAME_ATTRIB ) );
			elementDefn.addSlot( slotDefn );
		}

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( TYPE_TAG ) )
				return new SlotTypeState( );
			if ( tagName.equalsIgnoreCase( TRIGGER_TAG ) )
				return new TriggerState( );
			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		public void end( ) throws SAXException
		{
			super.end( );
			slotDefn = null;
		}

	}

	class SlotTypeState extends InnerParseState
	{

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			boolean ok = ( slotDefn != null );
			String name = attrs.getValue( NAME_ATTRIB );
			if ( StringUtil.isBlank( name ) )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				ok = false;
			}

			if ( ok )
				slotDefn.addType( name );
		}
	}

	/**
	 * The state to parse a method under a class.
	 */

	class ClassMethodState extends AbstractMethodState
	{

		private boolean isConstructor = false;

		ClassMethodState( Object obj, boolean isConstructor )
		{
			super( obj );
			this.isConstructor = isConstructor;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.metadata.MetaDataHandler.AbstractMethodState#getMethodInfo()
		 */

		MethodInfo getMethodInfo( String name )
		{
			ClassInfo classInfo = (ClassInfo) owner;

			if ( classInfo != null )
			{
				if ( isConstructor )
					methodInfo = (MethodInfo) classInfo.getConstructor( );
				else
					methodInfo = classInfo.findMethod( name );
			}

			if ( methodInfo == null )
				methodInfo = new MethodInfo( isConstructor );

			return methodInfo;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.metadata.MetaDataHandler.AbstractMethodState#addDefnTo()
		 */

		void addDefnTo( )
		{
			assert owner instanceof ClassInfo;

			ClassInfo classInfo = (ClassInfo) owner;
			try
			{
				if ( isConstructor )
				{
					if ( classInfo.getConstructor( ) == null )
						classInfo.setConstructor( methodInfo );
				}
				else
				{
					if ( classInfo.findMethod( methodInfo.getName( ) ) == null )
						classInfo.addMethod( methodInfo );
				}
			}
			catch ( MetaDataException e )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								e,
								MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
			}

		}
	}

	/**
	 * The state to parse a method under an element.
	 */

	class ElementMethodState extends AbstractMethodState
	{

		SystemPropertyDefn localPropDefn = new SystemPropertyDefn( );

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.metadata.MetaDataHandler.AbstractMethodState#getMethodInfo()
		 */

		MethodInfo getMethodInfo( String name )
		{
			return new MethodInfo( false );
		}

		ElementMethodState( Object obj )
		{
			super( obj );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */
		public void parseAttrs( Attributes attrs )
		{
			super.parseAttrs( attrs );
			localPropDefn.setValueRequired( getBooleanAttrib( attrs,
					VALUE_REQUIRED_ATTRIB, false ) );
			localPropDefn.setSince( attrs.getValue( SINCE_ATTRIB ) );
			localPropDefn.setContext( attrs.getValue( CONTEXT_ATTRIB ) );
			localPropDefn.setReturnType( attrs.getValue( RETURN_TYPE_ATTRIB ) );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.metadata.MetaDataHandler.AbstractMethodState#addDefnTo()
		 */

		void addDefnTo( )
		{
			assert owner instanceof ElementDefn;

			PropertyType typeDefn = dictionary
					.getPropertyType( PropertyType.SCRIPT_TYPE );

			String name = methodInfo.getName( );
			String displayNameID = methodInfo.getDisplayNameKey( );

			localPropDefn.setName( name );
			localPropDefn.setDisplayNameID( displayNameID );
			localPropDefn.setType( typeDefn );
			localPropDefn.setGroupNameKey( null );
			localPropDefn.setCanInherit( true );
			localPropDefn.setIntrinsic( false );
			localPropDefn.setStyleProperty( false );
			localPropDefn.setDetails( methodInfo );
			try
			{
				( (ElementDefn) owner ).addProperty( localPropDefn );
			}
			catch ( MetaDataException e )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								e,
								MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
			}
		}
	}

	/**
	 * Parses an method state either under an element or class tag.
	 */

	abstract class AbstractMethodState extends InnerParseState
	{

		/**
		 * The element contains this state. Can be either a
		 * <code>ElementDefn</code> or <code>ClassInfo</code>.
		 */

		protected Object owner = null;

		/**
		 * The cached <code>MethodInfo</code> for the state.
		 */

		protected MethodInfo methodInfo = null;

		/**
		 * The cached argument list.
		 */

		private ArgumentInfoList argumentList = null;

		/**
		 * Constructs a <code>MethodState</code> with the given owner.
		 * 
		 * @param obj
		 *            the parent object of this state
		 */

		AbstractMethodState( Object obj )
		{
			assert obj != null;
			this.owner = obj;
		}

		/**
		 * Addes method information to the ElementDefn or ClassInfo.
		 */

		abstract void addDefnTo( );

		/**
		 * Returns method information with the given method name.
		 * 
		 * @param name
		 *            the method name
		 * @return the <code>MethodInfo</code> object
		 */

		abstract MethodInfo getMethodInfo( String name );

		public void parseAttrs( Attributes attrs )
		{
			String name = attrs.getValue( NAME_ATTRIB );
			String displayNameID = attrs.getValue( DISPLAY_NAME_ID_ATTRIB );
			String toolTipID = attrs.getValue( TOOL_TIP_ID_ATTRIB );
			String returnType = attrs.getValue( RETURN_TYPE_ATTRIB );
			boolean isStatic = getBooleanAttrib( attrs, IS_STATIC_ATTRIB, false );

			boolean ok = true;
			if ( name == null )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				ok = false;
			}
			if ( displayNameID == null )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED ) );
				ok = false;
			}

			if ( !ok )
				return;

			// Note that here ROM supports overloadding, while JavaScript not.
			// finds the method info if it has been parsed.

			methodInfo = getMethodInfo( name );

			methodInfo.setName( name );
			methodInfo.setDisplayNameKey( displayNameID );
			methodInfo.setReturnType( returnType );
			methodInfo.setToolTipKey( toolTipID );
			methodInfo.setStatic( isStatic );

			addDefnTo( );
		}

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( ARGUMENT_TAG ) )
				return new ArgumentState( );
			return super.startElement( tagName );
		}

		public void end( ) throws SAXException
		{
			if ( argumentList == null )
				argumentList = new ArgumentInfoList( );

			methodInfo.addArgumentList( argumentList );

			methodInfo = null;
			propDefn = null;
		}

		class ArgumentState extends InnerParseState
		{

			public void parseAttrs( Attributes attrs )
			{
				String name = attrs.getValue( NAME_ATTRIB );
				String tagID = attrs.getValue( TAG_ID_ATTRIB );
				String type = attrs.getValue( TYPE_ATTRIB );

				if ( name == null )
					return;

				ArgumentInfo argument = new ArgumentInfo( );
				argument.setName( name );
				argument.setType( type );
				argument.setDisplayNameKey( tagID );

				if ( argumentList == null )
					argumentList = new ArgumentInfoList( );

				try
				{
					argumentList.addArgument( argument );
				}
				catch ( MetaDataException e )
				{
					errorHandler
							.semanticError( new MetaDataParserException(
									e,
									MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
				}
			}
		}
	}

	class ClassState extends InnerParseState
	{

		ClassInfo classInfo = null;

		public void parseAttrs( Attributes attrs )
		{
			String name = attrs.getValue( NAME_ATTRIB );
			String displayNameID = attrs.getValue( DISPLAY_NAME_ID_ATTRIB );
			String toolTipID = attrs.getValue( TOOL_TIP_ID_ATTRIB );
			String isNative = attrs.getValue( NATIVE_ATTRIB );

			boolean ok = true;
			if ( name == null )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				ok = false;
			}
			if ( displayNameID == null )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED ) );
				ok = false;
			}

			if ( !ok )
				return;

			classInfo = new ClassInfo( );
			classInfo.setName( name );
			classInfo.setDisplayNameKey( displayNameID );
			classInfo.setToolTipKey( toolTipID );

			if ( Boolean.TRUE.toString( ).equalsIgnoreCase( isNative ) )
				classInfo.setNative( true );
			else if ( Boolean.FALSE.toString( ).equalsIgnoreCase( isNative ) )
				classInfo.setNative( false );

			try
			{
				dictionary.addClass( classInfo );
			}
			catch ( MetaDataException e )
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								e,
								MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
			}
		}

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( CONSTRUCTOR_TAG ) )
				return new ClassMethodState( classInfo, true );
			if ( tagName.equalsIgnoreCase( MEMBER_TAG ) )
				return new MemberState( );
			if ( tagName.equalsIgnoreCase( METHOD_TAG ) )
				return new ClassMethodState( classInfo, false );

			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		public void end( ) throws SAXException
		{
			super.end( );
			classInfo = null;
		}

		private class MemberState extends InnerParseState
		{

			public void parseAttrs( Attributes attrs )
			{
				String name = attrs.getValue( NAME_ATTRIB );
				String displayNameID = attrs.getValue( DISPLAY_NAME_ID_ATTRIB );
				String toolTipID = attrs.getValue( TOOL_TIP_ID_ATTRIB );
				String dataType = attrs.getValue( DATA_TYPE_ATTRIB );

				boolean ok = true;
				if ( name == null )
				{
					errorHandler
							.semanticError( new MetaDataParserException(
									MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
					ok = false;
				}
				if ( displayNameID == null )
				{
					errorHandler
							.semanticError( new MetaDataParserException(
									MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED ) );
					ok = false;
				}
				if ( dataType == null )
				{
					errorHandler
							.semanticError( new MetaDataParserException(
									MetaDataParserException.DESIGN_EXCEPTION_DATA_TYPE_REQUIRED ) );
					ok = false;
				}

				if ( !ok )
					return;

				MemberInfo memberDefn = new MemberInfo( );
				memberDefn.setName( name );
				memberDefn.setDisplayNameKey( displayNameID );
				memberDefn.setToolTipKey( toolTipID );
				memberDefn.setDataType( dataType );
				memberDefn.setStatic( getBooleanAttrib( attrs,
						IS_STATIC_ATTRIB, false ) );

				try
				{
					classInfo.addMemberDefn( memberDefn );
				}
				catch ( MetaDataException e )
				{
					errorHandler
							.semanticError( new MetaDataParserException(
									e,
									MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED ) );
				}
			}
		}
	}

	class TriggerState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */
		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			assert propDefn != null || slotDefn != null;

			String validatorName = attrs.getValue( VALIDATOR_ATTRIB );
			String targetElement = attrs.getValue( TARGET_ELEMENT_ATTRIB );

			if ( !StringUtil.isBlank( validatorName ) )
			{
				SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(
						validatorName );

				triggerDefn.setPreRequisite( getBooleanAttrib( attrs,
						PRE_REQUISITE_ATTRIB, false ) );
				if ( !StringUtil.isBlank( targetElement ) )
					triggerDefn.setTargetElement( targetElement );

				if ( propDefn != null )
					propDefn.getTriggerDefnSet( ).add( triggerDefn );

				if ( slotDefn != null )
					slotDefn.getTriggerDefnSet( ).add( triggerDefn );
			}
			else
			{
				errorHandler
						.semanticError( new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_VALIDATOR_NAME_REQUIRED ) );
			}
		}
	}

	/**
	 * Checks if dictionary contains a specified ChoiceSet with the name
	 * <code>choiceSetName</code>.
	 * 
	 * @param choiceSetName
	 *            the name of ChoiceSet to be checked.
	 * @return the validated choiceSet. If not found, return null.
	 */

	private ChoiceSet validateChoiceSet( String choiceSetName )
	{
		IChoiceSet choiceSet = dictionary.getChoiceSet( choiceSetName );
		if ( choiceSet == null )
		{
			errorHandler
					.semanticError( new MetaDataParserException(
							MetaDataParserException.DESIGN_EXCEPTION_INVALID_CHOICE_TYPE ) );
			return null;
		}

		return (ChoiceSet) choiceSet;
	}
}