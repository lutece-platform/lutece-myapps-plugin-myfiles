/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.myfiles.business.database;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for Bucket objects
 */
public final class BucketDAO implements IBucketDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT name_id_bucket FROM myfiles_bucket WHERE name_id_bucket = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO myfiles_bucket ( name_id_bucket ) VALUES ( ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM myfiles_bucket WHERE name_id_bucket = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE myfiles_bucket SET name_id_bucket = ? WHERE name_id_bucket = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT name_id_bucket FROM myfiles_bucket";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT name_id_bucket FROM myfiles_bucket";
    private static final String SQL_QUERY_COUNT_BUCKETS = "SELECT COUNT(*) FROM myfiles_bucket WHERE name_id_bucket = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Bucket bucket, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nIndex = 1;
        
        daoUtil.setString( nIndex++ , bucket.getNameId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Bucket load( String strKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setString( 1 , strKey );
        daoUtil.executeQuery( );
        Bucket bucket = null;

        if ( daoUtil.next( ) )
        {
            bucket = new Bucket();
            int nIndex = 1;
            
            bucket.setNameId( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );
        return bucket;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( String strKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setString( 1 , strKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Bucket bucket, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;
        
        daoUtil.setString( nIndex++ , bucket.getNameId( ) );
        daoUtil.setString( nIndex , bucket.getNameId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Bucket> selectBucketsList( Plugin plugin )
    {
        List<Bucket> bucketList = new ArrayList<>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Bucket bucket = new Bucket(  );
            int nIndex = 1;
            
            bucket.setNameId( daoUtil.getString( nIndex++ ) );

            bucketList.add( bucket );
        }

        daoUtil.free( );
        return bucketList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> selectIdBucketsList( Plugin plugin )
    {
        List<String> bucketList = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            bucketList.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free( );
        return bucketList;
    }

    /**
    * Returns true if the bucket exists
    * @return boolean the existance of the bucket
    * @param strKey The key of bucket
    * @param plugin The Plugin object
    */
    public boolean bucketExists( String strKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_BUCKETS, plugin );
        daoUtil.setString( 1, strKey );

        int nCounted = 0;
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nCounted = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        if ( nCounted < 1 )
        {
            return false;
        }

        return true;
    }
}