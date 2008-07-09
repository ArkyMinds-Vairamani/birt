/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse private License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.script.function.bre;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.MathUtil;
import org.eclipse.birt.core.script.function.i18n.Messages;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;

/**
 * 
 */
class BirtMath implements IScriptFunctionExecutor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IScriptFunctionExecutor executor;
	BirtMath( String functionName ) throws BirtException
	{
		if( "add".equals( functionName ))
			this.executor = new Function_Add();
		else if( "subtract".equals( functionName ))
			this.executor = new Function_Subtract( );
		else if( "multiple".equals( functionName ))
			this.executor = new Function_Multiple( );
		else if( "divide".equals( functionName ))
			this.executor = new Function_Divide( );
		else if( "round".equals( functionName ))
			this.executor = new Function_Round( );
		else if( "roundUp".equals( functionName ))
			this.executor = new Function_RoundUp( );
		else if( "roundDown".equals( functionName ))
			this.executor = new Function_RoundDown( );
		else if( "ceiling".equals( functionName ))
			this.executor = new Function_Ceiling( );
		else if( "mod".equals( functionName ))
			this.executor = new Function_Mod( );
		else if( "safeDivide".equals( functionName ))
			this.executor = new Function_SafeDivide( );
		else
			throw new BirtException( "org.eclipse.birt.core.script.function.bre",
					null,
					Messages.getString( "invalid.function.name" )
							+ "BirtMath." + functionName );	}
	
	private class Function_Add extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_Add()
		{
			length = 2;
			isFixed = true;
		}
		
		protected Object getValue( final Object[] args ) throws BirtException
		{
			return MathUtil.add( args[0], args[1] );
		}
	}	
	
	/**
	 *
	 */
	private class Function_Subtract extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * 
		 */
		Function_Subtract()
		{
			length = 2;
			isFixed = true;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.core.script.bre.Function_temp#getValue(java.lang.Object[])
		 */
		protected Object getValue( final Object[] args ) throws BirtException
		{
			return MathUtil.subtract( args[0], args[1] );
		}
	}	
	
	/**
	 * 
	 */
	private class Function_Multiple extends Function_temp
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_Multiple( )
		{
			length = 2;
			isFixed = true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.core.script.bre.Function_temp#getValue(java.lang.Object[])
		 */
		protected Object getValue( final Object[] args ) throws BirtException
		{
			return MathUtil.multiply( args[0], args[1] );
		}
	}	
	/**
	 * 
	 */
	private class Function_Divide extends Function_temp
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * 
		 */
		Function_Divide( )
		{
			length = 2;
			isFixed = true;
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.core.script.bre.Function_temp#getValue(java.lang.Object[])
		 */
		protected Object getValue( final Object[] args ) throws BirtException
		{
			return MathUtil.divide( args[0], args[1] );
		}
	}	
	
	
	private class Function_Round extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_Round()
		{
			length = 2;
			isFixed = false;
		}
		
		protected Object getValue( Object[] args ) throws BirtException
		{
			if( args.length == 1)
				return new Double(round(toDoubleValue(args[0])));
			else
				return new Double(round(toDoubleValue(args[0]),(int)toDoubleValue(args[1])));
				
		}
	}
	
	/**
	 * Rounds a number to the integer.
	 * 
	 * @param value
	 * @return
	 */
	private static double round( double value )
	{
		return round( value, 0 );
	}
	
	/**
	 * Rounds a number to the specified number of digits. dec is an integer and
	 * can be negative.
	 * 
	 * @param value
	 * @param dec
	 * @return
	 */
	private static double round( double value, int dec )
	{
		double multiple = getMultiple( dec );
		return Math.round( value * multiple ) / multiple;
	}
	
	private class Function_RoundUp extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Function_RoundUp()
		{
			length = 2;
			isFixed = false;
		}
		
		protected Object getValue( Object[] args ) throws BirtException
		{
			if( args.length == 1)
				return new Double(roundUp(toDoubleValue(args[0])));
			else
				return new Double(roundUp(toDoubleValue(args[0]),(int)toDoubleValue(args[1])));
				
		}
	}
	/**
	 * Rounds a number up, away from 0.
	 * 
	 * @param value
	 * @return
	 */
	private static double roundUp( double value )
	{
		return roundUp( value, 0 );
	}
	
	/**
	 * Rounds a number up, away from 0, to the specified number of digits.
	 * Default for dec is 0.
	 * 
	 * @param value
	 * @param dec
	 * @return
	 */
	private static double roundUp( double value, int dec )
	{
		double multiple = getMultiple( dec );
		return Math.round( Math.ceil( value * multiple) ) / multiple;
	}

	/**
	 * @param dec
	 * @return
	 */
	private static double getMultiple( int dec )
	{
		double multiple = 1;
		if ( dec >= 0 )
		{
			for ( int i = 0; i < dec; i++ )
			{
				multiple *= 10;
			}
		}
		else
		{
			double adjustment = 1;
			for( int i = dec; i < 0; i++ )
			{
				multiple *= 0.1;
				adjustment *= 10;
			}
			multiple = Math.round( multiple * adjustment ) / adjustment; 
		}
		return multiple;
	}
		
	private class Function_RoundDown extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Function_RoundDown()
		{
			length = 2;
			isFixed = false;
		}
		
		protected Object getValue( Object[] args ) throws BirtException
		{
			if( args.length == 1)
				return new Double(roundDown(toDoubleValue(args[0])));
			else
				return new Double(roundDown(toDoubleValue(args[0]),(int)toDoubleValue(args[1])));
				
		}
	}
	/**
	 * Rounds a number down, away from 0, to the specified number of digits.
	 * 
	 * @param value
	 * @param dec
	 * @return
	 */
	private static double roundDown( double value, int dec )
	{
		double multiple = getMultiple( dec );
		return Math.round( Math.floor( value * multiple) ) / multiple;
	}
	
	/**
	 * Rounds a number down, away from 0.
	 * 
	 * @param value
	 * @return
	 */
	private static double roundDown( double value )
	{
		return roundDown( value, 0 );
	}
	
	private class Function_Ceiling extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Function_Ceiling()
		{
			length = 2;
			isFixed = true;
		}
		
		protected Object getValue( Object[] args ) throws BirtException
		{
			return new Double(ceiling(toDoubleValue(args[0]),toDoubleValue(args[1])));
		}
	}	
	
	/**
	 * Rounds a number up, away from zero, to the nearest multiple of
	 * significance.
	 * 
	 * @param n
	 * @param significance
	 * @return
	 */
	private static double ceiling( double n, double significance )
	{
		if( significance == 0 || n == 0 )
			return 0;
		
		if( n * significance < 0 )
			throw new IllegalArgumentException("The given significance cannot be applied to the number");
		
		if( Math.abs( n ) < Math.abs( significance ) )
			return significance;
		
		double multiple = Math.ceil( n/significance );
		return multiple*significance;
	}
	
	private class Function_Mod extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Function_Mod()
		{
			length = 2;
			isFixed = true;
		}
		
		protected Object getValue( Object[] args ) throws BirtException
		{
			return new Double(mod(toDoubleValue(args[0]),toDoubleValue(args[1])));
		}
	}
	
	/**
	 * Remainder after number is divided by divisor. The result has the same
	 * sign as divisor
	 * 
	 * @param n
	 * @param div
	 * @return
	 */
	private static double mod( double n, double div )
	{
		if( div == 0 )
			throw new IllegalArgumentException("The divisor cannot be 0"); 
		
		return n - div*Math.floor((n/div));
	}
	
	private class Function_SafeDivide extends Function_temp
	{
		private static final long serialVersionUID = 1L;

		Function_SafeDivide( )
		{
			length = 3;
			isFixed = true;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			return MathUtil.safeDivide( args[0], args[1], args[2] );
		}
	}

	private static double toDoubleValue( Object o )
	{
		if( o instanceof Number )
			return ((Number)o).doubleValue( );
		else 
			return o == null ? 0:Double.valueOf( o.toString( )).doubleValue( );
	}

	public Object execute( Object[] arguments ) throws BirtException
	{
		return this.executor.execute( arguments );
	}
} 
