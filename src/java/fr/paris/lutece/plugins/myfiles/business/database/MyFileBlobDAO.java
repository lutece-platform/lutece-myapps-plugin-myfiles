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
 * This class provides Data Access methods for MyFileBlob objects
 */
public final class MyFileBlobDAO implements IMyFileBlobDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_myfile_blob ) FROM myfiles_myfileblob";
    private static final String SQL_QUERY_SELECT = "SELECT id_myfile_blob, bucket_name_id, file_content_type, file_size, file_name, blob_value FROM myfiles_myfileblob WHERE id_myfile_blob = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO myfiles_myfileblob ( id_myfile_blob, bucket_name_id, file_content_type, file_size, file_name, blob_value ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM myfiles_myfileblob WHERE id_myfile_blob = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE myfiles_myfileblob SET id_myfile_blob = ?, bucket_name_id = ?, file_content_type = ?, file_size = ?, file_name = ?, blob_value = ? WHERE id_myfile_blob = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_myfile_blob, bucket_name_id, file_content_type, file_size, file_name, blob_value FROM myfiles_myfileblob";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_myfile_blob FROM myfiles_myfileblob";
    private static final String SQL_QUERY_COUNT_FILES = "SELECT COUNT(*) FROM myfiles_myfileblob WHERE bucket_name_id = ? AND file_name = ?";
    private static final String SQL_QUERY_COUNT_BUCKETS = "SELECT COUNT(*) FROM myfiles_myfileblob WHERE bucket_name_id = ?";
    private static final String SQL_QUERY_SELECTBYNAME = "SELECT id_myfile_blob, bucket_name_id, file_content_type, file_size, file_name, blob_value FROM myfiles_myfileblob WHERE bucket_name_id = ? AND file_name = ?";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK , plugin  );
        daoUtil.executeQuery( );
        int nKey = 1;

        if( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free();
        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( MyFileBlob myFileBlob, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        myFileBlob.setId( newPrimaryKey( plugin ) );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , myFileBlob.getId( ) );
        daoUtil.setString( nIndex++ , myFileBlob.getBucketNameId( ) );
        daoUtil.setString( nIndex++ , myFileBlob.getContentType( ) );
        daoUtil.setLong( nIndex++ , myFileBlob.getSize( ) );
        daoUtil.setString( nIndex++ , myFileBlob.getName( ) );
        daoUtil.setBinaryStream( nIndex++ , myFileBlob.getInputstream( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MyFileBlob load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );
        MyFileBlob myFileBlob = null;

        if ( daoUtil.next( ) )
        {
            myFileBlob = new MyFileBlob();
            int nIndex = 1;
            
            myFileBlob.setId( daoUtil.getInt( nIndex++ ) );
            myFileBlob.setBucketNameId( daoUtil.getString( nIndex++ ) );
            myFileBlob.setContentType( daoUtil.getString( nIndex++ ) );
            myFileBlob.setSize( daoUtil.getInt( nIndex++ ) );
            myFileBlob.setName( daoUtil.getString( nIndex++ ) );
            myFileBlob.setInputstream( daoUtil.getBinaryStream( nIndex++ ) );
        }

        daoUtil.free( );
        return myFileBlob;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MyFileBlob loadByName( String strNameBucket, String strNameFile, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTBYNAME, plugin );
        daoUtil.setString( 1 , strNameBucket );
        daoUtil.setString( 2 , strNameFile );
        daoUtil.executeQuery( );
        MyFileBlob myFileBlob = null;

        if ( daoUtil.next( ) )
        {
            myFileBlob = new MyFileBlob();
            int nIndex = 1;
            
            myFileBlob.setId( daoUtil.getInt( nIndex++ ) );
            myFileBlob.setBucketNameId( daoUtil.getString( nIndex++ ) );
            myFileBlob.setContentType( daoUtil.getString( nIndex++ ) );
            myFileBlob.setSize( daoUtil.getInt( nIndex++ ) );
            myFileBlob.setName( daoUtil.getString( nIndex++ ) );
            myFileBlob.setInputstream( daoUtil.getBinaryStream( nIndex++ ) );
        }

        daoUtil.free( );
        return myFileBlob;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( MyFileBlob myFileBlob, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , myFileBlob.getId( ) );
        daoUtil.setString( nIndex++ , myFileBlob.getBucketNameId( ) );
        daoUtil.setString( nIndex++ , myFileBlob.getContentType( ) );
        daoUtil.setLong( nIndex++ , myFileBlob.getSize( ) );
        daoUtil.setString( nIndex++ , myFileBlob.getName( ) );
        daoUtil.setBinaryStream( nIndex++ , myFileBlob.getInputstream( ) );
        daoUtil.setInt( nIndex , myFileBlob.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<MyFileBlob> selectMyFileBlobsList( Plugin plugin )
    {
        List<MyFileBlob> myFileBlobList = new ArrayList<MyFileBlob>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            MyFileBlob myFileBlob = new MyFileBlob(  );
            int nIndex = 1;
            
            myFileBlob.setId( daoUtil.getInt( nIndex++ ) );
            myFileBlob.setBucketNameId( daoUtil.getString( nIndex++ ) );
            myFileBlob.setContentType( daoUtil.getString( nIndex++ ) );
            myFileBlob.setSize( daoUtil.getInt( nIndex++ ) );
            myFileBlob.setName( daoUtil.getString( nIndex++ ) );
            myFileBlob.setInputstream( daoUtil.getBinaryStream( nIndex++ ) );

            myFileBlobList.add( myFileBlob );
        }

        daoUtil.free( );
        return myFileBlobList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdMyFileBlobsList( Plugin plugin )
    {
        List<Integer> myFileBlobList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            myFileBlobList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return myFileBlobList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean myFileBlobExists( String strNameBucket, String strNameFile, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_FILES, plugin );
        daoUtil.setString( 1, strNameBucket );
        daoUtil.setString( 2, strNameFile );

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

    /**
     * {@inheritDoc }
     */
    @Override
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