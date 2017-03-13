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

import fr.paris.lutece.plugins.myfiles.business.MyFileData;
import fr.paris.lutece.plugins.myfiles.business.MyFileLink;
import fr.paris.lutece.plugins.myfiles.service.storage.FileStorage;
import fr.paris.lutece.plugins.myfiles.service.storage.NoStorageException;
import fr.paris.lutece.plugins.myfiles.service.storage.StorageException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Item;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/**
 * MinioFileStorage
 */
public class MinioFileStorage implements FileStorage
{

    private static final String PROPERTY_SERVER_URL = "myfiles.server.url";
    private static final String PROPERTY_ACCESS_KEY = "myfiles.server.access_key";
    private static final String PROPERTY_SECRET_KEY = "myfiles.server.secret_key";

    private static MinioClient _client;

    /**
     * Constructor
     */
    public MinioFileStorage( )
    {
        try
        {
            String strServerUrl = AppPropertiesService.getProperty( PROPERTY_SERVER_URL );
            String strAccessKey = AppPropertiesService.getProperty( PROPERTY_ACCESS_KEY );
            String strSecretKey = AppPropertiesService.getProperty( PROPERTY_SECRET_KEY );
            _client = new MinioClient( strServerUrl, strAccessKey, strSecretKey );
        }
        catch( InvalidEndpointException | InvalidPortException ex )
        {
            AppLogService.error( "FileStorageService : error creating minio client : " + ex.getMessage( ), ex );
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<MyFileLink> getFiles( String strUserId ) throws NoStorageException, StorageException
    {
        List<MyFileLink> listFiles = new ArrayList<>( );
        try
        {
            String strBucketName = strUserId;
            boolean bExist = _client.bucketExists( strBucketName );
            if ( bExist )
            {
                for ( Result<Item> result : _client.listObjects( strBucketName ) )
                {
                    Item item = result.get( );
                    MyFileLink myFile = new MyFileLink( );
                    myFile.setName( item.objectName( ) );
                    ObjectStat stat = _client.statObject( strBucketName, item.objectName( ) );
                    myFile.setContentType( stat.contentType( ) );
                    String strUrl = _client.presignedGetObject( strBucketName, item.objectName( ), 60 * 60 * 24 );
                    myFile.setUrl( strUrl );
                    listFiles.add( myFile );
                }
            }
            else
            {
                throw new NoStorageException( );
            }
        }
        catch( InvalidExpiresRangeException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException
                | InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException | InternalException ex )
        {
            throw new StorageException( "Error getting files : " + ex.getMessage( ), ex );
        }

        return listFiles;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void createStorage( String strNameId ) throws StorageException
    {
        try
        {
            _client.makeBucket( strNameId );
        }
        catch( InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException | NoResponseException
                | XmlPullParserException | ErrorResponseException | InternalException ex )
        {
            throw new StorageException( "Error getting files : " + ex.getMessage( ), ex );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addFile( String strUserId, MyFileData myFileData ) throws StorageException
    {
        try
        {
            String strBucketName = strUserId;
            _client.putObject( strBucketName, myFileData.getName( ), myFileData.getInputstream( ), myFileData.getSize( ), myFileData.getContentType( ) );
        }
        catch( InvalidArgumentException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException
                | NoResponseException | XmlPullParserException | ErrorResponseException | InternalException ex )
        {
            throw new StorageException( "Error getting files : " + ex.getMessage( ), ex );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void removeFile( String strUserId, String strFilename ) throws StorageException
    {
        try
        {
            String strBucketName = strUserId;
            _client.removeObject( strBucketName, strFilename );
        }
        catch( InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException | NoResponseException
                | XmlPullParserException | ErrorResponseException | InternalException ex )
        {
            throw new StorageException( "Error removing file : " + ex.getMessage( ), ex );
        }

    }

}
