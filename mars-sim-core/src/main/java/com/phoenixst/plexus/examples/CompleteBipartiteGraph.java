/*
 *  $Id: CompleteBipartiteGraph.java,v 1.12 2006/06/07 19:56:36 rconner Exp $
 *
 *  Copyright (C) 1994-2006 by Phoenix Software Technologists,
 *  Inc. and others.  All rights reserved.
 *
 *  THIS PROGRAM AND DOCUMENTATION IS PROVIDED UNDER THE TERMS OF THE
 *  COMMON PUBLIC LICENSE ("AGREEMENT") WHICH ACCOMPANIES IT.  ANY
 *  USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 *  RECIPIENT'S ACCEPTANCE OF THE AGREEMENT.
 *
 *  The license text can also be found at
 *    http://opensource.org/licenses/cpl.php
 */

package com.phoenixst.plexus.examples;

import java.io.*;

import com.phoenixst.plexus.GraphTransformer;
import com.phoenixst.plexus.operations.Join;


/**
 *  A complete bipartite graph.
 *
 *  @version    $Revision: 1.12 $
 *  @author     Ray A. Conner
 *
 *  @since      1.0
 */
public class CompleteBipartiteGraph extends Join
{

    private static final long serialVersionUID = 3L;

    private transient int m;
    private transient int n;


    /**
     *  Creates a new <code>CompleteBipartiteGraph</code>.
     */
    public CompleteBipartiteGraph( int m, int n )
    {
        super( new EmptyGraph( m ),
               new GraphTransformer( new EmptyGraph( n ),
                                     new IntegerOffsetTransformer( m ) ),
               true );
        this.m = m;
        this.n = n;
    }


    private void readObject( ObjectInputStream in )
        throws ClassNotFoundException,
               IOException
    {
        in.defaultReadObject();

        // We could do a lot more checking in here, but why bother?
        // It's only an example after all.
        m = getLeftOperand().nodes( null ).size();
        n = getRightOperand().nodes( null ).size();
    }


    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append( "Complete Bipartite( " );
        s.append( m );
        s.append( ", " );
        s.append( n );
        s.append( " )" );
        return s.toString();
    }

}
