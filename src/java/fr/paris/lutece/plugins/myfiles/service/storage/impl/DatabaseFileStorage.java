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

package fr.paris.lutece.plugins.myfiles.service.storage.impl;

import fr.paris.lutece.plugins.myfiles.business.database.BucketHome;
import fr.paris.lutece.plugins.myfiles.business.MyFileData;
import fr.paris.lutece.plugins.myfiles.business.MyFileLink;
import fr.paris.lutece.plugins.myfiles.service.storage.FileStorage;
import fr.paris.lutece.plugins.myfiles.service.storage.NoStorageException;
import fr.paris.lutece.plugins.myfiles.service.storage.StorageException;
import fr.paris.lutece.plugins.myfiles.business.database.Bucket;
import fr.paris.lutece.plugins.myfiles.business.database.MyFileBlob;
import fr.paris.lutece.plugins.myfiles.business.database.MyFileBlobHome;
import fr.paris.lutece.util.url.UrlItem;
import java.util.ArrayList;
import java.util.List;

/**
 * MinioFileStorage
 */
public class DatabaseFileStorage implements FileStorage
{

    private static final String PATH_SERVLET_DOWNLOAD = "servlet/plugins/myfiles/download";

    private static final String PARAMETER_FILENAME = "filename";

    private static final long FILE_MAX_SIZE = 16 * 1024 * 1024 ;

    /**
     * Constructor
     */
    public DatabaseFileStorage( )
    {
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<MyFileLink> getFiles( String strUserId ) throws NoStorageException, StorageException
    {
        List<MyFileLink> listFiles = new ArrayList<>( );

        String strBucketName = strUserId;
        UrlItem urlItem = new UrlItem( PATH_SERVLET_DOWNLOAD );
        boolean bExist = BucketHome.bucketExists( strBucketName );
        if ( bExist )
        {
            List<MyFileBlob> myFileBlobList = MyFileBlobHome.getMyFileBlobsList(  );

            for( MyFileBlob myFileBlob : myFileBlobList )
            {
                if( myFileBlob.getBucketNameId(  ).equals( strBucketName ) )
                {
                    MyFileLink myFileLink = new MyFileLink(  );
                    myFileLink.setName( myFileBlob.getName(  ) );
                    myFileLink.setContentType( myFileBlob.getContentType(  ) );
                    urlItem.addParameter( PARAMETER_FILENAME, myFileBlob.getName(  ) );
                    String strUrl = urlItem.getUrl(  );
                    myFileLink.setUrl( strUrl );
                    listFiles.add( myFileLink );
                }
            }
        }
        else
        {
            throw new NoStorageException( );
        }

        return listFiles;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MyFileData getFile( String strUserId, String strFilename ) throws NoStorageException, StorageException
    {
        MyFileData myFile = null ;
        String strBucketName = strUserId;
        boolean bExist = BucketHome.bucketExists( strBucketName );
        if ( bExist )
        {
            myFile = new MyFileData(  );
            MyFileBlob myFileBlob = MyFileBlobHome.findByName( strBucketName, strFilename );

            if( myFileBlob == null )
            {
                throw new StorageException( "Error getting file : File not found" );
            }

            myFile.setName( strFilename );
            myFile.setContentType( myFileBlob.getContentType(  ) );
            myFile.setSize( myFileBlob.getSize(  ) );
            myFile.setInputstream( myFileBlob.getInputstream(  ) );
        }
        else
        {
            throw new NoStorageException( );
        }
        
        return myFile;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void createStorage( String strNameId ) throws StorageException
    {
        Bucket bucket = new Bucket(  );
        bucket.setNameId( strNameId );
        BucketHome.create( bucket );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addFile( String strUserId, MyFileData myFileData ) throws StorageException
    {
        String strBucketName = strUserId;
        boolean bBucketExist = BucketHome.bucketExists( strBucketName );
        if ( bBucketExist )
        {
            boolean bFileExist = MyFileBlobHome.myFileBlobExists( strBucketName, strUserId );
            if( bFileExist )
            {
                throw new StorageException( "Error adding file : File already exists" );
            }

            if( myFileData.getSize(  ) > FILE_MAX_SIZE )
            {
                throw new StorageException( "Error adding file : File size is too big" );
            }

            MyFileBlob myFileBlob = new MyFileBlob(  );            
            myFileBlob.setBucketNameId( strBucketName );
            myFileBlob.setName( myFileData.getName(  ) );
            myFileBlob.setSize( myFileData.getSize(  ) );
            myFileBlob.setContentType( myFileData.getContentType(  ) );
            myFileBlob.setInputstream( myFileData.getInputstream(  ) );
            MyFileBlobHome.create( myFileBlob );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void removeFile( String strUserId, String strFilename ) throws StorageException
    {
        String strBucketName = strUserId;

        MyFileBlob myFileBlob = MyFileBlobHome.findByName( strBucketName, strFilename );
        
        if( myFileBlob == null )
        {
            throw new StorageException( "Error removing file : File not found" );
        }
        
        MyFileBlobHome.remove( myFileBlob.getId(  ) );
    }

}
